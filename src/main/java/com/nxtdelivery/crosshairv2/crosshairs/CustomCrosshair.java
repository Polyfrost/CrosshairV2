package com.nxtdelivery.crosshairv2.crosshairs;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.gui.elements.Button;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CustomCrosshair {
    public static File customCrossFile = new File(CrosshairV2.modDir, "customCrosshair.png");
    public static ResourceLocation customLoc;

    public static void write(List<Button> buttonList) {
        BufferedImage bufferedImage = new BufferedImage(15, 15, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();

        Iterator<Button> itr = buttonList.iterator();
        for (int i = 0; i < 15; i++) {
            for (int i1 = 0; i1 < 15; i1++) {
                Button btn = itr.next();
                if (btn.isToggled()) {
                    graphics2D.setColor(Color.WHITE);
                    graphics2D.drawRect(i, i1, 0, 0);
                }
            }
        }
        graphics2D.dispose();
        File output = customCrossFile;
        try {
            ImageIO.write(bufferedImage, "png", output);
            customLoc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("crosshairCustom", new DynamicTexture(ImageIO.read(customCrossFile)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void read(List<Button> buttonList) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(customCrossFile);
            customLoc = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("crosshairCustom", new DynamicTexture(img));
        } catch (IOException e) {
            e.printStackTrace();
            write(buttonList);
            read(buttonList);
        }
        Iterator<Button> itr = buttonList.iterator();
        for (int i = 0; i < 15; i++) {
            for (int i1 = 0; i1 < 15; i1++) {
                Button btn = itr.next();
                if (img != null && img.getRGB(i, i1) != 0) {
                    btn.setClickToggle(true);
                }
            }
        }
    }

    public static void clear(List<Button> buttonList) {
        for (Button btn : buttonList) {
            btn.setClickToggle(false);
        }
        write(buttonList);
    }
}
