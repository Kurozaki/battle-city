package com.yotwei.bc.battlecity.exception;

import java.io.IOException;

/**
 * Created by YotWei on 2018/6/12.
 */
public class InitialException extends IOException {
    private static final String PREFIX = "Initial failed: ";

    public InitialException(String err) {
        super(PREFIX + err);
    }
}
