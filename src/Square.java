import java.util.ArrayList;
import java.util.List;

public class Square {

    private int xCoordinate;

    private int yCoordinate;

    private List<Square> connections;

    public Square(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        connections = new ArrayList<>();
    }

    public int getX() {
        return xCoordinate;
    }

    public int getY() {
        return yCoordinate;
    }

    public void addConnection(Square square) {
        connections.add(square);
    }

    public List<Square> getConnections() {
        return connections;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Square)) {
            return false;
        }
        Square target = (Square) o;
        return this.xCoordinate == target.xCoordinate && this.yCoordinate == target.yCoordinate;
    }
}
