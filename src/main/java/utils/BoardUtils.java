package utils;

public class BoardUtils {
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);
    public static final boolean[] SECOND_RANK = initRow(48);
    public static final boolean[] SEVENTH_RANK = initRow(8);

    public static final boolean[] EIGHTH_RANK = initRow(0);  // Indices 0-7
    public static final boolean[] SIXTH_RANK = initRow(16);  // Indices 16-23
    public static final boolean[] FIFTH_RANK = initRow(24);  // Indices 24-31
    public static final boolean[] FOURTH_RANK = initRow(32); // Indices 32-39
    public static final boolean[] THIRD_RANK = initRow(40);  // Indices 40-47
    public static final boolean[] FIRST_RANK = initRow(56);  // Indices 56-63

    public static final int NUM_SQUARES = 64;
    public static final int NUM_SQUARES_PER_ROW = 8;

    private BoardUtils(){
        throw new RuntimeException("Cannot instantiate");
    }

    private static boolean[] initRow(int rowNumber) {
        final boolean[] row = new boolean[NUM_SQUARES];
        for(int i = 0; i < row.length; i++) {
            row[i] = false;
        }
        do {
            row[rowNumber] = true;
            rowNumber++;
        } while (rowNumber % NUM_SQUARES_PER_ROW  != 0);
        return row;
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
