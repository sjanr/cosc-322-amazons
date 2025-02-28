
package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;

/**
 * An example illustrating how to implement a GamePlayer
 * @author Yong Gao (yong.gao@ubc.ca)
 * Jan 5, 2021
 *
 */
public class COSC322Test extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;

	private Board board;
 
	
    /**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
    	// COSC322Test player = new COSC322Test(args[0], args[1]);
		COSC322Test player = new COSC322Test("cosc322", "cosc322");

    	if(player.getGameGUI() == null) {
    		player.Go();
    	}
    	else {
    		BaseGameGUI.sys_setup();
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                	player.Go();
                }
            });
    	}
    }
	
    /**
     * Any name and passwd 
     * @param userName
      * @param passwd
     */
    public COSC322Test(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	
    	//To make a GUI-based player, create an instance of BaseGameGUI
    	//and implement the method getGameGUI() accordingly
    	this.gamegui = new BaseGameGUI(this);

		this.board = new Board();
    }
 


    @Override
    public void onLogin() {
    	// System.out.println("Congratualations!!! "
    	// 		+ "I am called because the server indicated that the login is successfully");
    	// System.out.println("The next step is to find a room and join it: "
    	// 		+ "the gameClient instance created in my constructor knows how!"); 

		
		userName = gameClient.getUserName();
		if(gamegui != null) {
			gamegui.setRoomInformation(gameClient.getRoomList());
		}

		gameClient.joinRoom("Bear Lake");
    }

    @Override
    public boolean handleGameMessage(String messageType, Map<String, Object> msgDetails) {
    	//This method will be called by the GameClient when it receives a game-related message
    	//from the server.
		System.out.println("MSG RXD: " + messageType);

		if(messageType.equals("cosc322.game-state.board")) {
			System.out.println("RXD Board: " + msgDetails.get("game-state"));
			gamegui.setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
			
			board.setGameboard((ArrayList<Integer>)msgDetails.get("game-state"));


			
		} else if (messageType.equals("cosc322.game-action.move")) {
			System.out.println("RXD Move Msg: " + msgDetails);


			ArrayList<Integer> queenCurr =(ArrayList<Integer>) msgDetails.get("queen-position-current");
			ArrayList<Integer> queenNext =(ArrayList<Integer>) msgDetails.get("queen-position-next");
			ArrayList<Integer> arrowPos =(ArrayList<Integer>) msgDetails.get("arrow-position");
			

			gamegui.updateGameState(queenCurr, queenNext, arrowPos);
			board.updateGameboard(queenCurr, queenNext, arrowPos, 2);

			// System.out.println("UPDATING");
		
			ActionFactory af = new ActionFactory();
			List<Map<String,ArrayList<Integer>>> possibleActions = af.getActions(1, board);

			Random rand = new Random();
			Map<String, ArrayList<Integer>> randomAction = possibleActions.get(rand.nextInt(possibleActions.size()));			
			System.out.println("MY MOVE: " + randomAction.get("queen-position-current") +", "+ randomAction.get("queen-position-next") +", "+ randomAction.get("arrow-position"));
			gameClient.sendMoveMessage(randomAction.get("queen-position-current"), randomAction.get("queen-position-next"), randomAction.get("arrow-position"));
			gamegui.updateGameState(randomAction.get("queen-position-current"), randomAction.get("queen-position-next"), randomAction.get("arrow-position"));
			board.updateGameboard(randomAction, 1);
		}



    	//For a detailed description of the message types and format, 
    	//see the method GamePlayer.handleGameMessage() in the game-client-api document. 
    	return true;   	
    }
    
    
    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// TODO Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// TODO Auto-generated method stub

		return this.gamegui;
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}

 
}//end of class
