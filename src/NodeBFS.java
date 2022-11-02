public class NodeBFS extends Point {
    public NodeBFS parent;

    public NodeBFS(int row, int col) {
        super(row, col);
        parent = null;
    }

    public NodeBFS(Point point) {
        this(point.row, point.col);
    }

    public NodeBFS[] getNeighbours() {
        int[][] adj = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        NodeBFS[] neighbours = new NodeBFS[adj.length];

        for (int i = 0; i < adj.length; i++) {
            neighbours[i] = new NodeBFS(this.row + adj[i][0], this.col + adj[i][1]);
        }
        return neighbours;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof NodeBFS node))
            return false;

        return this.row == node.row && this.col == node.col;
    }
}