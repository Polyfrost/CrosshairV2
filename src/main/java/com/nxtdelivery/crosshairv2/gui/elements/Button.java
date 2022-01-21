package com.nxtdelivery.crosshairv2.gui.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.input.Mouse;

import java.awt.*;

import static com.nxtdelivery.crosshairv2.crosshairs.Crosshairs.resolution;

/**
 * Button API with support for custom textures, and checkbox-style GUI elements with text.
 * Part of my unreleased GUI library I have done some work on, quickGUI.
 *
 * @author nxtdaydelivery
 */
@SuppressWarnings("unused")
public class Button {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final FontRenderer fr = mc.fontRendererObj;
    private String text;
    private final boolean hoverFx;
    private boolean clickFx = true;
    private int padY = 2;
    private int padX = 1;
    private int texX;
    private int texY;
    private int hitBoxX = 0;
    private int hitBoxY = 0;
    private String tooltip;
    private int target = 110;
    private int wrapWidth;
    private boolean checkbox = false;
    private boolean hovered = false;
    private boolean clickPersist = false;
    private boolean toggled = false;
    private boolean rightToggled = false;
    private boolean clicked = false;
    private boolean rightClicked = false;
    private boolean textured = false;
    private boolean hasText = false;
    private boolean retract = false;
    private float animSpeed = 15f;
    private ResourceLocation btnOff, btnOn;
    float percentComplete = 0f;
    private int buttonLeft, buttonRight, buttonBottom, buttonTop;
    private int bgColor = new Color(0, 0, 0, 0).getRGB();
    private final int baseColor = new Color(16, 16, 16, 255).getRGB();
    private final int selectedColor = new Color(0, 158, 72, 255).getRGB();
    private final int buttonColor = new Color(16, 16, 16, 170).getRGB();

    /**
     * Create a new button with a string and checkbox.
     *
     * @param text    to be displayed
     * @param hoverFx weather or not to use hover effects
     */
    public Button(String text, boolean checkbox, boolean hoverFx) {
        this.text = text;
        this.hoverFx = hoverFx;
        this.checkbox = checkbox;
    }

    /**
     * Create a new button with a custom texture.
     *
     * @param off      ResourceLocation of the texture to be displayed when the button is off
     * @param on       ResourceLocation of the texture to be displayed when the button is on
     * @param hoverFx  weather or not to use hover effects
     * @param paddingX X padding in pixels around the image
     * @param paddingY Y padding in pixels around the image
     * @param textureY Y size of the image
     * @param textureX X size of image
     */
    public Button(@NotNull ResourceLocation off, @NotNull ResourceLocation on, boolean hoverFx, int paddingX, int paddingY, int textureX, int textureY) {
        this.hoverFx = hoverFx;
        this.btnOff = off;
        this.btnOn = on;
        this.padX = paddingX;
        this.padY = paddingY;
        textured = true;
        this.texX = textureX;
        this.texY = textureY;
        this.hasText = false;
    }

    /**
     * Create a new button with a custom texture and text string.
     *
     * @param text     text to display on the button
     * @param off      ResourceLocation of the texture to be displayed when the button is off
     * @param on       ResourceLocation of the texture to be displayed when the button is on
     * @param hoverFx  weather or not to use hover effects
     * @param paddingX X padding in pixels around the image
     * @param paddingY Y padding in pixels around the image
     * @param textureX X size of the image
     * @param textureY Y size of the image
     */
    public Button(@NotNull String text, @NotNull ResourceLocation off, @NotNull ResourceLocation on, boolean hoverFx, int paddingX, int paddingY, int textureX, int textureY) {
        this.text = text;
        this.hasText = true;
        this.hoverFx = hoverFx;
        this.btnOff = off;
        this.btnOn = on;
        this.padX = paddingX;
        this.padY = paddingY;
        textured = true;
        this.texX = textureX;
        this.texY = textureY;
    }

    /**
     * Draw the button with the specified bounding box, and check if it is hovered/clicked.
     * Use setXYPadding to change the default padding of this box.
     *
     * @param left   left of bounding box
     * @param top    top of bounding box
     * @param right  right of bounding box
     * @param bottom bottom of bounding box
     */
    public void draw(int left, int top, int right, int bottom) {
        if (resolution == null) {
            resolution = new ScaledResolution(mc);
        }
        if (mc.currentScreen instanceof GuiVideoSettings) {
            resolution = new ScaledResolution(mc);
        }
        if (!textured) {
            //if (fr.getStringWidth(text) > right - left) right = fr.getStringWidth(text) + 3;
            Gui.drawRect(left - padX, top - padY, right + padX, bottom + padY, bgColor);
            if (checkbox) {
                Gui.drawRect(left + 1, top, left + 11, bottom, baseColor);
                fr.drawString(text, left + 13, top + 2, -1);
            } else {
                fr.drawString(text, left + 2, top + 2, -1);
            }
        } else {
            if (!toggled || btnOff.equals(btnOn)) {
                GlStateManager.color(1f, 1f, 1f, 1f);
                mc.getTextureManager().bindTexture(btnOff);
                Gui.drawModalRectWithCustomSizedTexture(left, top, 0, 0, texX, texY, texX, texY);
                if (hasText) {
                    fr.drawString(text, left + texX + 2, top + 1, -1);
                }
            }
        }
        buttonBottom = bottom + padY;
        buttonTop = top - padY;
        buttonLeft = left - padX;
        buttonRight = right + padX;
        update();
    }

