echo "Waiting for Kafka to come online..."
cub kafka-ready -b kafka:9092 1 20
# create the src-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic incoming-transactions \
  --replication-factor 1 \
  --partitions 1 \
  --create
# create the out-topic topic
kafka-topics \
  --bootstrap-server kafka:9092 \
  --topic preprocessed-transactions \
  --replication-factor 1 \
  --partitions 1 \
  --create
sleep infinity