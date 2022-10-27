import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static boolean isInvalidPointVoronoi(int[][] grid, NodeBFS point) {
        if (point.row < 0 || point.row >= grid.length || point.col < 0 || point.col >= grid[0].length)
            return true;
        // Return if it is a number between 1 and 10
        return grid[point.row][point.col] >= 1 && grid[point.row][point.col] <= 10;
    }

    public static void VoronoiDiagram(int[][] boardVoronoi, ArrayList<Point> zombieSnakeHeads, ArrayList<Point> snakeHeads, ArrayList<Integer> cellCount) {

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
        int logCount = MyAgent.logCount;

        while (hasNodes) {

            int index = 0;
            hasNodes = false;


            for (Queue<NodeBFS> queue : toVisit) {

                index++;

                int queueLength = queue.size();
                for (int j = 0; j < queueLength; j++) {

                    if ((index < 7) && (logCount % 2 == 0)) {
                        logCount++;
                        continue;
                    }
                    logCount++;

                    NodeBFS topNode = queue.poll();

                    assert topNode != null;

                    for (NodeBFS nb : topNode.getNeighbours()) {

                        if (isInvalidPointVoronoi(boardVoronoi, nb))
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
}
