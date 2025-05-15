import java.util.*;

/**
 * Komplettering:
 * Ändra checkboard så att du kollar alla potentiella drag runt omrking alla dess drag som den redan gjort i checkBoard().
 * Mejla till Beatrice
 */

public class MiniMaxAlphaBeta {
    private static final int MAX_DEPTH = 3;
    private static List<int[]> moveOrder = null;
    private static final Set<Integer> movesAroundAlreadyPlayed = new HashSet<>();

    MiniMaxAlphaBeta(){
    }

    public static int[] getBestMove(Board board, int rowHuman, int colHuman){
        int[] bestMove = new int[]{-1,-1};
        int bestValue = Integer.MIN_VALUE;
        int boardWidth = board.getBoardWidth();
        int centerOffset = boardWidth / 2;

        // Add empty squares around human players move.
        if(rowHuman != -1 && colHuman != -1){
            int[] humanMove = {rowHuman, colHuman};
            movesAroundAlreadyPlayed.addAll(getBlankSquaresAround(board, humanMove));
            movesAroundAlreadyPlayed.remove(calculateIndexOnBoard(boardWidth, rowHuman, colHuman));
        }

        if(moveOrder == null)
            moveOrder = getCenterFocusedPos(centerOffset, boardWidth);


        for (int[] move : moveOrder) {
            int row =move[0];
            int col = move[1];
            if(!board.isSquareMarked(row, col)){
                board.setMarkAt(row, col, Mark.X);
                int moveValue = miniMax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
                board.setMarkAt(row, col, Mark.BLANK);
                if(moveValue > bestValue){
                    bestMove[0] = row;
                    bestMove[1] = col;
                    bestValue = moveValue;
                }
            }
        }



        if(bestMove[0] == -1 || bestMove [1] == -1)
            throw new RuntimeException("BestMove was not found");

        // Save comp move
        movesAroundAlreadyPlayed.addAll(getBlankSquaresAround(board, bestMove));
        movesAroundAlreadyPlayed.remove(calculateIndexOnBoard(boardWidth, bestMove[0], bestMove[1]));
        moveOrder.remove(bestMove);


        return bestMove;
    }

    /**
     * Evaluates the board using minimax-algorithm together with alpha-beta pruning.
     * Return the value of the best move for the maximizing player on the board.
     *
     * @param board The board the move is played on.
     * @param depth The current depth of the recursion. Initial call should use {@code MAX_DEPTH} and decrements its value for each recursive call.
     * @param alpha Current best achievable value for the maximizing player (for pruning).
     * @param beta Current best achievable value for the minimizing player (for pruning).
     * @param isMax {@code true} if it's the maximizing players turn. {@code false} if it's the minimizing players turn.
     * @return the value of the best move for the computer player.
     */
    private static int miniMax(Board board, int depth,int alpha, int beta, boolean isMax){
        int boardVal = checkBoard(board, depth);
        if( Math.abs(boardVal) == 10+MAX_DEPTH || depth == 0 || board.isBoardFull()){
            return boardVal;
        }

        if(isMax){
            int highestVal = Integer.MIN_VALUE;
            int boardWidth = board.getBoardWidth();
            for(int row = 0; row < boardWidth; row++){
                for(int col = 0; col < boardWidth; col++){
                    if(!board.isSquareMarked(row, col)){
                        board.setMarkAt(row, col, Mark.X);
                        highestVal = Math.max(highestVal, miniMax(board, depth-1, alpha,beta, false));
                        board.setMarkAt(row, col, Mark.BLANK);

                        //Pruning
                        alpha = Math.max(alpha, highestVal);
                        if(alpha >= beta)
                            return highestVal;

                    }
                }
            }
            return highestVal;

        }
        else {
            int lowestVal = Integer.MAX_VALUE;
            int boardWidth = board.getBoardWidth();
            for(int row = 0; row < boardWidth; row++){
                for(int col = 0; col < boardWidth; col++){
                    if(!board.isSquareMarked(row, col)){
                        board.setMarkAt(row, col, Mark.O);
                        lowestVal = Math.min(lowestVal, miniMax(board, depth-1,alpha, beta, true));
                        board.setMarkAt(row, col, Mark.BLANK);

                        // Pruning
                        beta = Math.min(beta, lowestVal);
                        if(beta <= alpha)
                            return lowestVal;

                    }
                }
            }
            return lowestVal;
        }
    }