    /**
     * Draw the button with a scaled size. *Only works with textured buttons!*
     *
     * @param x       left of button
     * @param y       top of button
     * @param scaledX scaled X of the button
     * @param scaledY scaled Y of the button
     */
    public void drawScaled(int x, int y, int scaledX, int scaledY) {
        if (resolution == null) {
            resolution = new ScaledResolution(mc);
        }
        if (mc.currentScreen instanceof GuiVideoSettings) {
            resolution = new ScaledResolution(mc);
        }
        if (textured) {
            if (!toggled || btnOff.equals(btnOn)) {
                GlStateManager.color(1f, 1f, 1f, 1f);
                mc.getTextureManager().bindTexture(btnOff);
                Gui.drawScaledCustomSizeModalRect(x, y, 0, 0, scaledX, scaledY, scaledX, scaledY, scaledX, scaledY);
                if (hasText) {
                    fr.drawString(text, x + texX + 2, y + 1, -1);
                }
            }
        }
        buttonBottom = y + scaledY + padY;
        buttonTop = y - padY;
        buttonLeft = x - padX;
        buttonRight = x + scaledX + padX;
        update();
    }


    /**
     * Draw the button with automatic padding, with only x and y arguments.
     * Use setXYPadding to change the default pad arguments.
     *
     * @param x the x (left) of button
     * @param y the y (top) of button
     */
    public void draw(int x, int y) {
        int bottom;
        int right;
        if (!textured) bottom = y + 10;
        else bottom = y + texY;
        if (hasText) right = texX + fr.getStringWidth(text) + 30;
        else if (text != null) right = x + fr.getStringWidth(text) + 3;
        else right = texX + x;
        if (checkbox) right += 12;
        draw(x, y, right, bottom);
    }


    /**
     * Update the buttons click and hover status, along with all other states.
     */
    public void update() {
        int mouseX = Mouse.getX() / resolution.getScaleFactor();
        int mouseY = Math.abs((Mouse.getY() / resolution.getScaleFactor()) - resolution.getScaledHeight());
        hovered = mouseX > buttonLeft - hitBoxX && mouseY > buttonTop - hitBoxY && mouseX < buttonRight + hitBoxX && mouseY < buttonBottom + hitBoxY;
        if (hovered) {
            if (Mouse.isButtonDown(0) && !clicked) {            // convert into just one poll on press
                toggled = !toggled;
                if (retract) retract = false;
                if (!toggled) retract = true;
            }
            clicked = Mouse.isButtonDown(0);

            if (Mouse.isButtonDown(1) && !rightClicked) {            // convert into just one poll on press
                rightToggled = !rightToggled;
            }
            rightClicked = Mouse.isButtonDown(1);
        } else if (!clickPersist) {
            clicked = false;
        }
        if (hoverFx) {
            percentComplete = clamp(easeOut(percentComplete, hovered ? 1f : 0f));
            Gui.drawRect(buttonLeft, buttonTop, buttonRight, buttonBottom, new Color(70, 70, 70, (int) (percentComplete * target)).getRGB());
        }
        if (clicked && hovered && clickFx) {
            Gui.drawRect(buttonLeft, buttonTop, buttonRight, buttonBottom, new Color(70, 70, 70, target + 10).getRGB());
        }
        if (toggled && !textured && checkbox) {
            Gui.drawRect(buttonLeft + 4, buttonTop + 4, buttonLeft + 10, buttonBottom - 4, selectedColor);           // selection box
        }
        if (toggled && textured && !btnOff.equals(btnOn)) {
            GlStateManager.color(1f, 1f, 1f, 1f);
            mc.getTextureManager().bindTexture(btnOn);
            Gui.drawModalRectWithCustomSizedTexture(buttonLeft + padX, buttonTop + padY, 0, 0, texX, texY, texX, texY);
        }
        if (percentComplete == 1f && tooltip != null && !clicked && hoverFx) {
            if (fr.splitStringWidth(tooltip, wrapWidth) == 9) wrapWidth = fr.getStringWidth(tooltip) + 3;
            Gui.drawRect(mouseX, mouseY, mouseX + wrapWidth + 2, mouseY + fr.splitStringWidth(tooltip, wrapWidth), buttonColor);
            fr.drawSplitString(tooltip, mouseX + 3, mouseY + 2, wrapWidth, -1);
        }
    }


