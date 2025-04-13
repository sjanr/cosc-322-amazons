package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates all legal move combinations (queen move + arrow shot) for a given player.
 */
public class ActionFactory {

    /**
     * Returns all valid (move + arrow) actions for the given player on the current board.
     */
    public List<Map<String, ArrayList<Integer>>> getActions(int playerId, Board board) {
        List<Map<String, ArrayList<Integer>>> validMoves = new ArrayList<>();

        for (ArrayList<Integer> queen : board.getQueenPositions(playerId)) {
            List<ArrayList<Integer>> moves = getMoveActions(queen, board);

            for (ArrayList<Integer> move : moves) {
                List<ArrayList<Integer>> shots = getMoveActions(move, board);

                for (ArrayList<Integer> shot : shots) {
                    Map<String, ArrayList<Integer>> action = new HashMap<>();
                    action.put("queen-position-current", queen);
                    action.put("queen-position-next", move);
                    action.put("arrow-position", shot);
                    validMoves.add(action);
                }

                // Optionally add a fallback arrow shot to the queen's previous position
                Map<String, ArrayList<Integer>> fallback = new HashMap<>();
                fallback.put("queen-position-current", queen);
                fallback.put("queen-position-next", move);
                fallback.put("arrow-position", queen);
                validMoves.add(fallback);
            }
        }

        return validMoves;
    }

    /**
     * Returns all straight-line legal moves (or arrow shots) from a given position.
     */
    public List<ArrayList<Integer>> getMoveActions(ArrayList<Integer> position, Board board) {
        Board boardCopy = new Board(board.getGameboard());
        boardCopy.setBoardPosition(position, 0); // Allow arrows to land where the queen was

        int initialX = position.get(0);
        int initialY = position.get(1);
        List<ArrayList<Integer>> moves = new ArrayList<>();

        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        for (int[] dir : directions) {
            int dx = dir[0], dy = dir[1];
            int x = initialX + dx;
            int y = initialY + dy;

            while (x > 0 && x <= 10 && y > 0 && y <= 10 && boardCopy.getBoardPosition(x, y) == 0) {
                ArrayList<Integer> validMove = new ArrayList<>();
                validMove.add(x);
                validMove.add(y);
                moves.add(validMove);

                x += dx;
                y += dy;
            }
        }

        return moves;
    }
}
