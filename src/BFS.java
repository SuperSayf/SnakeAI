import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static boolean isInvalidPointVornoi(char[][] grid, Node point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        return grid[point.row][point.col] != 'x';
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

    public static void VornoiDiagram(char[][] grid, int[][] boardVornoi, ArrayList<Integer> ZombiesnakeHeadX, ArrayList<Integer> ZombiesnakeHeadY, ArrayList<Integer> snakeHeadX, ArrayList<Integer> snakeHeadY) {

        ArrayList<Integer> HeadsY = new ArrayList<>(ZombiesnakeHeadX);
        ArrayList<Integer> HeadsX = new ArrayList<>(ZombiesnakeHeadY);
        HeadsY.addAll(snakeHeadX);
        HeadsX.addAll(snakeHeadY);

        int numberOfHeads = HeadsX.size();

        // Have a queue for each head and add it to an array of queues, and perform directional BFS on each queue
        ArrayList<Queue<Node>> toVisit = new ArrayList<>();
        for (int i = 0; i < numberOfHeads; i++) {
            Queue<Node> queue = new LinkedList<>();
            queue.add(new Node(HeadsX.get(i), HeadsY.get(i), new Point()));
            toVisit.add(queue);
        }

        // Peform a n-directional BFS on each queue
        while (true) {
            boolean allEmpty = true;
            for (int i = 0; i < numberOfHeads; i++) {
                Queue<Node> queue = toVisit.get(i);
                if (!queue.isEmpty()) {
                    allEmpty = false;
                    Node topNode = queue.poll();
                    grid[topNode.row][topNode.col] = 'x';

                    boardVornoi[topNode.row][topNode.col] = i + 1;

                    for (int[] off : adj) {
                        Node nb = new Node(topNode.row + off[0], topNode.col + off[1], new Point());
                        if (isInvalidPoint(grid, nb)) {
                            boardVornoi[topNode.row][topNode.col] = i + 1;
                            continue;
                        }
                        queue.add(nb);
                    }
                }
            }
            if (allEmpty)
                break;
        }

        MyAgent.printIntGrid(boardVornoi);

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