    private float clamp(float number) {          // animation math
        return number < (float) 0.0 ? (float) 0.0 : Math.min(number, (float) 1.0);
    }

    private float easeOut(float current, float goal) {
        if (Math.floor(Math.abs(goal - current) / (float) 0.01) > 0) {
            return current + (goal - current) / animSpeed;
        } else {
            return goal;
        }
    }

    /**
     * Set the animation speed of this button. (default 15f)
     *
     * @param animSpeed animation speed to set
     */
    public void setAnimSpeed(float animSpeed) {
        this.animSpeed = animSpeed;
    }

    /**
     * Set the tooltip for the button, to be rendered when hovered for a little while.
     *
     * @param newTooltip tooltip to set
     * @param wrapWidth  wrap width for the tooltip
     */
    public void setTooltip(String newTooltip, int wrapWidth) {
        this.tooltip = newTooltip;
        this.wrapWidth = wrapWidth;
    }

    /**
     * Retrieve the current tooltip of the button.
     *
     * @return String tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Retrieve weather or not the button is currently hovered.
     *
     * @return boolean state of hover
     */
    public boolean isHovered() {
        return hovered;
    }

    /**
     * Retrieve weather or not the button is clicked or not.
     *
     * @return boolean click state
     */
    public boolean isClicked() {
        return clicked;
    }

    /**
     * Set the click state of the button.
     *
     * @param clicked click state to set to
     */
    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    /**
     * Retrieve weather or not the button is right-clicked or not.
     *
     * @return boolean right click
     */
    public boolean isRightClicked() {
        return rightClicked;
    }

    /**
     * Retrieve weather or not the button is currently toggled on or off.
     *
     * @return boolean state of the button
     */
    public boolean isToggled() {
        return toggled;
    }

    /**
     * Retrieve weather or not the button is currently right-clicked toggled or not.
     *
     * @return boolean right click
     */
    public boolean isRightToggled() {
        return rightToggled;
    }

    /**
     * Retrieve weather or not the to retract flag is true (used for further GUI drawing)
     *
     * @return boolean retraction state
     */
    public boolean isRetract() {
        return retract;
    }

    /**
     * Set the default padding of the bounding box (default 2,1)
     *
     * @param padX padding X around the button
     * @param padY padding Y around the button
     */
    public void setXYPadding(int padX, int padY) {
        this.padY = padY;
        this.padX = padX;
    }

    /**
     * Add invisible padding to the hit box of the button, allowing for it to be clicked easier.
     *
     * @param padX pixels on X axis to add
     * @param padY pixels on Y axis to add
     */
    public void setInvisibleHitBox(int padX, int padY) {
        this.hitBoxX = padX;
        this.hitBoxY = padY;
    }

    /**
     * Change the text currently displayed on the button.
     *
     * @param text String to display
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * set the alpha limit for the button.
     *
     * @param limit the alpha value (0-255) to target
     */
    public void setAlphaLimit(int limit) {
        this.target = limit;
    }

    /**
     * set the current state of the button. not recommended in normal API use.
     *
     * @param state the state to set to
     */
    public void setClickToggle(boolean state) {
        this.toggled = state;
    }

    /**
     * set the current right click state of the button.
     *
     * @param state the state to set to
     */
    public void setRightToggled(boolean state) {
        this.rightToggled = state;
    }

    /**
     * enable/disable click effects, regardless of weather or not hover effects is enabled.
     *
     * @param state the state to set to
     */
    public void doClickEffects(boolean state) {
        this.clickFx = state;
    }

    /**
     * Set the background color for the button.
     *
     * @param color color to set
     * @implNote use: new Color(...).getRGB();
     */
    public void setBackgroundColor(int color) {
        this.bgColor = color;
    }

    /**
     * enable/disable click persistence, allowing the button to stay as if clicked even though it is not hovered (used for dragging)
     *
     * @param state state to set
     */
    public void enableClickPersistence(boolean state) {
        this.clickPersist = state;
    }

    /**
     * Static method to update the Button's class ScaledResolution, to be called to update the bounding boxes when rendering buttons.
     *
     * @implNote use: if(mc.currentScreen instanceof GuiVideoSettings) Button.updateResolution();
     */
    public static void updateResolution() {
        resolution = new ScaledResolution(mc);
    }
}
