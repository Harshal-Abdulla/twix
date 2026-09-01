package com.twix.app.config;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Says whether the API is up and whether it can actually reach the database,
 * without needing an account or any data.
 *
 * It opens a real connection rather than trusting a pooled handle, so a 200
 * here means the database genuinely answered. On failure it reports the driver
 * error with any credentials stripped out.
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {

  private final DataSource dataSource;

  public HealthController(DataSource dataSource) {
    this.dataSource = dataSource;
  }

  @GetMapping
  public ResponseEntity<Map<String, Object>> health() {
    Map<String, Object> body = new LinkedHashMap<>();

    try (Connection connection = dataSource.getConnection()) {
      boolean valid = connection.isValid(5);
      body.put("ok", valid);
      body.put("database", connection.getMetaData().getDatabaseProductName());
      body.put("version", connection.getMetaData().getDatabaseProductVersion());
      return valid
          ? ResponseEntity.ok(body)
          : ResponseEntity.status(503).body(body);
    } catch (Exception e) {
      body.put("ok", false);
      body.put("error", e.getClass().getSimpleName());
      body.put("reason", scrub(e.getMessage()));
      return ResponseEntity.status(503).body(body);
    }
  }

  /** Drivers quote the connection string back in some errors. */
  private static String scrub(String message) {
    if (message == null) return "unknown";
    String cleaned = message.replaceAll("(?i)(postgres(ql)?://)[^@\\s]*@", "$1***:***@");
    return cleaned.length() > 300 ? cleaned.substring(0, 300) : cleaned;
  }
}
