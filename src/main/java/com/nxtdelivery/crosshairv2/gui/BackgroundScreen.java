package com.nxtdelivery.crosshairv2.gui;

import net.minecraft.client.gui.GuiScreen;

import java.awt.*;

public class BackgroundScreen extends GuiScreen {
    private float currentProgress = 0f;

    public boolean doesGuiPauseGame() {
        return false;
    }

    public BackgroundScreen() {
        super.initGui();
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        currentProgress = easeOut(currentProgress, GuiMain.retract ? 0f : 1f);
        int alphaVal = (int) (50 * currentProgress);
        drawGradientRect(0, 0, super.width, super.height, new Color(80, 80, 80, alphaVal).getRGB(), new Color(80, 80, 80, alphaVal + 10).getRGB());
    }

    public void onGuiClosed() {
        GuiMain.retract = true;
    }

    private float easeOut(float current, float goal) {          // animation math (I really like this method)
        if (Math.floor(Math.abs(goal - current) / (float) 0.01) > 0) {
            return current + (goal - current) / (float) 20.0;
        } else {
            return goal;
        }
    }
}
