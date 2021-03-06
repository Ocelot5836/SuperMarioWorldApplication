package com.ocelot.mod;

import org.apache.logging.log4j.Logger;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.print.PrintingManager;
import com.mrcrayfish.device.core.Laptop;
import com.ocelot.api.mod.SuperMarioWorldModLoader;
import com.ocelot.mod.application.ApplicationGame;
import com.ocelot.mod.application.MarioPrint;
import com.ocelot.mod.config.ModConfig;
import com.ocelot.mod.game.core.entity.summonable.SummonableEntityRegistry;
import com.ocelot.mod.game.main.gamestate.store.ImageShopItem;
import com.ocelot.mod.game.main.gamestate.store.ShopItemRegistry;
import com.ocelot.mod.lib.Lib;
import com.ocelot.mod.registry.Registry;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
@Mod(modid = SuperMarioWorld.MOD_ID, version = SuperMarioWorld.VERSION, acceptedMinecraftVersions = "[1.12,1.12.2]", guiFactory = "com.ocelot.mod.config.ModConfigGuiFactory", useMetadata = true)
public class SuperMarioWorld {

	/** The mod id */
	public static final String MOD_ID = "osmw";
	/** The current version of the mod */
	public static final String VERSION = "0.1.2";
	/** The id for the game app */
	public static final ResourceLocation GAME_ID = new ResourceLocation(MOD_ID, "smw");

	/** The mod's instance. Probably not too useful but might as well have it */
	@Instance(MOD_ID)
	public static SuperMarioWorld instance;

	/** The mod's logger */
	private static Logger logger;

	@EventHandler
	public static void pre(FMLPreInitializationEvent event) {
		logger = event.getModLog();

		MinecraftForge.EVENT_BUS.register(new Registry());

		ModConfig.pre();
		SuperMarioWorldModLoader.load(event.getAsmData());
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			SummonableEntityRegistry.init(event.getAsmData());
			ModConfig.clientPre();
			Lib.pre();
		}
	}

	@EventHandler
	public static void init(FMLInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			registerGame();
		}
	}

	@EventHandler
	public static void post(FMLPostInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			registerShopItems();
		}
	}

	/**
	 * Registers all the things required for the game to function.
	 */
	@SideOnly(Side.CLIENT)
	private static void registerGame() {
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_green_hills.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_snow_hills.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_jungle_vines.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_mushroom_mountains.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_green_mountain_tops.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_white_mountains.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_green_mountains.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_castle.png"));

		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_caves.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_icy_caves.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_underwater.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_ghost_house.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_castle_alt.png"));
		Laptop.addWallpaper(new ResourceLocation(SuperMarioWorld.MOD_ID, "textures/app/laptop_wallpaper_mario_starry_night.png"));

		ApplicationManager.registerApplication(GAME_ID, ApplicationGame.class);

		PrintingManager.registerPrint(new ResourceLocation(MOD_ID, "mario_screenshot"), MarioPrint.Print.class);

		SuperMarioWorldModLoader.getModList().forEach((entry) -> entry.register());
	}

	/**
	 * Registers all the shop items.
	 */
	@SideOnly(Side.CLIENT)
	private static void registerShopItems() {
		ShopItemRegistry.register(new ImageShopItem(ImageShopItem.SHOP_IMAGES.getSubimage(0, 0, 16, 16), "Mario", 5));
		ShopItemRegistry.register(new ImageShopItem(ImageShopItem.SHOP_IMAGES.getSubimage(16, 0, 16, 16), "Luigi", 5));
		ShopItemRegistry.register(new ImageShopItem(ImageShopItem.SHOP_IMAGES.getSubimage(0, 16, 16, 16), "Mushroom", 10));
		ShopItemRegistry.register(new ImageShopItem(ImageShopItem.SHOP_IMAGES.getSubimage(16, 16, 16, 16), "1-UP", 15));
	}

	/**
	 * @return A logger that uses the mod id as the name
	 */
	public static Logger logger() {
		return logger;
	}

	/**
	 * @return Whether or not the mod is in a deobfuscated environment
	 */
	public static boolean isDebug() {
		return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	}
}