import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Map {

    private int width;

    private int height;

    private Square[][] array;

    private List<Square> squares;

    private int xStart;

    private int yStart;

    private int xEnd;

    private int yEnd;

    private List<Square> potentialBranches;

    public Map(int width, int height, int xStart, int yStart, int xEnd, int yEnd) {
        this.width = width;
        this.height = height;
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
        array = new Square[width][height];
        squares = new ArrayList<>();
        potentialBranches = new ArrayList<>();
        buildMap();
    }

    public List<Square> getSquares() {
        return squares;
    }

    private void buildConnection(Square square1, Square square2) {
        square1.addConnection(square2);
        square2.addConnection(square1);
    }

    private boolean checkValidSquare(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        }
        return array[x][y] == null;
    }

    private boolean checkSquareNotBlocked(Square square) {
        int x = square.getX();
        int y = square.getY();
        if (checkValidSquare(x + 1, y)) {
            return true;
        } else if (checkValidSquare(x - 1, y)) {
            return true;
        } else if (checkValidSquare(x, y + 1)) {
            return true;
        } else return checkValidSquare(x, y - 1);
    }

    private boolean checkIfExit(Square square) {
        return square.getX() == xEnd && square.getY() == yEnd;
    }

    private void generatePath(Square square) {
        int x = square.getX();
        int y = square.getY();
        // Generate random path until trapped
        while (true) {
            square = array[x][y];
            // Check if exit
            if (checkIfExit(square)) {
                break;
            }
            // Check any path is possible
            if (!checkSquareNotBlocked(square)) {
                break;
            }
            // Pick random direction that is not occupied
            int direction = 0;
            do {
                switch ((int) Math.ceil(Math.random() * 4)) {
                    // South
                    case 1:
                        if (checkValidSquare(x, y + 1)) {
                            direction = 1;
                            y += 1;
                        }
                        break;
                    // East
                    case 2:
                         if (checkValidSquare(x + 1, y)) {
                             direction = 2;
                             x += 1;
                         }
                        break;
                    // North
                    case 3:
                        if (checkValidSquare(x, y - 1)) {
                            direction = 3;
                            y -= 1;
                        }
                        break;
                    // West
                    case 4:
                        if (checkValidSquare(x - 1, y)) {
                            direction = 4;
                            x -= 1;
                        }
                        break;
                }
            } while (direction == 0);
            squares.add(new Square(x, y));
            array[x][y] = squares.get(squares.size() - 1);
            potentialBranches.add(squares.get(squares.size() - 1));
            buildConnection(squares.get(squares.size() - 1), squares.get(squares.size() - 2));
        }
    }

    private void buildMap() {
        squares.add(new Square(xStart, yStart));
        array[xStart][yStart] = squares.get(squares.size() - 1);
        potentialBranches.add(squares.get(squares.size() - 1));
        generatePath(squares.get(squares.size() - 1));
        while (potentialBranches.size() > 0) {
            int branch = (int) Math.floor(Math.random() * potentialBranches.size());

            Collections.swap(squares, squares.indexOf(potentialBranches.get(branch)), squares.size() - 1);
            generatePath(potentialBranches.get(branch));
            potentialBranches.remove(branch);
        }
    }
}
