package com.yotwei.battlecity.game.datastruct;

/**
 * Created by YotWei on 2019/3/23.
 * <p>
 * 对二维数组的封装，提供方便操作的API
 */
public class Array2D<T> {

    private final Object[][] arr2d;

    private final int rowCount, colCount;

    public Array2D(int row, int col) {
        arr2d = new Object[row][col];
        rowCount = row;
        colCount = col;
    }

    public Array2D(int row, int col, T defaultValue) {
        this(row, col);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                arr2d[i][j] = defaultValue;
            }
        }
    }

    private void boundaryCheck(int r, int c) {
        if (r < 0 || r >= rowCount
                || c < 0 || c >= colCount) {
            throw new ArrayIndexOutOfBoundsException(
                    String.format(
                            "size: (%d, %d), access: (%d, %d)",
                            rowCount,
                            colCount,
                            r,
                            c));
        }
    }

    public int getColCount() {
        return colCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void set(int r, int c, T val) {
        boundaryCheck(r, c);
        arr2d[r][c] = val;
    }

    @SuppressWarnings("unchecked")
    public T get(int r, int c) {
        boundaryCheck(r, c);
        return (T) arr2d[r][c];
    }
}
