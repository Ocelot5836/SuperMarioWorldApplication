package com.ocelot.mod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Handles the basic sounds.
 * 
 * @author Ocelot5836
 */
public class Sounds {

	public static SoundEvent MUSIC_NORMAL_OVERWORLD;
	public static SoundEvent MUSIC_FAST_OVERWORLD;

	public static SoundEvent TEST;
	
	public static SoundEvent PLAYER_JUMP;
	public static SoundEvent PLAYER_KICK;
	public static SoundEvent PLAYER_STOMP;
	public static SoundEvent PLAYER_STOMP_SPIN;

	public static SoundEvent KOOPA_SHELL_RICOCHET;
	
	public static SoundEvent COLLECT_ONE_UP;
	public static SoundEvent COLLECT_COIN;

	public static void init() {
		MUSIC_NORMAL_OVERWORLD = registerSound("music.normal.overworld");
		MUSIC_FAST_OVERWORLD = registerSound("music.fast.overworld");

		TEST = registerSound("test");
		
		PLAYER_JUMP = registerSound("entity.mario.jump");
		PLAYER_KICK = registerSound("entity.mario.kick");
		PLAYER_STOMP = registerSound("entity.mario.stomp");
		PLAYER_STOMP_SPIN = registerSound("entity.mario.stomp_spin");
		
		KOOPA_SHELL_RICOCHET = registerSound("entity.koopa.shell_ricochet");
		
		COLLECT_ONE_UP = registerSound("entity.item.collect.one_up");
		COLLECT_COIN = registerSound("entity.item.collect.coin");
	}
	
	private static SoundEvent registerSound(String soundName) {
		ResourceLocation soundID = new ResourceLocation(Mod.MOD_ID, soundName);
		SoundEvent event = new SoundEvent(soundID);
		ForgeRegistries.SOUND_EVENTS.register(event.setRegistryName(soundID));
		return event;
	}
}