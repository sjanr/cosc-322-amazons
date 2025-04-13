/*
 * Implements the Minimax algorithm with Alpha-Beta pruning and transposition table support.
 * Used by TeamPlayer.java to compute the best move under time and depth constraints.
 */
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

    // Fallback constructor for non-memoized Minimax (unused in final version)
    public Minimax() {}

    /**
     * Executes Minimax with Alpha-Beta pruning and transposition table memoization.
     */
    public List<Object> execAlphaBetaMinimax(Board board, int depth, boolean isMax, int playerId, int alpha, int beta, Instant endTime) {
        int opponantId = (playerId == 1) ? 2 : 1;
        int bestMove;
        List<Object> bestResult = new ArrayList<>();
        Map<String, ArrayList<Integer>> bestMoveAction = null;

        // Base case: timeout, game over, or depth limit reached
        if (depth == 0 || board.isGameOver() || Instant.now().isAfter(endTime)) {
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId));
            return result;
        }

        long boardHash = hash.computeBoardHash(board);
        if (transpositionTable.containsKey(boardHash)) {
            bestResult = transpositionTable.get(boardHash);
            tableUsed++;
        }

        if (isMax) {
            bestMove = Integer.MIN_VALUE;
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(playerId, board);

            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard());
                testMove.updateGameboard(move, playerId);

                List<Object> res = execAlphaBetaMinimax(testMove, depth - 1, false, playerId, alpha, beta, endTime);
                int value = (int) res.get(0);

                if (value > bestMove) {
                    bestMove = value;
                    bestMoveAction = move;
                }

                alpha = Math.max(alpha, bestMove);
                if (beta <= alpha) {
                    branchesPruned++;
                    break;
                }
            }

        } else { // Minimizer
            bestMove = Integer.MAX_VALUE;
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(opponantId, board);

            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard());
                testMove.updateGameboard(move, opponantId);

                List<Object> res = execAlphaBetaMinimax(testMove, depth - 1, true, playerId, alpha, beta, endTime);
                int value = (int) res.get(0);

                if (value < bestMove) {
                    bestMove = value;
                    bestMoveAction = move;
                }

                beta = Math.min(beta, bestMove);
                if (beta <= alpha) {
                    branchesPruned++;
                    break;
                }
            }
        }

        bestResult.add(bestMove);
        bestResult.add(bestMoveAction);
        transpositionTable.put(boardHash, bestResult); // Memoize result
        return bestResult;
    }

    /*
     * Legacy Minimax without alpha-beta or memoization. Not used in final AI.
     * Kept for reference.
     */
    /*
    public List<Object> execMinimax(Board board, int depth, boolean isMax, int playerId) {
        int opponantId = playerId == 1 ? 2 : 1;

        if (depth == 0 || board.isGameOver()) {
            List<Object> result = new ArrayList<>();
            result.add(board.getUtility(playerId));
            return result;
        }

        int bestMove;
        List<Object> bestResult = new ArrayList<>();
        Map<String, ArrayList<Integer>> bestMoveAction = null;

        if (isMax) {
            bestMove = Integer.MIN_VALUE;
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(playerId, board);

            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard());
                testMove.updateGameboard(move, playerId);

                List<Object> res = execMinimax(testMove, depth - 1, false, playerId);
                int value = (int) res.get(0);

                if (value > bestMove) {
                    bestMove = value;
                    bestMoveAction = move;
                }
            }

        } else {
            bestMove = Integer.MAX_VALUE;
            ActionFactory af = new ActionFactory();
            List<Map<String, ArrayList<Integer>>> moves = af.getActions(opponantId, board);

            for (Map<String, ArrayList<Integer>> move : moves) {
                Board testMove = new Board(board.getGameboard());
                testMove.updateGameboard(move, opponantId);

                List<Object> res = execMinimax(testMove, depth - 1, true, playerId);
                int value = (int) res.get(0);

                if (value < bestMove) {
                    bestMove = value;
                    bestMoveAction = move;
                }
            }
        }

        bestResult.add(bestMove);
        bestResult.add(bestMoveAction);
        return bestResult;
    }
    */

    public int getTableUsed() {
        return this.tableUsed;
    }

    public int getBranchesPruned() {
        return this.branchesPruned;
    }
}
