// PostgresqlConnectionProperties.java
package com.peryloth.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "adapters.r2dbc")
public class PostgresqlConnectionProperties {
    private String host;
    private Integer port;
    private String database;
    private String schema;
    private String username;
    private String password;

    public String host() { return host; }
    public void setHost(String host) { this.host = host; }

    public Integer port() { return port; }
    public void setPort(Integer port) { this.port = port; }

    public String database() { return database; }
    public void setDatabase(String database) { this.database = database; }

    public String schema() { return schema; }
    public void setSchema(String schema) { this.schema = schema; }

    public String username() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String password() { return password; }
    public void setPassword(String password) { this.password = password; }
}