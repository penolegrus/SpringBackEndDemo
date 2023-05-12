package db;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@EqualsAndHashCode
@Setter
@Getter
public class ConnConfig {
    private String dbHost;
    private Integer dbPort;
    private String dbName;

    private String username;
    private String password;
    private String persistenceUnitName;

    private String jdbcClass;
    private String jdbcPrefix;
    private String dialect;

    private String jdbcUrl;

    ConnConfig validate() {
        if (jdbcUrl == null) {
            if (dbHost == null)
                throw new IllegalStateException("db host must not be null.");
            if (dbName == null)
                throw new IllegalStateException("db name must not be null.");
            if (dbPort == null)
                throw new IllegalStateException("db port must not be null.");
            if (jdbcPrefix == null)
                throw new IllegalStateException("JDBC prefix must not be null. For example: jdbc:postgresql or jdbc:mysql");
        }

        if (username == null)
            throw new IllegalStateException("db username must not be null.");
        if (password == null)
            throw new IllegalStateException("db password must not be null.");
        if (persistenceUnitName == null)
            throw new IllegalStateException("persistence unit name must not be null.");
        if (jdbcClass == null)
            throw new IllegalStateException("JDBC class name must not be null.");
        if (dialect == null)
            throw new IllegalStateException("Hibernate dialect must not be null.");
        return this;
    }
}
