package tictactoe.ai;

import tictactoe.board.Board;
import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

import java.util.List;

public class MiniMax extends Computer {
    private int evaluate(Board board) {
        // Currently only works on boards with size of 3. In the future I want to make this work with any size main.board.
        // TODO: comment above, plus clean up spaghetti

        // Evaluation method based on this document
        // https://john.cs.olemiss.edu/~dwilkins/CSCI531/fall12/slides/AI_09_games.pdf

        BoardState winner = board.winner();
        if (winner == BoardState.X) {
            return Integer.MAX_VALUE;
        } else if (winner == BoardState.O) {
            return Integer.MIN_VALUE;
        } else if (winner == null) {
            return 0;
        }

        // eval = 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row)
        BoardState[][] rowsColsDiags = board.getRowsColsDiags();

        int x_2_in_row = 0;
        int x_1_in_row = 0;
        int o_2_in_row = 0;
        int o_1_in_row = 0;

        for (BoardState[] rowColDiag : rowsColsDiags) {
            int x = 0;
            int o = 0;

            for (BoardState state : rowColDiag) {
                if (state == BoardState.X) {
                    x++;
                } else if (state == BoardState.O) {
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

        return 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row);
    }

    /**
     * Minimax algorithm. Positive evaluations are best for X. Negative evaluations are best for O.
     * @param board The main.board to evaluate.
     * @param depth The depth of the search.
     * @param findMax Whether to find the maximum or minimum.
     */
    private int minimax(Board board, int depth, boolean findMax) {
        if (depth == 0 || board.winner() != BoardState.EMPTY) {
            return this.evaluate(board);
        }

        if (findMax) {
            int maxEval = Integer.MIN_VALUE;

            for (BoardSpace space : this.getEmptySpaces(board)) {
                space.setState(BoardState.X);
                int evaluation = this.minimax(board, depth - 1, false);
                maxEval = Math.max(maxEval, evaluation);
                space.setState(BoardState.EMPTY);
            }

            return maxEval;
        }

        int minEval = Integer.MAX_VALUE;

        for (BoardSpace space : this.getEmptySpaces(board)) {
            space.setState(BoardState.O);
            int evaluation = this.minimax(board, depth - 1, true);
            minEval = Math.min(minEval, evaluation);
            space.setState(BoardState.EMPTY);
        }

        return minEval;
    }

    @Override
    public void makeMove(Board board) {
        List<BoardSpace> emptySpaces = this.getEmptySpaces(board);

        int bestEval = Integer.MIN_VALUE;
        BoardSpace bestSpace = emptySpaces.get(0);

        for (BoardSpace space : emptySpaces) {
            space.setState(BoardState.X);
            int evaluation = this.minimax(board, 9, false);
            space.setState(BoardState.EMPTY);

            if (evaluation > bestEval) {
                bestEval = evaluation;
                bestSpace = space;
            }
        }

        bestSpace.setState(BoardState.X);
    }
}
