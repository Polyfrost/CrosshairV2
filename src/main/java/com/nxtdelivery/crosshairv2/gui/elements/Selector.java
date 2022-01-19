package com.nxtdelivery.crosshairv2.gui.elements;


import com.nxtdelivery.crosshairv2.CrosshairV2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Selector {
    private final List<Button> buttonList = new ArrayList<>();
    private final Minecraft mc = Minecraft.getMinecraft();
    private final int bgColor = new Color(16, 16, 16, 200).getRGB();
    private final Button main;
    private float percentComplete = 0f;
    private final FontRenderer fr = mc.fontRendererObj;
    private final int wrapWidth;
    private final String[] options;
    private final int size;
    private int selectedItem;
    private boolean retract = false;

    /**
     * Create a new selector menu.
     *
     * @param options options as a String[] for the selector menu
     * @param def     default option to show (0-indexed)
     */
    public Selector(String[] options, int def) {
        ResourceLocation arrowLoc = new ResourceLocation(CrosshairV2.ID, "textures/arrowsmall.png");
        this.options = options;
        int i = 0;
        for (String name : options) {
            buttonList.add(new Button(name, false, true));
            int k = fr.getStringWidth(name) + 9;
            i = Math.max(i, k);
        }
        wrapWidth = i;
        size = options.length;
        selectedItem = def;
        main = new Button(options[def], arrowLoc, arrowLoc, false, 1, 2, 7, 7);
        main.doClickEffects(false);
    }

    /**
     * Draw the selector menu at the specified coordinates.
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public void draw(int x, int y) {
        percentComplete = clamp(easeOut(percentComplete, retract ? 0f : 1f));
        int target = 13 * size;
        Gui.drawRect(x - 2, y - 1, x + wrapWidth + 1, y + 12 + (int) (percentComplete * target), bgColor);
        main.draw(x, y, x + wrapWidth, y + 11);
        retract = !main.isToggled();

        if (percentComplete > 0.9f) {
            int i = 0;
            for (Button btn : buttonList) {
                btn.draw(x, y + (i * 13) + 11);
                if (btn.isClicked()) {
                    selectedItem = i;
                    main.setText(options[i]);
                    main.setClickToggle(false);
                    btn.setClicked(false);
                    retract = true;
                }
                i++;
            }
        }
    }

    private float clamp(float number) {          // animation math
        return number < (float) 0.0 ? (float) 0.0 : Math.min(number, (float) 1.0);
    }

    private float easeOut(float current, float goal) {
        if (Math.floor(Math.abs(goal - current) / (float) 0.01) > 0) {
            return current + (goal - current) / 15f;
        } else {
            return goal;
        }
    }

    /**
     * Return the size of the list.
     *
     * @return the size of the list
     */
    public int getSize() {
        return size;
    }

    /**
     * Get the current selected item from this selector.
     *
     * @return the current selected item
     */
    public int getSelectedItem() {
        return selectedItem;
    }

    /**
     * Return the item at an index in the menu.
     *
     * @param index 0-indexed index of the wanted item
     * @return *Button* at that index
     */
    public Button getItem(int index) {
        return buttonList.get(index);
    }

    /**
     * Close the selector menu.
     */
    public void close() {
        retract = true;
    }

    /**
     * Open the selector menu.
     */
    public void open() {
        retract = false;
    }

    /**
     * Set the text that is currently displayed on the Selector
     *
     * @param text to set
     */
    public void setText(String text) {
        main.setText(text);
    }

    /**
     * Set a tooltip for the selector.
     *
     * @param tooltip tooltip to set
     */
    public void setTooltip(String tooltip) {
        main.setTooltip(tooltip, fr.getStringWidth(tooltip));
    }

}
