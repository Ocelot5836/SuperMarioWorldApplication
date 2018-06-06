package com.ocelot.mod.game;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.gui.MarioGui;
import com.ocelot.mod.game.core.save.SaveFileManager;
import com.ocelot.mod.game.main.MarioFontRenderer;
import com.ocelot.mod.game.main.entity.player.PlayerProperties;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;

public class Game extends GameTemplate {

	public static final int WIDTH = 256;
	public static final int HEIGHT = 150;

	private GameStateManager gsm;
	private SaveFileManager saveFileManager;
	private PlayerProperties playerProperties;
	private MarioFontRenderer fontRenderer;

	public MarioGui currentDisplayedGui;

	public Game() {
		super(WIDTH, HEIGHT);
	}

	@Override
	public void init() {
		this.gsm = new GameStateManager(this);
		this.saveFileManager = new SaveFileManager(this);
		this.playerProperties = new PlayerProperties(this);
		// this.fontRenderer = new MarioFontRenderer(fontSheet, charFileLocation, fontFileLocation);

		this.currentDisplayedGui = null;
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
			gui.drawCenteredString(mc.fontRenderer, I18n.format("exception." + Mod.MOD_ID + ".thrown"), WIDTH / 2, HEIGHT / 2 - 8, 0xffdd0000);
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
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		gsm.onMousePressed(mouseButton, mouseX, mouseY);
	}

	@Override
	public void load(NBTTagCompound nbt) {
		this.saveFileManager.deserializeNBT(nbt.getCompoundTag("saveFiles"));

		gsm.load(this.saveFileManager.getSaveCompound());
		this.playerProperties.deserializeNBT(this.saveFileManager.load("playerProperties"));
	}

	@Override
	public void save(NBTTagCompound nbt) {
		gsm.save(this.saveFileManager.getSaveCompound());
		this.saveFileManager.save("playerProperties", this.playerProperties.serializeNBT());

		nbt.setTag("saveFiles", this.saveFileManager.serializeNBT());
	}

	@Override
	public void onClose() {
		gsm.unloadState();
	}

	public PlayerProperties getPlayerProperties() {
		return playerProperties;
	}

	public SaveFileManager getSaveFileManager() {
		return saveFileManager;
	}
}