package simple.dao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class pool {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        System.out.println("10");
        config.setDriverClassName("org.postgresql.Driver");

        config.setJdbcUrl("jdbc:postgresql://localhost:5432/скуфеу");
        config.setUsername("postgres");
        config.setPassword("postgres");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        System.out.println("11");
        ds = new HikariDataSource(config);
        System.out.println("12");
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
