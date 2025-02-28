package ubc.cosc322;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionFactory {
    
    
    public List<Map<String, ArrayList<Integer>>> getActions(int playerId, Board board) {
        List<Map<String, ArrayList<Integer>>> validMoves = new ArrayList<>();
        
        for(ArrayList<Integer> queen : board.getQueenPositions(playerId)) { //for each queen, generate move list.
            //Get all possible moves for selected queen.
            List<ArrayList<Integer>> moves = getMoveActions(queen, board);

            for(ArrayList<Integer> move : moves) { //for every move, get all possible arrow shots.
                
                List<ArrayList<Integer>> shots = getMoveActions(move, board);
                for(ArrayList<Integer> shot : shots) {
                    // System.out.printf("\nQueen: %d, %d    Move: %d, %d    Shot: %d, %d", queen.get(0), queen.get(1), move.get(0), move.get(1), shot.get(0), shot.get(1));
                   
                    //Add moves to map
                    Map<String, ArrayList<Integer>> action = new HashMap<>();
                    action.put("queen-position-current", queen);
                    action.put("queen-position-next", move);
                    action.put("arrow-position", shot);

                    //Add map to validMoves
                    validMoves.add(action);

                }
            }
            
            
        }
        
        return validMoves;
    }

    private List<ArrayList<Integer>> getMoveActions(ArrayList<Integer> position, Board board) { //Helper function to return all possible moves for a queen at locaiton initialX, initialY
        int initialX = position.get(0);
        int initialY = position.get(1);


        //Make a copy of the board to temporarily simulate the move. Will get deleted after this funciton.
        Board boardCopy = new Board(board.getGameboard());
        boardCopy.setBoardPosition(position, 0); //We do this so that the arrow can land where the queen was placed before move.

        List<ArrayList<Integer>> moves = new ArrayList<>();
    
        //All directions, North, South, East, West, and diagonals, NE, SE, SW, NW.
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        
        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            //move in the current direction
            int x = initialX + dx;
            int y = initialY + dy;

            //keep moving in the direction as long as the move is within bounds and not blocked
            while (x > 0 && x <= 10 && y > 0 && y <= 10 && boardCopy.getBoardPosition(x, y) == 0) {
                // System.out.println("  Valid: " + x + ", " + y);

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