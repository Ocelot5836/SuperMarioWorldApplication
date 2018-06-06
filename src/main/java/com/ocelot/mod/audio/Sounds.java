package com.ocelot.mod.audio;

import java.util.List;

import com.google.common.collect.Lists;
import com.ocelot.mod.Mod;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

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

	private static final List<SoundEvent> SOUNDS = Lists.<SoundEvent>newArrayList();

	public static final SoundEvent MUSIC_NORMAL_OVERWORLD;
	public static final SoundEvent MUSIC_FAST_OVERWORLD;

	public static final SoundEvent TEST;

	public static final SoundEvent PLAYER_JUMP;
	public static final SoundEvent PLAYER_KICK;
	public static final SoundEvent PLAYER_STOMP;
	public static final SoundEvent PLAYER_STOMP_SPIN;
	public static final SoundEvent PLAYER_SWIM;

	public static final SoundEvent KOOPA_SHELL_RICOCHET;

	public static final SoundEvent COLLECT_ONE_UP;
	public static final SoundEvent COLLECT_COIN;

	public static final SoundEvent SWITCH_ACTIVATE;
	public static final SoundEvent SWITCH_ENDING;
	
	public static final SoundEvent TILE_MESSAGE_HIT;

	static {
		MUSIC_NORMAL_OVERWORLD = registerSound("music.normal.overworld");
		MUSIC_FAST_OVERWORLD = registerSound("music.fast.overworld");

		TEST = registerSound("test");

		PLAYER_JUMP = registerSound("entity.mario.jump");
		PLAYER_KICK = registerSound("entity.mario.kick");
		PLAYER_STOMP = registerSound("entity.mario.stomp");
		PLAYER_STOMP_SPIN = registerSound("entity.mario.stomp_spin");
		PLAYER_SWIM = registerSound("entity.mario.swim");

		KOOPA_SHELL_RICOCHET = registerSound("entity.koopa.shell_ricochet");

		COLLECT_ONE_UP = registerSound("entity.item.collect.one_up");
		COLLECT_COIN = registerSound("entity.item.collect.coin");
		
		SWITCH_ACTIVATE = registerSound("entity.item.tswitch.activate");
		SWITCH_ENDING = registerSound("entity.item.tswitch.ending");
		
		TILE_MESSAGE_HIT = registerSound("tile.message.hit");
	}

	private static SoundEvent registerSound(String soundName) {
		ResourceLocation resource = new ResourceLocation(Mod.MOD_ID, soundName);
		SoundEvent sound = new SoundEvent(resource).setRegistryName(soundName);
		return sound;
	}

	public static SoundEvent[] getSounds() {
		return SOUNDS.toArray(new SoundEvent[0]);
	}
}