    private static int checkBoard(Board board, int depth){
        int width = board.getBoardWidth();
        int inARow = width <5 ? width : 5;
        String xWin = String.valueOf(Mark.X.getMark()).repeat(inARow).trim();
        String oWin = String.valueOf(Mark.O.getMark()).repeat(inARow).trim();

        // Check empty squares around previous moves.
        return checkMovesAround(board, xWin, oWin, depth);
    }

    private static int checkMovesAround(Board board, String xWin, String oWin, int depth) {
        // Call the check-methods to check if move can result in win / prevent win.
        int result = 0;
        Set<Integer> checkedRows = new HashSet<>();
        Set<Integer> checkedCols = new HashSet<>();
        Set<Integer> checkedLeftBottomToRightTOp = new HashSet<>();
        Set<Integer> checkedRightBottomToLeftTop = new HashSet<>();

        StringBuilder totalRow = new StringBuilder();
        for (int move : movesAroundAlreadyPlayed) {
            int col = calculateColumn(board, move);
            int row = calculateRow(board,move,  col);

            int width = board.getBoardWidth();
            if(!checkedRows.contains(row)){
                checkedRows.add(row);
                result = checkRow(board, width, totalRow, row, xWin, oWin, depth);
                if(result != 0){
                    return result;
                }
            }
            totalRow = new StringBuilder();

            if(!checkedCols.contains(col)){
                checkedCols.add(col);
                result = checkColumn(board, width, totalRow, col, xWin, oWin,depth);
                if(result != 0){
                    return result;
                }
            }

            // Check if diagonal is already checked.
            int startRow = row - Math.min(row, width - 1 - col);
            int startCol = col + Math.min(row, width - 1 - col);
            int indexOnBoard = (startRow * width) + startCol;
            if(!checkedLeftBottomToRightTOp.contains(indexOnBoard)){
                checkedLeftBottomToRightTOp.add(indexOnBoard);
                result = checkTopRightToBottomLeft(board, row, col, xWin, oWin, width,depth);
                if(result != 0) {
                    return result;
                }
            }
            totalRow = new StringBuilder();

            int startRow2 = row - Math.min(row, col);
            int startCol2 = col - Math.min(row, col);
            int indexOnBoard2 = (startRow2 * width) + startCol2;
            if(!checkedRightBottomToLeftTop.contains(indexOnBoard2)){
                checkedRightBottomToLeftTop.add(indexOnBoard2);
                result = checkTopLeftToBottomRight(board, row, col, xWin, oWin, width,depth);
                if(result != 0) {
                    return result;
                }
            }

        }
        return result;
    }


