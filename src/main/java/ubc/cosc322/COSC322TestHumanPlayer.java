package ubc.cosc322;

import java.util.Map;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;
import ygraph.ai.smartfox.games.amazons.HumanPlayer;

/**
 * Launches a human-controlled player for the Game of Amazons.
 */
public class COSC322TestHumanPlayer extends GamePlayer {

    private GameClient gameClient = null;
    private BaseGameGUI gamegui = null;

    private String userName = null;
    private String passwd = null;

    /**
     * Entry point for launching the human player.
     */
    public static void main(String[] args) {
        GamePlayer player = new HumanPlayer();

        if (player.getGameGUI() == null) {
            player.Go();
        } else {
            BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(() -> player.Go());
        }
    }

    /**
     * Constructor for a human player with username and password.
     */
    public COSC322TestHumanPlayer(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
        this.gamegui = new BaseGameGUI(this);
    }

    @Override
    public void onLogin() {
        // Optional: handle post-login actions
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
        // Handle incoming game messages (e.g., moves, state updates)
        return true;
    }

    @Override
    public String userName() {
        return userName;
    }

    @Override
    public GameClient getGameClient() {
        return this.gameClient;
    }

    @Override
    public BaseGameGUI getGameGUI() {
        return this.gamegui;
    }

    @Override
    public void connect() {
        gameClient = new GameClient(userName, passwd, this);
    }

}
