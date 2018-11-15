package com.ocelot.api.mod;

import java.util.List;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.ocelot.mod.SuperMarioWorld;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

/**
 * Loads all maps into the game for the Super Mario World app to identify.
 * 
 * @author Ocelot5836
 */
public class SuperMarioWorldModLoader {

	private static final List<MarioPlugin> MOD_LIST = Lists.<MarioPlugin>newArrayList();

	/**
	 * Searches the data table and loads all classes with the {@link SMWMod} annotation to a list.
	 * 
	 * @param table
	 *            The data that is used to detect the mods
	 */
	public static void load(ASMDataTable table) {
		MOD_LIST.clear();
		table.getAll(SMWMod.class.getCanonicalName()).forEach((entry) -> {
			try {
				Class clazz = Class.forName(entry.getClassName());
				Object obj = clazz.newInstance();
				if (obj instanceof MarioPlugin) {
					MarioPlugin plugin = (MarioPlugin) obj;
					MOD_LIST.add(plugin);
					SuperMarioWorld.logger().info("Loaded mod \'" + plugin.getClass().getName() + "\'");
				} else {
					throw new RuntimeException("Class " + clazz.getName() + " does not implement MarioPlugin!");
				}
			} catch (ClassNotFoundException e) {
				SuperMarioWorld.logger().warn("Could not find class \'" + entry.getClassName() + "\'", e);
			} catch (InstantiationException e) {
				SuperMarioWorld.logger().warn("Could not instantiate class \'" + entry.getClassName() + "\'. Make sure you have an empty constructor!", e);
			} catch (IllegalAccessException e) {
				SuperMarioWorld.logger().warn("Could not access class \'" + entry.getClassName() + "\'", e);
			}
		});
		SuperMarioWorld.logger().info("Loaded " + MOD_LIST.size() + " mod(s)");
	}

	/**
	 * @return All the mods that were loaded and can be currently activated
	 */
	public static Stream<MarioPlugin> getModList() {
		return MOD_LIST.stream();
	}
}