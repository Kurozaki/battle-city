package com.yotwei.bc.battlecity.factory;

import com.yotwei.bc.battlecity.core.Const;
import com.yotwei.bc.battlecity.exception.InitialException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YotWei on 2018/6/12.
 */
public enum GraphicsFactory {

    INSTANCE;

    private static final File GRAPHICS_DIR = new File(Const.ROOT_DIR_URL.getFile(), "graphics/");

    private final Map<String, BufferedImage> imageCacheMap = new HashMap<>();

    public BufferedImage getResourceById(String rId) {
        return imageCacheMap.get(rId);
    }

    public void loadGraphics() throws InitialException {
        File[] list = GRAPHICS_DIR.listFiles();
        if (list == null)
            throw new InitialException("Graphics file not found");
        for (File item : list) {
            try {
                String rId = item.getName().replace(".png", "");
                BufferedImage image = ImageIO.read(item);
                imageCacheMap.put(rId, image);
            } catch (IOException e) {
                e.printStackTrace();
                throw new InitialException(String.format("Error at reading file \"%s\"", item.getName()));
            }
        }
    }

}
