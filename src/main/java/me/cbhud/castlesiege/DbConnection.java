package me.cbhud.castlesiege;

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
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL driver
            connection = DriverManager.getConnection("jdbc:mysql://" + plugin.getConfigManager().getHost() + ":" + plugin.getConfigManager().getPort() + "/" + plugin.getConfigManager().getDatabase(), plugin.getConfigManager().getUsername(), plugin.getConfigManager().getPassword());
            plugin.getLogger().info("Connected to MySQL database!");

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
        } catch (ClassNotFoundException e) {
            plugin.getLogger().severe("MySQL JDBC driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            plugin.getLogger().severe("Failed to connect to MySQL database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (connection != null) {
            try {
                connection.close();
                plugin.getLogger().info("MySQL database connection closed.");
            } catch (SQLException e) {
                plugin.getLogger().severe("Error closing MySQL database connection: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() {
        return connection != null;
    }






    /////


    public boolean hasData(UUID uuid) {
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            String query = "SELECT COUNT(*) FROM player_stats WHERE uuid = ?";
            statement = connection.prepareStatement(query);
            statement.setString(1, uuid.toString());
            rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }



    public void createProfileIfNotExists(UUID playerUUID, String username) {
        if (!hasData(playerUUID)) {
            PreparedStatement statement = null;
            try {
                String query = "INSERT INTO player_stats (uuid, username, wins, kills, deaths, king_kills) " +
                        "VALUES (?, ?, 0, 0, 0, 0)";
                statement = connection.prepareStatement(query);
                statement.setString(1, playerUUID.toString());
                statement.setString(2, username);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (statement != null) {
                        statement.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
