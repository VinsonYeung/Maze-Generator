import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Stack;

public class Controller implements Initializable {

    @FXML private Canvas canvas ;

    private GraphicsContext gc ;

    private Maze maze;

    private Solver solver;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        maze = new Maze(80, 60, 0, 0, 79, 59);
        solver = new Solver(maze);
        gc = canvas.getGraphicsContext2D();
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

        gc.setStroke(Color.RED);
        gc.setLineWidth(1);
        Stack<Square> path = solver.getPath();
        Square square = path.pop();
        while (!path.isEmpty()) {
            gc.strokeLine(square.getX() * 10 + 4, square.getY() * 10 + 4,
                    path.peek().getX() * 10 + 4, path.peek().getY() * 10 + 4);
            square = path.pop();
        }
    }
}