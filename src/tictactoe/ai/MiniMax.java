package tictactoe.ai;

import tictactoe.Turn;
import tictactoe.board.Board;
import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

import java.util.List;

public class MiniMax extends Computer {
    private int evaluate(Board board, Turn playingAs) {
        // Currently only works on boards with size of 3. In the future I want to make this work with any size main.board.
        // TODO: comment above, plus clean up spaghetti
        // TODO: reward wins that happen closer in the future

        // Evaluation method based on this document
        // https://john.cs.olemiss.edu/~dwilkins/CSCI531/fall12/slides/AI_09_games.pdf

        BoardState winner = board.winner();
        if (winner == playingAs.toBoardState()) {
            return Integer.MAX_VALUE;
        } else if (winner == playingAs.switchTurn().toBoardState()) {
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

            if (x == board.getSize() - 1 && o == 0) {
                x_2_in_row++;
            } else if (x > 0 && o == 0) {
                x_1_in_row++;
            } else if (o == board.getSize() - 1 && x == 0) {
                o_2_in_row++;
            } else if (o > 0 && x == 0) {
                o_1_in_row++;
            }
        }

        if (playingAs == Turn.X) {
            return 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row);
        }

        return 3 * o_2_in_row + o_1_in_row - (3 * x_2_in_row + x_1_in_row);
    }

    /**
     * Minimax algorithm. Positive evaluations are best for X. Negative evaluations are best for O.
     * @param board The main.board to evaluate.
     * @param depth The depth of the search.
     * @param findMax Whether to find the maximum or minimum.
     */
    private int minimax(Board board, int depth, boolean findMax, int alpha, int beta, Turn playingAs) {
        if (depth == 0 || board.winner() != BoardState.EMPTY) {
            return this.evaluate(board, playingAs);
        }

        int maxOrMin = findMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (BoardSpace space : this.getEmptySpaces(board)) {
            BoardState stateToSet = findMax ? playingAs.toBoardState() : playingAs.switchTurn().toBoardState();

            space.setState(stateToSet);
            int evaluation = this.minimax(board, depth - 1, !findMax, alpha, beta, playingAs);
            maxOrMin = findMax ? Math.max(maxOrMin, evaluation) : Math.min(maxOrMin, evaluation);

            space.setState(BoardState.EMPTY);

            // Alpha beta pruning
            if (findMax) {
                alpha = Math.max(alpha, evaluation);
            } else {
                beta = Math.min(beta, evaluation);
            }

            if (beta <= alpha) {
                break;
            }
        }

        return maxOrMin;
    }

    @Override
    public void makeMove(Board board, Turn playingAs) {
        List<BoardSpace> emptySpaces = this.getEmptySpaces(board);

        if (emptySpaces.isEmpty()) {
            return;
        }

        int bestEval = Integer.MIN_VALUE;
        BoardSpace bestSpace = emptySpaces.get(0);

        for (BoardSpace space : emptySpaces) {
            space.setState(playingAs.toBoardState());
            int evaluation = this.minimax(board, 4, false, Integer.MIN_VALUE, Integer.MAX_VALUE, playingAs);
            space.setState(BoardState.EMPTY);

            if (evaluation > bestEval) {
                bestEval = evaluation;
                bestSpace = space;
            }
        }

        bestSpace.setState(playingAs.toBoardState());
    }
}
