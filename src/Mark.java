public enum Mark {
    X(1),
    O(2),
    BLANK(0);

    private final int mark;

    Mark(int initMark) {
        this.mark = initMark;
    }

    public boolean isMarked() {
        return this != BLANK;
    }

    public int getMark() {
        return this.mark;
    }

    @Override
    public String toString() {
        return String.valueOf(mark);
    }
}