import za.ac.wits.snake.DevelopmentAgent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

class MyAgent extends DevelopmentAgent {

    public static void main(String[] args) {
        MyAgent agent = new MyAgent();
        MyAgent.start(agent, args);
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

    public static void printIntGrid(int[][] grid) {
        StringBuilder map = new StringBuilder();

        for (int[] ints : grid) {
            for (int anInt : ints) {
                map.append(anInt).append(" ");
            }
            map.append("\n");
        }

        //Logger.log(map.toString());
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            char[][] board = new char[50][50];
            int[][] boardFuture = new int[50][50];

            while (true) {

                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        board[i][j] = '-';
                        boardFuture[i][j] = -1;
                    }
                }

                String line = br.readLine();
                if (line.contains("Game Over")) {
                    break;
                }

                // Print the apple on the board
                String[] apple = line.split(" ");
                int appleX = Integer.parseInt(apple[1]);
                int appleY = Integer.parseInt(apple[0]);
                board[appleX][appleY] = 'G';

                // Zombies

                ArrayList<Integer> ZombiesnakeHeadX = new ArrayList<>();
                ArrayList<Integer> ZombiesnakeHeadY = new ArrayList<>();
                ArrayList<String> ZombieLineArray = new ArrayList<>();

                for (int zombie = 0; zombie < 6; zombie++) {
                    String zombieLine = br.readLine();
                    ZombieLineArray.add(zombieLine);

                    // Get the head of the zombie snake
                    String[] zombieHead = zombieLine.split(" ");
                    int x = Integer.parseInt(zombieHead[0].split(",")[0]);
                    int y = Integer.parseInt(zombieHead[1].split(",")[1]);

                    ZombiesnakeHeadX.add(x);
                    ZombiesnakeHeadY.add(y);

                    // Surround the head of the zombie snake with x's

                    int[] dRow = {1, 0, -1, 0};
                    int[] dCol = {0, 1, 0, -1};

                    for (int i = 0; i < 4; i++) {
                        int row = y + dRow[i];
                        int col = x + dCol[i];
                        if (row >= 0 && row < 50 && col >= 0 && col < 50) {
                            board[row][col] = 'x';
                        }
                    }

                    DrawBoard.drawSnake(zombieLine, board);
                    DrawBoard.drawSnakeFuture(zombieLine, boardFuture, 5);
                }

                int mySnakeNum = Integer.parseInt(br.readLine());
                int xHead = 0, yHead = 0, xTail = 0, yTail = 0;
                int length = 0;
                ArrayList<Integer> snakeHeadX = new ArrayList<>();
                ArrayList<Integer> snakeHeadY = new ArrayList<>();

                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();

                    // Get details of my snake
                    String[] snakeDetails = snakeLine.split(" ");
                    boolean alive = snakeDetails[0].equals("alive");
                    length = Integer.parseInt(snakeDetails[1]);
                    int kills = Integer.parseInt(snakeDetails[2]);

                    if (alive) {

                        StringBuilder snake = new StringBuilder();

                        // Get the head coordinates of my snake
                        if (i == mySnakeNum) {
                            String[] head = snakeDetails[3].split(",");
                            xHead = Integer.parseInt(head[0]);
                            yHead = Integer.parseInt(head[1]);

                            // Last element in the snakeDetails array is the tail
                            String[] tail = snakeDetails[snakeDetails.length - 1].split(",");
                            xTail = Integer.parseInt(tail[0]);
                            yTail = Integer.parseInt(tail[1]);
                        } else {
                            // Get the step count of each snake to the apple
                            String[] head = snakeDetails[3].split(",");
                            int xHeadj = Integer.parseInt(head[0]);
                            int yHeadj = Integer.parseInt(head[1]);

                            snakeHeadX.add(xHeadj);
                            snakeHeadY.add(yHeadj);
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

                    }
                }

                ArrayList<Integer> stepsList = new ArrayList<>();

                // Get the steps of the zombie snakes
//                for (int i = 0; i < ZombiesnakeHeadX.size(); i++) {
//                    int xHeadj = ZombiesnakeHeadX.get(i);
//                    int yHeadj = ZombiesnakeHeadY.get(i);
//
//                    char c = board[xHeadj][yHeadj];
//
//                    board[xHeadj][yHeadj] = 'S';
//
//                    board[appleX][appleY] = 'A';
//                    board[yHead][xHead] = 'G';
//
//                    ArrayList<String> path = BFS.startBFSAnalyze(board);
//
//                    int steps = path.size();
//
//                    //System.out.println("log Steps: " + steps);
//
//                    stepsList.add(steps);
//
//                    board[xHeadj][yHeadj] = c;
//
//                    // If there are more than 3 steps, then loop through the first 2 elements of the path
//                    for (int j = 0; j < 2; j++) {
//                        if (steps >= 3) {
//                            String[] tempPath = path.get(j).split(",");
//                            int x = Integer.parseInt(tempPath[0]);
//                            int y = Integer.parseInt(tempPath[1]);
//
//                            if (board[x][y] != 'G' || board[x][y] != 'S' || board[x][y] != 'A') {
//                                board[y][x] = 'x';
//                            }
//                        }
//                    }
//                }
//
//                stepsList.clear();
//                board[appleX][appleY] = 'G';

                // Get the steps of the other snakes
                for (int i = 0; i < snakeHeadX.size(); i++) {
                    int xHeadj = snakeHeadX.get(i);
                    int yHeadj = snakeHeadY.get(i);

                    char c = board[xHeadj][yHeadj];

                    board[xHeadj][yHeadj] = 'S';

                    ArrayList<String> path = BFS.startBFSAnalyze(board);

                    int steps = path.size();

                    //System.out.println("log Steps: " + steps);

                    stepsList.add(steps);

                    board[xHeadj][yHeadj] = c;

                    // If there are more than 3 steps, then loop through the first 2 elements of the path
                    for (int j = 0; j < 2; j++) {
                        if (steps >= 3) {
                            String[] tempPath = path.get(j).split(",");
                            int x = Integer.parseInt(tempPath[0]);
                            int y = Integer.parseInt(tempPath[1]);

                            if (board[x][y] != 'G' || board[x][y] != 'S' || board[x][y] != 'A') {
                                board[y][x] = 'x';
                            }
                        }
                    }
                }

                board[yHead][xHead] = 'S';

                //DrawBoard.printBoard(board);

                int mySteps = BFS.startBFS(board, boardFuture, false, false);

                // Output true or false if my snake is closer to the apple than the other snakes
                boolean isCloser = mySteps < stepsList.stream().min(Integer::compareTo).orElse(0);
                //System.out.println("log " + "My snake is closer to the apple: " + isCloser);

                // Checking who is closer using manhattan distance

//                ArrayList<Double> distances = new ArrayList<>();
//
//                Point tempApple = new Point(appleY, appleX);
//
//                for (int i = 0; i < snakeHeadX.size(); i++) {
//                    int xHeadj = snakeHeadX.get(i);
//                    int yHeadj = snakeHeadY.get(i);
//
//                    Point snakeHead = new Point(xHeadj, yHeadj);
//
//                    double distance = snakeHead.distanceTo(tempApple);
//                    distances.add(distance);
//                }
//
//                Point myHead = new Point(yHead, xHead);
//                double myDistance = myHead.distanceTo(tempApple);

                // Output true or false if my snake is closer to the apple than the other snakes
                // boolean isCloser = myDistance < distances.stream().min(Double::compareTo).orElse(0.0);
                //System.out.println("log " + "My snake is closer to the apple: " + isCloser);


                int move = 5;

                if (isCloser) {

                    if (Astar.startAStar(board) == -1) {
                        // Remove the 'x's surrounding the zombie snake head
                        for (int i = 0; i < ZombiesnakeHeadX.size(); i++) {
                            int xHeadj = ZombiesnakeHeadX.get(i);
                            int yHeadj = ZombiesnakeHeadY.get(i);

                            int[] dRow = {1, 0, -1, 0};
                            int[] dCol = {0, 1, 0, -1};

                            for (int k = 0; k < 4; k++) {
                                int row = yHeadj + dRow[k];
                                int col = xHeadj + dCol[k];
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

                        //System.out.println("log " + "Removed zombie buffer");

                        move = Astar.startAStar(board);
                        printGrid(board);

                    } else {
                        move = Astar.startAStar(board);
                        //move = BFS.startBFS(board, true);
                        printGrid(board);
                        //printIntGrid(boardFuture);
                    }


                } else {

                    // Find the co-ordinate in a 50x50 char grid which has the least 'x's surrounding it
                    int[] dCol = {1, 0, -1, 0};
                    int[] dRow = {0, 1, 0, -1};

                    int min = Integer.MAX_VALUE;
                    int minRow = 0, minCol = 0;

                    for (int i = 0; i < 50; i++) {
                        for (int j = 0; j < 50; j++) {
                            if (board[i][j] == 'x') {
                                int count = 0;
                                for (int k = 0; k < 4; k++) {
                                    int row = i + dRow[k];
                                    int col = j + dCol[k];
                                    if (row >= 0 && row < 50 && col >= 0 && col < 50) {
                                        if (board[row][col] == 'x') {
                                            count++;
                                        }
                                    }
                                }
                                // Find the coordinate with the least 'x's
                                if (count < min) {
                                    min = count;
                                    minRow = i;
                                    minCol = j;
                                }
                            }
                        }
                    }

                    if (board[minCol][minRow] != 'x') {
                        board[appleX][appleY] = 'A';
                        board[minCol][minRow] = 'G';
                        //System.out.println("log " + minCol + " " + minRow);
                    }


                    if (BFS.startBFS(board, boardFuture, true, false) == -1) {

                        // Remove the 'x's surrounding the zombie snake head
                        for (int i = 0; i < ZombiesnakeHeadX.size(); i++) {
                            int xHeadj = ZombiesnakeHeadX.get(i);
                            int yHeadj = ZombiesnakeHeadY.get(i);

                            for (int k = 0; k < 4; k++) {
                                int row = yHeadj + dRow[k];
                                int col = xHeadj + dCol[k];
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

                        //System.out.println("log " + "Removed zombie buffer");

                        move = BFS.startBFS(board, boardFuture, true, false);
                        printGrid(board);
                        //printIntGrid(boardFuture);

                    } else {
                        move = BFS.startBFS(board, boardFuture, true, false);
                        printGrid(board);
                        //BFS.VornoiDiagram(board, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);
                        //printIntGrid(boardFuture);
                    }


                    // Move to tail
//                    board[appleX][appleY] = '-';
//                    board[yTail][xTail] = 'G';
//                    move = Tail.startBFS(board, false);
                }

                // Now I need to check if I have a path back to my tail once the move is taken

                // Case that the move is 1, 2, 3 or 4

                switch (move) {
                    case 0 -> {
                        // Up (relative to the play area - north)
                        board[yHead][xHead] = 'x';
                        //System.out.println("log " + yHead + " " + (xHead + 1));

                        board[yHead - 1][xHead] = 'S';
                        board[appleX][appleY] = '-';
                        board[yTail][xTail] = 'G';
                        if (Tail.startBFS(board, false) == -1) {
                            //System.out.println("log dodge up");
                            //System.out.println("log " + yHead + " " + (xHead + 1));
                            board[yHead][xHead] = 'S';
                            board[yHead - 1][xHead] = '-';
                            move = Tail.startBFS(board, true);
                        } else {
                            //printGrid(board);
                        }
                    }
                    case 1 -> {
                        // Down (relative to the play area - south)
                        board[yHead][xHead] = 'x';
                        //System.out.println("log " + yHead + " " + (xHead - 1));

                        board[yHead + 1][xHead] = 'S';
                        board[appleX][appleY] = '-';
                        board[yTail][xTail] = 'G';
                        if (Tail.startBFS(board, false) == -1) {
                            //System.out.println("log dodge down");
                            //System.out.println("log " + yHead + " " + (xHead - 1));
                            board[yHead][xHead] = 'S';
                            board[yHead + 1][xHead] = '-';
                            move = Tail.startBFS(board, true);
                        } else {
                            //printGrid(board);
                        }
                    }
                    case 2 -> {
                        // Left (relative to the play area - west)
                        board[yHead][xHead] = 'x';
                        //System.out.println("log " + (yHead - 1) + " " + xHead);

                        board[yHead][xHead - 1] = 'S';
                        board[appleX][appleY] = '-';
                        board[yTail][xTail] = 'G';
                        if (Tail.startBFS(board, false) == -1) {
                            //System.out.println("log dodge left");
                            //System.out.println("log " + (yHead - 1) + " " + xHead);
                            board[yHead][xHead] = 'S';
                            board[yHead][xHead - 1] = '-';
                            move = Tail.startBFS(board, true);
                        } else {
                            //printGrid(board);
                        }
                    }
                    case 3 -> {
                        // Right (relative to the play area - east)
                        board[yHead][xHead] = 'x';
                        //System.out.println("log " + (yHead + 1) + " " + xHead);

                        board[yHead][xHead + 1] = 'S';
                        board[appleX][appleY] = '-';
                        board[yTail][xTail] = 'G';
                        if (Tail.startBFS(board, false) == -1) {
                            //System.out.println("log dodge right");
                            //System.out.println("log " + (yHead + 1) + " " + xHead);
                            board[yHead][xHead] = 'S';
                            board[yHead][xHead + 1] = '-';
                            move = Tail.startBFS(board, true);
                        } else {
                            //printGrid(board);
                        }
                    }
                }

                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}