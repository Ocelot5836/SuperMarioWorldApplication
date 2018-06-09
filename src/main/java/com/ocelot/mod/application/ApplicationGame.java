package com.ocelot.mod.application;

import org.lwjgl.opengl.GL11;

import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.app.Layout;
import com.mrcrayfish.device.api.io.File;
import com.mrcrayfish.device.core.Laptop;
import com.mrcrayfish.device.core.io.FileSystem;
import com.mrcrayfish.device.util.GLHelper;
import com.ocelot.mod.Mod;
import com.ocelot.mod.audio.Jukebox;
import com.ocelot.mod.game.Game;
import com.ocelot.mod.game.core.GameTemplate;
import com.ocelot.mod.lib.MemoryLib;

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

	private File file;
	private boolean loaded;
	private Layout layout;
	private GameTemplate game;

	private int oldMouseX, oldMouseY;

	@Override
	public void init(NBTTagCompound nbt) {
		game = new Game();
		game.init();
		layout = new Layout(game.getWidth(), game.getHeight());
		setCurrentLayout(layout);

		loaded = false;
		FileSystem.getApplicationFolder(this, (folder, success) -> {
			if (success) {
				folder.search(file -> file.isForApplication(this) && file.getName().equalsIgnoreCase("save")).forEach(file -> {
					readFromFile(file.getData());
					this.file = file;
					loaded = true;
				});
			}
		});
		if (!loaded) {
			saveFile();
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		// if (getActiveDialog() == null) {
		// if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
		// MarioPrint.printScreenshot(this, this.getCurrentLayout().xPosition, this.getCurrentLayout().yPosition, this.getCurrentLayout().width, this.getCurrentLayout().height);
		// }
		// }

		try {
			game.update();
		} catch (Throwable e) {
			Game.stop(e, "Error while updating");
		}
		if (Game.isClosed()) {
			if (getActiveDialog() == null) {
				DialogCrashLog dialog = new DialogCrashLog(I18n.format("exception." + Mod.MOD_ID + ".dialog", Game.getCloseInfo()));
				dialog.setPositiveListener((mouseButton, mouseX, mouseY) -> {
					this.init(new NBTTagCompound());
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
	public void handleMouseClick(int mouseX, int mouseY, int mouseButton) {
		super.handleMouseClick(mouseX, mouseY, mouseButton);
		if (!Game.isClosed()) {
			try {
				game.onMousePressed(mouseButton, oldMouseX, oldMouseY);
			} catch (Throwable e) {
				Game.stop(e, "Error when mouse pressed");
			}
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
		GLHelper.pushScissor(x, y, getWidth(), getHeight());
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, 0);
		try {
			game.render(laptop, mc, mouseX - x, mouseY - y, partialTicks);
		} catch (Throwable e) {
			Game.stop(e, "Error while rendering");
		}
		GLHelper.popScissor();
		GlStateManager.popMatrix();
		GL11.glDisable(GL11.GL_SCISSOR_TEST);

		oldMouseX = mouseX - x;
		oldMouseY = mouseY - y;
	}

	@Override
	public void load(NBTTagCompound nbt) {
	}

	@Override
	public void save(NBTTagCompound nbt) {
	}

	private void readFromFile(NBTTagCompound nbt) {
		try {
			game.load(nbt);
		} catch (Throwable e) {
			Game.stop(e, "Error while loading");
		}
	}

	private void writeToFile(NBTTagCompound nbt) {
		try {
			game.save(nbt);
		} catch (Throwable e) {
			Game.stop(e, "Error while saving");
		}
	}

	private void saveFile() {
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToFile(nbt);
		file = new File("save", this, nbt);
		Dialog.SaveFile dialog = new Dialog.SaveFile(this, nbt);
		dialog.setFolder(this.getApplicationFolderPath());
		this.openDialog(dialog);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (file != null) {
			NBTTagCompound nbt = new NBTTagCompound();
			this.writeToFile(nbt);
			try {
				file.setData(nbt);
			} catch (Exception e) {
			}
		}
		game.onClose();
		MemoryLib.clear();
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			Jukebox.stopMusic();
		}
		layout.clear();
		layout = null;
		game = null;
	}
}