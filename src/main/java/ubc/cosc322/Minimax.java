package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Minimax {

    // public int execMinimax(Board board, int depth, boolean isMax, int playerId) {
    //     int opponantId = playerId == 1 ? 2:1;
        
    //     if(depth == 0 || board.isGameOver()) {
    //         return board.getUtility(playerId);
    //     }

    //     if(isMax) {
    //         // System.out.println("max");
    //         int bestMove = Integer.MIN_VALUE;
    //         ActionFactory af = new ActionFactory();
    //         List<Map<String, ArrayList<Integer>>> moves = af.getActions(1, board);
    //         for (Map<String, ArrayList<Integer>> move : moves) {
    //             Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
    //             testMove.updateGameboard(move, playerId);
    //             int value = execMinimax(testMove, depth - 1, !isMax, playerId);
                
    //             bestMove = Math.max(value, bestMove);
    //         }

    //         return bestMove;
    //     } else {
    //         // System.out.println("min");
    //         int bestMove = Integer.MAX_VALUE;
            
    //         ActionFactory af = new ActionFactory();
    //         List<Map<String, ArrayList<Integer>>> moves = af.getActions(2, board);
    //         for (Map<String, ArrayList<Integer>> move : moves) {
    //             Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
    //             testMove.updateGameboard(move, opponantId);
    //             int value = execMinimax(testMove, depth - 1, !isMax, playerId);
                
    //             bestMove = Math.min(value, bestMove);
    //         }

    //         return bestMove;
    //     }
    // }

    public List<Object> execMinimax(Board board, int depth, boolean isMax, int playerId) {
        int opponantId = playerId == 1 ? 2 : 1;
    
        // Base case: if depth is 0 or the game is over
        if (depth == 0 || board.isGameOver()) {
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId));  // score
            result.add(null);  // No move for the base case
            return result;
        }
    
        List<Object> bestResult = new ArrayList<>();
        
        // Initialize best move and value
        int bestMoveScore;
        Map<String, ArrayList<Integer>> bestMove = null;
    
        // ActionFactory to generate possible moves
        ActionFactory af = new ActionFactory();
        List<Map<String, ArrayList<Integer>>> moves = af.getActions(isMax ? 1 : 2, board);
    
        if (isMax) {
            // Maximizing player, start with a very low value
            bestMoveScore = Integer.MIN_VALUE;
        } else {
            // Minimizing player, start with a very high value
            bestMoveScore = Integer.MAX_VALUE;
        }
    
        for (Map<String, ArrayList<Integer>> move : moves) {
            Board testMove = new Board(board.getGameboard()); // Make a copy of the board
            testMove.updateGameboard(move, isMax ? playerId : opponantId);
    
            // Recursively call minimax on the new board state
            List<Object> result = execMinimax(testMove, depth - 1, !isMax, playerId);
            int currentScore = (int) result.get(0); // Get the score from the result
    
            // Maximize or minimize the score based on current turn
            if (isMax) {
                if (currentScore > bestMoveScore) {
                    bestMoveScore = currentScore;
                    bestMove = move;
                }
            } else {
                if (currentScore < bestMoveScore) {
                    bestMoveScore = currentScore;
                    bestMove = move;
                }
            }
        }
    
        // Return the best score and move found
        bestResult.add(bestMoveScore);  // score
        bestResult.add(bestMove);       // best move
        return bestResult;
    }
    
}