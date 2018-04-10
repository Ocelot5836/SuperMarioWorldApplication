package com.ocelot.mod.audio;

import javax.annotation.Nullable;

import com.ocelot.mod.config.ModConfig;

import net.minecraft.client.Minecraft;
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

	protected boolean stopped = false;
	
	protected Sound sound;
	@Nullable
	private SoundEventAccessor soundEvent;
	protected SoundCategory category;
	protected ResourceLocation soundLocation;
	protected float pitch;
	protected int repeatDelay;

	public MarioMusic(SoundEvent event) {
		this(event.getSoundName());
	}

	public MarioMusic(ResourceLocation soundLocation) {
		this.pitch = 1.0F;
		this.soundLocation = soundLocation;
		this.category = SoundCategory.PLAYERS;
	}

	public ResourceLocation getSoundLocation() {
		return this.soundLocation;
	}

	public SoundEventAccessor createAccessor(SoundHandler handler) {
		this.soundEvent = handler.getAccessor(this.soundLocation);

		if (this.soundEvent == null) {
			this.sound = SoundHandler.MISSING_SOUND;
		} else {
			this.sound = this.soundEvent.cloneEntry();
		}

		return this.soundEvent;
	}

	public Sound getSound() {
		return this.sound;
	}

	public SoundCategory getCategory() {
		return this.category;
	}

	public boolean canRepeat() {
		return !this.stopped;
	}

	public int getRepeatDelay() {
		return this.repeatDelay;
	}

	public float getVolume() {
		return (float) ModConfig.marioMusicVolume * this.sound.getVolume();
	}

	public float getPitch() {
		return this.pitch * this.sound.getPitch();
	}

	public float getXPosF() {
		return (float) Minecraft.getMinecraft().player.posX;
	}

	public float getYPosF() {
		return (float) Minecraft.getMinecraft().player.posY;
	}

	public float getZPosF() {
		return (float) Minecraft.getMinecraft().player.posZ;
	}

	public ISound.AttenuationType getAttenuationType() {
		return ISound.AttenuationType.LINEAR;
	}
	
	public MarioMusic setPitch(float pitch) {
		this.pitch = pitch;
		return this;
	}

	public void stop() {
		this.stopped = true;
	}
}