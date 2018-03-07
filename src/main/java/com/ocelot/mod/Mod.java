package com.ocelot.mod;

import org.apache.logging.log4j.Logger;

import com.mrcrayfish.device.api.ApplicationManager;
import com.ocelot.api.utils.GuiUtils;
import com.ocelot.mod.application.ApplicationGame;

import net.minecraft.client.Minecraft;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The main mod class.
 * 
 * @author Ocelot5836
 */
@net.minecraftforge.fml.common.Mod(modid = Mod.MOD_ID, version = Mod.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", dependencies = "required-after:cdm@[0.2.1,)", useMetadata = true)
public class Mod {

	/** The mod id */
	public static final String MOD_ID = "osmw";
	/** The current version of the mod */
	public static final String VERSION = "0.0.2";
	/** The id for the game app */
	public static final ResourceLocation GAME_ID = new ResourceLocation(MOD_ID, "smw");

	/** The mod's instance. Probably not too useful but might as well have it */
	@Instance(MOD_ID)
	public static Mod instance;

	/** The mod's logger */
	private static Logger logger;

	@EventHandler
	public void pre(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ApplicationManager.registerApplication(GAME_ID, ApplicationGame.class);
	}

	/**
	 * @return A logger that uses the mod's id as the name
	 */
	public static Logger logger() {
		return logger;
	}
	

	/**
	 * @return Whether or not the mod is in debig mode
	 */
	public static boolean isDebug() {
		return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}
	
	/**
	 * @return Whether or not the user logged in on client side is MrCrayfish
	 */
	public static boolean isClientUserMrCrayfish() {
		return Usernames.MR_CRAYFISH.equalsIgnoreCase(Minecraft.getMinecraft().player.getName());
	}
	
	/**
	 * @return Whether or not the user logged in on client side is Ocelot5836
	 */
	public static boolean isClientUserOcelot5836() {
		return Usernames.OCELOT5836.equalsIgnoreCase(Minecraft.getMinecraft().player.getName());
	}
}