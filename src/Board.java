/**
 * Computer player has mark X (MIN)
 * Human player has mark O (MAX)
 */
public class Board {
    private Mark[][] board;
    private final int size;
    private final int boardWidth;
    private int squaresUsed;

    public Board() {
        this(10);
    }

    public Board(int x) {
        if (x > 15 || x < 3)
            throw new IllegalArgumentException("Board can only be up to 15 x 15 for now.");

        this.boardWidth = x;
        size = boardWidth * boardWidth;
        board = new Mark[boardWidth][boardWidth];
        squaresUsed = 0;
        initBoard();
    }

    private void initBoard() {
        for (int row = 0; row < boardWidth; row++) {
            for (int col = 0; col < boardWidth; col++) {
                board[row][col] = Mark.BLANK;
            }
        }
    }

    public Mark checkIfSomeoneWon(){
        StringBuilder rowSum = new StringBuilder();
        int width = boardWidth;
        int inARow = (width < 5) ? width : 5;
        String xWin = String.valueOf(Mark.X.getMark()).repeat(inARow).trim();
        String oWin = String.valueOf(Mark.O.getMark()).repeat(inARow).trim();
        Mark result = Mark.BLANK;

        // ROW
        result = checkRow(width, rowSum, xWin, oWin);
        if(result != Mark.BLANK) return result;

        // COLUMN
        result =  checkColumn(width, xWin, oWin);
        if(result != Mark.BLANK) return result;

        // DIAGONALS
        result = checkAllDiagonals(this, xWin, oWin);
        return result;
    }

    // H E L P E R   M E T H O D - checkIfSomeoneWon()
    private Mark checkColumn(int width, String xWin, String oWin) {
        StringBuilder rowSum;
        rowSum = new StringBuilder();
        for(int col = 0; col < width; col++){
            for(int row = 0; row < width; row++){
                rowSum.append(board[row][col].getMark());
            }
            Mark x = checkWinner(rowSum.toString(), xWin, oWin);
            if (x != Mark.BLANK) return x;

            rowSum = new StringBuilder();
        }
        return Mark.BLANK;
    }

    private Mark checkRow(int width, StringBuilder rowSum, String xWin, String oWin) {
        for(int row = 0; row < width; row++){
            for(int col = 0; col < width; col++){
                rowSum.append(board[row][col].getMark());
            }
            Mark x = checkWinner(rowSum.toString(), xWin, oWin);
            if (x != Mark.BLANK) return x;
            rowSum = new StringBuilder();
        }
        return Mark.BLANK;
    }

    private Mark checkWinner(String rowSum, String xWin, String oWin) {
        if(rowSum.contains(xWin)){
            System.out.println(board);
            return Mark.X;
        }
        else if(rowSum.contains(oWin)){
            System.out.println(board);
            return Mark.O;
        }

        return Mark.BLANK;
    }

    private Mark checkAllDiagonals(Board board, String xWin, String oWin) {
        int width = getBoardWidth();
        int winLength = xWin.length(); // assuming xWin and oWin have the same length

        // Check diagonals from top-left to bottom-right
        // Starting from top row
        for (int col = 0; col <= width - winLength; col++) {
            StringBuilder diag = new StringBuilder();
            for (int row = 0, currentCol = col; row < width && currentCol < width; row++, currentCol++) {
                diag.append(board.getMarkOnSquare(row, currentCol).getMark());
            }
            Mark result = checkWinner(diag.toString(), xWin, oWin);
            if (result != Mark.BLANK) return result;
        }
        // Starting from left column (skip the top-left corner since it was already checked)
        for (int row = 1; row <= width - winLength; row++) {
            StringBuilder diag = new StringBuilder();
            for (int currentRow = row, col = 0; currentRow < width && col < width; currentRow++, col++) {
                diag.append(board.getMarkOnSquare(currentRow, col).getMark());
            }
            Mark result = checkWinner(diag.toString(), xWin, oWin);
            if (result != Mark.BLANK) return result;
        }

        // Check diagonals from top-right to bottom-left
        // Starting from top row
        for (int col = winLength - 1; col < width; col++) {
            StringBuilder diag = new StringBuilder();
            for (int row = 0, currentCol = col; row < width && currentCol >= 0; row++, currentCol--) {
                diag.append(board.getMarkOnSquare(row, currentCol).getMark());
            }
            Mark result = checkWinner(diag.toString(), xWin, oWin);
            if (result != Mark.BLANK) return result;
        }
        // Starting from right column (skip the top-right corner since it was already checked)
        for (int row = 1; row <= width - winLength; row++) {
            StringBuilder diag = new StringBuilder();
            for (int currentRow = row, col = width - 1; currentRow < width && col >= 0; currentRow++, col--) {
                diag.append(board.getMarkOnSquare(currentRow, col).getMark());
            }
            Mark result = checkWinner(diag.toString(), xWin, oWin);
            if (result != Mark.BLANK) return result;
        }

        return Mark.BLANK;
    }


    public Mark getMarkOnSquare(int row, int col) {
        return board[row][col];
    }

    public boolean isSquareMarked(int row, int col) {
        if (row < 0 || row >= boardWidth || col < 0 || col >= boardWidth) {
            throw new IndexOutOfBoundsException("Invalid board index: " + " row: " + row + ", col: " + col);
        }
        return board[row][col].isMarked();
    }

    public void setMarkAt(int row, int col, Mark mark) {
        board[row][col] = mark;
    }

    public boolean isBoardFull() {
        return size - 1 == squaresUsed;
    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int size(){
        return size;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = board.length - 1; i >= 0; i--) {
            Mark[] row = board[i];
            builder.append("[");
            for (int j = 0; j <= row.length - 1; j++) {
                if (j == row.length - 1) {
                    if (row[j].equals(Mark.X))
                        builder.append(" " + "X" +" ");
                    else if (row[j].equals(Mark.BLANK))
                        builder.append("   ");
                    else
                        builder.append(" " + "O" + " ");
                } else {
                    if (row[j].equals(Mark.X))
                        builder.append(" " +"X" + ",");
                    else if (row[j].equals(Mark.BLANK))
                        builder.append("  ,");
                    else
                        builder.append(" " + "O" + ",");
                }
            }
            builder.append("]\n");
        }
        return builder.toString();
    }
}