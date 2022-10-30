public class Snake {
    public Point head, tail;
    public int length, kills, index;
    boolean isAlive, isZombie;

    public Snake(Point head, Point tail, int length, int kills, int index, boolean isAlive, boolean isZombie) {
        this.head = head;
        this.tail = tail;
        this.length = length;
        this.kills = kills;
        this.index = index;
        this.isAlive = isAlive;
        this.isZombie = isZombie;
    }

    public void setHead(Point head) {
        this.head = head;
    }

    public void setTail(Point tail) {
        this.tail = tail;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public void setIsZombie(boolean isZombie) {
        this.isZombie = isZombie;
    }
}
