import za.ac.wits.snake.DevelopmentAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class MyAgent extends DevelopmentAgent {

    public static int mySnakeNum = 0;
    public static int logCount = 0;

    public static int deadSnakes = 0;

    public static void main(String[] args) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);

            char[][] board = new char[50][50];
            //int[][] boardFuture = new int[50][50];
            int[][] boardVoronoi = new int[50][50];
            int[][] boardVoronoiCopy = new int[50][50];

            while (true) {

                deadSnakes = 1;

                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        board[i][j] = '-';
                        //boardFuture[i][j] = -1;
                        boardVoronoi[i][j] = -1;
                    }
                }

                String line = br.readLine();

                if (line.contains("Game Over")) {
                    break;
                }

                // Print the apple on the board
                Point applePoint = new Point(line, " ");
                board[applePoint.row][applePoint.col] = 'G';

                ArrayList<Snake> Snakes = new ArrayList<>();

                // Zombies
                ArrayList<String> ZombieLineArray = new ArrayList<>();
                ArrayList<Point> zombieSnakeHeads = new ArrayList<>();

                for (int zombie = 0; zombie < 6; zombie++) {
                    String zombieLine = br.readLine();
                    ZombieLineArray.add(zombieLine);

                    String zombiescoord[] = zombieLine.split("\\s");
                    String zombiescoord2[] = zombiescoord[0].split(",");
                    Point zombiePoint = new Point(Integer.parseInt(zombiescoord2[1]), Integer.parseInt(zombiescoord2[0]));


                    // Get the head of the zombie snake
                    //Point zombiePoint = new Point(Integer.parseInt(zombieHead[1].split(",")[1]), Integer.parseInt(zombieHead[0].split(",")[0]));
                    zombieSnakeHeads.add(zombiePoint);
                    Snakes.add(new Snake(zombiePoint, null, 5, 0, zombie + 1, true, true));

                    // Surround the head of the zombie snake with x's

                    int[] dRow = {1, 0, -1, 0};
                    int[] dCol = {0, 1, 0, -1};

                    for (int i = 0; i < 4; i++) {
                        int row = zombiePoint.row + dRow[i];
                        int col = zombiePoint.col + dCol[i];
                        if (row >= 0 && row < 50 && col >= 0 && col < 50) {
                            board[row][col] = 'x';
//                            for (int j = 0; j < 4; j++) {
//                                int row2 = row + dRow[j];
//                                int col2 = col + dCol[j];
//                                if ((row2 >= 0 && row2 < 50 && col2 >= 0 && col2 < 50)) {
//                                    board[row2][col2] = 'x';
//                                }
//                            }
                        }
                    }

                    DrawBoard.drawSnake(zombieLine, board);
                    //DrawBoard.drawSnakeFuture(zombieLine, boardFuture, 5);
                    DrawBoard.drawSnakeVornoi(zombieLine, boardVoronoi, zombie + 1);
                }

                mySnakeNum = Integer.parseInt(br.readLine());

                Point myHead = null, myTail = null;
                Snake mySnake = null;

                boolean alive = false;

                ArrayList<Point> snakeHeads = new ArrayList<>();
                int snakeHeadArrPos = 0;

                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();

                    // Get details of my snake
                    String[] snakeDetails = snakeLine.split(" ");
                    alive = snakeDetails[0].equals("alive");
                    int length = Integer.parseInt(snakeDetails[1]);
                    int kills = Integer.parseInt(snakeDetails[2]);

                    if (alive) {

                        StringBuilder snake = new StringBuilder();

                        // Get the head coordinates of the snake
                        String[] head = snakeDetails[3].split(",");

                        if (i == mySnakeNum) {

                            myHead = new Point(Integer.parseInt(head[1]), Integer.parseInt(head[0]));
                            snakeHeads.add(myHead);
                            snakeHeadArrPos = snakeHeads.size() - 1;

                            // Last element in the snakeDetails array is the tail
                            String[] tail = snakeDetails[snakeDetails.length - 1].split(",");
                            myTail = new Point(Integer.parseInt(tail[1]), Integer.parseInt(tail[0]));

                            mySnake = new Snake(myHead, myTail, length, kills, i + 7, true, false);

                            Snakes.add(mySnake);

                        } else {

                            //snakeLengths.add(i + " " + length);

                            // Just store the head of the other snakes
                            Point snakeHead = new Point(Integer.parseInt(head[1]), Integer.parseInt(head[0]));
                            snakeHeads.add(snakeHead);

                            Snakes.add(new Snake(snakeHead, null, length, kills, i + 7, true, false));

                        }

                        for (int j = 3; j < snakeDetails.length; j++) {
                            if (j + 1 == snakeDetails.length) {
                                snake.append(snakeDetails[j]);
                            } else {
                                snake.append(snakeDetails[j]).append(" ");
                            }
                        }

                        DrawBoard.drawSnake(snake.toString(), board);
                        //DrawBoard.drawSnakeFuture(snake.toString(), boardFuture, length);
                        DrawBoard.drawSnakeVornoi(snake.toString(), boardVoronoi, i + 7);

                    } else {
                        deadSnakes++;
                    }

                }

                for (Point head : snakeHeads) {
                    // Surround the head of the player snake with x's

                    if (head == myHead) {
                        continue;
                    }

                    int[] dRow = {1, 0, -1, 0};
                    int[] dCol = {0, 1, 0, -1};

                    for (int i = 0; i < 4; i++) {
                        int row = head.row + dRow[i];
                        int col = head.col + dCol[i];
                        if (row >= 0 && row < 50 && col >= 0 && col < 50) {
                            board[row][col] = 'x';
                        }
                    }
                }

                // Set the head of my snake to be an S
                assert myHead != null;
                board[myHead.row][myHead.col] = 'S';

                // DrawBoard.printGrid(board);

                // Perform a Voronoi search
                ArrayList<Integer> cellCount = new ArrayList<>();
