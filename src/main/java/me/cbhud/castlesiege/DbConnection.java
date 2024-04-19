package me.cbhud.castlesiege;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.*;
import java.util.UUID;

public class DbConnection {
    private final Main plugin;
    private Connection connection;

    public DbConnection(Main plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        try {
            // Create an H2 data source
            JdbcDataSource dataSource = new JdbcDataSource();
            dataSource.setURL("jdbc:h2:" + plugin.getDataFolder().getAbsolutePath() + "/data");

            // Connect to H2 database
            connection = dataSource.getConnection();

            // Create the table if it doesn't exist
            try (Statement statement = connection.createStatement()) {
                String createTableSQL = "CREATE TABLE IF NOT EXISTS player_stats (" +
                        "uuid VARCHAR(36) PRIMARY KEY," +
                        "username VARCHAR(16) NOT NULL," +
                        "wins INT NOT NULL DEFAULT 0," +
                        "kills INT NOT NULL DEFAULT 0," +
                        "deaths INT NOT NULL DEFAULT 0," +
                        "king_kills INT NOT NULL DEFAULT 0" +
                        ")";
                statement.executeUpdate(createTableSQL);
                plugin.getLogger().info("[CastleSiege] Stats loaded successfully!");
            } catch (SQLException e) {
                plugin.getLogger().severe("Error creating player stats table: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                plugin.getLogger().severe("Error closing database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    public boolean hasData(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM player_stats WHERE uuid = ? LIMIT 1")) {
            statement.setString(1, uuid.toString());
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void createProfileIfNotExists(UUID playerUUID, String username) {
        if (!hasData(playerUUID)) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO player_stats (uuid, username, wins, kills, deaths, king_kills) " +
                            "VALUES (?, ?, 0, 0, 0, 0)")) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, username);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void incrementWins(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET wins = wins + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementKills(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET kills = kills + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void incrementDeaths(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET deaths = deaths + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void incrementKingKills(UUID uuid) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE player_stats SET king_kills = king_kills + 1 WHERE uuid = ?");
            statement.setString(1, uuid.toString());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}