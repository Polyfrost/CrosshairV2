package com.nxtdelivery.crosshairv2.gui;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.config.CrosshairConfig;
import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.EssentialGUI;
import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIImage;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.*;
import gg.essential.elementa.constraints.animation.AnimatingConstraints;
import gg.essential.elementa.constraints.animation.Animations;
import gg.essential.universal.USound;
import gg.essential.vigilance.gui.VigilancePalette;
import kotlin.Unit;

import java.awt.*;

public class BackgroundScreen extends EssentialGUI {

    private boolean display = false;
    private boolean hovering = false;

    public BackgroundScreen() {
        super("CrosshairV2", EssentialAPI.getGuiUtil().getGuiScale(), false);
        UIBlock enabledButton = new UIBlock(Color.BLACK);
        enabledButton.setX(new SiblingConstraint(10));
        enabledButton.setY(new CenterConstraint());
        enabledButton.setWidth(new PixelConstraint(10));
        enabledButton.setHeight(new PixelConstraint(10));
        enabledButton.setChildOf(getTitleBar());

        enabledButton.onMouseEnter((a) -> {
            enabledButton.animateTo(new AnimatingConstraints(enabledButton, enabledButton.getConstraints()).setColorAnimation(Animations.OUT_EXP, 0.5f, new ConstantColorConstraint(new Color(70, 70, 70, 110))));
            return Unit.INSTANCE;
        });

        enabledButton.onMouseLeave((a) -> {
            enabledButton.animateTo(new AnimatingConstraints(enabledButton, enabledButton.getConstraints()).setColorAnimation(Animations.OUT_EXP, 0.5f, new ConstantColorConstraint(Color.BLACK)));
            return Unit.INSTANCE;
        });

        UIText enabledText = new UIText(CrosshairConfig.enabled ? "" : "< click me to enable!");
        enabledText.setX(new SiblingConstraint(10));
        enabledText.setY(new CenterConstraint());
        enabledText.setChildOf(getTitleBar());

        UIBlock enabled = new UIBlock(CrosshairConfig.enabled ? VigilancePalette.INSTANCE.getAccent() : new Color(0, 0, 0, 0));
        enabled.setX(new CenterConstraint());
        enabled.setY(new CenterConstraint());
        enabled.setWidth(new PixelConstraint(6));
        enabled.setHeight(new PixelConstraint(6));
        enabled.setChildOf(enabledButton);

        enabledButton.onMouseClick((a, f) -> {
            CrosshairConfig.enabled = !CrosshairConfig.enabled;
            CrosshairV2.config.markDirty();
            CrosshairV2.config.writeData();
            enabled.setColor(CrosshairConfig.enabled ? VigilancePalette.INSTANCE.getAccent() : new Color(0, 0, 0, 0));
            enabledText.setText(CrosshairConfig.enabled ? "" : "< click me to enable!");
            return Unit.INSTANCE;
        });

        UIContainer container = new UIContainer();
        container.setX(new RelativeConstraint(0.95F));
        container.setY(new CenterConstraint());
        container.setWidth(new PixelConstraint(15));
        container.setHeight(new PixelConstraint(15));
        container.setChildOf(getTitleBar());
        UIImage infoImage = UIImage.ofResourceCached("/assets/crosshairv2/textures/infobtn.png");
        infoImage.setX(new CenterConstraint());
        infoImage.setY(new CenterConstraint());
        infoImage.setWidth(new PixelConstraint(9));
        infoImage.setHeight(new PixelConstraint(9));
        infoImage.setChildOf(container);
        UIImage closeImage = UIImage.ofResourceCached("/assets/crosshairv2/textures/close.png");
        closeImage.setX(new CenterConstraint());
        closeImage.setY(new CenterConstraint());
        closeImage.setWidth(new PixelConstraint(9));
        closeImage.setHeight(new PixelConstraint(9));
        container.onMouseClick((a, b) -> {
            USound.INSTANCE.playButtonPress();
            display = !display;
            if (display) {
                container.replaceChild(closeImage, infoImage);
            } else {
                container.replaceChild(infoImage, closeImage);
            }
            return Unit.INSTANCE;
        });
        container.onMouseEnter((a) -> {
            hovering = true;
            (display ? closeImage : infoImage).animateTo(new AnimatingConstraints((display ? closeImage : infoImage), (display ? closeImage : infoImage).getConstraints()).setColorAnimation(Animations.OUT_EXP, 0.5f, new ConstantColorConstraint(VigilancePalette.INSTANCE.getAccent())));
            return Unit.INSTANCE;
        });
        container.onMouseLeave((a) -> {
            hovering = false;
            (display ? closeImage : infoImage).animateTo(new AnimatingConstraints((display ? closeImage : infoImage), (display ? closeImage : infoImage).getConstraints()).setColorAnimation(Animations.OUT_EXP, 0.5f, new ConstantColorConstraint(VigilancePalette.INSTANCE.getBrightText())));
            return Unit.INSTANCE;
        });
    }

    @Override
    public void onDrawScreen(int mouseX, int mouseY, float partialTicks) {
        super.onDrawScreen(mouseX, mouseY, partialTicks);
        CrosshairV2.gui.render(Math.round(getContent().getWidth()), Math.round(getContent().getHeight()) - 50, mouseX, mouseY, partialTicks, display, hovering);
    }

    @Override
    public void onScreenClose() {
        super.onScreenClose();
        CrosshairV2.gui.writeValuesAndClose();
    }
}
