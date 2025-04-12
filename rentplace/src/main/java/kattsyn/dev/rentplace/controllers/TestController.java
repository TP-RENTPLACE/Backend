package kattsyn.dev.rentplace.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("${api.path}/test")
public class TestController {

    @GetMapping("/")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Hello World!!!");
    }

}
