package com.ocelot.mod.game.core.gameState;

import org.lwjgl.input.Keyboard;

import com.ocelot.mod.Mod;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.GameStateManager;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.game.core.gfx.Background;
import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.game.core.level.Level;
import com.ocelot.mod.game.main.entity.Player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

/***
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Used to test the game engine.
 * 
 * @author Ocelot5836
 */
public class TestState extends GameState {

	private Background bg;
	private Level level;
	private Player player;

	public TestState(GameStateManager gsm, GameTemplate game) {
		super(gsm, game);
	}

	@Override
	public void init() {
		bg = new Background(new Sprite(Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(Blocks.STONEBRICK, 1, 1)).getParticleTexture()), 0.1);
		level = new Level(16, new ResourceLocation(Mod.MOD_ID, "maps/test.map"));
		level.add(player = new Player(game));
		player.setPosition(50, 50);
	}

	@Override
	public void update() {
		level.update();

		level.getMap().setPosition(player.getX() - Game.WIDTH / 2, 0);
		bg.setPosition(level.getMap().getX(), 0);

		if (Mod.isDebug()) {
			if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				player.setY(player.getY() - 10);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				player.setY(player.getY() + 10);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				player.setX(player.getX() - 10);
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				player.setX(player.getX() + 10);
			}
		}
	}

	@Override
	public void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		bg.render();
		level.render(gui, mc, mouseX, mouseY, partialTicks);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar) {
		level.onKeyPressed(keyCode, typedChar);
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar) {
		level.onKeyReleased(keyCode, typedChar);
	}

	@Override
	public void onLoseFocus() {
		player.onLoseFocus();
	}
}