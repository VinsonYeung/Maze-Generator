import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {

    @FXML private Canvas canvas ;

    @FXML private MenuItem newMaze;

    @FXML private ToggleGroup pathColourGroup;

    @FXML private ToggleGroup wrongTurnGroup;

    private GraphicsContext gc ;

    private Maze maze;

    private Solver solver;

    private Color pathColour;

    private String wrongTurnMarker;

    private void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.WHITESMOKE);
        for (Square square: maze.getSquares()) {
            gc.fillRect(square.getX() * 10 - 1, square.getY() * 10 - 1, 9, 9);
        }

        // Connections
        gc.setStroke(Color.WHITESMOKE);
        gc.setLineWidth(8.2);
        for (Square square: maze.getSquares()) {
            for (Square connection : square.getConnections()) {
                gc.strokeLine(square.getX() * 10 + 3.5, square.getY() * 10 + 3.5,
                        connection.getX() * 10 + 3.5, connection.getY() * 10 + 3.5);
            }
        }

        if (wrongTurnMarker.equals("Lines")) {
            gc.setStroke(Color.GRAY);
            gc.setLineWidth(1);
            Square checkSquare = solver.getBlockList().get(0);
            for (Square square: solver.getBlockList().subList(1, solver.getBlockList().size())) {
                // Check if squares are adjacent
                if (checkSquare.isConnected(square)) {
                    gc.strokeLine(checkSquare.getX() * 10 + 4, checkSquare.getY() * 10 + 4,
                            square.getX() * 10 + 4, square.getY() * 10 + 4);
                }
                checkSquare = square;
            }
        } else if (wrongTurnMarker.equals("Circles")) {
            // Block list
            gc.setFill(Color.DARKGRAY);
            for (Square square : solver.getBlockList()) {
                gc.fillOval(square.getX() * 10 + 1, square.getY() * 10 + 1, 5, 5);
            }
        }

        // Path
        gc.setStroke(pathColour);
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
        pathColour = Color.RED;
        wrongTurnMarker = "Hidden";
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
                    pathColour = Color.RED;
                    break;
                case "bluePath":
                    pathColour = Color.BLUE;
                    break;
                case "hidePath":
                    pathColour = Color.WHITESMOKE;
                    break;
            }
            solver = new Solver(maze);
            update();
        }));

        wrongTurnGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {
            // The old value switches to null before switching from null to new value
            if (newValue == null) {
                return;
            }
            switch (((RadioMenuItem) newValue).getId()) {
                case "circleWrongTurns":
                    wrongTurnMarker = "Circles";
                    break;
                case "lineWrongTurns":
                    wrongTurnMarker = "Lines";
                    break;
                case "hideWrongTurns":
                    wrongTurnMarker = "Hidden";
                    break;
            }
            solver = new Solver(maze);
            update();
        }));
    }
}
