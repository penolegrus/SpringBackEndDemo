package db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.*;

public enum EmfContext {
    INSTANCE;

    private final Logger log = LoggerFactory.getLogger(EmfContext.class);
    private final Map<ConnConfig, EntityManagerFactory> container = new HashMap<>();

    /**
     * Получает существующий или новый экземпляр EntityManagerFactory.
     */
    synchronized EntityManagerFactory get(ConnConfig connConfig) {
        if (container.containsKey(connConfig))
            return container.get(connConfig);
        else {
            log.warn("### Init EntityManagerFactory ###");

            Map<String, String> settings = new HashMap<>();
            settings.put(
                    "hibernate.connection.provider_class",
                    "org.hibernate.hikaricp.internal.HikariCPConnectionProvider"
            );
            settings.put("hibernate.hikari.dataSourceClassName", connConfig.getJdbcClass());
            settings.put("hibernate.hikari.maximumPoolSize", "32");
            settings.put("hibernate.hikari.minimumIdle", "0");
            settings.put("hibernate.hikari.idleTimeout", "240000");
            settings.put("hibernate.hikari.maxLifetime", "270000");
            settings.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
            settings.put("hibernate.hikari.dataSource.url",
                    Objects.requireNonNull(connConfig.getJdbcUrl(),
                            () -> connConfig.getJdbcPrefix() + "://" + connConfig.getDbHost() + ":" + connConfig.getDbPort() + "/" + connConfig.getDbName()));
            settings.put("hibernate.hikari.dataSource.user", connConfig.getUsername());
            settings.put("hibernate.hikari.dataSource.password", connConfig.getPassword());
            settings.put("hibernate.dialect", connConfig.getDialect());

            EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory(connConfig.getPersistenceUnitName(), settings);
            container.put(connConfig, entityManagerFactory);
            return entityManagerFactory;
        }
    }

    /**
     * Получает все EntityManagerFactories.
     */
    Collection<EntityManagerFactory> storedEmf() {
        return container.values();
    }
}