//
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoi[i], 0, boardVoronoiCopy[i], 0, 50);
                }

//                for (Point zombieHead : zombieSnakeHeads) {
//                    boardVoronoi[zombieHead.row][zombieHead.col] = 0;
//                }

                //Up
//                Point up = new Point(myHead.row - 1, myHead.col);
//                //Down
//                Point down = new Point(myHead.row + 1, myHead.col);
//                //Left
//                Point left = new Point(myHead.row, myHead.col - 1);
//                //Right
//                Point right = new Point(myHead.row, myHead.col + 1);
//
//                boardVoronoi[up.row][up.col] = -2;
//                boardVoronoi[down.row][down.col] = -3;
//                boardVoronoi[left.row][left.col] = -4;
//                boardVoronoi[right.row][right.col] = 0;

//                for (Point snakeHead : snakeHeads) {
//                    boardVoronoi[snakeHead.row][snakeHead.col] = 0;
//                }
//
//                DrawBoard.printIntGrid(boardVoronoi);

                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoiCopy[i], 0, boardVoronoi[i], 0, 50);
                }

                BFS.VoronoiDiagram(boardVoronoi, Snakes, cellCount, mySnake.index);

//                for (Point head : zombieSnakeHeads) {
//                    // Surround the heads of the zombie snakes with 1's
//
//                    int[] dRow = {1, 0, -1, 0};
//                    int[] dCol = {0, 1, 0, -1};
//
//                    for (int k = 0; k < 4; k++) {
//                        int row = head.row + dRow[k];
//                        int col = head.col + dCol[k];
//                        if ((row >= 0 && row < 50 && col >= 0 && col < 50)) {
//                            boardVoronoi[row][col] = 1;
//                        }
//                    }
//                }

