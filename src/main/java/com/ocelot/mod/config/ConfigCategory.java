package com.ocelot.mod.config;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;

public class ConfigCategory {

	public static List<ConfigCategory> configs = new ArrayList<ConfigCategory>();

	private String name;
	private Class clazz;

	public ConfigCategory(String name) {
		this(name, ModConfigGuiFactory.CategoryEntryDefault.class);
	}

	public ConfigCategory(String name, Class<? extends CategoryEntry> clazz) {
		this.name = name;
		this.clazz = clazz;
		configs.add(this);
	}

	public String getName() {
		return name;
	}

	public Class<? extends CategoryEntry> getClazz() {
		return clazz;
	}
}