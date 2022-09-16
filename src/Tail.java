import java.util.LinkedList;
import java.util.Queue;

public class Tail {

    static int[][] adj = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    public static boolean isInvalidPoint(char[][] grid, Node point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        return grid[point.row][point.col] == 'x';
    }

    public static int startBFS(char[][] grid) {
        Point start = new Point(), end = new Point();

        // Find the start and end points using the grid
        // Note: I am setting the apple as the head and the head as the apple so that I can easily the last co-ordinate of the path
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                if (grid[i][j] == 'G')
                    start.set(i, j);

                else if (grid[i][j] == 'S')
                    end.set(i, j);
            }
        }

        Node head = new Node(start.row, start.col, end);
        Node apple = new Node(end.row, end.col, end);


        boolean[][] visited = new boolean[50][50];
        Queue<Node> toVisit = new LinkedList<>();

        toVisit.add(head);
        visited[head.row][head.col] = true;

        while (!toVisit.isEmpty()) {
            Node topNode = toVisit.poll();

            if (topNode.equals(apple)) {
                return getMove(apple, topNode.parent);
            }

            for (int[] off : adj) {
                Node nb = new Node(topNode.row + off[0], topNode.col + off[1], end);

                if (isInvalidPoint(grid, nb) || visited[nb.row][nb.col])
                    continue;

                visited[nb.row][nb.col] = true;
                toVisit.add(nb);
                nb.parent = topNode;
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
