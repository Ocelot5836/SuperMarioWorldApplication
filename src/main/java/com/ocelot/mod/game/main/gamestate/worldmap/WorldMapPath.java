package com.ocelot.mod.game.main.gamestate.worldmap;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class WorldMapPath {

	private List<Vector2f> points;

	protected WorldMapPath(Vector2f... points) {
		this.points = new ArrayList<Vector2f>();
		for (int i = 0; i < points.length; i++) {
			if (i < points.length - 1) {
				this.points.add(points[i]);
				this.points.add(points[i + 1]);
			}
		}
	}

	protected void update() {
	}

	protected void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GL11.glLineWidth(1.5f);
		for (int i = 0; i < points.size() - 1; i++) {
			Vector2f point = points.get(i);
			Vector2f nextPoint = points.get(i + 1);
			this.drawLine(point, nextPoint);
		}
		GlStateManager.color(1, 1, 1, 1);
	}

	private void drawLine(Vector2f point, Vector2f nextPoint) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		GlStateManager.disableTexture2D();
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) point.x, (double) point.y, 0.0D).endVertex();
		bufferbuilder.pos((double) nextPoint.x, (double) nextPoint.y, 0.0D).endVertex();
		tessellator.draw();
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GL11.glDisable(GL11.GL_BLEND);
		GlStateManager.enableTexture2D();
	}
}