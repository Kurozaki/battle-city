package com.yotwei.battlecity.game.object.effect;

import com.yotwei.battlecity.game.engine.ResourcePackage;
import com.yotwei.battlecity.game.object.GameObject;
import com.yotwei.battlecity.game.object.LevelContext;
import com.yotwei.battlecity.game.object.properties.Physic;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Created by YotWei on 2019/3/9.
 */
public class Effect extends GameObject {

    private BufferedImage image;

    private final Rectangle hitbox;
    private int frameCount;
    private int frameInterval;

    private int ticker;

    public Effect(LevelContext lvlCtx) {
        super(lvlCtx);

        hitbox = new Rectangle();
    }

    public void setId(int id) {
        image = ResourcePackage.getImage("effect-" + id);
    }

    public void setFrameCount(int frameCount) {
        this.frameCount = frameCount;
    }

    public void setFrameInterval(int frameInterval) {
        this.frameInterval = frameInterval;
    }

    public void setFrameSize(int width, int height) {
        hitbox.setSize(width, height);
    }

    public void setCoordinate(int x, int y) {
        hitbox.setLocation(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        int frameX = (ticker / frameInterval) % frameCount;

        g.drawImage(
                image,

                hitbox.x,
                hitbox.y,
                hitbox.x + hitbox.width,
                hitbox.y + hitbox.height,

                frameX * hitbox.width,
                0,
                (frameX + 1) * hitbox.width,
                hitbox.height,
                null
        );
    }

    @Override
    public int getDrawPriority() {
        return 6;
    }

    @Override
    public void onActive() {
    }

    @Override
    public void update() {
        if (ticker++ > frameInterval * frameCount) {
            setActive(false);
        }
    }

    @Override
    public void onInactive() {

    }

    @Override
    public Rectangle getHitbox() {
        return hitbox;
    }

    @Override
    public void onCollide(Physic<? extends Shape> anotherObject) {

    }

    @Override
    public void onTouchBound(Rectangle bound) {

    }
}
