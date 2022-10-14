import java.util.Objects;

public class Node extends Point implements Comparable<Node> {
    double fCost;
    Node parent;

    public Node(int row, int col, Point end) {
        super(row, col);


        //Edit this heuristic to change the algorithm
        fCost = end.distanceTo(this);

        parent = null;
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(this.fCost, other.fCost);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof Node node))
            return false;

        return this.row == node.row && this.col == node.col;
    }

    public String toString() {
        return "{" + this.row + ", " + this.col + "}";
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
