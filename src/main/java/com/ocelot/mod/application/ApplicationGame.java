package com.ocelot.mod.application;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.core.Laptop;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.lib.MemoryLib;
import com.ocelot.mod.lib.RenderHelper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

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

	@Override
	public void init() {
		game = new Game();
		game.init();
		layout = new Layout(game.getWidth(), game.getHeight());
		setCurrentLayout(layout);
	}

	@Override
	public void onTick() {
		super.onTick();
//		if (getActiveDialog() == null) {
//			if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
//				MarioPrint.printScreenshot(this, this.getCurrentLayout().xPosition, this.getCurrentLayout().yPosition, this.getCurrentLayout().width, this.getCurrentLayout().height);
//			}
//		}

		try {
			game.update();
		} catch (Throwable e) {
			Game.stop(e, "Error while updating");
		}
		if (Game.isClosed()) {
			if (getActiveDialog() == null) {
				DialogCrashLog dialog = new DialogCrashLog(I18n.format("exception." + Mod.MOD_ID + ".dialog", Game.getCloseInfo()));
				dialog.setPositiveListener((mouseButton, mouseX, mouseY) -> {
					this.init();
				});
				openDialog(dialog);
			}
		}
	}

	@Override
	public void handleKeyTyped(char character, int code) {
		super.handleKeyTyped(character, code);
		if (!Game.isClosed()) {
			try {
				game.onKeyPressed(code, character);
			} catch (Throwable e) {
				Game.stop(e, "Error when key pressed");
			}
		}
	}

	@Override
	public void handleKeyReleased(char character, int code) {
		super.handleKeyReleased(character, code);
		if (!Game.isClosed()) {
			try {
				game.onKeyReleased(code, character);
			} catch (Throwable e) {
				Game.stop(e, "Error when key released");
			}
		}
	}

	@Override
	public void render(Laptop laptop, Minecraft mc, int x, int y, int mouseX, int mouseY, boolean active, float partialTicks) {
		partialTicks = mc.getRenderPartialTicks();

		super.render(laptop, mc, x, y, mouseX, mouseY, active, partialTicks);

		if (mouseX < x || mouseX >= x + this.getWidth() || mouseY < y || mouseY >= y + this.getHeight()) {
			mouseX = oldMouseX;
			mouseY = oldMouseY;
		}

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		RenderHelper.scissor(x, y, getWidth(), getHeight());
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		try {
			game.render(laptop, mc, mouseX - x, mouseY - y, partialTicks);
		} catch (Throwable e) {
			Game.stop(e, "Error while rendering");
		}
		GlStateManager.popMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		if (mouseX != oldMouseX)
			oldMouseX = mouseX;
		if (mouseY != oldMouseY)
			oldMouseY = mouseY;
	}

	@Override
	public void load(NBTTagCompound nbt) {
		try {
			game.load(nbt);
		} catch (Throwable e) {
			Game.stop(e, "Error while loading");
		}
	}

	@Override
	public void save(NBTTagCompound nbt) {
		try {
			game.save(nbt);
		} catch (Throwable e) {
			Game.stop(e, "Error while saving");
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		MemoryLib.clear();
		markDirty();
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			Jukebox.stopMusic();
		}
	}
}