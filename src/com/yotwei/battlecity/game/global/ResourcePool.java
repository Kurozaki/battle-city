package com.yotwei.battlecity.game.global;

import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YotWei on 2019/3/14.
 */
public class ResourcePool {

    private static final Logger logger = LoggerFactory.getLogger(ResourcePool.class);

    private static Map<String, BufferedImage> images;
    private static Map<String, AudioClip> audios;

    private ResourcePool() {
    }

    public static void init() {

        try {
            if (images == null) {
                loadImageResources();
            }

            if (audios == null) {
                loadAudioResources();
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("ResourcePool initialize failed");
        }
    }

    private static void loadImageResources() throws IOException {
        File dir = new File(
                Objects.requireNonNull(Constant.RES_PATH).getFile(),
                "graphics"
        );

        images = new HashMap<>();

        for (File imageFile : Objects.requireNonNull(dir.listFiles())) {
            String filename = imageFile.getName();
            images.put(
                    filename.substring(0, filename.lastIndexOf(".")),    // 以文件的名字（不含拓展名）作为key
                    ImageIO.read(imageFile)
            );
            if (logger.isDebugEnabled()) {
                logger.debug("Loaded image {}", filename);
            }
        }
    }

    private static void loadAudioResources() throws IOException {
        File dir = new File(
                Objects.requireNonNull(Constant.RES_PATH).getFile(),
                "sounds"
        );

        audios = new HashMap<>();

        for (File audioFile : Objects.requireNonNull(dir.listFiles())) {
            String filename = audioFile.getName();
            audios.put(
                    filename.substring(0, filename.lastIndexOf(".")),    // 以文件的名字（不含拓展名）作为key
                    Applet.newAudioClip(audioFile.toURI().toURL())
            );
            if (logger.isDebugEnabled()) {
                logger.debug("Loaded audio {}", filename);
            }
        }
    }

    public static BufferedImage getImage(String key) {
        return Objects.requireNonNull(images).get(key);
    }

    public static AudioClip getAudioClip(String key) {
        return Objects.requireNonNull(audios.get(key));
    }
}
