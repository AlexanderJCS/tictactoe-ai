package tictactoe.ai;

import tictactoe.Turn;
import tictactoe.board.Board;
import tictactoe.board.BoardSpace;
import tictactoe.board.BoardState;

import java.util.List;

public class MiniMax extends Computer {
    public MiniMax() {
        super();
    }

    public MiniMax(Board board, Turn playingAs) {
        super(board, playingAs);
    }

    private int evaluate(int depth) {
        // TODO: clean up spaghetti

        // Evaluation method based on this document
        // https://john.cs.olemiss.edu/~dwilkins/CSCI531/fall12/slides/AI_09_games.pdf

        BoardState winner = this.board.winner();
        if (winner == this.playingAs.toBoardState()) {
            // The addition is to make sure that the AI chooses the move that wins the fastest.
            // The -100000 is to ensure that the integer does not overflow
            // The Math.max() is to make sure that, if the integer does overflow (which is very unlikely), it will not
            // overflow to a negative number.

            return Math.max(Integer.MAX_VALUE - 100000 + depth, Integer.MAX_VALUE);

        } else if (winner == this.playingAs.switchTurn().toBoardState()) {
            return Integer.MIN_VALUE;
        } else if (winner == null) {
            return 0;
        }

        // eval = 3 * x_2_in_row + x_1_in_row - (3 * o_2_in_row + o_1_in_row)
        BoardState[][] rowsColsDiags = this.board.getRowsColsDiags();

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

            if (x == this.board.getSize() - 1 && o == 0) {
                x_2_in_row++;
            } else if (x > 0 && o == 0) {
                x_1_in_row++;
            } else if (o == this.board.getSize() - 1 && x == 0) {
                o_2_in_row++;
            } else if (o > 0 && x == 0) {
                o_1_in_row++;
            }
        }

        if (this.playingAs == Turn.X) {
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
    private int minimax(int depth, boolean findMax, int alpha, int beta) {
        if (depth == 0 || this.board.winner() != BoardState.EMPTY) {
            return this.evaluate(depth);
        }

        int maxOrMin = findMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (BoardSpace space : this.getEmptySpaces()) {
            BoardState stateToSet = findMax ? this.playingAs.toBoardState() : this.playingAs.switchTurn().toBoardState();

            space.setState(stateToSet);
            int evaluation = this.minimax(depth - 1, !findMax, alpha, beta);
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
    public void run() {
        List<BoardSpace> emptySpaces = this.getEmptySpaces();

        if (emptySpaces.isEmpty()) {
            return;
        }

        int bestEval = Integer.MIN_VALUE;
        BoardSpace bestSpace = emptySpaces.get(0);

        for (BoardSpace space : emptySpaces) {
            space.setState(this.playingAs.toBoardState());
            int evaluation = this.minimax(4, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
            space.setState(BoardState.EMPTY);

            if (evaluation > bestEval) {
                bestEval = evaluation;
                bestSpace = space;
            }
        }

        bestSpace.setState(this.playingAs.toBoardState());
    }
}
