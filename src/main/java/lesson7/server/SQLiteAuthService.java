package lesson7.server;

import java.sql.*;
import java.util.Optional;

public class SQLiteAuthService implements AuthService {

    private static Connection connection;
    private static Statement statement;

    @Override
    public void start() {
        try {
            connect();
            createTable();
            //insertUsers();
            //dropTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/java/lesson7/server/chat_db.db");
        statement = connection.createStatement();
    }

    private void createTable() throws SQLException {
        statement.executeUpdate("create table if not exists users (\n" +
                "    id integer primary key autoincrement not null,\n" +
                "    login text not null,\n" +
                "    password text not null,\n" +
                "    nick text not null\n" +
                ");");
    }

    private static void insertUsers() throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "insert into users (login, password, nick) " +
                        "values (?, ?, ?)")) {
            for (int i = 0; i < 3; i++) {
                ps.setString(1, "login" + i);
                ps.setString(2, "pass" + i);
                ps.setString(3, "nick" + i);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void stop() {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Optional<String> getNickByLoginAndPass(String login, String password) {
        try (ResultSet rs = statement.executeQuery(
                "select * from users " +
                        "where " +
                        " users.login = " + "'" + login + "'" + " AND " + " users.password = " + "'" + password + "'" +
                        " LIMIT 1")) {
            return Optional.ofNullable(rs.getString("nick"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private static void dropTable() throws SQLException {
        statement.executeUpdate("drop table users");
    }
}