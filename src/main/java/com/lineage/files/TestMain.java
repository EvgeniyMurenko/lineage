package com.lineage.files;

import com.lineage.domain.PictureAnalytic;

public class TestMain {


    public static void main(String[] args) throws Exception {
        PictureAnalytic pictureAnalytic = new PictureAnalytic();

        System.out.println(pictureAnalytic.checkPicture());

//        try {
//
//            BufferedImage image = new Robot()
//                    .createScreenCapture(new Rectangle(310, 370 , 250, 20 ));
//
//            BufferedImage result = new BufferedImage(
//                    image.getWidth(),
//                    image.getHeight(),
//                    BufferedImage.TYPE_BYTE_BINARY);
//
//            Graphics2D graphic = result.createGraphics();
//            graphic.drawImage(image, 0, 0, Color.WHITE, null);
//            graphic.dispose();
//
//
//
//
//            File output = new File("C:\\Users\\admin\\IdeaProjects\\lineage\\src\\main\\resources\\files\\001_1.png");
//            ImageIO.write(result, "png", output);
//
//        }  catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
