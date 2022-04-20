package lesson7.server;

/**
 * Сервис аутентификации
 */
public interface AuthService {

    /**
     * Запустить сервис
     */
    void start();

    /**
     * Отключить сервис
     */
    void stop();

    /**
     * Получить никнейм по логину и паролю
     * @param login - логин пользователя
     * @param password - пароль пользователя
     * @return никнейм найден = никнейм, не найден = null
     */
    String getNickByLoginAndPass(String login, String password);
}
