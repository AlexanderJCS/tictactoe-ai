import ai.Computer;
import ai.MiniMax;
import board.Board;
import board.BoardSpace;
import jangl.Jangl;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import org.lwjgl.glfw.GLFW;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;

    private final Board board;
    private final Computer ai;
    private Turn turn;

    public TicTacToe() {
        this.board = new Board(BOARD_SIZE);
        this.turn = Turn.X;
        this.ai = new MiniMax();
    }

    public void draw() {
        this.board.draw();
    }

    public void update() {
        if (this.turn == Turn.X) {
            this.ai.makeMove(this.board);
            this.turn = this.turn.switchTurn();
        }

        for (MouseEvent event : Mouse.getEvents()) {
            // The event was not a left click
            if (event.button != GLFW.GLFW_MOUSE_BUTTON_1 || event.action != GLFW.GLFW_PRESS) {
                continue;
            }

            if (this.board.selectSpace(this.turn.toBoardState(), Mouse.getMousePos())) {
                this.turn = this.turn.switchTurn();
            }
        }
    }

    public void run() {
        while (Window.shouldRun() && this.board.winner() == BoardSpace.BoardState.EMPTY) {
            Window.clear();

            this.update();
            this.draw();

            Jangl.update();
        }

        System.out.println("Winner: " + this.board.winner());
    }

    public enum Turn {
        X, O;

        public BoardSpace.BoardState toBoardState() {
            return switch (this) {
                case X -> BoardSpace.BoardState.X;
                case O -> BoardSpace.BoardState.O;
            };
        }

        public Turn switchTurn() {
            return switch (this) {
                case X -> Turn.O;
                case O -> Turn.X;
            };
        }
    }
}
