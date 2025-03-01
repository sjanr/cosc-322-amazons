package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Minimax {

    public int execMinimax(Board board, int depth, boolean isMax, int playerId) {
        int opponantId = playerId == 1 ? 2:1;
        
        if(depth == 0 || board.isGameOver()) {
            return board.getUtility(playerId);
        }

        if(isMax) {
            int bestMove = Integer.MIN_VALUE;
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(1, board);
            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, playerId);
                int value = execMinimax(testMove, depth - 1, !isMax, playerId);
                
                bestMove = Math.max(value, bestMove);
            }

            return bestMove;
        } else {
            int bestMove = Integer.MAX_VALUE;
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(2, board);
            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, opponantId);
                int value = execMinimax(testMove, depth - 1, !isMax, playerId);
                
                bestMove = Math.min(value, bestMove);
            }

            return bestMove;
        }
    }

}