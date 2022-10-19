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

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            String initString = br.readLine();
            String[] temp = initString.split(" ");
            int nSnakes = Integer.parseInt(temp[0]);
            
            char[][] board = new char[50][50];
            int[][] boardFuture = new int[50][50];
            int[][] boardVornoi = new int[50][50];

            while (true) {

                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        board[i][j] = '-';
                        boardFuture[i][j] = -1;
                        boardVornoi[i][j] = -1;
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
                    DrawBoard.drawSnakeVornoi(zombieLine, boardVornoi, zombie + 1);
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

                            snakeHeadX.add(xHead);
                            snakeHeadY.add(yHead);

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
                        DrawBoard.drawSnakeVornoi(snake.toString(), boardVornoi, i + 1 + 6);

                    }
                }

                //ArrayList<Integer> stepsList = new ArrayList<>();

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
//                for (int i = 0; i < snakeHeadX.size(); i++) {
//                    int xHeadj = snakeHeadX.get(i);
//                    int yHeadj = snakeHeadY.get(i);
//
//                    char c = board[xHeadj][yHeadj];
//
//                    board[xHeadj][yHeadj] = 'S';
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


                //DrawBoard.printBoard(board);

                //int mySteps = BFS.startBFS(board, boardFuture, false, false);

                // Output true or false if my snake is closer to the apple than the other snakes
                //boolean isCloser = mySteps < stepsList.stream().min(Integer::compareTo).orElse(0);
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

                // Get the steps of the zombie snakes
