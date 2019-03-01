package com.yotwei.battlecity.framework;

import com.yotwei.battlecity.framework.stage.BootstrapStage;
import com.yotwei.battlecity.framework.stage.IStage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.temporal.ValueRange;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2018/10/30.
 */
public class App extends JFrame {

    private AppPanel panel;
    private Dimension winSize;

    public App(BootstrapStage bootstrap, Dimension winSize) {
        this.panel = new AppPanel(bootstrap, winSize);
        this.winSize = winSize;
    }

    private void init() {

        // set init properties
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(winSize);
        this.setLocation(
                Toolkit.getDefaultToolkit().getScreenSize().width - winSize.width >> 1,
                Toolkit.getDefaultToolkit().getScreenSize().height - winSize.height >> 1);
        this.add(panel);

        // disable resize
        this.setResizable(false);
        this.setUndecorated(true);

        // set to visible
        this.setVisible(true);

        // add mouse listener
        MouseActionAdapter adapter = new MouseActionAdapter();
        this.addMouseListener(adapter);
        this.addMouseMotionListener(adapter);

        // add keyboard listener
        this.addKeyListener(new KeyboardListener());
    }

    public void start() {
        this.init();

        this.panel.loop();
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

        AppPanel(BootstrapStage bootstrap, Dimension panelSize) {
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

                // record the time
                execRecord = System.currentTimeMillis();

                // do update
                stage.update(this);

                // do draw
                drawFinished = false;
                this.repaint();

                // waiting for draw finished
                // noinspection StatementWithEmptyBody
                for (; !drawFinished; ) ;

                // get execute cost
                execCost = System.currentTimeMillis() - execRecord;
                if (execCost < SLEEP_INTERVAL) {
                    try {
                        Thread.sleep(SLEEP_INTERVAL - execCost);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // get next handle stage
                IStage next = stage.next();

                if (next != stage) {    // stage change
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
     * mouse action listener
     * <p>
     * window could be moved while mouse dragging on window
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
