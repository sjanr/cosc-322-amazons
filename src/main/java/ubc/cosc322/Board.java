package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Represents the Amazons game board, including move validation,
 * updates, and territory evaluation using a Voronoi-based heuristic.
 */
public class Board {

    private ArrayList<Integer> gameboard;

    public Board() {}

    public Board(ArrayList<Integer> gameboard) {
        this.gameboard = new ArrayList<>(gameboard);
    }

    /**
     * Checks if the move is valid and logs it.
     */
    public boolean checkMove(Map<String, ArrayList<Integer>> gameMove, int playerId) {
        ArrayList<Integer> curr = gameMove.get("queen-position-current");
        ArrayList<Integer> next = gameMove.get("queen-position-next");
        ArrayList<Integer> arrow = gameMove.get("arrow-position");

        boolean valid = validateMove(gameMove);

        String msg = valid
            ? String.format("VALID MOVE by Player %d: %s -> %s, Arrow: %s", playerId, curr, next, arrow)
            : String.format("INVALID MOVE by Player %d: %s -> %s, Arrow: %s", playerId, curr, next, arrow);

        System.out.println(msg);
        return valid;
    }

    /**
     * Validates a move based on board constraints.
     */
    public boolean validateMove(Map<String, ArrayList<Integer>> gameMove) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");

        if (this.gameboard.get(convertXYToBoard(nextPos)) != 0) return false;
        if (nextPos.get(0) <= 0 || nextPos.get(0) >= 11 || nextPos.get(1) >= 11 || nextPos.get(1) <= 0) return false;
        if (this.gameboard.get(convertXYToBoard(arrowPos)) != 0) return false;

        return true;
    }

    /**
     * Updates the board with a full game move.
     */
    public void updateGameboard(Map<String, ArrayList<Integer>> gameMove, int playerId) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");

        setBoardPosition(currentPos, 0);
        setBoardPosition(nextPos, playerId);
        setBoardPosition(arrowPos, 3);
    }

    /**
     * Updates the board with move and arrow positions.
     */
    public void updateGameboard(ArrayList<Integer> queenCurr, ArrayList<Integer> queenNext, ArrayList<Integer> arrowPos, int playerId) {
        setBoardPosition(queenCurr, 0);
        setBoardPosition(queenNext, playerId);
        setBoardPosition(arrowPos, 3);
    }

    /**
     * Sets a specific board index to a value.
     */
    public boolean setBoardPosition(ArrayList<Integer> move, Integer value) {
        this.gameboard.set(convertXYToBoard(move), value);
        return true;
    }

    public Integer getBoardPosition(ArrayList<Integer> position) {
        return this.gameboard.get(convertXYToBoard(position));
    }

    public Integer getBoardPosition(int x, int y) {
        return getBoardPosition(new ArrayList<>(Arrays.asList(x, y)));
    }

    public Integer getBoardPosition(int boardIndex) {
        return this.gameboard.get(boardIndex);
    }

    /**
     * Converts (x, y) to board index.
     */
    public static Integer convertXYToBoard(ArrayList<Integer> loc) {
        return (11 - loc.get(0)) * 11 + loc.get(1);
    }

    /**
     * Converts board index to (x, y).
     */
    public static ArrayList<Integer> convertBoardtoXY(int location) {
        int xVal = 11 - (location / 11);
        int yVal = location % 11;
        return new ArrayList<>(Arrays.asList(xVal, yVal));
    }

    /**
     * Returns all queen positions for a player.
     */
    public List<ArrayList<Integer>> getQueenPositions(int playerId) {
        List<ArrayList<Integer>> positions = new ArrayList<>();
        for (int i = 11; i < this.gameboard.size(); i++) {
            if (getBoardPosition(i) == playerId) {
                positions.add(convertBoardtoXY(i));
            }
        }
        return positions;
    }

    public void setGameboard(ArrayList<Integer> gameboard) {
        this.gameboard = gameboard;
    }

    public ArrayList<Integer> getGameboard() {
        return this.gameboard;
    }

    /**
     * Returns true if either player has no legal moves.
     */
    public boolean isGameOver() {
        ActionFactory af = new ActionFactory();
        return af.getActions(1, this).isEmpty() || af.getActions(2, this).isEmpty();
    }

    /**
     * Returns a utility score based on Voronoi territory.
     */
    public int getUtility(int playerId) {
        int opponentId = (playerId == 1) ? 2 : 1;
        int[][] territory = computeVoronoi();

        int p1squares = 0, p2squares = 0;
        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                if (territory[i][j] == 1) p1squares++;
                else if (territory[i][j] == 2) p2squares++;
            }
        }

        return (playerId == 1) ? (p1squares - p2squares) : (p2squares - p1squares);
    }

    /**
     * Computes Voronoi region ownership for both players.
     */
    private int[][] computeVoronoi() {
        int[][] territory = new int[11][11];
        int[][] distance = new int[11][11];
        for (int[] row : distance) Arrays.fill(row, Integer.MAX_VALUE);

        Queue<int[]> queue = new LinkedList<>();

        for (int i = 1; i <= 10; i++) {
            for (int j = 1; j <= 10; j++) {
                int boardIndex = convertXYToBoard(new ArrayList<>(Arrays.asList(i, j)));
                int piece = gameboard.get(boardIndex);

                if (piece == 1 || piece == 2) {
                    queue.offer(new int[]{i, j, 0, piece});
                    distance[i][j] = 0;
                    territory[i][j] = piece;
                }
            }
        }

        int[][] directions = {
            {-1, 0}, {1, 0}, {0, -1}, {0, 1},
            {-1, -1}, {-1, 1}, {1, -1}, {1, 1}
        };

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1], d = cell[2], owner = cell[3];

            for (int[] dir : directions) {
                int nx = x + dir[0], ny = y + dir[1];

                if (nx >= 1 && nx <= 10 && ny >= 1 && ny <= 10) {
                    int boardIndex = convertXYToBoard(new ArrayList<>(Arrays.asList(nx, ny)));

                    if (gameboard.get(boardIndex) == 0) {
                        if (d + 1 < distance[nx][ny]) {
                            distance[nx][ny] = d + 1;
                            territory[nx][ny] = owner;
                            queue.offer(new int[]{nx, ny, d + 1, owner});
                        } else if (d + 1 == distance[nx][ny]) {
                            territory[nx][ny] = 0;
                        }
                    }
                }
            }
        }

        return territory;
    }
}
