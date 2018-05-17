package com.ocelot.mod.lib;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

/**
 * <em><b>Copyright (c) 2018 Ocelot5836.</b></em>
 * 
 * <br>
 * </br>
 * 
 * Handles any methods that have to deal with rendering things onto the screen.
 * 
 * @author Ocelot5836
 */
public class RenderHelper {

	/**
	 * Draws a hollow rectangle of color.
	 * 
	 * @param left
	 *            The x position
	 * @param top
	 *            The y position
	 * @param right
	 *            The x plus width
	 * @param bottom
	 *            The y plus height
	 * @param color
	 *            The color
	 */
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			double i = top;
			top = bottom;
			bottom = i;
		}

		float a = (float) (color >> 24 & 255) / 255.0F;
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		GlStateManager.color(r, g, b, a);
		bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
	}

	/**
	 * Draws a solid rectangle of color.
	 * 
	 * @param left
	 *            The x position
	 * @param top
	 *            The y position
	 * @param right
	 *            The x plus width
	 * @param bottom
	 *            The y plus height
	 * @param color
	 *            The color
	 */
	public static void fillRect(double left, double top, double right, double bottom, int color) {
		if (left < right) {
			double i = left;
			left = right;
			right = i;
		}

		if (top < bottom) {
			double i = top;
			top = bottom;
			bottom = i;
		}

		float a = (float) (color >> 24 & 255) / 255.0F;
		float r = (float) (color >> 16 & 255) / 255.0F;
		float g = (float) (color >> 8 & 255) / 255.0F;
		float b = (float) (color & 255) / 255.0F;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		GlStateManager.color(r, g, b, a);
		bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) left, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) bottom, 0.0D).endVertex();
		bufferbuilder.pos((double) right, (double) top, 0.0D).endVertex();
		bufferbuilder.pos((double) left, (double) top, 0.0D).endVertex();
		tessellator.draw();
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.enableTexture2D();
	}

	/**
	 * The exact same as the {@link Gui} method, except that is takes in doubles instead of integers.
	 * 
	 * @see Gui#drawScaledCustomSizeModalRect(int, int, float, float, int, int, int, int, float, float)
	 */
	public static void drawScaledCustomSizeModalRect(double x, double y, double u, double v, double uWidth, double vHeight, double width, double height, double tileWidth, double tileHeight) {
		double f = 1.0 / tileWidth;
		double f1 = 1.0 / tileHeight;
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
		bufferbuilder.pos(x, (y + height), 0.0D).tex((u * f), ((v + vHeight) * f1)).endVertex();
		bufferbuilder.pos((x + width), (y + height), 0.0D).tex(((u + uWidth) * f), ((v + vHeight) * f1)).endVertex();
		bufferbuilder.pos((x + width), y, 0.0D).tex(((u + uWidth) * f), (v * f1)).endVertex();
		bufferbuilder.pos(x, y, 0.0D).tex((u * f), (v * f1)).endVertex();
		tessellator.draw();
	}

	/**
	 * Cuts out the specified region. Will be removed soon
	 * 
	 * <br>
	 * </br>
	 * 
	 * <b><i>NOTE: THIS WILL NOT BE COMPATIBLE WITH THE MOST RECENT VERSION OF THE DEVICE MOD AFTER V0.3.1!!!</i></b>
	 */
	public static void scissor(int x, int y, int width, int height) {
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution resolution = new ScaledResolution(mc);
		int scale = resolution.getScaleFactor();
		GL11.glScissor(x * scale, mc.displayHeight - y * scale - height * scale, width * scale, height * scale);
	}
}