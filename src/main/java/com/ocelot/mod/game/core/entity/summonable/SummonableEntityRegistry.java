package com.ocelot.mod.game.core.entity.summonable;

import java.util.Map;

import com.google.common.collect.Maps;
import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.Game;

import net.minecraftforge.fml.common.discovery.ASMDataTable;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Handles all the code that has to deal with summoning entities from file.
 * 
 * @author Ocelot5836
 */
public class SummonableEntityRegistry {

	private static Map<String, IFileSummonable> summonables = Maps.<String, IFileSummonable>newHashMap();

	/**
	 * Sets the classes to be registered.
	 * 
	 * @param table
	 *            The table to get the data from
	 */
	public static void init(ASMDataTable table) {
		table.getAll(FileSummonableEntity.class.getCanonicalName()).forEach((entity) -> {
			try {
				Class clazz = Class.forName(entity.getClassName());
				if (clazz.isAnnotationPresent(FileSummonableEntity.class)) {
					FileSummonableEntity fse = (FileSummonableEntity) clazz.getAnnotation(FileSummonableEntity.class);
					Class<? extends IFileSummonable> summonableClazz = fse.value();
					try {
						IFileSummonable summonable = summonableClazz.newInstance();
						registerSummonable(summonable.getRegistryName(), summonable);
					} catch (InstantiationException e) {
						throw new RuntimeException("Class " + summonableClazz.getName() + " is missing a default contructor");
					} catch (Exception e) {
						SuperMarioWorld.logger().catching(e);
					}
				} else {
					throw new RuntimeException("Class " + clazz.getName() + " does not have the @FileSummonableEntity annotation");
				}
			} catch (ClassNotFoundException e) {
				SuperMarioWorld.logger().warn("Could not find class \'" + entity.getClassName() + "\'", e);
			}
		});
	}

	/**
	 * Registers a summonable entity from file.
	 * 
	 * @param id
	 *            The id
	 * @param summonable
	 *            The entity that can be summoned
	 */
	private static void registerSummonable(String id, IFileSummonable summonable) {
		if (!summonables.containsKey(id)) {
			summonables.put(id, summonable);
		} else {
			Game.stop(new RuntimeException("Tried to register summonable with id \'" + id + "\' over an already existing one!"), "Could not register summonables.");
		}
	}

	/**
	 * @param key
	 *            The id of the summonable to get
	 * @return The summonable
	 */
	public static IFileSummonable getSummonable(String key) {
		return summonables.get(key);
	}
}