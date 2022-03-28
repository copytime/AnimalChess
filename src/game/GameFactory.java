package game;

public class GameFactory {
    public static GameFactory gameFactory = null;

    public static GameFactory getInstance() {
        return gameFactory;
    }

    private IGameManager gameManager = null;

    public GameFactory() {
        gameFactory = null;
        gameManager = GameManagerServer.getNewInstance();
        gameFactory = this;
    }

    public GameFactory(String ipAddr) {
        gameFactory = null;

        gameManager = GameManagerClient.getNewInstance(ipAddr);

        gameFactory = this;
    }

    public IGameManager getGameManager() {
        return gameManager;
    }
}
