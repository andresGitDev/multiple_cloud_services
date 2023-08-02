package ar.com.multiplecludservices.controller;

import ar.com.multiplecludservices.util.Path;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Path.HEALTCHECK)
public class HealthCheckController {

    @GetMapping
    public ResponseEntity<?> healthCheck() {
        return ResponseEntity.ok().build();
    }

}
