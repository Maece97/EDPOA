package ch.unisg.card.controller;

import ch.unisg.card.dto.LimitUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
class UpdateLimitRestController {

    @Autowired
    StreamBridge streamBridge;

    @PostMapping("/update")
    public void startMessageProcess(@RequestBody LimitUpdateDto limitUpdateDto){
        System.out.println("Here i am");
        streamBridge.send("update-limit-out-0","kafka1",limitUpdateDto);
    }

}