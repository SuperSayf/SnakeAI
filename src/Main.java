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

            while (true) {

                for (int i = 0; i < 50; i++) {
                    for (int j = 0; j < 50; j++) {
                        board[i][j] = '-';
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

                //do stuff with apples

                for (int zombie = 0; zombie < 6; zombie++) {
                    String zombieLine = br.readLine();

                    // Get the head of the zombie snake
                    String[] zombieHead = zombieLine.split(" ");
                    int x = Integer.parseInt(zombieHead[0].split(",")[0]);
                    int y = Integer.parseInt(zombieHead[1].split(",")[1]);

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

                    DrawBoard.drawSnake(zombieLine, '*', board);
                }

                int mySnakeNum = Integer.parseInt(br.readLine());
                int xHead = 0, yHead = 0, xTail = 0, yTail = 0;
                ArrayList<Integer> snakeHeadX = new ArrayList<>();
                ArrayList<Integer> snakeHeadY = new ArrayList<>();

                for (int i = 0; i < nSnakes; i++) {
                    String snakeLine = br.readLine();

                    // Get details of my snake
                    String[] snakeDetails = snakeLine.split(" ");
                    boolean alive = snakeDetails[0].equals("alive");
                    int length = Integer.parseInt(snakeDetails[1]);
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

                        DrawBoard.drawSnake(snake.toString(), i + 1, board);

                    }
                }

                ArrayList<Integer> stepsList = new ArrayList<>();

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

                    // If there are more than 3 steps, then loop through the first 3 elements of the path
                    for (int j = 0; j < 1; j++) {
                        if (path.size() > 1) {
                            String[] tempPath = path.get(j).split(",");
                            int x = Integer.parseInt(tempPath[0]);
                            int y = Integer.parseInt(tempPath[1]);

                            board[y][x] = 'x';
                        }
                    }
                }

                board[yHead][xHead] = 'S';

                //DrawBoard.printBoard(board);

                int mySteps = BFS.startBFS(board, false);

                // Output true or false if my snake is closer to the apple than the other snakes
                boolean isCloser = mySteps < stepsList.stream().min(Integer::compareTo).orElse(0);
                System.out.println("log " + "My snake is closer to the apple: " + isCloser);

                int move = 5;

                if (isCloser) {
                    move = Astar.startAStar(board);
                    //move = BFS.startBFS(board, true);
                } else {
                    move = BFS.startBFS(board, true);

                    // Move to tail
//                    board[appleX][appleY] = '-';
//                    board[yTail][xTail] = 'G';
//                    move = Tail.startBFS(board);
                }

                System.out.println(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}