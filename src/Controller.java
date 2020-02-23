import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {

    @FXML private Canvas canvas ;

    @FXML private MenuItem newMaze;

    @FXML private ToggleGroup pathColourGroup;

    private GraphicsContext gc ;

    private Maze maze;

    private Solver solver;

    private Color colour;

    private void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITESMOKE);

        for (Square square: maze.getSquares()) {
            gc.fillRect(square.getX() * 10 - 1, square.getY() * 10 - 1, 9, 9);
        }

        gc.setStroke(Color.WHITESMOKE);
        gc.setLineWidth(8.2);
        for (Square square: maze.getSquares()) {
            for (Square connection : square.getConnections()) {
                gc.setLineWidth(8.2);
                gc.strokeLine(square.getX() * 10 + 3.5, square.getY() * 10 + 3.5,
                        connection.getX() * 10 + 3.5, connection.getY() * 10 + 3.5);
            }
        }

        gc.setStroke(colour);
        gc.setLineWidth(1);
        Stack<Square> path = solver.getPath();
        Square square = path.pop();
        while (!path.isEmpty()) {
            gc.strokeLine(square.getX() * 10 + 4, square.getY() * 10 + 4,
                    path.peek().getX() * 10 + 4, path.peek().getY() * 10 + 4);
            square = path.pop();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maze = new Maze(80, 60, 0, 0, 79, 59);
        solver = new Solver(maze);
        gc = canvas.getGraphicsContext2D();
        colour = Color.RED;
        update();

        canvas.setOnMouseClicked(event -> {
            int x = (int) event.getX() / 10, y = (int) event.getY() / 10;
            if (event.getButton() == MouseButton.PRIMARY) {
                maze.setxEnd(x);
                maze.setyEnd(y);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                maze.setxStart(x);
                maze.setyStart(y);
            }
            solver = new Solver(maze);
            update();
        });

        newMaze.setOnAction(event -> {
            maze = new Maze(80, 60, 0, 0, 79, 59);
            solver = new Solver(maze);
            update();
        });

        pathColourGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            // The old value switches to null before switching from null to new value
            if (newValue == null) {
                return;
            }
            switch (((RadioMenuItem) newValue).getId()) {
                case "redPath":
                    colour = Color.RED;
                    break;
                case "bluePath":
                    colour = Color.BLUE;
                    break;
                case "hide":
                    colour = Color.WHITESMOKE;
                    break;
            }
            solver = new Solver(maze);
            update();
        }));

    }
}
