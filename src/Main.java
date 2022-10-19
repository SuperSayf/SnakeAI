import za.ac.wits.snake.DevelopmentAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class MyAgent extends DevelopmentAgent {

    public static int logCount = 0;

    public static void main(String[] args) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {

            logCount++;

            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);

            char[][] board = new char[50][50];
            int[][] boardFuture = new int[50][50];
            int[][] boardVoronoi = new int[50][50];

            while (true) {

                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        board[i][j] = '-';
                        boardFuture[i][j] = -1;
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
                        }
                    }

                    DrawBoard.drawSnake(zombieLine, board);
                    //DrawBoard.drawSnakeFuture(zombieLine, boardFuture, 5);
                    DrawBoard.drawSnakeVornoi(zombieLine, boardVoronoi, zombie + 1);
                }

                int mySnakeNum = Integer.parseInt(br.readLine());

                Point myHead = null, myTail = null;

                int length = 0;

                ArrayList<Point> snakeHeads = new ArrayList<>();

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
                        DrawBoard.drawSnakeFuture(snake.toString(), boardFuture, length);
                        DrawBoard.drawSnakeVornoi(snake.toString(), boardVoronoi, i + 1 + 6);

                    }

                }

                // Set the head of my snake to be an S
                assert myHead != null;
                board[myHead.row][myHead.col] = 'S';

                // Perform a Voronoi search
                ArrayList<Integer> cellCount = new ArrayList<>();
                int[][] boardVoronoiCopy = new int[50][50];

                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoi[i], 0, boardVoronoiCopy[i], 0, 50);
                }
                BFS.VornoiDiagram(boardVoronoi, zombieSnakeHeads, snakeHeads, cellCount);
                boolean isCloser = boardVoronoi[applePoint.row][applePoint.col] <= 7;
                // Set the boardVoronoi back to the original
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVoronoiCopy[i], 0, boardVoronoi[i], 0, 50);
                }
                cellCount.clear();


                int move = 0;

                if (isCloser) {

                    move = Astar.startAStar(board, applePoint, myHead);

                    if (move == -1) {
                        // Remove the 'x's surrounding the zombie snake head
                        for (Point zombiePoint : zombieSnakeHeads) {

                            int[] dRow = {1, 0, -1, 0};
                            int[] dCol = {0, 1, 0, -1};

                            for (int k = 0; k < 4; k++) {
                                int row = zombiePoint.row + dRow[k];
                                int col = zombiePoint.col + dCol[k];
                                if (row >= 0 && row < 50 && col >= 0 && col < 50) {
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
                    int[][] bestVoronoiBoard = new int[50][50];
                    ArrayList<Point> possibleMoves = new ArrayList<>();
                    //Up
                    possibleMoves.add(new Point(myHead.row - 1, myHead.col));
                    //Down
                    possibleMoves.add(new Point(myHead.row + 1, myHead.col));
                    //Left
                    possibleMoves.add(new Point(myHead.row, myHead.col - 1));
                    //Right
                    possibleMoves.add(new Point(myHead.row, myHead.col + 1));

                    for (int i = 0; i < 4; i++) {

                        move = i;
                        Point newHead = possibleMoves.get(i);

                        // Check if the point is not out of bounds
                        if (newHead.row < 50 && newHead.row >= 0 && newHead.col < 50 && newHead.col >= 0) {
                            if (board[newHead.row][newHead.col] != 'x') {
                                // Change my head coordinates in the snake head array
                                snakeHeads.set(0, newHead);
                                BFS.VornoiDiagram(boardVoronoi, zombieSnakeHeads, snakeHeads, cellCount);

                                if (boardVoronoi[newHead.row][newHead.col] == 7) {
                                    if (cellCount.get(6) > highestCellCount) {
                                        highestCellCount = cellCount.get(6);
                                        bestMove = move;
                                    }
                                }

                                // Make a copy of the boardVoronoi to the bestVoronoiBoard
                                for (int j = 0; j < 50; j++) {
                                    System.arraycopy(boardVoronoi[j], 0, bestVoronoiBoard[j], 0, 50);
                                }
                                // Reset the boardVoronoi back to the original
                                for (int j = 0; j < 50; j++) {
                                    System.arraycopy(boardVoronoiCopy[j], 0, boardVoronoi[j], 0, 50);
                                }

                                cellCount.clear();

                            }
                        }

                    }

                    move = bestMove;

                }

                // Now I need to check if I have a path back to my tail once the move is taken

                // Case that the move is 1, 2, 3 or 4

//                switch (move) {
//                    case 0 -> {
//                        // Up (relative to the play area - north)
//                        board[yHead][xHead] = 'x';
//                        //System.out.println("log " + yHead + " " + (xHead + 1));
//
//                        board[yHead - 1][xHead] = 'S';
//                        board[appleX][appleY] = '-';
//                        board[yTail][xTail] = 'G';
//                        if (Tail.startBFS(board, false) == -1) {
//                            //System.out.println("log dodge up");
//                            //System.out.println("log " + yHead + " " + (xHead + 1));
//                            board[yHead][xHead] = 'S';
//                            board[yHead - 1][xHead] = '-';
//                            move = Tail.startBFS(board, true);
//                        } else {
//                            //printGrid(board);
//                        }
//                    }
//                    case 1 -> {
//                        // Down (relative to the play area - south)
//                        board[yHead][xHead] = 'x';
//                        //System.out.println("log " + yHead + " " + (xHead - 1));
//
//                        board[yHead + 1][xHead] = 'S';
//                        board[appleX][appleY] = '-';
//                        board[yTail][xTail] = 'G';
//                        if (Tail.startBFS(board, false) == -1) {
//                            //System.out.println("log dodge down");
//                            //System.out.println("log " + yHead + " " + (xHead - 1));
//                            board[yHead][xHead] = 'S';
//                            board[yHead + 1][xHead] = '-';
//                            move = Tail.startBFS(board, true);
//                        } else {
//                            //printGrid(board);
//                        }
//                    }
//                    case 2 -> {
//                        // Left (relative to the play area - west)
//                        board[yHead][xHead] = 'x';
//                        //System.out.println("log " + (yHead - 1) + " " + xHead);
//
//                        board[yHead][xHead - 1] = 'S';
//                        board[appleX][appleY] = '-';
//                        board[yTail][xTail] = 'G';
//                        if (Tail.startBFS(board, false) == -1) {
//                            //System.out.println("log dodge left");
//                            //System.out.println("log " + (yHead - 1) + " " + xHead);
//                            board[yHead][xHead] = 'S';
//                            board[yHead][xHead - 1] = '-';
//                            move = Tail.startBFS(board, true);
//                        } else {
//                            //printGrid(board);
//                        }
//                    }
//                    case 3 -> {
//                        // Right (relative to the play area - east)
//                        board[yHead][xHead] = 'x';
//                        //System.out.println("log " + (yHead + 1) + " " + xHead);
//
//                        board[yHead][xHead + 1] = 'S';
//                        board[appleX][appleY] = '-';
//                        board[yTail][xTail] = 'G';
//                        if (Tail.startBFS(board, false) == -1) {
//                            //System.out.println("log dodge right");
//                            //System.out.println("log " + (yHead + 1) + " " + xHead);
//                            board[yHead][xHead] = 'S';
//                            board[yHead][xHead + 1] = '-';
//                            move = Tail.startBFS(board, true);
//                        } else {
//                            //printGrid(board);
//                        }
//                    }
//                }

                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}