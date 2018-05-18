package com.ocelot.mod.application;

import java.nio.FloatBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;
import com.mrcrayfish.device.api.app.Application;
import com.mrcrayfish.device.api.app.Dialog;
import com.mrcrayfish.device.api.print.IPrint;
import com.mrcrayfish.device.api.utils.RenderUtil;
import com.mrcrayfish.device.programs.ApplicationPixelPainter;
import com.ocelot.mod.game.Game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class MarioPrint {
	
	public static class Print implements IPrint {
		private String name;
		private int[] pixels;
		private boolean cut;

		public Print() {
		}

		public Print(String name, int[] pixels) {
			this.name = name;
			this.setPicture(pixels);
		}

		private void setPicture(int[] pixels) {
			this.pixels = pixels;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public int speed() {
			return 50;
		}

		@Override
		public boolean requiresColor() {
			for (int pixel : pixels) {
				int r = (pixel >> 16 & 255);
				int g = (pixel >> 8 & 255);
				int b = (pixel & 255);
				if (r != g || r != b) {
					return true;
				}
			}
			return false;
		}

		@Override
		public NBTTagCompound toTag() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", name);
			tag.setIntArray("pixels", pixels);
			if (cut)
				tag.setBoolean("cut", cut);
			return tag;
		}

		@Override
		public void fromTag(NBTTagCompound tag) {
			name = tag.getString("name");
			cut = tag.getBoolean("cut");
			setPicture(tag.getIntArray("pixels"));
		}

		@Override
		public Class<? extends IPrint.Renderer> getRenderer() {
			return PictureRenderer.class;
		}
	}

	private static class PictureRenderer implements IPrint.Renderer {

		private static final Map<int[], Integer> CACHE = Maps.<int[], Integer>newHashMap();

		@Override
		public boolean render(NBTTagCompound data) {
			if (data.hasKey("pixels", Constants.NBT.TAG_INT_ARRAY) && data.hasKey("resolution", Constants.NBT.TAG_INT)) {
				int[] pixels = data.getIntArray("pixels");
				int width = Game.WIDTH;
				int height = Game.HEIGHT;
				boolean cut = data.getBoolean("cut");

				GlStateManager.enableBlend();
				OpenGlHelper.glBlendFunc(770, 771, 1, 0);
				GlStateManager.disableLighting();
				GlStateManager.rotate(180, 0, 1, 0);

				if (!cut) {
					Minecraft.getMinecraft().getTextureManager().bindTexture(ApplicationPixelPainter.PictureRenderer.TEXTURE);
					RenderUtil.drawRectWithTexture(-1, 0, 0, 0, 1, 1, 16, 16, 16, 16);
				}

				int textureId = CACHE.get(pixels);
				if (!CACHE.containsKey(pixels)) {
					int[] flippedPixels = new int[pixels.length];
					for (int i = 0; i < width; i++) {
						for (int j = 0; j < height; j++) {
							flippedPixels[width - i - 1 + (height - j - 1) * width] = pixels[i + j * width];
						}
					}

					textureId = TextureUtil.glGenTextures();
					TextureUtil.allocateTexture(textureId, width, height);
					TextureUtil.uploadTexture(textureId, flippedPixels, width, height);
					CACHE.put(pixels, textureId);
				}
				GlStateManager.bindTexture(textureId);
				RenderUtil.drawRectWithTexture(-1, 0, 0, 0, 1, 1, width, height, width, height);

				GlStateManager.disableRescaleNormal();
				GlStateManager.disableBlend();
				GlStateManager.enableLighting();
				return true;
			}
			return false;
		}
	}

	public static void printScreenshot(Application app, int xPos, int yPos, int width, int height) {
		FloatBuffer imageData = BufferUtils.createFloatBuffer(width * height * 3);
		GL11.glReadPixels(xPos, yPos, width, height, GL11.GL_RGB, GL11.GL_FLOAT, imageData);
		imageData.rewind();

		int[] rgbArray = new int[width * height];
		for (int y = 0; y < height; ++y) {
			for (int x = 0; x < width; ++x) {
				int r = (int) (imageData.get() * 255) << 16;
				int g = (int) (imageData.get() * 255) << 8;
				int b = (int) (imageData.get() * 255);
				int i = ((height - 1) - y) * width + x;
				rgbArray[i] = r + g + b;
			}
		}

		Dialog.Print dialog = new Dialog.Print(new MarioPrint.Print("Screenshot-" + getSystemTime(false), rgbArray));
		app.openDialog(dialog);
	}

	private static String getSystemTime(boolean getTimeOnly) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(getTimeOnly ? "HH-mm-ss" : "yyyy-MM-dd'T'HH-mm-ss");
		return dateFormat.format(new Date());
	}
}