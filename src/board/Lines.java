package board;

import jangl.color.ColorFactory;
import jangl.coords.WorldCoords;
import jangl.graphics.shaders.ShaderProgram;
import jangl.graphics.shaders.premade.ColorShader;
import jangl.shapes.Rect;

public class Lines {
    private static final ShaderProgram COLOR_SHADER = new ShaderProgram(new ColorShader(ColorFactory.BLACK));
    private final Rect[] lines;

    public Lines(int boardSize, float thickness) {
        this.lines = new Rect[boardSize * 2];

        WorldCoords topRight = WorldCoords.getTopRight();

        // Set i = 2 instead of i = 0 to skip the first two lines, which border the board
        for (int i = 2; i < this.lines.length; i += 2) {
            this.lines[i] = new Rect(new WorldCoords(topRight.x / boardSize * (i / 2), topRight.y), thickness, topRight.y);
            this.lines[i + 1] = new Rect(new WorldCoords(0, topRight.y / boardSize * (i / 2)), topRight.x, thickness);
        }
    }

    public void draw() {
        for (Rect line : this.lines) {
            if (line == null) {
                continue;
            }

            line.draw(COLOR_SHADER);
        }
    }
}
