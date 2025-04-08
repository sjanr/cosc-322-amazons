/*
 * The new advanced player is minimax with alpha beta pruning, iterative deepening, 
 * and with memoization for "cache"-ing moves and remembering it for subsequent 
 * searches. It also uses voronoi diagram heuristic for determining utility of a game state.
 */

package ubc.cosc322;

import java.time.Instant;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import ygraph.ai.smartfox.games.BaseGameGUI;
import ygraph.ai.smartfox.games.GameClient;
import ygraph.ai.smartfox.games.GamePlayer;

public class TeamPlayer extends GamePlayer{

    private GameClient gameClient = null; 
    private BaseGameGUI gamegui = null;
	
    private String userName = null;
    private String passwd = null;

	private Board board;
	private int playerId;
	private int opponantId;
 
	private ZobristHash zHash;
	private Minimax m;

	/**
     * The main method
     * @param args for name and passwd (current, any string would work)
     */
    public static void main(String[] args) {				 
		TeamPlayer player = new TeamPlayer("Team#18", "cosc322");

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

    public TeamPlayer(String userName, String passwd) {
    	this.userName = userName;
    	this.passwd = passwd;
    	this.gamegui = new BaseGameGUI(this);
		this.board = new Board();
    }

    @Override
    public void onLogin() {
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
		if(messageType.equals("cosc322.game-state.board")) {
			System.out.println("Rx'd Board: " + msgDetails.get("game-state"));
			System.out.println("Initializing Game Board locally and gamegui.");
			gamegui.setGameState((ArrayList<Integer>) msgDetails.get("game-state"));
			board.setGameboard((ArrayList<Integer>)msgDetails.get("game-state"));
			//Set up memoization
			this.zHash = new ZobristHash(board);
			this.m = new Minimax(zHash);
			
		} else if (messageType.equals("cosc322.game-action.move")) {
			System.out.println("Opponant's Move: " + msgDetails); 
			//Gets board info and stores in an arraylist for local use.
			ArrayList<Integer> queenCurr =(ArrayList<Integer>) msgDetails.get("queen-position-current");
			ArrayList<Integer> queenNext =(ArrayList<Integer>) msgDetails.get("queen-position-next");
			ArrayList<Integer> arrowPos =(ArrayList<Integer>) msgDetails.get("arrow-position");

			// Combine opponent move parts into a single map for checking
			Map<String, ArrayList<Integer>> opponentMove = new HashMap<>();
			opponentMove.put("queen-position-current", queenCurr);
			opponentMove.put("queen-position-next", queenNext);
			opponentMove.put("arrow-position", arrowPos);

			// Validate opponents move and proceed to update GUI
			board.checkMove(opponentMove, opponantId);

			//Update gui and local board variable
			gamegui.updateGameState(queenCurr, queenNext, arrowPos);
			board.updateGameboard(queenCurr, queenNext, arrowPos, opponantId);

			if(board.isGameOver()) {
				System.err.println("GAME OVER; Good game.");
				System.out.println("Overall Branches Pruned: " + m.getBranchesPruned());
				System.out.println("Overall Successful Table References: " + m.getTableUsed());
				return false;
			}
			//return with move.
			makeMove();

		} else if(messageType.equals("cosc322.game-action.start")) {
			System.out.println("GAME START.");
			String whiteQueens = (String) msgDetails.get("player-white");
			String blackQueens = (String) msgDetails.get("player-black");

			System.out.println("Player White is " + whiteQueens);
			System.out.println("Player Black is " + blackQueens);
			System.out.println(blackQueens + " goes first.");

			playerId = whiteQueens.equals(this.userName) ? 1:2; //Set player Id based on active player usernames;
			opponantId = playerId==1 ? 2:1; //Set opponantId based on playerId;

			if(playerId == 2) {
				//if player 2, start by making a move. //Black moves first, according to lecture.
				System.out.println("I MOVE FIRST.");
				makeMove();
			} else {
				//if p1 do nothing. wait for move msg rx.			
				System.out.println("I MOVE SECOND.");
			}
		}

    	return true;   	
    }

	public void makeMove() { //for quick switching between random moves, minimax and minmax with alpha beta pruning methods.
		makeAlphaBetaMove();
	}

	// This method is for a MinMax move with alpha-beta pruning.
	public void makeAlphaBetaMove() {
		//Setup timing max of 24 seconds before exiting recusive stack to avoid timeout.
		Instant timeNow = Instant.now();
		Duration dur = Duration.ofSeconds(24);
		Instant timeEnd = timeNow.plus(dur);
		int depth = 1;
		List<Object> minimax = null;
	
		while(Instant.now().isBefore(timeEnd)) { //while time is not up, keep searching for best move.
			//stores the best move found so far
			List<Object> tempSaveMove = m.execAlphaBetaMinimax(board, depth++, true, playerId, Integer.MIN_VALUE, Integer.MAX_VALUE, timeEnd);
			System.out.println("Found move (Utility: " + tempSaveMove.get(0) + ") at depth: " + depth);
			if(minimax == null || ((Integer) tempSaveMove.get(0) > (Integer) minimax.get(0))) { //if previous found move better than now or if first run rewrite best move.
				minimax = tempSaveMove;
				System.out.println("Using better move.");
			}

		}
		
		// Retrieve the best move (Map<String, ArrayList<Integer>>)
		Map<String, ArrayList<Integer>> bestMove = (Map<String, ArrayList<Integer>>) minimax.get(1);

		// Validate best move
		board.checkMove(bestMove, playerId);

		// Access the "queen-position-current" and "queen-position-next" from the best move
		ArrayList<Integer> queen_pos_curr = bestMove.get("queen-position-current");
		ArrayList<Integer> queen_pos_next = bestMove.get("queen-position-next");
		ArrayList<Integer> arrow_pos = bestMove.get("arrow-position");
		System.out.println("MY Alpha-Beta MOVE: " + queen_pos_curr +", "+ queen_pos_next +", "+ arrow_pos);
		
		//Update client, gui, and local board of move.
		gameClient.sendMoveMessage(queen_pos_curr, queen_pos_next, arrow_pos);
		gamegui.updateGameState(queen_pos_curr, queen_pos_next, arrow_pos);
		board.updateGameboard(queen_pos_curr, queen_pos_next, arrow_pos, playerId);
	}

	
	// This method is for a MinMax move without alpha-beta pruning.
	// It is not used in the final version of the player, but is kept for reference.
	/*
	public void makeMinMaxMove() {
		Minimax mOld = new Minimax();
		List<Object> minimax = mOld.execMinimax(board, 1, true, playerId);

		// Retrieve the best move (Map<String, ArrayList<Integer>>)
		Map<String, ArrayList<Integer>> bestMove = (Map<String, ArrayList<Integer>>) minimax.get(1);

		// Access the "queen-position-current" and "queen-position-next" from the best move
		ArrayList<Integer> queen_pos_curr = bestMove.get("queen-position-current");
		ArrayList<Integer> queen_pos_next = bestMove.get("queen-position-next");
		ArrayList<Integer> arrow_pos = bestMove.get("arrow-position");

		System.out.println("MY Minimax MOVE: " + queen_pos_curr +", "+ queen_pos_next +", "+ arrow_pos);
		//Update client, gui, and local board of move.
		gameClient.sendMoveMessage(queen_pos_curr, queen_pos_next, arrow_pos);
		gamegui.updateGameState(queen_pos_curr, queen_pos_next, arrow_pos);
		board.updateGameboard(queen_pos_curr, queen_pos_next, arrow_pos, playerId);
	}
	*/

	// This method is for a random move.
	// It is not used in the final version of the player, but is kept for reference.
	/*
	public void makeRandomMove() {
		ActionFactory af = new ActionFactory();
		List<Map<String,ArrayList<Integer>>> possibleActions = af.getActions(playerId, board);

		Random rand = new Random();
		Map<String, ArrayList<Integer>> randomAction = null;
		try { //try getting a random action, if unable game over.
			randomAction = possibleActions.get(rand.nextInt(possibleActions.size()));			
		} catch (IllegalArgumentException i) {
			System.err.println("GAME OVER; Good game.");
		}
		
		ArrayList<Integer> queen_pos_curr = randomAction.get("queen-position-current");
		ArrayList<Integer> queen_pos_next = randomAction.get("queen-position-next");
		ArrayList<Integer> arrow_pos = randomAction.get("arrow-position");
		
		System.out.println("MY Random MOVE: " + queen_pos_curr +", "+ queen_pos_next +", "+ arrow_pos);
		//Update client, gui, and local board of move.
		gameClient.sendMoveMessage(queen_pos_curr, queen_pos_next, arrow_pos);
		gamegui.updateGameState(queen_pos_curr, queen_pos_next, arrow_pos);
		board.updateGameboard(randomAction, playerId);
	}
	*/

    @Override
    public String userName() {
    	return userName;
    }

	@Override
	public GameClient getGameClient() {
		// Auto-generated method stub
		return this.gameClient;
	}

	@Override
	public BaseGameGUI getGameGUI() {
		// Auto-generated method stub

		return this.gamegui;
	}

	@Override
	public void connect() {
		// Auto-generated method stub
    	gameClient = new GameClient(userName, passwd, this);			
	}
}
