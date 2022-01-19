package com.nxtdelivery.crosshairv2.command;


import com.nxtdelivery.crosshairv2.CrosshairV2;
import gg.essential.api.commands.Command;
import gg.essential.api.commands.DefaultHandler;

public class CrosshairCommand extends Command {
    public CrosshairCommand() {
        super(CrosshairV2.ID, true);
    }

    @DefaultHandler
    public void handle() {
        CrosshairV2.guiOpen = true;
    }
}