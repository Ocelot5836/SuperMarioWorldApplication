package com.ocelot.mod.game;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.main.entity.player.PlayerProperties;
import com.ocelot.mod.game.main.gui.GuiOverlay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.nbt.NBTTagCompound;

public class Game extends GameTemplate {

	public static final int WIDTH = 256;
	public static final int HEIGHT = 150;

	private GameStateManager gsm;
	private PlayerProperties playerProperties;
	public MarioGui currentDisplayedGui;

	public Game() {
		super(WIDTH, HEIGHT);
		playerProperties = new PlayerProperties(this);
	}

	@Override
	public void init() {
		gsm = new GameStateManager(this);
		currentDisplayedGui = null;
	}

	@Override
	public void update() {
		if (!closed) {
			gsm.update();
			if (currentDisplayedGui != null) {
				currentDisplayedGui.update();
				gsm.onLoseFocus();
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (!closed) {
			gsm.render(gui, mc, mouseX, mouseY, partialTicks);
			if (currentDisplayedGui != null)
				currentDisplayedGui.render(gui, mouseX, mouseY, partialTicks);
			if (Mod.isDebug()) {
				gui.drawString(mc.fontRenderer, "Current: " + gsm.getSelectedState().toString(), 2, 2, 0xffffffff);
			}
		} else {
			gui.drawCenteredString(mc.fontRenderer, "An exception was thrown!", WIDTH / 2, HEIGHT / 2 - 8, 0xffdd0000);
		}
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		gsm.onKeyPressed(keyCode, typedChar);
		if (currentDisplayedGui != null)
			currentDisplayedGui.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		gsm.onKeyReleased(keyCode, typedChar);
		if (currentDisplayedGui != null)
			currentDisplayedGui.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void load(NBTTagCompound nbt) {
		gsm.load(nbt);
		this.playerProperties.deserializeNBT(nbt.getCompoundTag("playerProperties"));
	}

	@Override
	public void save(NBTTagCompound nbt) {
		gsm.save(nbt);
		nbt.setTag("playerProperties", this.playerProperties.serializeNBT());
	}

	public PlayerProperties getPlayerProperties() {
		return playerProperties;
	}
}