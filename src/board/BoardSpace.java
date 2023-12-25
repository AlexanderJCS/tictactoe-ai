package board;

import jangl.coords.WorldCoords;
import jangl.graphics.textures.Texture;
import jangl.shapes.Rect;
import jangl.shapes.Shape;

public class BoardSpace {
    private static final Texture X_TEXTURE = new Texture("resources/x.png");
    private static final Texture O_TEXTURE = new Texture("resources/O.png");

    private final Rect rect;
    private BoardState state;

    public BoardSpace(WorldCoords topLeft, float size) {
        this.rect = new Rect(topLeft, size, size);
        this.state = BoardState.EMPTY;
    }

    public boolean collides(WorldCoords coords) {
        return Shape.collides(this.rect, coords);
    }

    public void draw() {
        if (this.state == BoardState.X) {
            this.rect.draw(X_TEXTURE);
        }  else if (this.state == BoardState.O) {
            this.rect.draw(O_TEXTURE);
        }
    }

    public BoardState getState() {
        return this.state;
    }

    public void setState(BoardState state) {
        this.state = state;
    }

    public enum BoardState {
        EMPTY, X, O
    }

    @Override
    public String toString() {
        return "BoardSpace{state=" + this.state + "}";
    }
}
