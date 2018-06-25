package com.yotwei.bc.battlecity.exception;

/**
 * Created by YotWei on 2018/6/13.
 */
public class IllegalCounterStatusException extends RuntimeException {

    public IllegalCounterStatusException() {
        super("Could not set status to 'COUNTING'");
    }
}
