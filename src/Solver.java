import java.util.*;

public class Solver {

    private Stack<Square> path;

    private Stack<Square> nodes;
    // List of squares after nodes which lead to dead-ends
    private List<Square> blockList;

    private Square square;

    private int x;

    private int y;

    public Solver(Maze maze) {
        path = new Stack<>();
        nodes = new Stack<>();
        blockList = new ArrayList<>();
        solve(maze);
    }

    public Stack<Square> getPath() {
        return path;
    }

    public List<Square> getBlockList() {
        return blockList;
    }

    private Square pickConnection() {
        if (square.getConnections().size() == 1) {
            return square.getConnections().get(0);
        } else if (square.getConnections().size() == 2) {
            // Goto square not on path
            if (path.contains(square.getConnections().get(0))) {
                return square.getConnections().get(1);
            } else {
                return square.getConnections().get(0);
            }
        } else {
            List<Square> routes = new ArrayList<>();
            for (Square connection : square.getConnections()) {
                if (!blockList.contains(connection) && !path.contains(connection)) {
                    routes.add(connection);
                }
            }
            // If routes exhausted
            if (routes.isEmpty()) {
                revertNode();
                return pickConnection();
            }
            return routes.get((int) Math.floor(Math.random() * routes.size()));
        }
    }

    private void revertNode() {
        nodes.pop();
        do {
            blockList.add(square);
            path.pop();
            square = path.peek();
            x = square.getX();
            y = square.getY();
        } while (nodes.peek() != square);
    }

    private void solve(Maze maze) {
        square = maze.getArray()[maze.getxStart()][maze.getyStart()];
        path.push(square);
        x = square.getX();
        y = square.getY();

        //Check if start is also the end
        if (maze.getxStart() == maze.getxEnd() && maze.getyStart() == maze.getyEnd()) {
            return;
        }

        // At start
        if (square.getConnections().size() > 1) {
            nodes.add(maze.getArray()[x][y]);
        }
        square = pickConnection();
        path.push(square);
        x = square.getX();
        y = square.getY();

        while (true) {
            if (x == maze.getxEnd() && y == maze.getyEnd()) { // End position
                path.push(square);
                return;
            } else if (square.getConnections().size() == 1) { // Dead-end
                // Go back to node
                // Current square is a node
                do {
                    blockList.add(square);
                    path.pop();
                    square = path.peek();
                    x = square.getX();
                    y = square.getY();
                } while (nodes.peek() != square);
            } else if (square.getConnections().size() == 2) { // Two connections
                square = pickConnection();
                path.push(square);
                x = square.getX();
                y = square.getY();
            } else {
                if (!nodes.contains(square)) {
                    nodes.add(square);
                }
                square = pickConnection();
                path.push(square);
                x = square.getX();
                y = square.getY();
            }
        }
    }
}
