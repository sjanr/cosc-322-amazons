package ubc.cosc322;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Minimax {

    private ZobristHash hash;
    private Map<Long, List<Object>> transpositionTable;
    private int tableUsed = 0;
    private int branchesPruned = 0;


    public Minimax(ZobristHash hash) {
        this.hash = hash;
        this.transpositionTable = new HashMap<>();
    }

    public Minimax() { //overload constructor for old minimax without memoization.
        
    }

    //This is basic minimax algorithm. Following this function is algorithm with Alpha-Beta pruning.
    //normal node returns will look like List<Map<String, ArrayList<Integer>>>
    //terminal node returns will only be List<Object> of arraylist with just the utility result.
    public List<Object> execMinimax(Board board, int depth, boolean isMax, int playerId) {
        int opponantId = playerId == 1 ? 2:1;

        if(depth == 0 || board.isGameOver()) {
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId)); //base case only has a utility. No move
            return result;
        }

        int bestMove;
        List<Object> bestResult = new ArrayList<>();
        Map<String, ArrayList<Integer>> bestMoveAction = null;
        
        if(isMax) { //MAXIMIZER (playerId)
            bestMove = Integer.MIN_VALUE;
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(playerId, board);
            for (Map<String, ArrayList<Integer>> move : moves) { //for every child node
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

        } else { //MINIMIZER (opponantId)
            bestMove = Integer.MAX_VALUE;
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(opponantId, board);
            for (Map<String, ArrayList<Integer>> move : moves) { //for every child node
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
        }
        
        //addd best value and move to return.
        bestResult.add(bestMove);
        bestResult.add(bestMoveAction);

        return bestResult;
    }
 
    //Alpha-Beta pruning added to minimax algorithm.
    public List<Object> execAlphaBetaMinimax(Board board, int depth, boolean isMax, int playerId, int alpha, int beta, Instant endTime) {
        int opponantId = playerId == 1 ? 2:1;
        int bestMove;
        List<Object> bestResult = new ArrayList<>();
        Map<String, ArrayList<Integer>> bestMoveAction = null;

        if(depth == 0 || board.isGameOver() || Instant.now().isAfter(endTime)) { //if time limit exceeded.
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId)); //base case only has a utility. No move
            return result;
        }

        long boardHash = hash.computeBoardHash(board);
        if(transpositionTable.containsKey(boardHash)) {
            bestResult = transpositionTable.get(boardHash);
            tableUsed++;
            // System.out.println("FOUND HASH " + ++tableUsed);
        }


        if(isMax) { //MAXIMIZER (playerId)
            bestMove = Integer.MIN_VALUE;
          
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(playerId, board);
            for (Map<String, ArrayList<Integer>> move : moves) { //for each child of node recursive call till terminal
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, playerId);
                
                List<Object> res = execAlphaBetaMinimax(testMove, depth - 1, false, playerId, alpha, beta, endTime);
                int value = (int) res.get(0); //get utility the returned recursive call
                // int res = execAlphaBetaMinimax(testMove, depth - 1, false, playerId, alpha, beta);
                // int value = res;
                
                // bestMove = Math.max(value, bestMove);
                if(value > bestMove) { //instead of just taking the max we check the values and store value and move.
                    bestMove = value;
                    bestMoveAction = move;
                }

                alpha = Math.max(alpha, bestMove);

                if(beta <= alpha) {
                    // System.out.println("branch pruned");
                    branchesPruned++;
                    break; //prune branches.
                }

                
            }
            
        } else { //MINIMIZER (opponantId)
            bestMove = Integer.MAX_VALUE;
            
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(opponantId, board);
            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard()); //make copy of board and simulate move
                testMove.updateGameboard(move, opponantId);
              
                List<Object> res = execAlphaBetaMinimax(testMove, depth - 1, true, playerId, alpha, beta, endTime);
                int value = (int) res.get(0);
                // int res = execAlphaBetaMinimax(testMove, depth - 1, true, playerId, alpha, beta);
                // int value = res;
                
                // bestMove = Math.min(value, bestMove);
                if(value < bestMove) { //instead of just taking the min we check the value and store the value and move.
                   bestMove = value;
                   bestMoveAction = move;
                }

                beta = Math.min(beta, bestMove);

                if(beta <= alpha) {
                    branchesPruned++;
                    // System.out.println("branch pruned");
                    break; //prune branch
                }
            }
            
        }


        //addd best value and move to return.
        bestResult.add(bestMove);
        bestResult.add(bestMoveAction);

        //add move to table (memoization)
        transpositionTable.put(boardHash, bestResult);

        return bestResult;
        // return bestMove;
    }

    public int getTableUsed() {
        return this.tableUsed;
    }

    public int getBranchesPruned() {
        return this.branchesPruned;
    }



}

//I AM WHITE PLAYER