import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML private Canvas canvas ;

    private GraphicsContext gc ;

    private Map map;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        map = new Map(80, 60, 0, 0, 79, 59);
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITESMOKE);

        for (Square square: map.getSquares()) {
            gc.fillRect(square.getX() * 10 - 1, square.getY() * 10 - 1, 9, 9);
        }

        gc.setStroke(Color.WHITESMOKE);
        for (Square square: map.getSquares()) {
            for (Square connection : square.getConnections()) {
                gc.setLineWidth(8.2);
                gc.strokeLine(square.getX() * 10 + 3.5, square.getY() * 10 + 3.5, connection.getX() * 10 + 3.5, connection.getY() * 10 + 3.5);
            }
        }
    }
}