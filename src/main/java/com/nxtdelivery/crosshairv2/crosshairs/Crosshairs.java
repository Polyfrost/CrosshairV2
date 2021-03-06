package com.nxtdelivery.crosshairv2.crosshairs;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.config.CrosshairConfig;
import com.nxtdelivery.crosshairv2.gui.GuiMain;
import gg.essential.universal.UResolution;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAmbientCreature;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;


public class Crosshairs {
    public static final ResourceLocation crossLoc = new ResourceLocation(CrosshairV2.ID, "textures/crosshairs.png");
    public static final Minecraft mc = Minecraft.getMinecraft();
    private static float scale = 1f;
    private static float percentDone = 1f;

    public static void render() {
        if (CrosshairConfig.enabled) {
            renderCrosshair(GuiMain.selectedCrosshair);
        } else renderVanilla();
    }

    public static void renderVanilla() {
        mc.getTextureManager().bindTexture(Gui.icons);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
        GlStateManager.enableAlpha();
        mc.ingameGUI.drawTexturedModalRect(UResolution.getScaledWidth() / 2 - 7, UResolution.getScaledHeight() / 2 - 7, 0, 0, 16, 16);
        GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
        GlStateManager.disableBlend();
    }

    public static void renderCrosshair(int number) {
        scale = CrosshairV2.gui.scaleSlider.getValue();
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.scale(scale, scale, 1f);
        mc.getTextureManager().bindTexture(crossLoc);
        if (CrosshairConfig.colorType == 0) {
            GlStateManager.color(CrosshairV2.gui.redSlider.getValue(), CrosshairV2.gui.greenSlider.getValue(), CrosshairV2.gui.blueSlider.getValue(), CrosshairV2.gui.alphaSlider.getValue());
            if (CrosshairV2.gui.chromaBtn.isToggled()) setGlRGB();
        }
        if (CrosshairConfig.colorType == 1) {
            Color color = new Color(getColor(), true);
            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        }
        if (CrosshairConfig.colorType == 2) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR, 1, 0);
            GlStateManager.enableAlpha();
        }
        if (CrosshairConfig.crosshairType == 1) {
            GlStateManager.translate(-scale, -scale, 1f);
            Gui.drawModalRectWithCustomSizedTexture(descaleNum(UResolution.getScaledWidth() / 2 - 6), descaleNum(UResolution.getScaledHeight() / 2 - 6), (number * 16), 0, 16, 16, 224, 16);
        }
        if (CrosshairConfig.crosshairType == 2) {
            mc.getTextureManager().bindTexture(CustomCrosshair.customLoc);
            GlStateManager.translate(-scale, -scale, 1f);       // TODO something with this to make it stay centered
            Gui.drawModalRectWithCustomSizedTexture(descaleNum(UResolution.getScaledWidth() / 2 - 6), descaleNum(UResolution.getScaledHeight() / 2 - 6), 0, 0, 15, 15, 15, 15);
        }
        if (CrosshairConfig.colorType == 2) {
            GlStateManager.tryBlendFuncSeparate(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, 1, 0);
            GlStateManager.disableBlend();
        }
        if (CrosshairConfig.crosshairType == 0) {
            int color = getColor();
            int length = (int) CrosshairV2.gui.lengthSlider.getValue();
            int gap = (int) CrosshairV2.gui.gapSlider.getValue();
            int dot = (int) CrosshairV2.gui.dotSlider.getValue();
            int thick = (int) CrosshairV2.gui.thickSlider.getValue() - 1;
            int gap2 = (int) CrosshairV2.gui.secondGapSlider.getValue();
            int length2 = (int) CrosshairV2.gui.secondLengthSlider.getValue();
            if (dot != 0f) {
                Gui.drawRect(UResolution.getScaledWidth() / 2 - dot + 1, UResolution.getScaledHeight() / 2 - dot + 1, UResolution.getScaledWidth() / 2 + dot, UResolution.getScaledHeight() / 2 + dot, color);    // MIDDLE
            }
            if (thick != -1) {
                Gui.drawRect(UResolution.getScaledWidth() / 2 - length - gap, UResolution.getScaledHeight() / 2 - thick, UResolution.getScaledWidth() / 2 - gap, UResolution.getScaledHeight() / 2 + 1 + thick, color);         // LEFT
                Gui.drawRect(UResolution.getScaledWidth() / 2 - thick, UResolution.getScaledHeight() / 2 - length - gap, UResolution.getScaledWidth() / 2 + 1 + thick, UResolution.getScaledHeight() / 2 - gap, color);         // TOP
                Gui.drawRect(UResolution.getScaledWidth() / 2 + 1 + gap, UResolution.getScaledHeight() / 2 - thick, UResolution.getScaledWidth() / 2 + 1 + length + gap, UResolution.getScaledHeight() / 2 + 1 + thick, color);     // RIGHT
                Gui.drawRect(UResolution.getScaledWidth() / 2 - thick, UResolution.getScaledHeight() / 2 + 1 + gap, UResolution.getScaledWidth() / 2 + 1 + thick, UResolution.getScaledHeight() / 2 + 1 + length + gap, color);     // BOTTOM
                Gui.drawRect(UResolution.getScaledWidth() / 2 - length2 - (int) (gap2 * percentDone), UResolution.getScaledHeight() / 2 - thick, UResolution.getScaledWidth() / 2 - (int) (gap2 * percentDone), UResolution.getScaledHeight() / 2 + 1 + thick, color);         // LEFT 2
                Gui.drawRect(UResolution.getScaledWidth() / 2 - thick, UResolution.getScaledHeight() / 2 - length2 - (int) (gap2 * percentDone), UResolution.getScaledWidth() / 2 + 1 + thick, UResolution.getScaledHeight() / 2 - (int) (gap2 * percentDone), color);         // TOP 2
                Gui.drawRect(UResolution.getScaledWidth() / 2 + 1 + (int) (gap2 * percentDone), UResolution.getScaledHeight() / 2 - thick, UResolution.getScaledWidth() / 2 + 1 + length2 + (int) (gap2 * percentDone), UResolution.getScaledHeight() / 2 + 1 + thick, color);     // RIGHT 2
                Gui.drawRect(UResolution.getScaledWidth() / 2 - thick, UResolution.getScaledHeight() / 2 + 1 + (int) (gap2 * percentDone), UResolution.getScaledWidth() / 2 + 1 + thick, UResolution.getScaledHeight() / 2 + 1 + length2 + (int) (gap2 * percentDone), color);     // BOTTOM 2
            }
            if (CrosshairConfig.dynamicMovement) {
                if (mc.thePlayer.motionX != 0 || mc.thePlayer.motionZ != 0) {
                    percentDone = easeOut(percentDone, (float) (CrosshairV2.gui.multiplierSlider.getValue() * (Math.abs(mc.thePlayer.motionX) + Math.abs(mc.thePlayer.motionZ)) * 10));
                } else percentDone = easeOut(percentDone, 1f);
            }
            if (CrosshairConfig.dynamicAiming) {
                float bowExtension = 0f;
                if (mc.thePlayer.getHeldItem() != null) {
                    if (mc.thePlayer.getItemInUse() != null) {          // stay safe out there kids and always wear protection
                        if (mc.thePlayer.getItemInUse().getItem() instanceof ItemBow) {
                            ItemStack bow = mc.thePlayer.getItemInUse();
                            int useTime = mc.thePlayer.getItemInUseCount();
                            bowExtension = 1f - (bow.getItem().getMaxItemUseDuration(bow) - useTime) / 20.0f;
                            if (useTime == 0 || bowExtension < 0f) {
                                bowExtension = 0f;
                            }
                        }
                    }
                    if (mc.thePlayer.getHeldItem().getItem() instanceof ItemBow) {
                        if (bowExtension == 0.95f) percentDone = CrosshairConfig.multiplier * 2;
                        percentDone = easeOut(percentDone, (CrosshairConfig.multiplier * 2) * bowExtension);
                    }
                } else if (!CrosshairConfig.dynamicMovement) easeOut(percentDone, 1f);
            }
            if (percentDone < 0.8f) percentDone = 0.8f;
        }
        GlStateManager.popMatrix();
        GlStateManager.color(1f, 1f, 1f, 1f);
    }

    public static void renderCrosshair(int number, int x, int y) {
        if (CrosshairConfig.colorType != 2) {
            GlStateManager.color(CrosshairV2.gui.redSlider.getValue(), CrosshairV2.gui.greenSlider.getValue(), CrosshairV2.gui.blueSlider.getValue(), CrosshairV2.gui.alphaSlider.getValue());
        } else GlStateManager.color(1f, 1f, 1f, 1f);
        if (CrosshairV2.gui.chromaBtn.isToggled()) setGlRGB();
        if (CrosshairConfig.crosshairType == 1) {
            mc.getTextureManager().bindTexture(crossLoc);
            Gui.drawModalRectWithCustomSizedTexture(x, y, (number * 16), 0, 16, 16, 224, 16);
        }
        if (CrosshairConfig.crosshairType == 0) {
            int color = getColor();
            int length = (int) CrosshairV2.gui.lengthSlider.getValue();
            int gap = (int) CrosshairV2.gui.gapSlider.getValue();
            int dot = (int) CrosshairV2.gui.dotSlider.getValue();
            int thick = (int) CrosshairV2.gui.thickSlider.getValue() - 1;
            int gap2 = (int) CrosshairV2.gui.secondGapSlider.getValue();
            int length2 = (int) CrosshairV2.gui.secondLengthSlider.getValue();
            if (dot != 0f) Gui.drawRect(x + 8 - dot, y + 8 - dot, x + 7 + dot, y + 7 + dot, color);      // MIDDLE
            if (thick != -1) {
                Gui.drawRect(x + 7 - length - gap, y + 7 - thick, x + 7 - gap, y + 8 + thick, color);       // LEFT
                Gui.drawRect(x + 7 - thick, y + 7 - length - gap, x + 8 + thick, y + 7 - gap, color);       // TOP
                Gui.drawRect(x + 8 + gap, y + 7 - thick, x + 8 + length + gap, y + 8 + thick, color);       // RIGHT
                Gui.drawRect(x + 7 - thick, y + 8 + gap, x + 8 + thick, y + 8 + length + gap, color);       // BOTTOM
                Gui.drawRect(x + 7 - length2 - gap2, y + 7 - thick, x + 7 - gap2, y + 8 + thick, color);       // LEFT 2
                Gui.drawRect(x + 7 - thick, y + 7 - length2 - gap2, x + 8 + thick, y + 7 - gap2, color);       // TOP 2
                Gui.drawRect(x + 8 + gap2, y + 7 - thick, x + 8 + length2 + gap2, y + 8 + thick, color);       // RIGHT 2
                Gui.drawRect(x + 7 - thick, y + 8 + gap2, x + 8 + thick, y + 8 + length2 + gap2, color);       // BOTTOM 2
            }
        }
    }

    public static int getColor() {
        int color = new Color(CrosshairV2.gui.redSlider.getValue(), CrosshairV2.gui.greenSlider.getValue(), CrosshairV2.gui.blueSlider.getValue(), CrosshairV2.gui.alphaSlider.getValue()).getRGB();
        if (CrosshairV2.gui.chromaBtn.isToggled()) {
            float val = CrosshairV2.gui.chromaSpeedSlider.getValue() * 1000f;
            if (val == 0f) val = 760f;
            color = Color.HSBtoRGB(System.currentTimeMillis() % (int) (val) / val, 0.8f, 0.8f);
        }
        if (CrosshairConfig.colorType == 1) {
            if (mc.pointedEntity instanceof EntityAmbientCreature || mc.pointedEntity instanceof EntityAgeable) {
                color = CrosshairConfig.colorEntityFriend.getRGB();
            } else if (mc.pointedEntity instanceof EntityMob) {
                color = CrosshairConfig.colorEntityHostile.getRGB();
            } else if (mc.pointedEntity instanceof EntityPlayer) {
                color = CrosshairConfig.colorPlayer.getRGB();
            } else if (mc.theWorld != null) {
                BlockPos blockPos = mc.objectMouseOver.getBlockPos();
                if (blockPos != null) {
                    IBlockState state = mc.theWorld.getBlockState(blockPos);
                    if (state != null && state.getBlock() instanceof BlockContainer) {
                        color = CrosshairConfig.colorContainer.getRGB();
                    }
                }
            } else color = CrosshairConfig.color.getRGB();
        }

        return color;
    }

    public static void setGlRGB() {
        float val = CrosshairV2.gui.chromaSpeedSlider.getValue() * 1000f;
        if (val == 0f) val = 760f;
        Color color = new Color(Color.HSBtoRGB(System.currentTimeMillis() % (int) (val) / val, 0.8f, 0.8f));
        float red = (float) color.getRed() / 255;
        float green = (float) color.getGreen() / 255;
        float blue = (float) color.getBlue() / 255;
        float alpha = (float) color.getAlpha() / 255;
        GlStateManager.color(red, green, blue, alpha);
    }

    private static float easeOut(float current, float goal) {          // animation math (I really like this method)
        if (Math.floor(Math.abs(goal - current) / (float) 0.01) > 0) {
            return current + (goal - current) / (float) 20.0;
        } else {
            return goal;
        }
    }

    private static int descaleNum(int num) {
        return Math.round(num / scale);
    }
}