//                for (Point zombieHead : zombieSnakeHeads) {
//                    boardVoronoi[zombieHead.row][zombieHead.col] = 0;
//                }
//
//                for (Point snakeHead : snakeHeads) {
//                    boardVoronoi[snakeHead.row][snakeHead.col] = 0;
//                }

                //DrawBoard.printIntGrid(boardVoronoi);
                boolean isCloser = boardVoronoi[applePoint.row][applePoint.col] == mySnake.index;

                // Set the boardVoronoi back to the original
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoiCopy[i], 0, boardVoronoi[i], 0, 50);
                }
                cellCount.clear();


                int move = 0;

                ArrayList<Point> possibleMoves = new ArrayList<>();
                ArrayList<Point> tailMoves = new ArrayList<>();

                if (alive) {
                    if (isCloser) {

                        //System.out.println("log a*");

                        move = Astar.startAStar(board, applePoint, myHead);

                        if (move == -1) {
                            // Remove the 'x's surrounding the zombie snake head
                            for (Point zombiePoint : zombieSnakeHeads) {

                                int[] dRow = {1, 0, -1, 0};
                                int[] dCol = {0, 1, 0, -1};

                                for (int k = 0; k < 4; k++) {
                                    int row = zombiePoint.row + dRow[k];
                                    int col = zombiePoint.col + dCol[k];
                                    if ((row >= 0 && row < 50 && col >= 0 && col < 50)) {
                                        if (board[row][col] == 'x') {
                                            board[row][col] = '-';
                                        }
                                    }
                                }
                            }

                            for (int zombie = 0; zombie < 6; zombie++) {
                                DrawBoard.drawSnake(ZombieLineArray.get(zombie), board);
                            }

                            // Now try again
                            move = Astar.startAStar(board, applePoint, myHead);

                        }

                    } else {

                        int bestMove = 0;
                        int highestCellCount = 0;
                        boolean foundMove = false;


                        //Up
                        possibleMoves.add(new Point(myHead.row - 1, myHead.col));
                        //Down
                        possibleMoves.add(new Point(myHead.row + 1, myHead.col));
                        //Left
                        possibleMoves.add(new Point(myHead.row, myHead.col - 1));
                        //Right
                        possibleMoves.add(new Point(myHead.row, myHead.col + 1));

//                        //Up
//                        tailMoves.add(new Point(myTail.row - 1, myTail.col));
//                        //Down
//                        tailMoves.add(new Point(myTail.row + 1, myTail.col));
//                        //Left
//                        tailMoves.add(new Point(myTail.row, myTail.col - 1));
//                        //Right
//                        tailMoves.add(new Point(myTail.row, myTail.col + 1));

                        for (int i = 0; i < 4; i++) {


                            move = i;
                            Point newHead = possibleMoves.get(i);

                            // Check if the point is not out of bounds
                            if (newHead.row < 50 && newHead.row >= 0 && newHead.col < 50 && newHead.col >= 0) {

                                // Check the neighbouring cells
                                if (board[newHead.row][newHead.col] == 'x') {
                                    continue;
                                }

                                Snakes.get(mySnake.index - deadSnakes).head = newHead;
                                BFS.VoronoiDiagram(boardVoronoi, Snakes, cellCount, mySnake.index);

//                                boolean pathToTail = false;
//                                for (Point tailMove : tailMoves) {
//                                    if (tailMove.row < 50 && tailMove.row >= 0 && tailMove.col < 50 && tailMove.col >= 0) {
//                                        if (boardVoronoi[tailMove.row][tailMove.col] == mySnake.index) {
//                                            pathToTail = true;
//                                        }
//                                    }
//                                }
//
//                                if (!pathToTail) {
//                                    // If it is not the last loop
//                                    if (i != 3) {
//                                        // Reset the boardVoronoi back to the original
//                                        for (int j = 0; j < 50; j++) {
//                                            System.arraycopy(boardVoronoiCopy[j], 0, boardVoronoi[j], 0, 50);
//                                        }
//                                        cellCount.clear();
//                                    }
//                                    continue;
//                                }

//                                for (Point head : zombieSnakeHeads) {
//                                    // Surround the heads of the zombie snakes with 1's
//
//                                    int[] dRow = {1, 0, -1, 0};
//                                    int[] dCol = {0, 1, 0, -1};
//
//                                    for (int k = 0; k < 4; k++) {
//                                        int row = head.row + dRow[k];
//                                        int col = head.col + dCol[k];
//                                        if ((row >= 0 && row < 50 && col >= 0 && col < 50)) {
//                                            boardVoronoi[row][col] = 1;
//                                        }
//                                    }
//                                }

                                if (boardVoronoi[newHead.row][newHead.col] == mySnake.index) {
                                    if (cellCount.get(0) >= highestCellCount) {
                                        highestCellCount = cellCount.get(0);
                                        bestMove = move;
                                        foundMove = true;
                                    }
                                }

                                // Reset the boardVoronoi back to the original
                                for (int j = 0; j < 50; j++) {
                                    System.arraycopy(boardVoronoiCopy[j], 0, boardVoronoi[j], 0, 50);
                                }
                                cellCount.clear();
                            }

                        }

                        move = bestMove;

                        //System.out.println("log " + logCount + cellCountPrint);


                        //System.out.println("log Found move: " + foundMove);

                        if (!foundMove) {
                            //System.out.println("log miss");

                            // Remove the 'x's surrounding the zombie snake head
                            for (Point zombiePoint : zombieSnakeHeads) {

                                int[] dRow = {1, 0, -1, 0};
                                int[] dCol = {0, 1, 0, -1};

                                for (int k = 0; k < 4; k++) {
                                    int row = zombiePoint.row + dRow[k];
                                    int col = zombiePoint.col + dCol[k];
                                    if ((row >= 0 && row < 50 && col >= 0 && col < 50)) {
                                        if (board[row][col] == 'x') {
                                            board[row][col] = '-';
                                        }
                                    }
                                }
                            }

                            for (int zombie = 0; zombie < 6; zombie++) {
                                DrawBoard.drawSnake(ZombieLineArray.get(zombie), board);
                            }

                            for (Point possibleMove : possibleMoves) {
                                if (possibleMove.row < 50 && possibleMove.row >= 0 && possibleMove.col < 50 && possibleMove.col >= 0) {
                                    if (board[possibleMove.row][possibleMove.col] != 'x') {
                                        move = possibleMoves.indexOf(possibleMove);
                                        //System.out.println("log took default move: " + move);
                                        break;
                                    }
                                }
                            }
                        }

                    }
                }

//                // End timer
//                Instant end = Instant.now();
//
//                // Calculate the time taken
//                Duration timeElapsed = Duration.between(start, end);
//
//                // Print the time taken
//                System.out.println("log " + timeElapsed.toMillis());

                snakeHeads.clear();
                Snakes.clear();
                zombieSnakeHeads.clear();
                cellCount.clear();
                ZombieLineArray.clear();
                possibleMoves.clear();
                tailMoves.clear();
                logCount++;
                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}