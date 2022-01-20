package com.nxtdelivery.crosshairv2;

import com.nxtdelivery.crosshairv2.command.CrosshairCommand;
import com.nxtdelivery.crosshairv2.config.CrosshairConfig;
import com.nxtdelivery.crosshairv2.crosshairs.Crosshairs;
import com.nxtdelivery.crosshairv2.gui.GuiMain;
import com.nxtdelivery.crosshairv2.util.Updater;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;

@Mod(modid = CrosshairV2.ID, name = CrosshairV2.NAME, version = CrosshairV2.VER)
public class CrosshairV2 {
    public static final String NAME = "@NAME@", VER = "@VER@", ID = "@ID@";
    public static File jarFile;
    public static final File modDir = new File(new File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME);
    public static CrosshairConfig config;
    private final Minecraft mc = Minecraft.getMinecraft();
    public static GuiMain gui;
    public static boolean guiOpen = false;

    @Mod.EventHandler
    protected void onFMLPreInitialization(FMLPreInitializationEvent event) {
        if (!modDir.exists()) modDir.mkdirs();
        jarFile = event.getSourceFile();
    }

    @Mod.EventHandler
    protected void onInitialization(FMLInitializationEvent event) {
        new CrosshairCommand().register();
        config = new CrosshairConfig();
        config.preload();
        gui = new GuiMain();
        Updater.update();
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRenderEvent(RenderGameOverlayEvent event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CROSSHAIRS) {
            if ((!CrosshairConfig.renderInF5 && mc.gameSettings.thirdPersonView != 0) || (!CrosshairConfig.renderOnGuis && mc.currentScreen != null)) {
                event.setCanceled(true);
                return;
            }
            Crosshairs.render();
            event.setCanceled(true);
        }
    }
}
