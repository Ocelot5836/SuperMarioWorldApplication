package com.ocelot.mod.application;

import org.lwjgl.opengl.GL11;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.util.GLHelper;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * The main application class.
 * 
 * @author Ocelot5836
 */
public class ApplicationGame extends Application {

	private Layout layout;
	private GameTemplate game;

	private int oldMouseX, oldMouseY;

	public ApplicationGame() {
		game = new Game();
	}

	@Override
	public void init() {
		game.init();
		layout = new Layout(game.getWidth(), game.getHeight());
		setCurrentLayout(layout);
	}

	@Override
	public void onTick() {
		super.onTick();
		game.update();
		if (Game.isClosed()) {
			if (getActiveDialog() == null) {
				DialogMessage dialog = new DialogMessage("An exception was thrown that has caused the game to forcefully close. Please restart and notify the author if the problem continues.\n\nError: \n" + Game.getCloseInfo());
				openDialog(dialog);
			}
		}
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
		if (!Game.isClosed()) {
			game.onKeyPressed(code, character);
		}
	}

	@Override
	public void handleKeyReleased(char character, int code) {
		super.handleKeyReleased(character, code);
		if (!Game.isClosed()) {
			game.onKeyReleased(code, character);
		}
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean active, float partialTicks) {
		super.render(laptop, mc, x, y, mouseX, mouseY, active, partialTicks);

		if (mouseX < x || mouseX >= x + this.getWidth() || mouseY < y || mouseY >= y + this.getHeight()) {
			mouseX = oldMouseX;
			mouseY = oldMouseY;
		}

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GLHelper.scissor(x, y, getWidth(), getHeight());
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		game.render(laptop, mc, mouseX - x, mouseY - y, partialTicks);
		GlStateManager.popMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		if (mouseX != oldMouseX)
			oldMouseX = mouseX;
		if (mouseY != oldMouseY)
			oldMouseY = mouseY;
	}

	@Override
	public void load(NBTTagCompound nbt) {
		game.save(nbt);
	}

	@Override
	public void save(NBTTagCompound nbt) {
		game.load(nbt);
	}
}