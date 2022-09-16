import java.util.HashSet;
import java.util.PriorityQueue;

public class Astar {

    static int[][] adj = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static void printPath(char[][] grid, Node start, Node end) {
        Node endNode = end.parent;

        while (!endNode.equals(start)) {
            grid[endNode.row][endNode.col] = '*';
            endNode = endNode.parent;
        }

        printGrid(grid);
    }

    public static void printGrid(char[][] grid) {
        StringBuilder map = new StringBuilder();
        for (char[] chars : grid) {
            for (char aChar : chars) {
                map.append(aChar).append(" ");
            }
            map.append("\n");
        }
        Logger.log(map.toString());
    }

    public static boolean isValidPoint(char[][] grid, Node point) {
        return point.row < 0 || point.row == 50 || point.col < 0 || point.col == 50 || grid[point.row][point.col] == 'x';
    }

    public static int startAStar(char[][] grid) {
        Point start = new Point(), end = new Point();

        for (int i = 0; i < 50; i++) // Finding start and end point coordinates
        {
            for (int j = 0; j < 50; j++) {
                if (grid[i][j] == 'G')
                    start.set(i, j);

                else if (grid[i][j] == 'S')
                    end.set(i, j);
            }
        }

        Node head = new Node(start.row, start.col, end);
        Node apple = new Node(end.row, end.col, end);

        // A Star

        PriorityQueue<Node> toVisit = new PriorityQueue<>();
        HashSet<Node> visited = new HashSet<>();
        toVisit.add(head);

        while (!toVisit.isEmpty()) {
            Node curr = toVisit.poll();
            visited.add(curr);

            if (curr.equals(apple)) {
                printPath(grid, head, curr);
                return getMove(apple, curr.parent);
            }

            for (int[] off : adj) {
                Node node = new Node(curr.row + off[0], curr.col + off[1], end);

                if (isValidPoint(grid, node) || visited.contains(node))
                    continue;

                visited.add(node);
                toVisit.add(node);
                node.parent = curr;
            }
        }

        return -1;
    }

    public static int getMove(Point from, Point to) {
        if (from.row != to.row)
            return from.row < to.row ? 1 : 0;

        return from.col < to.col ? 3 : 2;
    }
}
