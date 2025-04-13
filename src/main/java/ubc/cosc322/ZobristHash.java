package ubc.cosc322;

import java.util.Random;

/**
 * Implements Zobrist hashing for fast board state comparison and memoization.
 */
public class ZobristHash {

    public long[][] zobristTable; // 64-bit values indexed by [piece][position]

    /**
     * Initializes the Zobrist table with random 64-bit values for each piece at each board position.
     */
    public ZobristHash(Board board) {
        Random rand = new Random();
        int boardSize = board.getGameboard().size();
        zobristTable = new long[4][boardSize]; // 0: empty, 1–2: players, 3: arrow

        for (int i = 0; i < 4; i++) {
            for (int position = 11; position < boardSize; position++) {
                zobristTable[i][position] = rand.nextLong();
            }
        }
    }

    /**
     * Computes the hash value of a given board by XOR-ing all piece-position combinations.
     */
    public long computeBoardHash(Board board) {
        long hash = 0;

        for (int idx = 0; idx < board.getGameboard().size(); idx++) {
            int piece = board.getBoardPosition(idx);
            hash ^= zobristTable[piece][idx];
        }

        return hash;
    }
}
