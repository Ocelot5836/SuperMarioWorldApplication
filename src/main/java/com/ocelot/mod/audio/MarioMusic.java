package com.ocelot.mod.audio;

import javax.annotation.Nullable;

import com.ocelot.mod.config.ModConfig;
import com.ocelot.mod.lib.MarioCollisionHelper;

import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.Sound;
import net.minecraft.client.audio.SoundEventAccessor;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class MarioMusic implements ISound {

	private boolean stopped;
	private int repeatDelay;
	
	protected Sound sound;
	@Nullable
	private SoundEventAccessor soundEvent;
	protected ResourceLocation soundLocation;

	public MarioMusic(SoundEvent event, int repeatDelay) {
		this(event.getSoundName(), repeatDelay);
	}

	public MarioMusic(ResourceLocation soundLocation, int repeatDelay) {
		this.soundLocation = soundLocation;
		this.repeatDelay = repeatDelay;
	}

	@Override
	public ResourceLocation getSoundLocation() {
		return this.soundLocation;
	}

	@Override
	public SoundEventAccessor createAccessor(SoundHandler handler) {
		this.soundEvent = handler.getAccessor(this.soundLocation);

		if (this.soundEvent == null) {
			this.sound = SoundHandler.MISSING_SOUND;
		} else {
			this.sound = this.soundEvent.cloneEntry();
		}

		return this.soundEvent;
	}

	@Override
	public Sound getSound() {
		return this.sound;
	}

	@Override
	public SoundCategory getCategory() {
		return SoundCategory.NEUTRAL;
	}

	@Override
	public boolean canRepeat() {
		return !stopped;
	}

	@Override
	public int getRepeatDelay() {
		return repeatDelay;
	}

	@Override
	public float getVolume() {
		return (float) ModConfig.marioMusicVolume * this.sound.getVolume();
	}

	@Override
	public float getPitch() {
		return this.sound.getPitch();
	}

	@Override
	public float getXPosF() {
		return 0;
	}

	@Override
	public float getYPosF() {
		return 0;
	}

	@Override
	public float getZPosF() {
		return 0;
	}

	@Override
	public ISound.AttenuationType getAttenuationType() {
		return ISound.AttenuationType.NONE;
	}
	
	public void stop() {
		this.stopped = true;
	}
}