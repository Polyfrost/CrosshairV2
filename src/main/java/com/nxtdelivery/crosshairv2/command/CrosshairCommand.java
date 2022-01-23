package com.nxtdelivery.crosshairv2.command;


import com.nxtdelivery.crosshairv2.CrosshairV2;
import com.nxtdelivery.crosshairv2.gui.BackgroundScreen;
import gg.essential.api.EssentialAPI;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class CrosshairCommand extends Command {
    public CrosshairCommand() {
        super(CrosshairV2.ID, true);
    }

    @DefaultHandler
    public void handle() {
        EssentialAPI.getGuiUtil().openScreen(new BackgroundScreen());
    }
}