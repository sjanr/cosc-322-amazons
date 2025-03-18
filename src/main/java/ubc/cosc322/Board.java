package ubc.cosc322;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class Board {
    
    private ArrayList<Integer> gameboard;

    public Board() {
    } 

    public Board(ArrayList<Integer> gameboard) {
        this.gameboard = new ArrayList<>(gameboard);
    }
    

    //*INCOMPLETE*/ //Note: Project does not require our player to validate opponant moves, only ensure our own.
    public boolean validateMove(Map<String, ArrayList<Integer>> gameMove) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");
        
        //Check if move lands in a occupied space:
        if(this.gameboard.get(convertXYToBoard(nextPos)) != 0) {
            System.err.println("Move invalid, player move occupied. validateMove() - SmartPlayer.java");
            return false;
        }

        //Check if move is within bounds:
        if(nextPos.get(0) <= 0 || nextPos.get(0) >= 11 || nextPos.get(1) >= 11 || nextPos.get(1) <= 0) {
            System.err.println("Move out of bounds index <=0 or >=11. validateMove() - SmartPlayer.java");
            return false;
        }
        
        //Check if arrow lands in occupied space:
        if(this.gameboard.get(convertXYToBoard(arrowPos)) != 0) {
            System.err.println("Move invalid, arrow shot occupied. validateMove() - SmartPlayer.java");
            return false;
        }

        //Check that move is horizontal or vertical.
        //First I check for horizontal move:

        


        

        return true;
    }

    
    public void updateGameboard(Map<String, ArrayList<Integer>> gameMove, int playerId) {
        ArrayList<Integer> currentPos = gameMove.get("queen-position-current");
        ArrayList<Integer> nextPos = gameMove.get("queen-position-next");
        ArrayList<Integer> arrowPos = gameMove.get("arrow-position");
        
    
        setBoardPosition(currentPos, 0);
        setBoardPosition(nextPos, playerId);
        setBoardPosition(arrowPos, 3); //For our team project the number 3 denotes arrow locations.

    }

    //Overloaded function
    public void updateGameboard(ArrayList<Integer> queenCurr, ArrayList<Integer> queenNext, ArrayList<Integer> arrowPos, int playerId) {
        setBoardPosition(queenCurr, 0);
        setBoardPosition(queenNext, playerId);
        setBoardPosition(arrowPos, 3);
    }

    public boolean setBoardPosition(ArrayList<Integer> move, Integer value) { //Helper function to update single positions.
        this.gameboard.set(convertXYToBoard(move), value);
        return true;
    }
    
    public Integer getBoardPosition(ArrayList<Integer> position) { //Helper function param arraylist of x,y. Returns value at location. Ex [7,1]=1. Function Overload.
        return this.gameboard.get(convertXYToBoard(position));
    }

    //Overloaded function
    public Integer getBoardPosition(int x, int y) { //Helper function param ints x, y. Returns value at location. Function Overload.
        ArrayList<Integer> position = new ArrayList<>();
        position.add(x);
        position.add(y);
        return getBoardPosition(position);
    }

    public Integer getBoardPosition(int boardIndex) { //Helper funciton param board index. Returns value at location. Function Overload.
        return this.gameboard.get(boardIndex);
    }


    public static Integer convertXYToBoard(ArrayList<Integer> loc) {
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

    //Converts array index into row col pair.
    public static ArrayList<Integer> convertBoardtoXY(int location) {
        int xVal = 11 - (location / 11);
        int yVal = (location % 11);

        ArrayList<Integer> res = new ArrayList<>();
        res.add(xVal);
        res.add(yVal);

        return res;
    }

    public List<ArrayList<Integer>> getQueenPositions(int playerId) { //Returns queen positions in ArrayList. Loops from start of board to end finding playerId.
        List<ArrayList<Integer>> positions = new ArrayList<>();

        for(int i = 11; i < this.gameboard.size(); i++) { //start at 11 because the first row is always empty.
            if(getBoardPosition(i) == playerId) {
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

    public boolean isGameOver() { //Checks if either player has no moves left. Returns true/false;
        ActionFactory af = new ActionFactory(); 
        return af.getActions(1, this).isEmpty() || af.getActions(2, this).isEmpty();
        
    }

    /*INCOMPLETE*/ //For now, utility is just number of moves possible. 
    public int getUtility(int playerId) {
        
        /* This is a very basic heuristic that was initially used. 
        //Temporary utility calculation. In this function it just gets number of actions possible and takes difference.
        */
        ActionFactory af = new ActionFactory();
        if(playerId == 1)
        return af.getActions(playerId, this).size() - af.getActions(2, this).size();
        else
        return af.getActions(playerId, this).size() - af.getActions(1, this).size();
        
        /*
        //Min-Distance Heuristic:
        int opponantId = playerId == 1 ? 2:1;
        Board copy = new Board();
        copy.setGameboard(this.gameboard);
        int p1squares = 0, p2squares = 0;
        
        for(int i = 1; i <= 10; i++) {
            for(int j = 1; j <= 10; j++) { // for every position on the board
                ArrayList<Integer> target = new ArrayList<>(Arrays.asList(i, j));
                if(copy.getBoardPosition(target) == 0) { //if position is not arrow or occupied
                    int p1MovesToLoc = copy.getMinNumMovesToLoc(1, target);
                    int p2MovesToLoc = copy.getMinNumMovesToLoc(2, target);
                    
                    if (p1MovesToLoc != -1 && p2MovesToLoc == -1) {
                        p1squares++;
                        } else if (p2MovesToLoc != -1 && p1MovesToLoc == -1) {
                            p2squares++;
                            } else if (p1MovesToLoc < p2MovesToLoc) {
                                p1squares++;
                                } else if (p2MovesToLoc < p1MovesToLoc) {
                                    p2squares++;
                                    }
                                    }
                                    }
                                    }

                                    // System.out.println("asdfghjkdsfghj");
                                    // System.out.println(p1squares);
                                    // System.out.println(p2squares);
                                    
                                    
                                    int utility = (playerId == 1) ? (p1squares - p2squares) : (p2squares - p1squares);
                                    // System.out.println(utility);
                                    return utility;

         */
    }

    public int getMinNumMovesToLoc(int playerId, ArrayList<Integer> target) {
        int opponentId = playerId == 1 ? 2 : 1; // set opponent id for function.
        int boardPosValue = getBoardPosition(target); // value on board at target index.
        int minNumMoves = Integer.MAX_VALUE;
    
        // If target is already occupied return 0 or -1.
        if (boardPosValue == playerId) return 0; // If target loc is occupied by our own queen return 0;
        if (boardPosValue == opponentId || boardPosValue == 3) return -1; // If target is not accessible return -1;
    
        List<ArrayList<Integer>> queens = getQueenPositions(playerId); // queen positions list
        Queue<ArrayList<Integer>> queue = new LinkedList<>(); // queue to store current position to check.
        Set<ArrayList<Integer>> visited = new HashSet<>(); // global visited set for all queens
    
        // Add all queens to the queue and mark their initial positions as visited
        for (ArrayList<Integer> queen : queens) {
            queue.offer(queen); // start the queue with starting position
            visited.add(queen); // mark initial position as visited
        }
    
        int numMoves = 0;
        while (!queue.isEmpty()) {
            numMoves++;
            int currentLevelSize = queue.size(); // process all nodes at the current level (all moves from current set of positions)
    
            // Process all positions at the current level
            for (int i = 0; i < currentLevelSize; i++) {
                ArrayList<Integer> currentPos = queue.poll(); // get current move
    
                ActionFactory af = new ActionFactory();
                List<ArrayList<Integer>> nextMoves = af.getMoveActions(currentPos, this); // get all moves from currentPos
    
                for (ArrayList<Integer> move : nextMoves) { // for subsequent moves.
                    if (move.equals(target)) { //if target reached track number of moves.
                        minNumMoves = Math.min(minNumMoves, numMoves); // track the minimum num moves
                    }
    
                    if (!visited.contains(move)) { // If the move has not been visited
                        visited.add(move); // mark as visited
                        queue.offer(move); // add the move to the queue for further exploration
                    }
                }
            }
        }
    
        // Return the minimum number of moves after exploring all queens' paths
        return minNumMoves == Integer.MAX_VALUE ? -1 : minNumMoves; // If no path was found, return -1
    }
    

    //This is only for direct testing, the smart player class should not be run as main.
    public static void main(String[] args) {
        System.out.println("SmartPlayer Testing Main Function");


        Board b = new Board();
        // ArrayList<Integer> testBoard = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 2, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0)); 
        ArrayList<Integer> testBoard = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 3, 3, 3, 0, 0, 0,
        0, 2, 0, 0, 0, 3, 0, 3, 0, 0, 2,
        0, 0, 0, 0, 0, 3, 0, 3, 3, 3, 3,
        0, 3, 3, 0, 0, 3, 0, 3, 0, 0, 0,
        0, 1, 3, 0, 0, 3, 0, 0, 0, 3, 1,
        0, 0, 3, 0, 0, 0, 3, 3, 3, 3, 3,
        0, 3, 3, 3, 3, 3, 3, 3, 3, 3, 0,
        0, 0, 0, 3, 1, 0, 3, 1, 0, 3, 0)); 
        b.setGameboard(testBoard);

        ArrayList<Integer> tar = new ArrayList<>(Arrays.asList(10,3)); //1, 2
        System.out.println("What is at target: " + b.getBoardPosition(tar));
        // System.out.println("What is at target" + b.getBoardPosition(1, 3));

        System.out.println(b.getMinNumMovesToLoc(1,tar));
        System.out.println("utility " + b.getUtility(2));
        /*
        //Testing the minimax and alpha beta algorithms. 
        Board b = new Board();
        
        ArrayList<Integer> testBoard = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 0, 3, 3, 3, 0, 1, 3, 0, 0, 3, 2, 3, 1, 3, 2, 3, 2, 3, 0, 0, 3, 3, 3, 3, 0, 0, 3, 3, 3, 3, 0, 0, 3, 3, 3, 3, 3, 3, 3, 0, 3, 0, 0, 3, 0, 0, 3, 3, 3, 3, 3, 0, 0, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0, 3, 3, 3, 3, 3, 3, 0, 3, 0, 0, 0, 0, 3, 3, 3, 3, 3, 0, 0, 0, 0, 0, 0, 0, 3, 2, 0, 3, 1, 3, 0, 0, 0, 0, 0, 1, 3, 0, 3, 3, 0, 0)); 
        b.setGameboard(testBoard);

       
        ActionFactory af = new ActionFactory();
        List<Map<String, ArrayList<Integer>>> test = af.getActions(2, b);
        
        System.out.println("Number of actions: " + test.size());
        System.out.println("IsGameOver? "+ b.isGameOver());

        Minimax m = new Minimax();
        
        LocalTime myObj = LocalTime.now();
        System.out.println(myObj);
        List<Object> try1 = m.execMinimax(b, 1, true, 1);
        System.out.println(try1.get(0));
        System.out.println(try1.get(1));


        LocalTime myObj1 = LocalTime.now();
        System.out.println(myObj1);

        List<Object> try2 = m.execAlphaBetaMinimax(b, 5, true, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
        System.out.println(try2.get(0));
        System.out.println(try2.get(1));
        System.out.println("END");

        LocalTime myObj2 = LocalTime.now();
        System.out.println(myObj2);
        
        */
    } 

     
}

/*
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
 0, 0, 0, 0, 2, 0, 0, 2, 0, 0, 0,
  0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
   0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
    0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 2,
     0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
      0, 3, 3, 0, 0, 0, 0, 0, 0, 0, 0,
       0, 1, 3, 0, 0, 0, 0, 0, 0, 0, 1,
        0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0,
         0, 0, 3, 0, 0, 0, 0, 0, 0, 0, 0,
          0, 0, 0, 0, 1, 0, 0, 1, 0, 0, 0 */

/*

    //For parameters playerId (1,2) and target board location (ArrayList<Integer>: [10,8]). Function returns the lowest number of moves req'd to move to target position.
    public int getMinNumMovesToLocMYATTEMPT(int playerId, ArrayList<Integer> target) {
        int opponantId = playerId == 1 ? 2:1; //set opponant id for function.
        int boardPosValue = getBoardPosition(target); //value on board arraylist at target index.
        int minNumMoves = Integer.MAX_VALUE;

        //If target is already occupied return 0 or -1.
        if(boardPosValue == playerId) return 0; //If target loc is occupied by our own queen return 0;
        if(boardPosValue == opponantId || boardPosValue == 3) return -1; //If target is not accessible return -1;
    
        
        List<ArrayList<Integer>> queens = getQueenPositions(playerId); //queen positions list
        Queue<ArrayList<Integer>> queue = new LinkedList<>(); //queue to store current position to check.
       
        for(ArrayList<Integer> queen : queens) {
            Set<ArrayList<Integer>> visited = new HashSet<>();
            int numMoves = 0; //track for each queen number of moves;
            System.out.println("Queen: " + queen);

            queue.offer(queen); //start the queue with starting position

            while(!queue.isEmpty()) { //while the queue has moves
                numMoves++;
                ArrayList<Integer> currentPos = queue.poll(); //get current move
                
                ActionFactory af = new ActionFactory();
                List<ArrayList<Integer>> nextMoves =  af.getMoveActions(currentPos, this); //get all moves from currentPos
                for (ArrayList<Integer> move : nextMoves) { //for subsequent moves. 
                    System.out.println("trye move " + move + " : "+ numMoves);
                    //If move found return number of moves;
                    if(move.equals(target)) {
                        System.out.println(minNumMoves + " " + numMoves);
                        minNumMoves = Math.min(numMoves, minNumMoves);
                    }
                    
                    if(visited.contains(move))  {
                        System.out.println("already checked this pos");
                    } else {
                        visited.add(move);
                        queue.offer(move);
                        System.out.println("QUEUE step");
                    }
                }



            }



        }

        return minNumMoves;

    }
 */