import ai.Computer;
import ai.MiniMax;
import board.Board;
import board.BoardSpace;
import jangl.Jangl;
import jangl.io.Window;
import jangl.io.mouse.Mouse;
import jangl.io.mouse.MouseEvent;
import jangl.time.Clock;
import org.lwjgl.glfw.GLFW;

public class TicTacToe {
    private static final int BOARD_SIZE = 3;

    private final Board board;
    private final Computer ai;
    private static final float WAIT_TIME = 0.5f;
    private float timeToWait = Clock.getTimef() + WAIT_TIME;
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
        if (Clock.getTimef() < this.timeToWait) {
            return;
        }

        this.timeToWait = Clock.getTimef() + WAIT_TIME;

        if (this.turn == Turn.O) {
            this.ai.makeMove(this.board, this.turn.toBoardState());
            this.turn = this.turn.switchTurn();
            return;
        }

        this.ai.makeMove(this.board, this.turn.toBoardState());
        this.turn = this.turn.switchTurn();

//        for (MouseEvent event : Mouse.getEvents()) {
//            // The event was not a left click
//            if (event.button != GLFW.GLFW_MOUSE_BUTTON_1 || event.action != GLFW.GLFW_PRESS) {
//                continue;
//            }
//
//            if (this.board.selectSpace(this.turn.toBoardState(), Mouse.getMousePos())) {
//                this.turn = this.turn.switchTurn();
//            }
//        }
    }

    public BoardSpace.BoardState run() {
        while (Window.shouldRun() && this.board.winner() == BoardSpace.BoardState.EMPTY) {
            Window.clear();

            this.update();
            this.draw();

            Jangl.update();
        }

        return this.board.winner();
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
