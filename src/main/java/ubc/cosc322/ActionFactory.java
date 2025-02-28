package ubc.cosc322;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActionFactory {
    

    public List<Map<String, ArrayList<Integer>>> getActions(int playerId, Board board) {
        List<Map<String, ArrayList<Integer>>> validMoves = new ArrayList<>();
        
        for(ArrayList<Integer> queen : board.getQueenPositions(playerId)) { //for each queen generate move list.
            int initialX = queen.get(0);
            int initialY = queen.get(1);

            System.out.println("Queen: " + initialX + ", " + initialY);
        }

        // int[][] directions = {
        //     {-1, 0}, {1, 0}, {0, -1}, {0, 1}, //horizontal / vertical
        //     {-1, -1}, {-1, 1}, {1, -1}, {1, 1} //diagonals
        // };

        // for (int[] dir : directions) {
        //     int dx = dir[0];
        //     int dy = dir[1];

        //     //move in the current direction
        //     int x = startX + dx;
        //     int y = startY + dy;

        //     //keep moving in the direction as long as the move is within bounds and not blocked
        //     while (x > 0 && x <= 10 && y > 0 && y <= 10 && ) {
               
        //         validMoves.add(new Position(x, y));  // Add this position as a valid move
               
        //         x += dx;
        //         y += dy;
        //     }
        // }

        // return validMoves;

        return null;
        
    }
}
