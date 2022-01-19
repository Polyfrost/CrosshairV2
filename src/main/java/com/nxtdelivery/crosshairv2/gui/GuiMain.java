package com.nxtdelivery.crosshairv2.gui;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.config.CrosshairConfig;
import com.nxtdelivery.crosshairv2.crosshairs.Crosshairs;
import com.nxtdelivery.crosshairv2.crosshairs.CustomCrosshair;
import com.nxtdelivery.crosshairv2.gui.elements.Button;
import com.nxtdelivery.crosshairv2.gui.elements.Selector;
import com.nxtdelivery.crosshairv2.gui.elements.Slider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Mouse;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class GuiMain {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static ScaledResolution resolution;
    private static final FontRenderer fr = mc.fontRendererObj;
    private float percentOpenMain = 0f;
    private float percentOpenSecond = 0f;
    private boolean retract = false;
    private int left;
    private int right;
    private int top;
    private int currentBottom;
    private int selectedPreview = CrosshairConfig.preview;
    private static int crosshairType = 1;
    private static int scale = 4;
    private static int currentColor = 2;
    public static int selectedCrosshair = CrosshairConfig.crosshair;
    private static float goal = 1f;
    private final ResourceLocation barLoc = new ResourceLocation(CrosshairV2.ID, "textures/bar.png");
    private final ResourceLocation arrowLoc = new ResourceLocation(CrosshairV2.ID, "textures/arrow.png");
    private final ResourceLocation scene1Loc = new ResourceLocation(CrosshairV2.ID, "textures/scene1.png");
    private final ResourceLocation scene2Loc = new ResourceLocation(CrosshairV2.ID, "textures/scene2.png");
    private final ResourceLocation scene3Loc = new ResourceLocation(CrosshairV2.ID, "textures/scene3.png");
    private final ResourceLocation scene4Loc = new ResourceLocation(CrosshairV2.ID, "textures/scene4.png");
    private final ResourceLocation frameLoc = new ResourceLocation(CrosshairV2.ID, "textures/frame.png");
    private final ResourceLocation btnLoc = new ResourceLocation(CrosshairV2.ID, "textures/button.png");
    private final ResourceLocation infoLoc = new ResourceLocation(CrosshairV2.ID, "textures/infobtn.png");
    private final ResourceLocation crosshairFrameLoc = new ResourceLocation(CrosshairV2.ID, "textures/crosshairframe.png");
    private final int baseColor = new Color(27, 27, 27, 255).getRGB();
    private final Button closeButton = new Button(arrowLoc, arrowLoc, true, 3, 3, 9, 9);
    private final Button infoBtn = new Button(infoLoc, infoLoc, true, 3, 3, 9, 9);
    private final Button bar = new Button(barLoc, barLoc, false, 0, 0, 600, 15);
    private final Button scene1Btn = new Button(scene1Loc, scene1Loc, false, 0, 0, 768, 256);
    private final Button scene2Btn = new Button(scene2Loc, scene2Loc, false, 0, 0, 768, 256);
    private final Button scene3Btn = new Button(scene3Loc, scene3Loc, false, 0, 0, 768, 256);
    private final Button scene4Btn = new Button(scene4Loc, scene4Loc, false, 0, 0, 768, 256);
    private final Button smallScaleBtn = new Button("Small", true, true);
    private final Button normalScaleBtn = new Button("Normal", true, true);
    private final Button largeScaleBtn = new Button("Large", true, true);
    private final Button autoScaleBtn = new Button("Auto", true, true);
    private final Button showModUpdates = new Button("Show Mod Updates", true, true);
    private final Button updateNow = new Button("Update Now", false, true);
    public final Button debugBtn = new Button("Debug Mode", true, true);
    public final Button chromaBtn = new Button("Chroma", true, true);
    private final Button presetBtn = new Button(btnLoc, btnLoc, true, 0, 0, 142, 15);
    private final Button simpleBtn = new Button(btnLoc, btnLoc, true, 0, 0, 142, 15);
    private final Button customBtn = new Button(btnLoc, btnLoc, true, 0, 0, 142, 15);
    private final Button saveCrossBtn = new Button("Save Crosshair", false, true);
    private final Button clearCrossBtn = new Button("Clear Crosshair", false, true);
    private final Button movementBtn = new Button("Dynamic Movement", true, true);
    private final Button aimBtn = new Button("Dynamic Aim", true, true);
    private final Selector colorSelector = new Selector(new String[]{"Basic", "Dynamic", "Vanilla Blending"}, CrosshairConfig.colorType);
    private final Selector dynamicColorSelector = new Selector(new String[]{"Player", "Entity", "None/Other"}, 2);
    public final Slider redSlider = new Slider(0f, 1f, 50, CrosshairConfig.color.getRed() / 255f, false, new Color(200, 0, 0, 230).getRGB(), new Color(170, 0, 20, 255).getRGB());
    public final Slider blueSlider = new Slider(0f, 1f, 50, CrosshairConfig.color.getBlue() / 255f, false, new Color(0, 0, 200, 230).getRGB(), new Color(20, 0, 170, 255).getRGB());
    public final Slider greenSlider = new Slider(0f, 1f, 50, CrosshairConfig.color.getGreen() / 255f, false, new Color(0, 200, 0, 230).getRGB(), new Color(0, 170, 20, 255).getRGB());
    public final Slider alphaSlider = new Slider(0f, 1f, 50, CrosshairConfig.color.getAlpha() / 255f, false, new Color(255, 255, 255, 230).getRGB(), new Color(220, 220, 220, 255).getRGB());
    public final Slider thickSlider = new Slider(0f, 5f, 130, CrosshairConfig.thickness, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider gapSlider = new Slider(0f, 10f, 130, CrosshairConfig.gap, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider lengthSlider = new Slider(0f, 20f, 130, CrosshairConfig.lineLength, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider dotSlider = new Slider(0f, 5f, 130, CrosshairConfig.dotSize, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider chromaSpeedSlider = new Slider(1f, 67f, 120, CrosshairConfig.chromaSpeed, false, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider secondLengthSlider = new Slider(0f, 15f, 100, CrosshairConfig.secondLineLength, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider multiplierSlider = new Slider(1f, 5f, 100, CrosshairConfig.multiplier, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    public final Slider secondGapSlider = new Slider(0f, 25f, 100, CrosshairConfig.secondGap, true, new Color(0, 158, 72, 220).getRGB(), new Color(0, 158, 72, 255).getRGB());
    private final List<Button> presetButtonList = new ArrayList<>();
    private final List<Button> customButtonsList = new ArrayList<>();

    public GuiMain() {
        ResourceLocation crosshairBgLoc = new ResourceLocation(CrosshairV2.ID, "textures/crosshairwin.png");
        ResourceLocation customSelectedLoc = new ResourceLocation(CrosshairV2.ID, "textures/whitesquare.png");
        for (int i1 = 0; i1 < 256; i1++) {
            customButtonsList.add(new Button(crosshairBgLoc, customSelectedLoc, true, 0, 0, 15, 15));
        }
        for (int i = 0; i < 14; i++) {
            presetButtonList.add(new Button(crosshairBgLoc, crosshairBgLoc, true, 0, 0, 15, 15));
        }
        CustomCrosshair.read(customButtonsList);
        colorSelector.setTooltip("Change the color of your crosshair.");
        closeButton.setTooltip("Close the GUI.", 100);
        smallScaleBtn.setTooltip("Render the crosshair with an individual GUI scale of Small.", 120);
        normalScaleBtn.setTooltip("Render the crosshair with an individual GUI scale of Normal.", 120);
        largeScaleBtn.setTooltip("Render the crosshair with an individual GUI scale of Large.", 120);
        autoScaleBtn.setTooltip("Render the crosshair with your current GUI scale.", 120);
        presetBtn.setTooltip("Choose your crosshair from a variety of presets.", 300);
        simpleBtn.setTooltip("Create a simple crosshair with FPS-game style options.", 300);
        customBtn.setTooltip("Create a fully custom crosshair by drawing your own.", 300);
        chromaBtn.setTooltip("Enable chroma on the crosshair.", 200);
        saveCrossBtn.setTooltip("Save the crosshair to your disk and apply it.", 200);
        clearCrossBtn.setTooltip("Clear the grid and the crosshair and on your disk.", 200);
        aimBtn.setTooltip("Dynamically move crosshair parts when aiming.", 200);
        movementBtn.setTooltip("Dynamically move crosshair when running/walking.", 200);
        infoBtn.setTooltip("About CrosshairV2 and mod settings", 200);
        showModUpdates.setTooltip("Show a notification when you start Minecraft informing you of new updates.", 150);
        updateNow.setTooltip("Update by clicking the button.", 150);
        debugBtn.setTooltip("Enable debugging information.", 150);
        clearCrossBtn.setBackgroundColor(new Color(16, 16, 16, 255).getRGB());
        saveCrossBtn.setBackgroundColor(new Color(16, 16, 16, 255).getRGB());
        chromaBtn.setClickToggle(CrosshairConfig.chroma);
        aimBtn.setClickToggle(CrosshairConfig.dynamicAiming);
        movementBtn.setClickToggle(CrosshairConfig.dynamicMovement);
        showModUpdates.setClickToggle(CrosshairConfig.showUpdate);
        infoBtn.setClickToggle(false);
        bar.doClickEffects(false);
        bar.enableClickPersistence(true);
        crosshairType = CrosshairConfig.crosshairType;
        currentColor = CrosshairConfig.colorType;
        switch (CrosshairConfig.scale) {
            case 0:
                smallScaleBtn.setClickToggle(true);
                break;
            case 1:
                normalScaleBtn.setClickToggle(true);
                break;
            case 2:
                largeScaleBtn.setClickToggle(true);
                break;
            default:
            case 3:
                autoScaleBtn.setClickToggle(true);
                break;
        }
        register();
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        long startTime = System.nanoTime();
        if (mc.currentScreen instanceof GuiVideoSettings) {
            Button.updateResolution();
            Crosshairs.updateResolution();
            resolution = new ScaledResolution(mc);
        }
        if (!CrosshairV2.guiOpen && !retract) return;
        if (mc.currentScreen instanceof GuiIngameMenu) {
            retract = true;
        }
        if (resolution == null) resolution = new ScaledResolution(mc);
        int height = resolution.getScaledHeight();
        int width = resolution.getScaledWidth();
        int winWidth = 600;
        int winHeight = 265;
        if (left == 0 && right == 0) {
            left = (width / 2) - (winWidth / 2);
            top = height / 3;
        }
        right = left + winWidth;
        int bottom = top + winHeight;

        percentOpenMain = easeOut(percentOpenMain, retract ? 0f : goal);
        int currentRight = (int) (right * percentOpenMain);
        currentBottom = (int) (bottom * percentOpenMain);
        if (percentOpenMain == 1.18f) currentBottom = top + (int) (winHeight * 1.25f);
        if (currentRight < left) currentRight = left;
        if (currentBottom < top) currentBottom = top;
        if (currentRight > right) currentRight = right;
        Gui.drawRect(left, top, currentRight, currentBottom, baseColor);
        if(!Mouse.isButtonDown(0) && bar.isClicked()) {     // fix to stop it 'sticking'
            bar.setClicked(false);
        }
        if (percentOpenMain > 0.95f) {
            bar.draw(left, top);
            fr.drawStringWithShadow("CrosshairV2", left + 17, top + 4, -1);
            if (bar.isClicked()) {
                left += Mouse.getDX() / resolution.getScaleFactor();
                top -= Mouse.getDY() / resolution.getScaleFactor();
            }
            int btnY = 165;
            if (crosshairType == 2) btnY = 20;
            simpleBtn.draw(left + 12, top + btnY);
            presetBtn.draw(left + 165, top + btnY);
            customBtn.draw(left + 317, top + btnY);
            fr.drawStringWithShadow("Simple", left + 12 + (142 / 2 - fr.getStringWidth("Simple") / 2), top + btnY + 3, -1);
            fr.drawStringWithShadow("Presets", left + 165 + (142 / 2 - fr.getStringWidth("Presets") / 2), top + btnY + 3, -1);
            fr.drawStringWithShadow("Custom", left + 317 + (142 / 2 - fr.getStringWidth("Custom") / 2), top + btnY + 3, -1);
            if (simpleBtn.isClicked()) {
                crosshairType = 0;
                presetBtn.setClickToggle(false);
                customBtn.setClickToggle(false);
            }
            if (presetBtn.isClicked()) {
                crosshairType = 1;
                simpleBtn.setClickToggle(false);
                customBtn.setClickToggle(false);
            }
            if (customBtn.isClicked()) {
                crosshairType = 2;
                presetBtn.setClickToggle(false);
                simpleBtn.setClickToggle(false);
            }
            if (crosshairType != CrosshairConfig.crosshairType) {
                CrosshairConfig.crosshairType = crosshairType;
                CrosshairV2.config.markDirty();
                CrosshairV2.config.writeData();
            }

            if (crosshairType == 1) {
                goal = 1f;
                renderPreview();
                Gui.drawRect(left + 165, top + 165, left + 307, top + 180, 2015042331);
                int i = 0;
                for (Button btn : presetButtonList) {
                    btn.draw(left + 20 + (i * 23), top + 190);
                    mc.getTextureManager().bindTexture(Crosshairs.crossLoc);
                    GlStateManager.color(1f, 1f, 1f, 1f);
                    Gui.drawModalRectWithCustomSizedTexture(left + 20 + (i * 23), top + 190, (i * 16), 0, 16, 16, 224, 16);
                    if (btn.isToggled()) {
                        mc.getTextureManager().bindTexture(crosshairFrameLoc);
                        Gui.drawModalRectWithCustomSizedTexture(left + 20 + (i * 23), top + 190, (i * 15), 0, 15, 15, 15, 15);
                        for (Button btn2 : presetButtonList) {
                            btn2.setClickToggle(false);
                        }
                        btn.setClickToggle(true);
                        selectedCrosshair = i;
                    }
                    i++;
                }
            }
            if (crosshairType == 0) {
                Gui.drawRect(left + 12, top + 165, left + 154, top + 180, 2015042331);
                renderPreview();
                goal = 1.18f;
                movementBtn.draw(left + 320, top + 269);
                aimBtn.draw(left + 320, top + 255);
                fr.drawStringWithShadow("Secondary Line Length", left + 320, top + 185, -1);
                secondLengthSlider.draw(left + 340, top + 196);
                fr.drawStringWithShadow("Secondary Offset", left + 320, top + 209, -1);
                secondGapSlider.draw(left + 340, top + 220);
                if (movementBtn.isToggled() || aimBtn.isToggled()) {
                    fr.drawStringWithShadow("Dynamic Multiplier", left + 320, top + 233, -1);
                    multiplierSlider.draw(left + 340, top + 244);
                }

                fr.drawStringWithShadow("Thickness", left + 20, top + 190, -1);
                thickSlider.draw(left + 90, top + 190);
                fr.drawStringWithShadow("Line Length", left + 12, top + 210, -1);
                lengthSlider.draw(left + 90, top + 210);
                fr.drawStringWithShadow("Gap", left + 52, top + 230, -1);
                gapSlider.draw(left + 90, top + 230);
                fr.drawStringWithShadow("Dot Size", left + 30, top + 250, -1);
                dotSlider.draw(left + 90, top + 250);
                Gui.drawRect(left + 312, top + 170, left + 313, currentBottom - 50, -938208236);
            }

            if (crosshairType == 2) {
                goal = 1.18f;
                Gui.drawRect(left + 317, top + btnY, left + 459, top + btnY + 15, 2015042331);
                Iterator<Button> itr = customButtonsList.iterator();
                Gui.drawRect(left + 117, top + 142, left + 132, top + 157, 452984831);      // tint the middle one
                for (int i = 0; i < 15; i++) {
                    for (int i1 = 0; i1 < 15; i1++) {
                        Button btn = itr.next();
                        btn.draw(left + 12 + (i * 15), top + 37 + (i1 * 15));
                    }
                }
                fr.drawStringWithShadow("Custom Crosshair Creator", left + 246, top + 50, -603939256);
                Gui.drawRect(left + 246, top + 66, left + 501, top + 130, -15724528);
                fr.drawSplitString("- Click on a square to add color.", left + 250, top + 70,250, -1);
                fr.drawSplitString("- Click the same square again to remove it.", left + 250, top + 80, 250,-1);
                fr.drawSplitString("- You can click and drag to set multiple at once.", left + 250, top + 90, 250,-1);
                fr.drawSplitString("- Change the color in Color Options (below).", left + 250, top + 100, 250,-1);
                fr.drawSplitString("- Make sure to press 'Save Crosshair' when you are done.", left + 250, top + 110, 250,-1);
                saveCrossBtn.draw(left + 250, top + 150);
                clearCrossBtn.draw(left + 370, top + 150);
                if (saveCrossBtn.isClicked()) {
                    CustomCrosshair.write(customButtonsList);
                }
                if (clearCrossBtn.isClicked()) {
                    CustomCrosshair.clear(customButtonsList);
                }
            }

            renderColorOpts();
            renderCustomScaleOpts();
            renderInfoSystem();
            closeButton.draw(left + 3, top + 3);            // this is at the bottom as we always want to make sure we can press it
            if (debugBtn.isToggled()) {
                fr.drawString("F:" + ((float) (System.nanoTime() - startTime)) / 1000000f + "ms", left, currentBottom - 7, -1);     // TODO maybe do some more debug hud info??
            }

        }
        if (closeButton.isClicked()) {
            retract = true;
        }
        if (retract && percentOpenMain == 0f) {
            CrosshairV2.guiOpen = false;
            left = 0;
            right = 0;
            retract = false;
            infoBtn.setClickToggle(false);
            closeButton.setClicked(false);
            bar.setClicked(false);
            writeValues();
        }
    }

    private void renderInfoSystem() {
        infoBtn.draw(right - 12, top + 3);
        percentOpenSecond = easeOut(percentOpenSecond, infoBtn.isToggled() ? 1f : 0f);
        if (percentOpenSecond != 0f) {
            int currentRight = (int) (200 * percentOpenSecond);
            int currentTop = (int) (60 * percentOpenSecond);
            Gui.drawRect(right - currentRight, top - currentTop, right, top, baseColor);
        }
        if (percentOpenSecond > 0.98f) {
            fr.drawStringWithShadow("CrosshairV2 by W-OVERFLOW", right - 195, top - 56, -1);
            Gui.drawRect(right - 198, top - 44, right - 2, top - 45, -603939256);
            updateNow.draw(right - 70, top - 38);
            debugBtn.draw(right - 195, top - 13);
            showModUpdates.draw(right - 195, top - 38);
            if (updateNow.isToggled()) {
                updateNow.setClickToggle(false);
                CrosshairV2.config.update();
            }
        }
    }

    private void renderColorOpts() {
        fr.drawStringWithShadow("Color Options", left + 24, currentBottom - 50, -1);
        Gui.drawRect(left + 12, currentBottom - 45, left + 22, currentBottom - 46, -603939256);
        Gui.drawRect(left + 94, currentBottom - 45, right - 132, currentBottom - 46, -603939256);
        if (colorSelector.getSelectedItem() == 1) {
            dynamicColorSelector.draw(left + 12, currentBottom - 22);
        }
        if (colorSelector.getSelectedItem() == 0) {
            chromaBtn.draw(left + 12, currentBottom - 22);
        } else chromaBtn.setClickToggle(false);
        colorSelector.draw(left + 12, currentBottom - 38);
        Gui.drawRect(right - 169, currentBottom - 42, right - 132, currentBottom - 5, -15724528);
        if (colorSelector.getSelectedItem() != 2 && !chromaBtn.isToggled()) {
            fr.drawStringWithShadow("Red", left + 112, currentBottom - 35, Color.RED.getRGB());
            redSlider.draw(left + 110, currentBottom - 25);
            fr.drawStringWithShadow("Blue", left + 177, currentBottom - 35, 2830029);
            blueSlider.draw(left + 175, currentBottom - 25);
            fr.drawStringWithShadow("Green", left + 242, currentBottom - 35, Color.GREEN.getRGB());
            greenSlider.draw(left + 240, currentBottom - 25);
            fr.drawStringWithShadow("Alpha", left + 307, currentBottom - 35, -1);
            alphaSlider.draw(left + 305, currentBottom - 25);
        }
        if (colorSelector.getSelectedItem() == 1) {
            if (dynamicColorSelector.getSelectedItem() != currentColor) {
                switch (currentColor) {
                    default:
                    case 2:
                        CrosshairConfig.color = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                        break;
                    case 1:
                        CrosshairConfig.colorEntity = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                        break;
                    case 0:
                        CrosshairConfig.colorPlayer = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                        break;
                }
                CrosshairV2.config.markDirty();
                CrosshairV2.config.writeData();

                switch (dynamicColorSelector.getSelectedItem()) {
                    case 2:
                    default:
                        redSlider.setValue(CrosshairConfig.color.getRed() / 255f);
                        blueSlider.setValue(CrosshairConfig.color.getBlue() / 255f);
                        greenSlider.setValue(CrosshairConfig.color.getGreen() / 255f);
                        alphaSlider.setValue(CrosshairConfig.color.getAlpha() / 255f);
                        break;
                    case 1:
                        redSlider.setValue(CrosshairConfig.colorEntity.getRed() / 255f);
                        blueSlider.setValue(CrosshairConfig.colorEntity.getBlue() / 255f);
                        greenSlider.setValue(CrosshairConfig.colorEntity.getGreen() / 255f);
                        alphaSlider.setValue(CrosshairConfig.colorEntity.getAlpha() / 255f);
                        break;
                    case 0:
                        redSlider.setValue(CrosshairConfig.colorPlayer.getRed() / 255f);
                        blueSlider.setValue(CrosshairConfig.colorPlayer.getBlue() / 255f);
                        greenSlider.setValue(CrosshairConfig.colorPlayer.getGreen() / 255f);
                        alphaSlider.setValue(CrosshairConfig.colorPlayer.getAlpha() / 255f);
                        break;
                }
            }
            currentColor = dynamicColorSelector.getSelectedItem();
        }
        if (colorSelector.getSelectedItem() != 2 && !chromaBtn.isToggled()) {
            Gui.drawRect(right - 166, currentBottom - 39, right - 135, currentBottom - 8, new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue()).getRGB());
        } else if (colorSelector.getSelectedItem() == 0) {
            fr.drawStringWithShadow("Chroma Speed", left + 112, currentBottom - 35, -1);
            chromaSpeedSlider.draw(left + 110, currentBottom - 25);
            float val = chromaSpeedSlider.getValue() * 1000f;
            if (val == 0f) val = 760f;
            Gui.drawRect(right - 166, currentBottom - 39, right - 135, currentBottom - 8, Color.HSBtoRGB(System.currentTimeMillis() % (int) (val) / val, 0.8f, 0.8f));
        }
        if (colorSelector.getSelectedItem() == 2 && crosshairType == 0) {
            fr.drawSplitString("Sorry! A simple crosshair does not work with vanilla blending.", left + 120, currentBottom - 37, 200, -1);
        }
        if (colorSelector.getSelectedItem() != CrosshairConfig.colorType) {
            CrosshairConfig.colorType = colorSelector.getSelectedItem();
            CrosshairV2.config.markDirty();
            CrosshairV2.config.writeData();
        }
    }

    private void renderPreview() {
        fr.drawStringWithShadow("Preview", right - 128, top + 18, -1);
        scene1Btn.drawScaled(right - 130, top + 30, 120, 40);
        scene2Btn.drawScaled(right - 130, top + 72, 120, 40);
        scene3Btn.drawScaled(right - 130, top + 114, 120, 40);
        scene4Btn.drawScaled(right - 130, top + 156, 120, 40);
        if (scene1Btn.isClicked()) selectedPreview = 1;
        if (scene2Btn.isClicked()) selectedPreview = 2;
        if (scene3Btn.isClicked()) selectedPreview = 3;
        if (scene4Btn.isClicked()) selectedPreview = 4;

        GlStateManager.color(1f, 1f, 1f, 1f);
        mc.getTextureManager().bindTexture(frameLoc);
        Gui.drawModalRectWithCustomSizedTexture(right - 130, top + 30 + (selectedPreview * 42) - 42, 0, 0, 120, 40, 120, 40);
        ResourceLocation currentLoc;
        switch (selectedPreview) {
            default:
            case 1:
                currentLoc = scene1Loc;
                break;
            case 2:
                currentLoc = scene2Loc;
                break;
            case 3:
                currentLoc = scene3Loc;
                break;
            case 4:
                currentLoc = scene4Loc;
                break;
        }
        if (selectedPreview != CrosshairConfig.preview) {
            CrosshairConfig.preview = selectedPreview;
            CrosshairV2.config.markDirty();
            CrosshairV2.config.writeData();
        }
        mc.getTextureManager().bindTexture(currentLoc);
        Gui.drawScaledCustomSizeModalRect(left + 12, top + 20, 0, 0, 450, 142, 450, 142, 450, 142);
        Crosshairs.renderCrosshair(selectedCrosshair, left + 237, top + 91);
    }

    private void renderCustomScaleOpts() {
        fr.drawStringWithShadow("Custom Scale (Broken)", right - 123, currentBottom - 65, -1);
        Gui.drawRect(right - 126, currentBottom - 67, right - 127, currentBottom - 4, -938208236);
        Gui.drawRect(right - 125, currentBottom - 70, right - 4, currentBottom - 69, -938208236);
        smallScaleBtn.draw(right - 121, currentBottom - 55);
        normalScaleBtn.draw(right - 121, currentBottom - 42);
        largeScaleBtn.draw(right - 121, currentBottom - 29);
        autoScaleBtn.draw(right - 121, currentBottom - 16);
        if (smallScaleBtn.isClicked()) {        // TODO make this do something
            scale = 0;
            normalScaleBtn.setClickToggle(false);
            largeScaleBtn.setClickToggle(false);
            autoScaleBtn.setClickToggle(false);
        }
        if (normalScaleBtn.isClicked()) {
            scale = 1;
            smallScaleBtn.setClickToggle(false);
            largeScaleBtn.setClickToggle(false);
            autoScaleBtn.setClickToggle(false);
        }
        if (largeScaleBtn.isClicked()) {
            scale = 2;
            normalScaleBtn.setClickToggle(false);
            smallScaleBtn.setClickToggle(false);
            autoScaleBtn.setClickToggle(false);
        }
        if (autoScaleBtn.isClicked()) {
            scale = 3;
            normalScaleBtn.setClickToggle(false);
            largeScaleBtn.setClickToggle(false);
            smallScaleBtn.setClickToggle(false);
        }
        if (CrosshairConfig.scale != scale) {
            CrosshairConfig.scale = scale;
            CrosshairV2.config.markDirty();
            CrosshairV2.config.writeData();
        }
    }

    public void writeValues() {
        if (colorSelector.getSelectedItem() == 1) {
            switch (currentColor) {
                case 2:
                    CrosshairConfig.color = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                    break;
                case 1:
                    CrosshairConfig.colorEntity = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                    break;
                case 0:
                    CrosshairConfig.colorPlayer = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
                    break;
            }
        } else {
            CrosshairConfig.color = new Color(redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue(), alphaSlider.getValue());
        }
        CrosshairConfig.thickness = (int) thickSlider.getValue();
        CrosshairConfig.dotSize = (int) dotSlider.getValue();
        CrosshairConfig.gap = (int) gapSlider.getValue();
        CrosshairConfig.lineLength = (int) lengthSlider.getValue();
        CrosshairConfig.chromaSpeed = chromaSpeedSlider.getValue();
        CrosshairConfig.secondLineLength = (int) secondLengthSlider.getValue();
        CrosshairConfig.secondGap = (int) secondGapSlider.getValue();
        CrosshairConfig.multiplier = multiplierSlider.getValue();
        CrosshairConfig.dynamicMovement = movementBtn.isToggled();
        CrosshairConfig.dynamicAiming = aimBtn.isToggled();
        CrosshairConfig.showUpdate = showModUpdates.isToggled();
        CrosshairV2.config.markDirty();
        CrosshairV2.config.writeData();
    }

    private float easeOut(float current, float goal) {          // animation math (I really like this method)
        if (Math.floor(Math.abs(goal - current) / (float) 0.01) > 0) {
            return current + (goal - current) / (float) 20.0;
        } else {
            return goal;
        }
    }
}
