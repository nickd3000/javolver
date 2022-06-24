package com.physmo.javolverexamples;

//import com.github.dragon66.AnimatedGIFWriter;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AnimatedGifCreator {

    AnimatedGIFWriter writer = null;
    String filename = "";
    OutputStream os = null;

    public AnimatedGifCreator(String filename) {
        writer = new AnimatedGIFWriter(true);
        this.filename = filename;
        try {
            os = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void addFrame(Image image) {
        //BufferedImage bi = new BufferedImage(;
        BufferedImage bufferedImage = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics g = bufferedImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();

        addFrame(bufferedImage);
    }

    boolean prepareForWriteSet = false;


    public void addFrame(BufferedImage bufferedImage) {

        if (!prepareForWriteSet) {
            prepareForWriteSet=true;
            try {
                //writer.prepareForWrite(os, -1, -1);
                writer.prepareForWrite(os, bufferedImage.getWidth(), bufferedImage.getHeight());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {

            writer.writeFrame(os, bufferedImage,100);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            writer.finishWrite(os);

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
