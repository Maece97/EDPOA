package ch.unisg.fraudpreprocessing.controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.kstream.Window;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.HostInfo;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InteractiveQueries {
    private final HostInfo hostInfo;
    private final KafkaStreams streams;

    private static final Logger log = LoggerFactory.getLogger(InteractiveQueries.class);

    public InteractiveQueries(HostInfo hostInfo, KafkaStreams streams) {
        this.hostInfo = hostInfo;
        this.streams = streams;
    }

    ReadOnlyWindowStore<String, Long> getCountsStore() {
        return streams.store(
                StoreQueryParameters.fromNameAndType("transaction-counts", QueryableStoreTypes.windowStore()));
    }

    public void start() {
        Javalin app = Javalin.create().start(hostInfo.port());

        /** Local window store query: all entries */
        app.get("/transactions/all", this::getAll);
    }

    void getAll(Context ctx) {
        Map<String, Long> bpm = new HashMap<>();

        KeyValueIterator<Windowed<String>, Long> range = getCountsStore().all();
        while (range.hasNext()) {
            KeyValue<Windowed<String>, Long> next = range.next();
            Windowed<String> key = next.key;
            Long value = next.value;
            bpm.put(key.toString(), value);
        }
        // close the iterator to avoid memory leaks
        range.close();
        // return a JSON response
        ctx.json(bpm);
    }
}
