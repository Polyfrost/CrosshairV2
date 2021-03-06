package com.nxtdelivery.crosshairv2.util;

import com.nxtdelivery.crosshairv2.CrosshairV2;
import gg.essential.api.EssentialAPI;
import gg.essential.api.gui.ConfirmationModalBuilder;
import gg.essential.api.utils.Multithreading;
import gg.essential.elementa.WindowScreen;
import kotlin.Unit;

import java.io.File;

public class DownloadGui extends WindowScreen {
    public DownloadGui() {
        super(true, true, true, -1);
    }

    @Override
    public void initScreen(int width, int height) {
        super.initScreen(width, height);
        EssentialAPI.getEssentialComponentFactory().build(makeModal()).setChildOf(getWindow());
    }

    private ConfirmationModalBuilder makeModal() {
        ConfirmationModalBuilder builder = new ConfirmationModalBuilder();
        builder.setText("Are you sure you want to update?");
        builder.setSecondaryText("(This will update from v" + CrosshairV2.VER + " to " + Updater.latestTag + ")");
        builder.setOnConfirm((wyvest) -> {
            restorePreviousScreen();
            Multithreading.runAsync(() -> {
                if (Updater.download(
                        Updater.updateUrl,
                        new File(
                                "mods/" + CrosshairV2.NAME + "-" + Updater.latestTag.substring(Updater.latestTag.indexOf("v")) + ".jar"
                        )
                ) && Updater.download(
                        "https://github.com/Wyvest/Deleter/releases/download/v1.2/Deleter-1.2.jar",
                        new File(CrosshairV2.modDir.getParentFile(), "Deleter-1.2.jar")
                )
                ) {
                    EssentialAPI.getNotifications()
                            .push(CrosshairV2.NAME, "The ingame updater has successfully installed the newest version.");
                    Updater.addShutdownHook();
                    Updater.shouldUpdate = false;
                } else {
                    EssentialAPI.getNotifications().push(
                            CrosshairV2.NAME,
                            "The ingame updater has NOT installed the newest version as something went wrong."
                    );
                }
            });
            return Unit.INSTANCE;
        });

        builder.setOnDeny(() -> {
            restorePreviousScreen();
            return Unit.INSTANCE;
        });

        return builder;
    }
}