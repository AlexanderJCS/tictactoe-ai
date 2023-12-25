package ai;

import board.Board;
import board.BoardSpace;

import java.util.List;

public class MiniMax extends Computer {
    private int evaluate(Board board, BoardSpace.BoardState playingAs) {
        // TODO: change playingAs to be a TicTacToe.Turn

        // Currently only works on boards with size of 3. In the future I want to make this work with any size board.
        // TODO: comment above, plus clean up spaghetti

        // Evaluation method based on this document
        // https://john.cs.olemiss.edu/~dwilkins/CSCI531/fall12/slides/AI_09_games.pdf

        BoardSpace.BoardState winner = board.winner();
        if (winner == playingAs) {
            return Integer.MAX_VALUE;
        } else if (winner == null) {
            return 0;
        } else if (winner != BoardSpace.BoardState.EMPTY) {  // if the opponent won
            return Integer.MIN_VALUE;
        }

        // eval = 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row)
        BoardSpace.BoardState[][] rowsColsDiags = board.getRowsColsDiags();

        int x_2_in_row = 0;
        int x_1_in_row = 0;
        int o_2_in_row = 0;
        int o_1_in_row = 0;

        for (BoardSpace.BoardState[] rowColDiag : rowsColsDiags) {
            int x = 0;
            int o = 0;

            for (BoardSpace.BoardState state : rowColDiag) {
                if (state == BoardSpace.BoardState.X) {
                    x++;
                } else if (state == BoardSpace.BoardState.O) {
                    o++;
                }
            }

            if (x == 2 && o == 0) {
                x_2_in_row++;
            } else if (x == 1 && o == 0) {
                x_1_in_row++;
            } else if (o == 2 && x == 0) {
                o_2_in_row++;
            } else if (o == 1 && x == 0) {
                o_1_in_row++;
            }
        }

        if (playingAs == BoardSpace.BoardState.X) {
            return 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row);
        }

        return 3 * o_2_in_row + o_1_in_row - (3 * x_2_in_row + x_1_in_row);
    }

    /**
     * Minimax algorithm. Positive evaluations are best for X. Negative evaluations are best for O.
     * @param board The board to evaluate.
     * @param depth The depth of the search.
     * @param findMax Whether to find the maximum or minimum.
     */
    private int minimax(Board board, int depth, boolean findMax, BoardSpace.BoardState playingAs) {
        // TODO: clean up spaghetti

        if (board.winner() == playingAs) {
            return Integer.MAX_VALUE - 10000 + depth;
        } else if (board.winner() != BoardSpace.BoardState.EMPTY) {
            return Integer.MIN_VALUE;
        }

        if (depth == 0 || board.winner() != BoardSpace.BoardState.EMPTY) {
            return this.evaluate(board, playingAs);
        }

        if (findMax) {
            int maxEval = Integer.MIN_VALUE;

            for (BoardSpace space : this.getEmptySpaces(board)) {
                space.setState(playingAs);
                int evaluation = this.minimax(board, depth - 1, false, playingAs);
                maxEval = Math.max(maxEval, evaluation);
                space.setState(BoardSpace.BoardState.EMPTY);
            }

            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;

        for (BoardSpace space : this.getEmptySpaces(board)) {
            // TODO: replace with a flippy floppy method
            space.setState(playingAs == BoardSpace.BoardState.X ? BoardSpace.BoardState.O : BoardSpace.BoardState.X);
            int evaluation = this.minimax(board, depth - 1, true, playingAs);
            minEval = Math.min(minEval, evaluation);
            space.setState(BoardSpace.BoardState.EMPTY);
        }

        return minEval;
    }

    @Override
    public void makeMove(Board board, BoardSpace.BoardState playingAs) {
        List<BoardSpace> emptySpaces = this.getEmptySpaces(board);

        int bestEval = Integer.MIN_VALUE;
        BoardSpace bestSpace = emptySpaces.get(0);

        for (BoardSpace space : emptySpaces) {
            space.setState(playingAs);
            int evaluation = this.minimax(board, 5, false, playingAs);
            space.setState(BoardSpace.BoardState.EMPTY);

            if (evaluation > bestEval) {
                bestEval = evaluation;
                bestSpace = space;
            }
        }

        bestSpace.setState(playingAs);
    }
}
