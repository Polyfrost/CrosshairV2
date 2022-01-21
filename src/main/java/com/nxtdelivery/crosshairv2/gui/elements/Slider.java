package com.nxtdelivery.crosshairv2.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.nxtdelivery.crosshairv2.crosshairs.Crosshairs.resolution;

@SuppressWarnings({"IntegerDivisionInFloatingPointContext", "unused"})
public class Slider {
    private final FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
    private final float min;
    private final float max;
    private int color;
    private int colorHead;
    private boolean showNums;
    private final int length;
    private boolean showCurrent = false;
    private int progress;
    private final int baseColor = new Color(16, 16, 16, 200).getRGB();
    private final Button main;
    private float current;
    private boolean hovered;

    /**
     * Create a new slider.
     *
     * @param min        minimum value for the slider
     * @param max        maximum value for the slider
     * @param length     length of the slider (in pixels)
     * @param defaultNum default progress to show on the bar
     * @param showNums   render numbers describing to the user the minimum and maximum values, as well as the current value
     * @param colorBase  color for the body of the progress bar
     * @param colorHead  color for the head of the progress bar
     */
    public Slider(float min, float max, int length, float defaultNum, boolean showNums, int colorBase, int colorHead) {
        this.min = min;
        this.max = max;
        this.color = colorBase;
        this.colorHead = colorHead;
        this.length = length;
        this.showNums = showNums;
        this.current = defaultNum;
        this.progress = (int) ((current / max) * length);
        main = new Button("", false, true);
        main.setInvisibleHitBox(2, 2);
        main.setXYPadding(0, 0);
    }

    /**
     * Draw the slider at the selected position.
     *
     * @param x the X coordinate of the slider (left)
     * @param y the Y coordinate of the slider (top)
     */
    public void draw(int x, int y) {
        Gui.drawRect(x, y, x + length, y + 7, baseColor);
        int mouseX = Mouse.getX() / resolution.getScaleFactor();
        int mouseY = Math.abs((Mouse.getY() / resolution.getScaleFactor()) - resolution.getScaledHeight());
        hovered = mouseX > x && mouseX < x + length && mouseY > y && mouseY < y + 7;

        Gui.drawRect(x + 1, y + 1, x + progress, y + 6, color);
        if (showNums) {
            fr.drawStringWithShadow(String.valueOf(min), x - fr.getStringWidth(String.valueOf(min)) - 1, y, -1);
            fr.drawStringWithShadow(String.valueOf(max), x + length + 5, y, -1);
        }
        if (showCurrent) {
            fr.drawStringWithShadow(String.valueOf(current), x + progress - fr.getStringWidth(String.valueOf(current)) / 2 + 3, y + 11, -1);
        }
        Gui.drawRect(x + progress - 2, y - 1, x + progress + 6, y + 8, colorHead);
        main.draw(x + progress - 2, y - 1, x + progress + 6, y + 8);

        if (hovered && Mouse.isButtonDown(0) || main.isClicked()) progress = Math.abs(x - mouseX);

        if (progress > length) progress = length;
        try {
            current = ((float) progress / (float) length) * max;
        } catch (Exception e) {
            current = 0f;
        }
    }

    /**
     * Return weather or not the Slider is currently hovered.
     *
     * @return boolean hover state
     */
    public boolean isHovered() {
        return hovered;
    }

    /**
     * Return the current slider position, as a float between its max and minimum values.
     *
     * @return current position
     */
    public float getValue() {
        return current;
    }

    /**
     * Set the value of the slider.
     *
     * @param value value to set (between its max and minimum)
     */
    public void setValue(float value) {
        this.progress = (int) ((value / max) * length);
    }

    /**
     * Return the current slider position as an integer in pixels from the beginning of the bar.
     *
     * @return the current position as an int
     */
    public int getValueAsInt() {
        return progress;
    }

    /**
     * Set the main color of the progress bar.
     *
     * @param color color to set (decimal format)
     */
    public void setBaseColor(int color) {
        this.color = color;
    }

    /**
     * Set the head color of the progress bar.
     *
     * @param color color to set (decimal format)
     */
    public void setHeadColor(int color) {
        this.colorHead = color;
    }

    /**
     * Enable/disable the showing of numbers on the progress bar
     *
     * @param state state to set
     */
    public void shouldShowNums(boolean state) {
        this.showNums = state;
    }

    /**
     * Return weather or not the head of the progress bar is currently clicked.
     *
     * @return head state
     */
    public boolean isClicked() {
        return main.isClicked();
    }

    /**
     * Set the tooltip of the head of the progress bar.
     *
     * @param tooltip   tooltip to set
     * @param wrapWidth wrap width of the tooltip
     */
    public void setTooltip(String tooltip, int wrapWidth) {
        main.setTooltip(tooltip, wrapWidth);
    }

    /**
     * Get the slider's head as a Button for further manipulation.
     *
     * @return Button head
     */
    public Button getSlider() {
        return main;
    }

    public void showCurrent(boolean state) {
        this.showCurrent = state;
    }


}
