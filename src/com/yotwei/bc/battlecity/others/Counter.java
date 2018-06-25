package com.yotwei.bc.battlecity.others;


/**
 * Created by YotWei on 2018/6/13.
 */
public class Counter {

    enum Status {
        READY,
        COUNTING,
        FINISHED
    }

    public Counter() {
        clear();
    }

    private Status status;
    private int _counter;
    private int duration;

    public void clear() {
        this.status = Status.READY;
        this._counter = 0;
    }

    public void start(int duration) {
        if (status == Status.READY) {
            this.status = Status.COUNTING;
            this.duration = duration;
        }
    }

    public void update() {
        if (this.status != Status.COUNTING)
            return;
        if (this._counter >= this.duration)
            this.status = Status.FINISHED;
        else
            this._counter++;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasFinished() {
        return this.status == Status.FINISHED;
    }

    public int getProgress() {
        if (duration > 0)
            return _counter * 100 / duration;
        return 0;
    }
}
