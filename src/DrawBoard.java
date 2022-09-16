public class DrawBoard {

    public static void printBoard(char[][] board) {
        StringBuilder map = new StringBuilder();
        for (char[] chars : board) {
            for (char aChar : chars) {
                map.append(aChar).append(" ");
            }
            map.append("\n");
        }
        Logger.log(map.toString());
    }

    public static void drawSnake(String snake, int snakeNumber, char[][] board) {
        String[] snakePoints = snake.split(" ");
        for (int i = 0; i < snakePoints.length - 1; i++) {
            String[] snakePoint1 = snakePoints[i].split(",");
            String[] snakePoint2 = snakePoints[i + 1].split(",");
            drawLine(board, snakePoint1[0], snakePoint1[1], snakePoint2[0], snakePoint2[1], snakeNumber);
        }
    }

    public static void drawLine(char[][] board, String x1, String y1, String x2, String y2, int snakeNumber) {
        int minX = Math.min(Integer.parseInt(x1), Integer.parseInt(x2));
        int maxX = Math.max(Integer.parseInt(x1), Integer.parseInt(x2));
        int minY = Math.min(Integer.parseInt(y1), Integer.parseInt(y2));
        int maxY = Math.max(Integer.parseInt(y1), Integer.parseInt(y2));
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                board[j][i] = 'x';
            }
        }
    }

}
