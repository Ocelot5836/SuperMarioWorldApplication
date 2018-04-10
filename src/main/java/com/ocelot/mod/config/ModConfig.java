package com.ocelot.mod.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ocelot.mod.Mod;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.NumberSliderEntry;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Handles the configuration.
 * 
 * @author Ocelot5836
 */
public class ModConfig {

	public static final String CATEGORY_NAME_SOUNDS = "sounds";

	public static final ConfigCategory CATEGORY_SOUNDS = new ConfigCategory(CATEGORY_NAME_SOUNDS);

	private static Configuration config = null;

	public static boolean enableMarioMusic;
	public static boolean enableMarioSFX;
	public static double marioMusicVolume;
	public static double marioSFXVolume;

	/**
	 * Initializes the config.
	 */
	public static void preInit() {
		File configFile = new File(Loader.instance().getConfigDir(), Mod.MOD_ID + ".cfg");
		config = new Configuration(configFile);
		syncFromFiles();
	}

	/**
	 * Initializes the client config.
	 */
	public static void clientPreInit() {
		MinecraftForge.EVENT_BUS.register(new ConfigEventHandler());
	}

	/**
	 * Syncs the config to the files.
	 */
	public static void syncFromFiles() {
		syncConfig(true, true);
	}

	/**
	 * Syncs the config to the gui.
	 */
	public static void syncFromGui() {
		syncConfig(false, true);
	}

	/**
	 * Syncs the config from the fields in the gui.
	 */
	public static void syncFromFields() {
		syncConfig(false, false);
	}

	/**
	 * Syncs the config.
	 */
	private static void syncConfig(boolean loadFromConfigFile, boolean readFieldsFromConfig) {
		if (loadFromConfigFile) {
			config.load();
		}

		Property propertyEnableMarioMusic = config.get(CATEGORY_NAME_SOUNDS, "enable_mario_music", FactorySettings.ENABLE_MARIO_MUSIC);
		propertyEnableMarioMusic.setLanguageKey("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".enable_mario_music");
		propertyEnableMarioMusic.setComment(I18n.format("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".enable_mario_music.comment"));

		Property propertyEnableMarioSFX = config.get(CATEGORY_NAME_SOUNDS, "enable_mario_sfx", FactorySettings.ENABLE_MARIO_SFX);
		propertyEnableMarioSFX.setLanguageKey("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".enable_mario_sfx");
		propertyEnableMarioSFX.setComment(I18n.format("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".enable_mario_sfx.comment"));

		Property propertyMarioMusicVolume = config.get(CATEGORY_NAME_SOUNDS, "mario_music_volume", FactorySettings.MARIO_MUSIC_VOLUME_MAX * 100.0).setConfigEntryClass(NumberSliderEntry.class);
		propertyMarioMusicVolume.setMinValue(FactorySettings.MARIO_MUSIC_VOLUME_MIN);
		propertyMarioMusicVolume.setMaxValue(FactorySettings.MARIO_MUSIC_VOLUME_MAX * 100.0);
		propertyMarioMusicVolume.setLanguageKey("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".mario_music_volume");
		propertyMarioMusicVolume.setComment(I18n.format("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".mario_music_volume.comment"));

		Property propertyMarioSFXVolume = config.get(CATEGORY_NAME_SOUNDS, "mario_sfx_volume", FactorySettings.MARIO_SFX_VOLUME_MAX * 100.0).setConfigEntryClass(NumberSliderEntry.class);
		propertyMarioSFXVolume.setMinValue(FactorySettings.MARIO_SFX_VOLUME_MIN);
		propertyMarioSFXVolume.setMaxValue(FactorySettings.MARIO_SFX_VOLUME_MAX * 100.0);
		propertyMarioSFXVolume.setLanguageKey("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".mario_sfx_volume");
		propertyMarioSFXVolume.setComment(I18n.format("gui." + Mod.MOD_ID + ".config." + CATEGORY_NAME_SOUNDS + ".mario_sfx_volume.comment"));

		List<String> proeprtyOrderSounds = new ArrayList<String>();
		proeprtyOrderSounds.add(propertyEnableMarioMusic.getName());
		proeprtyOrderSounds.add(propertyEnableMarioSFX.getName());
		proeprtyOrderSounds.add(propertyMarioMusicVolume.getName());
		proeprtyOrderSounds.add(propertyMarioSFXVolume.getName());
		config.setCategoryPropertyOrder(CATEGORY_NAME_SOUNDS, proeprtyOrderSounds);

		if (readFieldsFromConfig) {
			enableMarioMusic = propertyEnableMarioMusic.getBoolean();
			enableMarioSFX = propertyEnableMarioSFX.getBoolean();
			marioMusicVolume = propertyMarioMusicVolume.getDouble() / 100.0;
			marioSFXVolume = propertyMarioSFXVolume.getDouble() / 100.0;
		}

		propertyEnableMarioMusic.set(enableMarioMusic);
		propertyEnableMarioSFX.set(enableMarioSFX);
		propertyMarioMusicVolume.set(Math.round(marioMusicVolume * 100.0));
		propertyMarioSFXVolume.set(Math.round(marioSFXVolume * 100.0));

		if (config.hasChanged()) {
			config.save();
		}
	}

	/**
	 * @return The configuration instance
	 */
	public static Configuration getConfig() {
		return config;
	}

	private static class ConfigEventHandler {
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public void onConfigChnagedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Mod.MOD_ID)) {
				syncFromGui();
			}
		}
	}
}