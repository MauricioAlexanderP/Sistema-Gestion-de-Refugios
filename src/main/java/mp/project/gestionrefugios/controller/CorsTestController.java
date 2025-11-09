package mp.project.gestionrefugios.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/test")
@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "false")
public class CorsTestController {

  @GetMapping("/cors")
  public ResponseEntity<Map<String, Object>> testCors() {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "CORS test successful");
    response.put("timestamp", System.currentTimeMillis());
    response.put("status", "OK");

    return ResponseEntity.ok(response);
  }

  @GetMapping("/cors-protected")
  public ResponseEntity<Map<String, Object>> testCorsProtected() {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "CORS test with authentication successful");
    response.put("timestamp", System.currentTimeMillis());
    response.put("status", "OK");
    response.put("user", "Authenticated user");

    return ResponseEntity.ok(response);
  }

  @PostMapping("/cors")
  public ResponseEntity<Map<String, Object>> testCorsPost(@RequestBody(required = false) Map<String, Object> data) {
    Map<String, Object> response = new HashMap<>();
    response.put("message", "CORS POST test successful");
    response.put("timestamp", System.currentTimeMillis());
    response.put("status", "OK");
    response.put("receivedData", data);

    return ResponseEntity.ok(response);
  }

  @RequestMapping(value = "/cors", method = RequestMethod.OPTIONS)
  public ResponseEntity<Void> handleOptions() {
    return ResponseEntity.ok().build();
  }
}