package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Minimax {
    
    public List<Object> execMinimax(Board board, int depth, boolean isMax, int playerId) {
        int opponantId = playerId == 1 ? 2:1;

        if(depth == 0 || board.isGameOver()) {
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId)); //base case only has a utility. No move
            return result;
        }

        if(isMax) { //MAXIMIZER (playerId)
            System.out.println("max");
            int bestMove = Integer.MIN_VALUE;
            Map<String, ArrayList<Integer>> bestMoveAction = null;
            List<Object> bestResult = new ArrayList<>();
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(playerId, board);
            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, playerId);
                
                List<Object> res = execMinimax(testMove, depth - 1, !isMax, playerId);
                int value = (int) res.get(0);
                
                // bestMove = Math.max(value, bestMove);
                if(value > bestMove) { //instead of just taking the max we check the values and store value and move.
                    bestMove = value;
                    bestMoveAction = move;
                }

            }

            //Add best value and move to return.
            bestResult.add(bestMove);
            bestResult.add(bestMoveAction);
            return bestResult;

        } else { //MINIMIZER (opponantId)
            System.out.println("min");
            int bestMove = Integer.MAX_VALUE;
            Map<String, ArrayList<Integer>> bestMoveAction = null;
            List<Object> bestResult = new ArrayList<>();

            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(opponantId, board);
            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, opponantId);
              
                List<Object> res = execMinimax(testMove, depth - 1, !isMax, playerId);
                int value = (int) res.get(0);
                
                
                // bestMove = Math.min(value, bestMove);
                if(value < bestMove) { //instead of just taking the min we check the value and store the value and move.
                   bestMove = value;
                   bestMoveAction = move;
                }
            }

            //Add best value and move to return.
            bestResult.add(bestMove);
            bestResult.add(bestMoveAction);
            return bestResult;
        }
    }
    
    
}