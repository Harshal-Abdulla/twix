package com.twix.app.config;

import com.zaxxer.hikari.HikariDataSource;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Builds the DataSource from whatever shape of connection string it is given.
 *
 * Hosted Postgres providers hand you a URL like
 *   postgresql://user:pass@host/db?sslmode=require&channel_binding=require
 * while the JDBC driver wants
 *   jdbc:postgresql://host/db?sslmode=require
 * with the user and password supplied separately, and it does not understand
 * channel_binding at all.
 *
 * Rather than rely on that conversion being done by hand in a dashboard, this
 * accepts either form: it lifts the credentials out of the URL when they are
 * embedded, adds the jdbc: prefix when it is missing, drops parameters the
 * driver rejects, and makes sure SSL is on. Getting the string "wrong" in the
 * ways people actually get it wrong is no longer a failure.
 */
@Configuration
public class DataSourceConfig {

  @Value("${TWIX_DB_URL:${DATABASE_URL:}}")
  private String rawUrl;

  @Value("${TWIX_DB_USER:}")
  private String configuredUser;

  @Value("${TWIX_DB_PASSWORD:}")
  private String configuredPassword;

  /** Parameters the PostgreSQL JDBC driver will not accept. */
  private static final List<String> UNSUPPORTED_PARAMS = List.of("channel_binding");

  @Bean
  public DataSource dataSource() {
    if (rawUrl == null || rawUrl.isBlank()) {
      throw new IllegalStateException(
          "No database URL. Set TWIX_DB_URL (or DATABASE_URL) in the environment.");
    }

    String url = rawUrl.trim();
    String user = configuredUser;
    String password = configuredPassword;

    // Provider-native form: credentials live inside the URL.
    if (url.startsWith("postgres://") || url.startsWith("postgresql://")) {
      URI uri = URI.create(url);

      String userInfo = uri.getUserInfo();
      if (userInfo != null && !userInfo.isBlank()) {
        int split = userInfo.indexOf(':');
        if (split >= 0) {
          if (user.isBlank()) user = userInfo.substring(0, split);
          if (password.isBlank()) password = userInfo.substring(split + 1);
        } else if (user.isBlank()) {
          user = userInfo;
        }
      }

      StringBuilder rebuilt = new StringBuilder("jdbc:postgresql://").append(uri.getHost());
      if (uri.getPort() != -1) rebuilt.append(':').append(uri.getPort());
      rebuilt.append(uri.getPath() == null || uri.getPath().isBlank() ? "/" : uri.getPath());
      String query = cleanQuery(uri.getQuery());
      if (!query.isBlank()) rebuilt.append('?').append(query);
      url = rebuilt.toString();
    } else if (url.startsWith("jdbc:postgresql://")) {
      int q = url.indexOf('?');
      if (q >= 0) {
        String query = cleanQuery(url.substring(q + 1));
        url = url.substring(0, q) + (query.isBlank() ? "" : "?" + query);
      }
    } else {
      throw new IllegalStateException(
          "Database URL must start with jdbc:postgresql://, postgresql:// or postgres://. "
              + "Got something beginning \"" + url.substring(0, Math.min(12, url.length())) + "\".");
    }

    // Managed Postgres requires TLS, and forgetting it fails in a confusing way.
    if (!url.contains("sslmode=")) {
      url += (url.contains("?") ? "&" : "?") + "sslmode=require";
    }

    HikariDataSource ds = new HikariDataSource();
    ds.setJdbcUrl(url);
    if (!user.isBlank()) ds.setUsername(user);
    if (!password.isBlank()) ds.setPassword(password);
    ds.setMaximumPoolSize(5);
    ds.setConnectionTimeout(10000);

    // The URL carries no credentials by this point, so it is safe to log.
    System.out.println("Connecting to " + url);
    return ds;
  }

  /** Drops parameters the driver rejects, keeping the rest in order. */
  private static String cleanQuery(String query) {
    if (query == null || query.isBlank()) return "";
    List<String> kept = new ArrayList<>();
    for (String param : query.split("&")) {
      if (param.isBlank()) continue;
      String key = param.contains("=") ? param.substring(0, param.indexOf('=')) : param;
      if (!UNSUPPORTED_PARAMS.contains(key)) kept.add(param);
    }
    return String.join("&", kept);
  }
}
