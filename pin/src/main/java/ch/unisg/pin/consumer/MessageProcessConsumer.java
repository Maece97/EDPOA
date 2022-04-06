package ch.unisg.pin.consumer;

import ch.unisg.pin.dto.PinCheckDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProcessConsumer {

    private final static String MESSAGE_START = "MessageKafkaDemo";
    private final static String MESSAGE_INTERMEDIATE = "MessageIntermediate";

    @KafkaListener(topics = "check-pin")
    public void checkPin(){
        System.out.println("The Pin service speaking here");
        //System.out.println(pinCheckDto.getPin());
        //Cool till here everything works...but how the fuck configure this correlation shit??!
    }


}