    public static List<Integer> getBlankSquaresAround(Board board, int[] move) {
        List<Integer> movesAround = new ArrayList<>();
        int row = move[0];
        int col = move[1];

        if(row < board.getBoardWidth() - 1 && !board.isSquareMarked(row+1, col)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(),row+1, col));
            //movesAround.add(new int[]{row+1, col});
        }

        if(row > 0 && !board.isSquareMarked(row-1, col)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row-1, col));
            //movesAround.add(new int[]{row-1, col});
        }

        if(row < board.getBoardWidth() - 1 && col < board.getBoardWidth() - 1 && !board.isSquareMarked(row+1, col+1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row+1, col+1));
            //movesAround.add(new int[]{row+1, col+1});
        }

        if(row < board.getBoardWidth() - 1 && col > 0 && !board.isSquareMarked(row+1, col-1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row+1, col-1));
            //movesAround.add(new int[]{row+1, col-1});
        }

        if(col > 0 && !board.isSquareMarked(row, col-1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row, col-1));
            //movesAround.add(new int[]{row, col-1 });
        }

        if(col < board.getBoardWidth() - 1 && !board.isSquareMarked(row, col+1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row, col+1));
            //movesAround.add(new int[]{row, col+1});
        }

        if(row > 0 && col > 0 && !board.isSquareMarked(row-1, col-1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row-1, col-1));
            //movesAround.add(new int[]{row-1, col-1});
        }
        if(row > 0 && col < board.getBoardWidth() - 1 && !board.isSquareMarked(row-1, col+1)){
            movesAround.add(calculateIndexOnBoard(board.getBoardWidth(), row-1, col+1));
            //movesAround.add(new int[]{row-1, col+1});
        }

        return movesAround;
    }


    // H E L P E R   M E T H O D S - checkBoard(Board board)
    private static int checkRow(Board board, int width, StringBuilder totalRow, int row, String xWin, String oWin, int depth) {
        for(int col = 0; col < width; col++){
            totalRow.append(board.getMarkOnSquare(row, col).getMark());
        }
        return checkWinner(totalRow.toString(), xWin, oWin, depth);
    }

    private static int checkColumn(Board board, int width, StringBuilder totalRow, int col, String xWin, String oWin,int depth) {
        for(int row = 0; row < width; row++){
            totalRow.append(board.getMarkOnSquare(row, col).getMark());
        }
        return checkWinner(totalRow.toString(), xWin, oWin,depth);
    }

    private static int checkTopRightToBottomLeft(Board board, int lastRow, int lastCol, String xWin, String oWin, int width,int depth) {
        int startRow;
        int result;
        int startCol;
        StringBuilder diag2 = new StringBuilder();
        startRow = lastRow - Math.min(lastRow, width - 1 - lastCol);
        startCol = lastCol + Math.min(lastRow, width - 1 - lastCol);

        for(int i = 0; startRow + i < width && startCol - i >= 0; i++) {
            diag2.append(board.getMarkOnSquare(startRow + i, startCol - i).getMark());
        }
        result = checkWinner(diag2.toString(), xWin, oWin,depth);
        return result;

    }

    private static int checkTopLeftToBottomRight(Board board, int lastRow, int lastCol, String xWin, String oWin, int width,int depth) {
        StringBuilder diag1 = new StringBuilder();
        int startRow = lastRow - Math.min(lastRow, lastCol);
        int startCol = lastCol - Math.min(lastRow, lastCol);

        for(int i = 0; startRow + i < width && startCol + i < width; i++) {
            diag1.append(board.getMarkOnSquare(startRow + i, startCol + i).getMark());
        }
        return checkWinner(diag1.toString(), xWin, oWin,depth);
    }

    private static int checkWinner(String rowSum, String xWin, String oWin,int depth) {
        if(rowSum.contains(xWin))
            return 10 + depth;

        else if(rowSum.contains(oWin))
            return -10 - depth;

        return 0;
    }

    // H E L P E R   M E T H O D  - getBestMove(Board board, int rowHuman, int colHuman)
    private static List<int[]> getCenterFocusedPos(int centerOffset, int boardWidth) {
        List<int[]> moveOrder = new ArrayList<>();

        // Start to add center moves first
        for (int rowOffset = 0; rowOffset <= centerOffset; rowOffset++) {
            for (int colOffset = 0; colOffset <= centerOffset; colOffset++) {
                // Skip if we've already added this exact position
                if (rowOffset == 0 && colOffset == 0) {
                    int row = centerOffset;
                    int col = centerOffset;
                    moveOrder.add(new int[]{row, col});
                    continue;
                }

                // Add positions from center in all four quadrants
                int[] rows = {centerOffset - rowOffset, centerOffset + rowOffset};
                int[] cols = {centerOffset - colOffset, centerOffset + colOffset};

                for (int r : rows) {
                    for (int c : cols) {
                        if (r >= 0 && r < boardWidth && c >= 0 && c < boardWidth) {
                            moveOrder.add(new int[]{r, c});
                        }
                    }
                }
            }
        }
        return moveOrder;
    }

    private static int calculateIndexOnBoard(int boardWidth, int row, int col) {
        return (row * boardWidth) + col;
    }

    private static int calculateRow(Board board, int i, int column) {
        return (i - column) / board.getBoardWidth();
    }

    private static int calculateColumn(Board board, int i) {
        if(i == 0)
            return 0;
        return i % board.getBoardWidth();
    }
}