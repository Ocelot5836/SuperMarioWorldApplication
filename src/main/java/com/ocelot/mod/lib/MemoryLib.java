package com.ocelot.mod.lib;

import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * This class is here to store any previous instances of objects so they can be recalled and reused. Helps with preventing memory leaks.
 * 
 * @author Ocelot5836
 */
public class MemoryLib {

	public static final Map<String, Comparator> GET_NEAREST_ENTITY_ENTITY = Maps.<String, Comparator>newHashMap();
	public static final Map<String, Comparator> GET_NEAREST_ENTITY_POINT = Maps.<String, Comparator>newHashMap();
	public static final Map<String, Comparator> GET_NEAREST_PLAYER_ENTITY = Maps.<String, Comparator>newHashMap();
	public static final Map<String, Comparator> GET_NEAREST_PLAYER_POINT = Maps.<String, Comparator>newHashMap();

	public static final Map<ResourceLocation, BufferedImage> LOADED_IMAGES = Maps.<ResourceLocation, BufferedImage>newHashMap();

	public static final Map<String, BufferedImage> COLORIZER_REPLACE_BUFFERED_PIXELS = Maps.<String, BufferedImage>newHashMap();

	/**
	 * Clears all the caches to help with memory
	 */
	public static void clear() {
		GET_NEAREST_ENTITY_ENTITY.clear();
		GET_NEAREST_ENTITY_POINT.clear();
		GET_NEAREST_PLAYER_ENTITY.clear();
		GET_NEAREST_PLAYER_POINT.clear();

		LOADED_IMAGES.clear();

		COLORIZER_REPLACE_BUFFERED_PIXELS.clear();
	}
}