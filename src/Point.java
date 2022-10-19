public class Point {
    public int row, col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public Point(String point, String split) {
        String[] coords = point.split(split);
        this.row = Integer.parseInt(coords[1]);
        this.col = Integer.parseInt(coords[0]);
    }

    public Point() {
        this.row = 0;
        this.col = 0;
    }

    public void set(int row, int col) {
        this.row = row;
        this.col = col;
    }

    double distanceTo(Point other) {
        // Calculate the manhattan distance
        return Math.abs(this.row - other.row) + Math.abs(this.col - other.col);
    }
}
