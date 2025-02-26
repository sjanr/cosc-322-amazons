package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class SmartPlayer {
    
    private ArrayList<Integer> gameboard;

    public SmartPlayer() {



    }
    
    public boolean validateMove(Map<String, ArrayList<Integer>> gameMove) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");
        





        

        return true;
    }
    
    public void updateGameboard(Map<String, ArrayList<Integer>> gameMove, int playerNum) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");
        
        this.gameboard.set(this.convertLocToBoard(currentPos), 0);
        this.gameboard.set(this.convertLocToBoard(nextPos), playerNum);
        this.gameboard.set(this.convertLocToBoard(arrowPos), 3);

    }

    private Integer convertLocToBoard(ArrayList<Integer> loc) {
        // Maps [row, col] pair to a single value in the gameboard.
        //loc.get(0) is row, loc.get(1) is col.
        
        return (11-loc.get(0))*11+(loc.get(1)); //removed the plus one from the formula below because of array indexing starting from zero.

        /* game-state.board
         * [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0]
         */
        
        /* Corresponding col/rows for indexing
         * [xx, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
         *  10, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
         *  09, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  08, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  07, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
         *  06, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  05, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  04, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1,
         *  03, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  02, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
         *  01, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0]
         * 
         *  (11-row)*11+(1+col) = location?
         *  [4,1] = (11-4)*11+(1+1) = 79
         *  [1,7] = (11-1)*11+(1+7) = 118
         *  
         */
    }

    public void setGameboard(ArrayList<Integer> gameboard) {
        this.gameboard = gameboard;
    }

    public ArrayList<Integer> getGameboard() {
        return this.gameboard;
    }


    //This is only for direct testing, the smart player class should not be run as main.
    public static void main(String[] args) {
        System.out.println("SmartPlayer Testing Main Function");
        SmartPlayer my = new SmartPlayer();
        
        ArrayList<Integer> testBoard = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0)); 
        my.setGameboard(testBoard);

        ArrayList<Integer> testLoc = new ArrayList<>(Arrays.asList(1,4));
        
        System.out.println(my.gameboard.toString());
        Integer tLoc = my.convertLocToBoard(testLoc);
        System.out.println(tLoc);
        
        my.gameboard.set(tLoc,7);
        System.out.println(my.gameboard.toString());

    }

     
}
