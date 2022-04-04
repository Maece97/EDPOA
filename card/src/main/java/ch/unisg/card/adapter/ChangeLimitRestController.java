package ch.unisg.card.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limit")
@RequiredArgsConstructor
public class ChangeLimitRestController {


    @PostMapping("/change")
    public void startMessageProcess(){
        System.out.println("Controller working");
    }

}
