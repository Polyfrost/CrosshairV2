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
    private final ArrayList<String> options;
    private int size;
    private int selectedItem;
    private boolean retract = false;

    /**
     * Create a new selector menu.
     *
     * @param options options as a String[] for the selector menu
     * @param def     default option to show (0-indexed)
     */
    public Selector(List<String> options, int def) {
        ResourceLocation arrowLoc = new ResourceLocation(CrosshairV2.ID, "textures/arrowsmall.png");
        this.options = new ArrayList<>(options);
        int i = 0;
        for (String name : options) {
            buttonList.add(new Button(name, false, true));
            int k = fr.getStringWidth(name) + 9;
            i = Math.max(i, k);
        }
        wrapWidth = i;
        size = options.size();
        selectedItem = def;
        main = new Button(options.get(def), arrowLoc, arrowLoc, false, 1, 2, 7, 7);
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
                    main.setText(options.get(i));
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
        size = buttonList.size();
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

    /**
     * Set the option at an index.
     *
     * @param index index to set the value at
     * @param value value to set
     */
    public void setOption(int index, String value) {
        options.set(index, value);
    }

    /**
     * Add an option to the end of the list.
     *
     * @param optionName option name
     */
    public void addOption(String optionName) {
        options.add(optionName);
        buttonList.add(new Button(optionName, false, true));
        size = buttonList.size();
        System.out.println(size);
    }

    /**
     * Set the currently selected item.
     *
     * @param index index of the item to select
     */
    public void setSelectedItem(int index) {
        size = buttonList.size();
        selectedItem = index;
        try {
            main.setText(options.get(index - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        main.setClickToggle(false);
        retract = true;
    }

    /**
     * Get the index of a Selector item from its name.
     *
     * @param name name to search
     * @return the index of that item, or -1 if fails.
     */
    public int getIndexFromName(String name) {
        for (Button btn : buttonList) {
            if (btn.getText().equals(name)) {
                return buttonList.indexOf(btn);
            }
        }
        return -1;
    }

    /**
     * Remove the item at the specified index.
     *
     * @param index index of the item to remove
     */
    public void remove(int index) {
        options.remove(index);
        size = buttonList.size();
        try {
            buttonList.remove(index - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        size = buttonList.size();
        setSelectedItem(0);
    }
}
