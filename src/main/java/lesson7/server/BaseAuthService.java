package lesson7.server;

import java.util.ArrayList;
import java.util.List;

public class BaseAuthService implements AuthService {

    private List<Entry> entries;

    private class Entry {
        private String login;
        private String password;
        private String nick;

        public Entry(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }

    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("login1", "pass1", "nick1"));
        entries.add(new Entry("login2", "pass2", "nick2"));
        entries.add(new Entry("login3", "pass3", "nick3"));
    }

    @Override
    public void start() {
        System.out.println("Сервис аунтефикации запущен.");
    }

    @Override
    public void stop() {
        System.out.println("Сервис аунтефикации остановлен.");
    }

    @Override
    public String getNickByLoginAndPass(String login, String password) {
        for (Entry entry : entries) {
            if (entry.login.equals(login) && entry.password.equals(password)) {
                return entry.nick;
            }
        }
        return null;
    }
}
