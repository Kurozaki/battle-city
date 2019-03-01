package com.yotwei.battlecity.framework;

/**
 * Created by YotWei on 2018/10/30.
 */
public interface IStageHandleContext {

    <T> T getVar(String varName);

    <T> T putVar(String varName, T varValue);
}
