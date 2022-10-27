import za.ac.wits.snake.DevelopmentAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;

class MyAgent extends DevelopmentAgent {

    public static int mySnakeNum = 0;
    public static int logCount = 0;

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

                // Start timer
//                Instant start = Instant.now();

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

                // Zombies
                ArrayList<Point> zombieSnakeHeads = new ArrayList<>();
                ArrayList<String> ZombieLineArray = new ArrayList<>();

                for (int zombie = 0; zombie < 6; zombie++) {
                    String zombieLine = br.readLine();
                    ZombieLineArray.add(zombieLine);

                    // Get the head of the zombie snake
                    String[] zombieHead = zombieLine.split(" ");
                    Point zombiePoint = new Point(Integer.parseInt(zombieHead[1].split(",")[1]), Integer.parseInt(zombieHead[0].split(",")[0]));
                    zombieSnakeHeads.add(zombiePoint);

                    // Surround the head of the zombie snake with x's

                    int[] dRow = {1, 0, -1, 0};
                    int[] dCol = {0, 1, 0, -1};

                    for (int i = 0; i < 4; i++) {
                        int row = zombiePoint.row + dRow[i];
                        int col = zombiePoint.col + dCol[i];
                        if (row >= 0 && row < 50 && col >= 0 && col < 50) {
                            board[row][col] = 'x';
                            //boardVoronoi[row][col] = zombie + 1;
                        }
                    }

                    DrawBoard.drawSnake(zombieLine, board);
                    //DrawBoard.drawSnakeFuture(zombieLine, boardFuture, 5);
                    DrawBoard.drawSnakeVornoi(zombieLine, boardVoronoi, zombie + 1);
                }

                mySnakeNum = Integer.parseInt(br.readLine());

                Point myHead = null, myTail = null;

                int length = 0;

                int otherSnakes = 7;

                ArrayList<Point> snakeHeads = new ArrayList<>();
                int snakeHeadArrPos = 0;

                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();

                    // Get details of my snake
                    String[] snakeDetails = snakeLine.split(" ");
                    boolean alive = snakeDetails[0].equals("alive");
                    length = Integer.parseInt(snakeDetails[1]);
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

                        } else {

                            // Just store the head of the other snakes
                            Point snakeHead = new Point(Integer.parseInt(head[1]), Integer.parseInt(head[0]));
                            snakeHeads.add(snakeHead);

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
                        DrawBoard.drawSnakeVornoi(snake.toString(), boardVoronoi, i + 1 + 6);

                    } else {
                        if (i == 0) {
                            otherSnakes--;
                        }

                        if (i == mySnakeNum) {
                            System.out.println("log dead");
                        }
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

                //DrawBoard.printGrid(board);

                // Perform a Voronoi search
                ArrayList<Integer> cellCount = new ArrayList<>();
//
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoi[i], 0, boardVoronoiCopy[i], 0, 50);
                }

                BFS.VoronoiDiagram(boardVoronoi, zombieSnakeHeads, snakeHeads, cellCount);
                //DrawBoard.printIntGrid(boardVoronoi);
                boolean isCloser = boardVoronoi[applePoint.row][applePoint.col] == mySnakeNum + otherSnakes;

                // Set the boardVoronoi back to the original
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoiCopy[i], 0, boardVoronoi[i], 0, 50);
                }
                cellCount.clear();


                int move = 0;

                ArrayList<Point> possibleMoves = new ArrayList<>();
                ArrayList<Point> tailMoves = new ArrayList<>();

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
                                if ((row >= 0 && row < 50 && col >= 0 && col < 50) && (boardVoronoi[row][col] >= 1 && boardVoronoi[row][col] <= 10)) {
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

                    //Up
                    tailMoves.add(new Point(myTail.row - 1, myTail.col));
                    //Down
                    tailMoves.add(new Point(myTail.row + 1, myTail.col));
                    //Left
                    tailMoves.add(new Point(myTail.row, myTail.col - 1));
                    //Right
                    tailMoves.add(new Point(myTail.row, myTail.col + 1));

                    for (int i = 0; i < 4; i++) {

                        move = i;
                        Point newHead = possibleMoves.get(i);

                        // Check if the point is not out of bounds
                        if (newHead.row < 50 && newHead.row >= 0 && newHead.col < 50 && newHead.col >= 0) {
                            if (board[newHead.row][newHead.col] != 'x') {
                                // Change my head coordinates in the snake head array
                                if (snakeHeadArrPos != mySnakeNum) {
                                    mySnakeNum = snakeHeadArrPos;
                                }
                                snakeHeads.set(mySnakeNum, newHead);
                                BFS.VoronoiDiagram(boardVoronoi, zombieSnakeHeads, snakeHeads, cellCount);

                                boolean pathToTail = false;
                                for (Point tailMove : tailMoves) {
                                    if (tailMove.row < 50 && tailMove.row >= 0 && tailMove.col < 50 && tailMove.col >= 0) {
                                        if (board[tailMove.row][tailMove.col] != 'x') {
                                            pathToTail = true;
                                        }
                                    }
                                }

                                if (!pathToTail) {
                                    // If it is not the last loop
                                    if (i != 3) {
                                        // Reset the boardVoronoi back to the original
                                        for (int j = 0; j < 50; j++) {
                                            System.arraycopy(boardVoronoiCopy[j], 0, boardVoronoi[j], 0, 50);
                                        }
                                        cellCount.clear();
                                    }
                                    continue;
                                }

                                if (boardVoronoi[newHead.row][newHead.col] == mySnakeNum + otherSnakes) {
                                    if (cellCount.get(mySnakeNum + (otherSnakes - 1)) > highestCellCount) {
                                        highestCellCount = cellCount.get(mySnakeNum + (otherSnakes - 1));
                                        bestMove = move;
                                        foundMove = true;
                                    }
                                }

                                // If it is not the last loop
                                if (i != 3) {
                                    // Reset the boardVoronoi back to the original
                                    for (int j = 0; j < 50; j++) {
                                        System.arraycopy(boardVoronoiCopy[j], 0, boardVoronoi[j], 0, 50);
                                    }
                                    cellCount.clear();
                                }
                            }
                        }

                    }

                    move = bestMove;

                    //System.out.println("log Found move: " + foundMove);

                    if (!foundMove) {
                        for (Point possibleMove : possibleMoves) {
                            if (possibleMove.row < 50 && possibleMove.row >= 0 && possibleMove.col < 50 && possibleMove.col >= 0) {
                                if (boardVoronoi[possibleMove.row][possibleMove.col] == mySnakeNum + otherSnakes) {
                                    move = possibleMoves.indexOf(possibleMove);
                                    break;
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