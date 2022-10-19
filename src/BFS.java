import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

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
        //Logger.log(map.toString());
    }

    public static boolean isInvalidPoint(char[][] grid, Node point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        return grid[point.row][point.col] == 'x';
    }

    public static boolean isInvalidPointVornoi(int[][] grid, NodeBFS point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        // Return if it is a number between 1 and 10
        return grid[point.row][point.col] >= 1 && grid[point.row][point.col] <= 10;
    }

    public static boolean isInvalidPointMain(char[][] grid, int[][] boardFuture, int queueLength, Node point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        //return grid[point.row][point.col] == 'x';
        return queueLength < boardFuture[point.row][point.col];

    }

    public static ArrayList<String> startBFSAnalyze(char[][] grid) {
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

        ArrayList<String> path = new ArrayList<>();

        while (!toVisit.isEmpty()) {
            Node topNode = toVisit.poll();

            if (topNode.equals(apple)) {

                while (!topNode.equals(head)) {
                    path.add(topNode.row + "," + topNode.col);
                    topNode = topNode.parent;
                }

                return path;
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

        return path;
    }

    public static void VornoiDiagram(int[][] boardVoronoi, ArrayList<Point> zombieSnakeHeads, ArrayList<Point> snakeHeads, ArrayList<Integer> cellCount) {

        ArrayList<Point> Heads = new ArrayList<>();
        Heads.addAll(zombieSnakeHeads);
        Heads.addAll(snakeHeads);

        //Initialize 0-10 on the cellCount array
        for (int i = 0; i < 10; i++) {
            cellCount.add(0);
        }

        // Have a queue for each head and add it to an array of queues, and perform directional BFS on each queue
        ArrayList<Queue<NodeBFS>> toVisit = new ArrayList<>();

        for (Point head : Heads) {
            Queue<NodeBFS> queue = new LinkedList<>();
            queue.add(new NodeBFS(head));
            toVisit.add(queue);
        }

        // Perform an n-directional BFS on each queue, and remember how many nodes are in the queue before you pop off, here you're only popping off one item, which isn't valid

        boolean hasNodes = true;

        while (hasNodes) {

            int index = 0;
            hasNodes = false;

            for (Queue<NodeBFS> queue : toVisit) {

                index++;

                int queueLength = queue.size();
                for (int j = 0; j < queueLength; j++) {
                    NodeBFS topNode = queue.poll();

                    assert topNode != null;

                    for (NodeBFS nb : topNode.getNeighbours()) {
                        if (isInvalidPointVornoi(boardVoronoi, nb))
                            continue;

                        if (boardVoronoi[nb.row][nb.col] == -1) {
                            boardVoronoi[nb.row][nb.col] = index;
                            cellCount.set(index - 1, cellCount.get(index - 1) + 1);
                            queue.add(nb);
                            nb.parent = topNode;
                            hasNodes = true;
                        }
                    }


                }
            }
        }

    }

    public static int startBFS(char[][] grid, int[][] boardFuture, boolean mySnake, boolean predictFuture) {
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
                if (mySnake) {
                    printPath(grid, head, topNode);
                    return getMove(apple, topNode.parent);
                } else {
                    // Return the number of steps to the apple (topNode length)
                    return getSteps(topNode);
                }
            }

            for (int[] off : adj) {
                Node nb = new Node(topNode.row + off[0], topNode.col + off[1], end);

                if (predictFuture) {
                    // Get the current length of the queue
                    int queueLength = toVisit.size();

                    if (isInvalidPointMain(grid, boardFuture, queueLength, nb) || visited[nb.row][nb.col])
                        continue;

                } else {
                    if (isInvalidPoint(grid, nb) || visited[nb.row][nb.col])
                        continue;
                }


                visited[nb.row][nb.col] = true;
                toVisit.add(nb);
                nb.parent = topNode;
            }
        }

        return -1;
    }

    public static int getSteps(Node topNode) {
        int steps = 0;
        while (topNode.parent != null) {
            steps++;
            topNode = topNode.parent;
        }
        return steps;
    }

    public static int getMove(Point from, Point to) {
        if (from.row != to.row)
            return from.row < to.row ? 1 : 0;

        return from.col < to.col ? 3 : 2;
    }
}
