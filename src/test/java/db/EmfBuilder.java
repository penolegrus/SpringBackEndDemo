package db;

import javax.persistence.EntityManagerFactory;

public class EmfBuilder {
    private final ConnConfig connConfig = new ConnConfig();

    public EmfBuilder postgres() {
        connConfig.setJdbcClass("org.postgresql.ds.PGSimpleDataSource");
        connConfig.setJdbcPrefix("jdbc:postgresql");
        connConfig.setDialect("org.hibernate.dialect.PostgreSQL94Dialect");
        return this;
    }

    public EmfBuilder mySql() {
        connConfig.setJdbcClass("com.mysql.cj.jdbc.MysqlDataSource");
        connConfig.setJdbcPrefix("jdbc:mysql");
        connConfig.setDialect("org.hibernate.dialect.MySQL5InnoDBDialect");
        return this;
    }

    public EmfBuilder h2() {
        connConfig.setJdbcClass("org.h2.jdbcx.JdbcDataSource");
        connConfig.setJdbcPrefix("jdbc:h2");
        connConfig.setDialect("org.hibernate.dialect.H2Dialect");
        return this;
    }

    public EmfBuilder jdbcUrl(String jdbcUrl) {
        connConfig.setJdbcUrl(jdbcUrl);
        return this;
    }

    public EmfBuilder dbHost(String dbHost) {
        connConfig.setDbHost(dbHost);
        return this;
    }

    public EmfBuilder dbName(String dbName) {
        connConfig.setDbName(dbName);
        return this;
    }

    public EmfBuilder username(String username) {
        connConfig.setUsername(username);
        return this;
    }

    public EmfBuilder password(String password) {
        connConfig.setPassword(password);
        return this;
    }

    public EmfBuilder persistenceUnitName(String persistenceUnitName) {
        connConfig.setPersistenceUnitName(persistenceUnitName);
        return this;
    }

    public EmfBuilder dbPort(int dbPort) {
        connConfig.setDbPort(dbPort);
        return this;
    }

    public EmfBuilder hibernateDialect(String dialect) {
        connConfig.setDialect(dialect);
        return this;
    }

    /**
     * Билдит объект с параметрами EntityManagerFactory
     */
    public EntityManagerFactory build() {
        return new ThreadSafeEntityManagerFactory(EmfContext.INSTANCE.get(connConfig.validate()));
    }
}
