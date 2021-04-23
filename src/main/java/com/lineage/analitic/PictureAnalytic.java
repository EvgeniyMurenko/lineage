package com.lineage.analitic;

import com.lineage.util.Utils;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PictureAnalytic {


    public boolean checkPicture() {
        BufferedImage mainPic = createScreen();

        Utils.delay(1000);
        BufferedImage changePic = createScreen();

        if (mainPic == null || changePic == null) return false;
        return bufferedImagesEqual(mainPic, changePic);
    }

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }

    private BufferedImage createScreen() {

        BufferedImage image = null;
        try {
//            image = new Robot().createScreenCapture(new Rectangle(310, 370, 250, 20));
            image = new Robot().createScreenCapture(new Rectangle(15, 415, 240, 20));
            BufferedImage result = new BufferedImage(
                    image.getWidth(),
                    image.getHeight(),
                    BufferedImage.TYPE_BYTE_BINARY);

            Graphics2D graphic = result.createGraphics();
            graphic.drawImage(image, 0, 0, Color.WHITE, null);
            graphic.dispose();
            return result;
        } catch (AWTException e) {
            e.printStackTrace();
        }

        return null;
    }
}
