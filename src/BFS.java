import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class BFS {

    public static boolean isInvalidPointVoronoi(int[][] grid, NodeBFS point) {
        return point.row < 0 || point.row >= 50 || point.col < 0 || point.col >= 50 || grid[point.row][point.col] >= 1;
    }

    public static void VoronoiDiagram(int[][] boardVoronoi, ArrayList<Snake> Snakes, ArrayList<Integer> cellCount, int mySnakeIndex) {

        ArrayList<Point> Heads = new ArrayList<>();
        ArrayList<Integer> Index = new ArrayList<>();

        for (Snake snake : Snakes) {
            Heads.add(snake.head);
            Index.add(snake.index);
        }

        cellCount.add(0);

        // Have a queue for each head and add it to an array of queues, and perform directional BFS on each queue
        ArrayList<Queue<NodeBFS>> toVisit = new ArrayList<>();

        for (Point head : Heads) {
            Queue<NodeBFS> queue = new LinkedList<>();
            queue.add(new NodeBFS(head));
            toVisit.add(queue);
        }

        // Perform an n-directional BFS on each queue, and remember how many nodes are in the queue before you pop off, here you're only popping off one item, which isn't valid

        boolean hasNodes = true;
        //int logCount = MyAgent.logCount;
        int logCount = 0;

        while (hasNodes) {

            int index = 0;
            hasNodes = false;


            for (Queue<NodeBFS> queue : toVisit) {

                int queueLength = queue.size();
                for (int j = 0; j < queueLength; j++) {

                    if ((Index.get(index) < 7) && ((MyAgent.logCount + logCount) % 2 != 0)) {
                        logCount++;
                        //queueLength--;
                        continue;
                    }
                    logCount++;

                    NodeBFS topNode = queue.poll();

                    assert topNode != null;

                    for (NodeBFS nb : topNode.getNeighbours()) {

                        if (isInvalidPointVoronoi(boardVoronoi, nb))
                            continue;

                        if (boardVoronoi[nb.row][nb.col] == -1) {

                            boardVoronoi[nb.row][nb.col] = Index.get(index);

                            if (Index.get(index) == mySnakeIndex) {
                                cellCount.set(0, cellCount.get(0) + 1);
                            }

                            queue.add(nb);
                            nb.parent = topNode;
                            hasNodes = true;
                        }
                    }

                }

                index++;
            }
        }

    }
}
