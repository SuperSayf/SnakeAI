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

    public static void VornoiDiagram(char[][] grid, ArrayList<Integer> ZombiesnakeHeadX, ArrayList<Integer> ZombiesnakeHeadY, ArrayList<Integer> snakeHeadX, ArrayList<Integer> snakeHeadY) {

        ArrayList<Integer> HeadsX = new ArrayList<>(snakeHeadX);
        ArrayList<Integer> HeadsY = new ArrayList<>(snakeHeadY);
        HeadsX.addAll(ZombiesnakeHeadX);
        HeadsY.addAll(ZombiesnakeHeadY);

        // Now we will have a shared visited array for all the snakes
        boolean[][] visited = new boolean[50][50];
        int[][] visitedView = new int[50][50];

        //do 1 iteration of BFS for each snake, meaning, for all of the nodes in that snake's queue, you'd explore each item in that queue and add it's neighbours to their queue, then switch to the next snake, that way, you kind of make a territory map, of which snake owns which cell
        for (int i = 0; i < (ZombiesnakeHeadX.size() + snakeHeadX.size()); i++) {

            Queue<Node> toVisit = new LinkedList<>();

            // Add the head of the snake to the queue
            toVisit.add(new Node(HeadsX.get(i), HeadsY.get(i), new Point(HeadsX.get(i), HeadsY.get(i))));

            // Mark the head of the snake as visited
            visited[HeadsX.get(i)][HeadsY.get(i)] = true;
            visitedView[HeadsX.get(i)][HeadsY.get(i)] = i;

            while (!toVisit.isEmpty()) {
                Node topNode = toVisit.poll();

                for (int[] off : adj) {
                    Node nb = new Node(topNode.row + off[0], topNode.col + off[1], new Point(topNode.row + off[0], topNode.col + off[1]));

                    if (isInvalidPoint(grid, nb) || visited[nb.row][nb.col])
                        continue;

                    visited[nb.row][nb.col] = true;
                    visitedView[nb.row][nb.col] = i;
                    toVisit.add(nb);
                    nb.parent = topNode;
                }
            }
        }

        // Indicate which snake owns which cell
        MyAgent.printIntGrid(visitedView);

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