//                ArrayList<Integer> stepsList = new ArrayList<>();
//
//                for (int i = 0; i < ZombiesnakeHeadX.size(); i++) {
//                    int xHeadj = ZombiesnakeHeadX.get(i);
//                    int yHeadj = ZombiesnakeHeadY.get(i);
//
//                    char c = board[yHeadj][xHeadj];
//
//                    board[yHeadj][xHeadj] = 'S';
//
//                    board[appleX][appleY] = 'A';
//
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
//                    board[yHeadj][xHeadj] = c;
//
//                    // To-do
//                    // This takes them straight to my head, I need it one in front of my head
//
//                    // If there are more than 3 steps, then loop through the first 2 elements of the path
//                    for (int j = 0; j < 2; j++) {
//                        if (steps >= 2) {
//                            String[] tempPath = path.get(j).split(",");
//                            int x = Integer.parseInt(tempPath[0]);
//                            int y = Integer.parseInt(tempPath[1]);
//
//                            // Replace the x-y zombie head coordinates with the x-y coordinates of the path
//                            ZombiesnakeHeadX.set(i, y);
//                            ZombiesnakeHeadY.set(i, x);
//
//                            if (board[x][y] != 'G' || board[x][y] != 'S' || board[x][y] != 'A') {
//                                board[x][y] = 'x';
//                            }
//                        }
//                    }
//                }
//
//                stepsList.clear();
//                board[yHead][xHead] = 'x';
//                board[appleX][appleY] = 'G';
//
//                //Get the steps of the other snakes
//                for (int i = 1; i < snakeHeadX.size(); i++) {
//                    int xHeadj = snakeHeadX.get(i);
//                    int yHeadj = snakeHeadY.get(i);
//
//                    char c = board[yHeadj][xHeadj];
//
//                    board[yHeadj][xHeadj] = 'S';
//
//                    ArrayList<String> path = BFS.startBFSAnalyze(board);
//
//                    int steps = path.size();
//
//                    //System.out.println("log Steps: " + steps);
//
//                    stepsList.add(steps);
//
//                    board[yHeadj][xHeadj] = c;
//
//                    // If there are more than 3 steps, then loop through the first 2 elements of the path
//                    for (int j = 0; j < 2; j++) {
//                        if (steps >= 3) {
//                            String[] tempPath = path.get(j).split(",");
//                            int x = Integer.parseInt(tempPath[0]);
//                            int y = Integer.parseInt(tempPath[1]);
//
//                            if (board[x][y] != 'G' || board[x][y] != 'S' || board[x][y] != 'A') {
//                                board[x][y] = 'x';
//                            }
//                        }
//                    }
//                }

                board[yHead][xHead] = 'S';

                //printGrid(board);

                // Make a copy og boardVolnori
                int[][] boardVornoiCopy = new int[50][50];
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVornoi[i], 0, boardVornoiCopy[i], 0, 50);
                }

                //printIntGrid(boardVornoi);
                //printIntGrid(boardVornoiCopy);
                BFS.VornoiDiagram(boardVornoi, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);
                //printIntGrid(boardVornoi);

                boolean isCloser = boardVornoi[appleX][appleY] == 7;
                //boolean isCloser = true;

                // Set the boardVornoi back to the original
                for (int i = 0; i < 50; i++) {
                    System.arraycopy(boardVornoiCopy[i], 0, boardVornoi[i], 0, 50);
                }

                //System.out.println("log " + "My snake is closer to the apple: " + isCloser);

                int move = 0;

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
                        //printGrid(board);
                        //printIntGrid(boardFuture);

                    } else {
                        move = Astar.startAStar(board);
                        //move = BFS.startBFS(board, true);
                        //printGrid(board);
                        //printIntGrid(boardFuture);
                    }


                } else {

                    int bestMove = 0;
                    int highestCellCount = 0;
                    int[][] bestVornoiBoard = new int[50][50];


                    for (int i = 0; i < 4; i++) {
                        move = i;

                        switch (move) {
                            case 0 -> {
                                // Up (relative to the play area - north)
                                // Check if the point is not out of bounds
                                if (yHead - 1 < 50 && yHead - 1 >= 0 && xHead < 50 && xHead >= 0) {
                                    if (board[yHead - 1][xHead] != 'x') {
                                        // Change my head coordinates in the snake head array
                                        snakeHeadX.set(0, xHead);
                                        snakeHeadY.set(0, yHead - 1);
                                        BFS.VornoiDiagram(boardVornoi, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);

                                        if (boardVornoi[yHead - 1][xHead] == 7) {

                                            // Scan through the boardVornoi and count the number of cells that are 7
                                            int cellCount = 0;
                                            for (int[] ints : boardVornoi) {
                                                for (int anInt : ints) {
                                                    if (anInt == 7) {
                                                        cellCount++;
                                                    }
                                                }
                                            }
                                            if (cellCount > highestCellCount) {
                                                highestCellCount = cellCount;
                                                bestMove = move;

                                                // Make a copy of the boardVornoi to the bestVornoiBoard
                                                for (int j = 0; j < 50; j++) {
                                                    System.arraycopy(boardVornoi[j], 0, bestVornoiBoard[j], 0, 50);
                                                }

                                            }
                                            // Reset the boardVornoi back to the original
                                            for (int j = 0; j < 50; j++) {
                                                System.arraycopy(boardVornoiCopy[j], 0, boardVornoi[j], 0, 50);
                                            }
                                        }

                                    }
                                }
                            }
                            case 1 -> {
                                // Down (relative to the play area - south)
                                if (yHead + 1 < 50 && yHead + 1 >= 0 && xHead < 50 && xHead >= 0) {
                                    if (board[yHead + 1][xHead] != 'x') {
                                        snakeHeadX.set(0, xHead);
                                        snakeHeadY.set(0, yHead + 1);
                                        BFS.VornoiDiagram(boardVornoi, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);
                                        if (boardVornoi[yHead + 1][xHead] == 7) {
                                            // Scan through the boardVornoi and count the number of cells that are 7
                                            int cellCount = 0;
                                            for (int[] ints : boardVornoi) {
                                                for (int anInt : ints) {
                                                    if (anInt == 7) {
                                                        cellCount++;
                                                    }
                                                }
                                            }
                                            if (cellCount > highestCellCount) {
                                                highestCellCount = cellCount;
                                                bestMove = move;

                                                // Make a copy of the boardVornoi to the bestVornoiBoard
                                                for (int j = 0; j < 50; j++) {
                                                    System.arraycopy(boardVornoi[j], 0, bestVornoiBoard[j], 0, 50);
                                                }

                                            }
                                            // Reset the boardVornoi back to the original
                                            for (int j = 0; j < 50; j++) {
                                                System.arraycopy(boardVornoiCopy[j], 0, boardVornoi[j], 0, 50);
                                            }
                                        }
                                    }
                                }
                            }
                            case 2 -> {
                                // Left (relative to the play area - west)
                                if (yHead < 50 && yHead >= 0 && xHead - 1 < 50 && xHead - 1 >= 0) {
                                    if (board[yHead][xHead - 1] != 'x') {
                                        snakeHeadX.set(0, xHead - 1);
                                        snakeHeadY.set(0, yHead);
                                        BFS.VornoiDiagram(boardVornoi, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);
                                        if (boardVornoi[yHead][xHead - 1] == 7) {
                                            // Scan through the boardVornoi and count the number of cells that are 7
                                            int cellCount = 0;
                                            for (int[] ints : boardVornoi) {
                                                for (int anInt : ints) {
                                                    if (anInt == 7) {
                                                        cellCount++;
                                                    }
                                                }
                                            }
                                            if (cellCount > highestCellCount) {
                                                highestCellCount = cellCount;
                                                bestMove = move;

                                                // Make a copy of the boardVornoi to the bestVornoiBoard
                                                for (int j = 0; j < 50; j++) {
                                                    System.arraycopy(boardVornoi[j], 0, bestVornoiBoard[j], 0, 50);
                                                }

                                            }
                                            // Reset the boardVornoi back to the original
                                            for (int j = 0; j < 50; j++) {
                                                System.arraycopy(boardVornoiCopy[j], 0, boardVornoi[j], 0, 50);
                                            }
                                        }
                                    }
                                }
                            }
                            case 3 -> {
                                // Right (relative to the play area - east)
                                if (yHead < 50 && yHead >= 0 && xHead + 1 < 50 && xHead + 1 >= 0) {
                                    if (board[yHead][xHead + 1] != 'x') {
                                        snakeHeadX.set(0, xHead + 1);
                                        snakeHeadY.set(0, yHead);
                                        BFS.VornoiDiagram(boardVornoi, ZombiesnakeHeadX, ZombiesnakeHeadY, snakeHeadX, snakeHeadY);
                                        if (boardVornoi[yHead][xHead + 1] == 7) {
                                            // Scan through the boardVornoi and count the number of cells that are 7
                                            int cellCount = 0;
                                            for (int[] ints : boardVornoi) {
                                                for (int anInt : ints) {
                                                    if (anInt == 7) {
                                                        cellCount++;
                                                    }
                                                }
                                            }
                                            if (cellCount > highestCellCount) {
                                                highestCellCount = cellCount;
                                                bestMove = move;

                                                // Make a copy of the boardVornoi to the bestVornoiBoard
                                                for (int j = 0; j < 50; j++) {
                                                    System.arraycopy(boardVornoi[j], 0, bestVornoiBoard[j], 0, 50);
                                                }

                                            }
                                            // Reset the boardVornoi back to the original
                                            for (int j = 0; j < 50; j++) {
                                                System.arraycopy(boardVornoiCopy[j], 0, boardVornoi[j], 0, 50);
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                    //System.out.println("log Territory owned: " + highestCellCount);
                    move = bestMove;
                    //move = BFS.startBFS(board, boardFuture, true, false);

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