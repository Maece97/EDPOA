package ch.unisg.pin.adapter.in;


import ch.unisg.pin.service.CheckPinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CheckPinWebAdapter {
    @GetMapping(path = "/checkPin")
    public ResponseEntity<Boolean> checkPin(@RequestParam String pin,@RequestParam String cardNumber) {
        CheckPinService pinChecker = new CheckPinService();
        boolean accepted = pinChecker.checkPin(pin,cardNumber);
        System.out.println(accepted);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}