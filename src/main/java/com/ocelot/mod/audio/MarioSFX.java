package com.ocelot.mod.audio;

import javax.annotation.Nullable;

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
public class MarioSFX implements ISound {

	protected Sound sound;
	@Nullable
	private SoundEventAccessor soundEvent;
	protected ResourceLocation soundLocation;
	protected float pitch;
	protected float volume;

	public MarioSFX(SoundEvent event, float pitch, float volume) {
		this(event.getSoundName(), pitch, volume);
	}

	public MarioSFX(ResourceLocation soundLocation, float pitch, float volume) {
		this.soundLocation = soundLocation;
		this.pitch = pitch;
		this.volume = volume;
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
		return false;
	}

	@Override
	public int getRepeatDelay() {
		return 0;
	}

	@Override
	public float getVolume() {
		return this.volume * this.sound.getVolume();
	}

	@Override
	public float getPitch() {
		return this.pitch * this.sound.getPitch();
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

	public MarioSFX setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public MarioSFX setVolume(float volume) {
		this.volume = volume;
		return this;
	}
}