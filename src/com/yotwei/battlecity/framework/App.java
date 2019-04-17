package com.yotwei.battlecity.framework;

import com.yotwei.battlecity.framework.stage.EntryStage;
import com.yotwei.battlecity.framework.stage.IStage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2018/10/30.
 */
public class App extends JFrame {

    private AppPanel panel;
    private Dimension winSize;

    public App(EntryStage bootstrap, Dimension winSize) {
        this.panel = new AppPanel(bootstrap, winSize);
        this.winSize = winSize;
    }

    private void init() {

        // 初始化窗口属性
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(winSize);
        this.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width - winSize.width >> 1,
                Toolkit.getDefaultToolkit().getScreenSize().height - winSize.height >> 1);
        this.add(panel);

        // 禁止改变尺寸
        this.setResizable(false);
        this.setUndecorated(true);

        // 展现
        this.setVisible(true);

        // 添加家鼠标事件
        MouseActionAdapter adapter = new MouseActionAdapter();
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);

        // 添加键盘事件
        this.addKeyListener(new KeyboardListener());
    }

    public void start() {
        init();

        panel.putVar("_WinSize", winSize);
        panel.loop();
    }

    public void clear() {
        System.exit(0);
    }

    private static class AppPanel extends JPanel
            implements IStageHandleContext {

        static final int SLEEP_INTERVAL = 16;

        private IStage stage;
        private volatile boolean drawFinished = false;

        private Map<String, Object> varsMap;

        AppPanel(EntryStage bootstrap, Dimension panelSize) {
            this.stage = bootstrap;
            this.setSize(panelSize);
            this.varsMap = new HashMap<>();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            if (stage != null) {
                stage.draw((Graphics2D) g);
            }
            drawFinished = true;
        }

        void loop() {

            long execCost, execRecord;

            for (; stage != null; ) {

                // 记录起始时间
                execRecord = System.currentTimeMillis();

                // 更新
                stage.update(this);

                // 绘制
                drawFinished = false;
                this.repaint();

                // 绘制方法开启新的线程，因此需要等待绘制完成
                // noinspection StatementWithEmptyBody
                for (; !drawFinished; ) ;

                // 获取这一帧的开销
                execCost = System.currentTimeMillis() - execRecord;
                if (execCost < SLEEP_INTERVAL) {
                    try {
                        Thread.sleep(SLEEP_INTERVAL - execCost);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // 获取下一个处理场景
                IStage next = stage.next();

                if (next != stage) {    // 场景发生了改变
                    stage.onFinished(this);

                    stage = next;
                    if (stage != null) {
                        stage.onInit(this);
                    }
                }
            }
        }

        @Override
        public <T> T getVar(String varName) {
            //noinspection unchecked
            return (T) varsMap.get(varName);
        }

        @Override
        public <T> T putVar(String varName, T varValue) {
            //noinspection unchecked
            return (T) varsMap.put(varName, varValue);
        }
    }

    /**
     * 鼠标事件监听器
     * 监听拖拽事件，实现拖拽窗口
     */
    private class MouseActionAdapter
            implements MouseListener, MouseMotionListener {

        private int offsetX, offsetY;

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            offsetX = e.getX();
            offsetY = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            App.this.setLocation(
                    e.getXOnScreen() - offsetX,
                    e.getYOnScreen() - offsetY);
        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    private class KeyboardListener implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            KeyInput.setKeyStatus(e.getKeyCode(), true);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            KeyInput.setKeyStatus(e.getKeyCode(), false);
        }
    }
}
