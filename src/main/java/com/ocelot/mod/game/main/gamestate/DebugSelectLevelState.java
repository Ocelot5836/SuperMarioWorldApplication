package com.ocelot.mod.game.main.gamestate;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.ocelot.mod.SuperMarioWorld;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gameState.GameState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.resources.I18n;

public class DebugSelectLevelState extends GameState {

	private List<String> levels;

	public DebugSelectLevelState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void load() {
		levels = new ArrayList<String>();

		for (Entry<String, Class<? extends GameState>> entry : this.getGsm().getGameStates()) {
			if (entry.getValue().isAnnotationPresent(DebugSelectStateLevel.class)) {
				levels.add(entry.getKey());
			}
		}
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(3, 12, 3 + Game.WIDTH - 6, 12 + Game.HEIGHT - 16, Color.DARK_GRAY.getRGB());
		mc.fontRenderer.drawString(I18n.format("state." + SuperMarioWorld.MOD_ID + ".select.selectlevel"), Game.WIDTH / 2 - mc.fontRenderer.getStringWidth(I18n.format("state." + SuperMarioWorld.MOD_ID + ".select.selectlevel")) / 2, 18, 0xffffff);
		for (int i = 0; i < this.levels.size(); i++) {
			String registryName = this.levels.get(i);
			int x = 5 + (i / 40) * 32;
			int y = 30 + (i % 40) * 12;
			mc.fontRenderer.drawString(registryName, x, y, mouseX >= x - 1 && mouseX < x + mc.fontRenderer.getStringWidth(registryName) + 1 && mouseY >= y - 1 && mouseY < y + mc.fontRenderer.FONT_HEIGHT + 1 ? 0xff7700 : 0xffffff, false);
		}
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
	}

	@Override
	public void onMousePressed(int mouseButton, int mouseX, int mouseY) {
		if (mouseButton == 0) {
			for (int i = 0; i < this.levels.size(); i++) {
				String registryName = this.levels.get(i);
				int x = 5 + (i / 40) * 32;
				int y = 30 + (i % 40) * 12;
				if (mouseX >= x - 1 && mouseX < x + Minecraft.getMinecraft().fontRenderer.getStringWidth(registryName) + 1 && mouseY >= y - 1 && mouseY < y + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT + 1) {
					this.getGsm().setState(registryName);
					break;
				}
			}
		}
	}
}