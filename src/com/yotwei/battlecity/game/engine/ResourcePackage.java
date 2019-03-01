package com.yotwei.battlecity.game.engine;

import com.yotwei.battlecity.util.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by YotWei on 2019/2/25.
 */
public class ResourcePackage {

    private static final Logger logger = LoggerFactory.getLogger("ResourcePackage");

    private static boolean initFlag = false;
    private static Map<String, BufferedImage> resMapImage;

    public static void init() throws IOException {

        if (initFlag) {
            if (logger.isWarnEnabled()) {
                logger.warn("ResourcePackage has been initialized");
            }
            return;
        }
        initFlag = true;

        if (logger.isDebugEnabled()) {
            logger.debug("ResourcePackage initialize...");
        }

        //
        // init image resource map
        // load image resources from directory
        //
        resMapImage = new HashMap<>();

        File imgdir = new File(
                Objects.requireNonNull(Constant.RES_PATH).getFile(),
                "graphics");

        for (File ifile : Objects.requireNonNull(imgdir.listFiles())) {

            // get image tag according to the name
            String itag = ifile.getName().substring(0, ifile.getName().lastIndexOf("."));

            // read image
            BufferedImage image = ImageIO.read(ifile);

            resMapImage.put(itag, image);
        }

        /*
         * load other resources
         */
        // TODO: 2019/2/26 load other resources

        if (logger.isDebugEnabled()) {
            logger.debug("ResourcePackage initialize finished");
        }
    }

    public static BufferedImage getImage(String tag) {
        return resMapImage.get(tag);
    }
}
