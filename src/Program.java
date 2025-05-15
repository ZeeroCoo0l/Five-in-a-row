import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Timer;

public class Program {
    public static void main(String[] args) {
        MiniMaxAlphaBeta engine = new MiniMaxAlphaBeta();//new MiniMax();
        InputReader input = new InputReader();

        Board board = new Board(10);
        //Mark mark = Mark.O;

        runProgram(input, engine, board);

    }
    private static void runProgram(InputReader input, MiniMaxAlphaBeta engine, Board board) {
        String answer = "";
        int rowHuman = -1;
        int colHuman = -1;

        while (!answer.toLowerCase().equals("exit")) {
            if (checkIfSomeoneWon(board)) return;

            // Computer player makes move
            int[] cord = engine.getBestMove(board, rowHuman, colHuman);
            int row = cord[0];
            int col = cord[1];
            board.setMarkAt(row, col, Mark.X);

            if (checkIfSomeoneWon(board)) {
                return;
            }

            //engine.getBlankSquaresAround(board, cord);

            // Human player makes move
            System.out.println(board);

            // Get row input
            boolean validInput = false;
            while (!validInput) {
                try {
                    answer = input.readString("Enter row: ");
                    if (answer.toLowerCase().equals("exit")) {
                        System.out.println("CLOSED GAME.");
                        return;
                    }

                    rowHuman = Integer.parseInt(answer);

                    // Get column input
                    answer = input.readString("Enter column: ");
                    if (answer.toLowerCase().equals("exit")) {
                        System.out.println("CLOSED GAME.");
                        return;
                    }

                    colHuman = Integer.parseInt(answer);

                    // Validate the coordinates
                    if (rowHuman < 0 || rowHuman >= board.getBoardWidth() ||
                            colHuman < 0 || colHuman >= board.getBoardWidth()) {
                        throw new IllegalArgumentException("Coordinates outside board bounds");
                    }

                    // Check if the square is already marked
                    if (board.isSquareMarked(rowHuman, colHuman)) {
                        throw new IllegalArgumentException("Square already marked");
                    }

                    validInput = true;
                }
                catch (NumberFormatException e) {
                    System.out.println("Please enter valid numbers for coordinates.");
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage() + ". Please try again.");
                }
            }

            // Place human's mark
            board.setMarkAt(rowHuman, colHuman, Mark.O);

            if (checkIfSomeoneWon(board)) return;
        }
    }


    /*private static void runProgram(InputReader input, MiniMaxAlphaBeta engine, Board board) {
        String answer = "";
        int rowHuman = -1;
        int colHuman = -1;

        while (!answer.toLowerCase().equals("exit")) {
            if (checkIfSomeoneWon(board)) return;


            // Computer player makes move
            LocalTime start = LocalTime.now();
            int[] cord = engine.getBestMove(board, rowHuman, colHuman);
            int row = cord[0];
            int col = cord[1];
            board.setMarkAt(row, col, Mark.X);
            LocalTime end = LocalTime.now();
            System.out.println("Time for move: " + Duration.between(start, end).getSeconds() + " sek");

            if (checkIfSomeoneWon(board)){
                return;
            }

            engine.getBlankSquaresAround(board, cord);

            // Human player makes move
            System.out.println(board);
            answer = input.readString("Move: ");
            int i = -1;
            while(i < 0){
                try{
                    if(answer.isBlank())
                        throw new NumberFormatException();
                    i = Integer.parseInt(answer);
                    colHuman = calculateColumn(board, i);
                    rowHuman = calculateRow(board, i, colHuman);
                /*int i = Integer.parseInt(answer);
                int col = calculateColumn(board, i);
                int row = calculateRow(board, i, col);
                System.out.println(row +" : " +col);
                //board.placeOnBoard(i, Mark.O);
                board.setMarkAt(row, col, Mark.O);

                //MoveInfo oMove = engine.getBestMove(board);
                int[] cord = engine.getBestMove(board);
                row = cord[0];
                col = cord[1];

                //int j = oMove.index;
                //board.placeOnBoard(j, Mark.X);
                board.setMarkAt(row, col, Mark.X);*/

/*
                    col = calculateColumn(board, i);
                    row = calculateRow(board, i, col);

                    if(board.isSquareMarked(row, col))
                        throw new IllegalArgumentException();
                    else{
                        // Show board
                        System.out.println(engine);
                    }
                }
                catch (NumberFormatException e){
                    if(!answer.equalsIgnoreCase("exit")){
                        System.out.println("Please enter the number of a square.");
                        answer = input.readString("Move: ");
                    }
                    else{
                        System.out.println("CLOSED GAME.");
                    }
                }
                catch (IllegalArgumentException e){
                    System.out.println("Please enter the number of an empty square.");
                    answer = input.readString("Move: ");
                    i = -1;
                }
            }



            board.setMarkAt(row, col, Mark.O);

            if (checkIfSomeoneWon(board)) return;

        }
    }*/

    private static boolean checkIfSomeoneWon(Board board) {
        Mark w = null;
        if((w = board.checkIfSomeoneWon()) != Mark.BLANK){
            System.out.println("\n\n######################\n");
            System.out.println(board);
            System.out.println("######################");
            System.out.println("WINNER: " + w);
            return true;
        }
        return false;
    }

    private static void endGame(Mark winner) {
        System.out.println("G A M E     F I N I S H E D");
        System.out.println("Winner is: " + winner);
    }

    private static int calculateRow(Board board, int i, int column) {
        int j = (i - column) / board.getBoardWidth();
        return j;
    }

    private static int calculateColumn(Board board, int i) {
        if(i == 0)
            return 0;

        int j = i % board.getBoardWidth();
        //System.out.println("COLUMN: "+j);
        return j;
    }

}

