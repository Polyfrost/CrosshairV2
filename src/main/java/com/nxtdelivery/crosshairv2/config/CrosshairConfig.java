package com.nxtdelivery.crosshairv2.config;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.util.DownloadGui;
import com.nxtdelivery.crosshairv2.util.Updater;
import gg.essential.api.EssentialAPI;
import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.Property;
import gg.essential.vigilance.data.PropertyType;

import java.awt.*;
import java.io.File;

public class CrosshairConfig extends Vigilant {

    @Property(
            name = "preview", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int preview = 2;

    @Property(
            name = "crosshair", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int crosshair = 1;

    @Property(
            name = "colorType", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int colorType = 2;

    @Property(
            name = "scale", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int scale = 3;

    @Property(
            name = "crosshairType", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int crosshairType = 1;


    @Property(
            name = "thickness", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int thickness = 0;

    @Property(
            name = "length", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int lineLength = 4;

    @Property(
            name = "length2", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int secondLineLength = 2;

    @Property(
            name = "gap2", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int secondGap = 2;

    @Property(
            name = "multiplier", category = "h",
            type = PropertyType.DECIMAL_SLIDER,
            hidden = true
    )
    public static float multiplier = 1f;

    @Property(
            name = "gap", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int gap = 1;
    @Property(
            name = "dotSize", category = "h",
            type = PropertyType.NUMBER,
            hidden = true
    )
    public static int dotSize = 1;

    @Property(
            name = "chromaSpd", category = "h",
            type = PropertyType.DECIMAL_SLIDER,
            hidden = true
    )
    public static float chromaSpeed = 30f;


    @Property(
            name = "movement", category = "h",
            type = PropertyType.SWITCH,
            hidden = true
    )
    public static boolean dynamicMovement = false;

    @Property(
            name = "aiming", category = "h",
            type = PropertyType.SWITCH,
            hidden = true
    )
    public static boolean dynamicAiming = true;

    @Property(
            name = "chroma", category = "h",
            type = PropertyType.SWITCH,
            hidden = true
    )
    public static boolean chroma = false;


    @Property(
            type = PropertyType.COLOR,
            name = "color", category = "h",
            hidden = true
    )
    public static Color color = new Color(1f, 1f, 1f, 1f);

    @Property(
            type = PropertyType.COLOR,
            name = "colorEntity", category = "h",
            hidden = true
    )
    public static Color colorEntity = new Color(1f, 0f, 0f, 1f);

    @Property(
            type = PropertyType.COLOR,
            name = "colorPlayer", category = "h",
            hidden = true
    )
    public static Color colorPlayer = new Color(0f, 1f, 0f, 1f);


    @Property(
            type = PropertyType.SWITCH,
            name = "Show Update Notification",
            description = "Show a notification when you start Minecraft informing you of new updates.",
            category = "Updater"
    )
    public static boolean showUpdate = true;

    @Property(
            type = PropertyType.BUTTON,
            name = "Update Now",
            description = "Update by clicking the button.",
            category = "Updater"
    )
    public void update() {
        if (Updater.shouldUpdate) EssentialAPI.getGuiUtil()
                .openScreen(new DownloadGui());
        else EssentialAPI.getNotifications()
                .push(CrosshairV2.NAME, "No update had been detected at startup, and thus the update GUI has not been shown.");
    }

    public CrosshairConfig() {
        super(new File(CrosshairV2.modDir, CrosshairV2.ID + ".toml"), CrosshairV2.NAME);
        initialize();
    }
}
