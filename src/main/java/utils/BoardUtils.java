package utils;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    public static final int NUM_SQUARES = 64;
    public static final int NUM_SQUARES_PER_ROW = 8;

    private BoardUtils(){
        throw new RuntimeException("Cannot instantiate");
    }

    private static boolean[] initColumn(int columnNumber) {
        final boolean[] column = new boolean[NUM_SQUARES];
        do {
            column[columnNumber] = true;
            columnNumber += NUM_SQUARES_PER_ROW;
        } while (columnNumber < NUM_SQUARES);
        return column;
    }

    public static boolean isValidSquareCoordinate(final int squareCoordinate) {
        return squareCoordinate >= 0 && squareCoordinate < NUM_SQUARES;
    }
}
