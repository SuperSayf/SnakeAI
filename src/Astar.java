import java.util.HashSet;
import java.util.PriorityQueue;

public class Astar {

    static int[][] adj = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static boolean isValidPoint(char[][] grid, Node point) {
        return point.row < 0 || point.row == 50 || point.col < 0 || point.col == 50 || grid[point.row][point.col] == 'x';
    }

    public static int startAStar(char[][] grid, Point start, Point end) {

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
