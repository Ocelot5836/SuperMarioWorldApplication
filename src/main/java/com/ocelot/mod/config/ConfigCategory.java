package com.ocelot.mod.config;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;

public class ConfigCategory {

	public static List<ConfigCategory> configs = Lists.<ConfigCategory>newArrayList();

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