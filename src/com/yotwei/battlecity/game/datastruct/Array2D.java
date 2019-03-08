package com.yotwei.battlecity.game.datastruct;

/**
 * Created by YotWei on 2019/3/6.
 */
public class Array2D<E> {

    private final int row, col;

    private Object[][] array2d;

    public Array2D(int row, int col) {
        this.row = row;
        this.col = col;
        this.array2d = new Object[row][col];
    }

    @SuppressWarnings("unchecked")
    public E get(int row, int col) {
        if (isInRange(row, col)) {
            return (E) array2d[row][col];
        } else {
            return null;
        }
    }

    public boolean set(int row, int col, E e) {
        boolean inRange = isInRange(row, col);
        if (inRange) {
            array2d[row][col] = e;
        }
        return inRange;
    }

    public boolean isInRange(int r, int c) {
        return 0 <= r && r < row
                && 0 <= c && c < col;
    }
}
