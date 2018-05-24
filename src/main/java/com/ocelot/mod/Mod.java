package com.ocelot.mod;

import org.apache.logging.log4j.Logger;
import org.objectweb.asm.Type;

import com.mrcrayfish.device.api.ApplicationManager;
import com.mrcrayfish.device.api.print.PrintingManager;
import com.ocelot.mod.application.ApplicationGame;
import com.ocelot.mod.application.MarioPrint;
import com.ocelot.mod.config.ModConfig;
import com.ocelot.mod.game.core.entity.Entity;
import com.ocelot.mod.game.core.entity.summonable.FileSummonableEntity;
import com.ocelot.mod.game.core.entity.summonable.SummonableEntityRegistry;
import com.ocelot.mod.game.main.entity.Fruit;
import com.ocelot.mod.game.main.entity.enemy.Galoomba;
import com.ocelot.mod.game.main.entity.enemy.Koopa;
import com.ocelot.mod.game.main.entity.item.ItemCheese;
import com.ocelot.mod.game.main.entity.item.ItemCracker;
import com.ocelot.mod.game.main.entity.item.ItemCrayfish;
import com.ocelot.mod.game.main.entity.item.ItemKoopaShell;
import com.ocelot.mod.game.main.entity.item.ItemPSwitch;
import com.ocelot.mod.game.main.entity.player.Player;
import com.ocelot.mod.lib.Lib;
import com.ocelot.mod.registry.Registry;

import net.minecraft.launchwrapper.Launch;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
@net.minecraftforge.fml.common.Mod(modid = Mod.MOD_ID, version = Mod.VERSION, acceptedMinecraftVersions = "[1.12,1.12.9]", dependencies = "required-after:cdm@[0.4.0,)", guiFactory = "com.ocelot.mod.config.ModConfigGuiFactory", useMetadata = true)
public class Mod {

	/** The mod id */
	public static final String MOD_ID = "osmw";
	/** The current version of the mod */
	public static final String VERSION = "0.1.1";
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

		MinecraftForge.EVENT_BUS.register(new Registry());

		ModConfig.pre();
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			ModConfig.clientPre();
			Lib.pre();
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			registerGame();
		}
	}
	
	@EventHandler
	public void post(FMLPostInitializationEvent event) {
		if(FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			SummonableEntityRegistry.registerAllSummonables();
		}
	}

	/**
	 * Registers all the things required for the game to function.
	 */
	@SideOnly(Side.CLIENT)
	private static void registerGame() {
		SummonableEntityRegistry.registerClass(Fruit.class);
		SummonableEntityRegistry.registerClass(Player.class);
		SummonableEntityRegistry.registerClass(Koopa.class);
		SummonableEntityRegistry.registerClass(Galoomba.class);
		SummonableEntityRegistry.registerClass(ItemCheese.class);
		SummonableEntityRegistry.registerClass(ItemCracker.class);
		SummonableEntityRegistry.registerClass(ItemCrayfish.class);
		SummonableEntityRegistry.registerClass(ItemKoopaShell.class);
		SummonableEntityRegistry.registerClass(ItemPSwitch.class);
		
		ApplicationManager.registerApplication(GAME_ID, ApplicationGame.class);
		
		PrintingManager.registerPrint(new ResourceLocation(MOD_ID, "mario_screenshot"), MarioPrint.Print.class);
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