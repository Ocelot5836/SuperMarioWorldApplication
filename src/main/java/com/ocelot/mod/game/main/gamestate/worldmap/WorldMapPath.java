package com.ocelot.mod.game.main.gamestate.worldmap;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import com.ocelot.mod.game.core.gfx.Sprite;
import com.ocelot.mod.lib.AxisAlignedBB;
import com.ocelot.mod.lib.Lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class WorldMapPath {

	private Sprite sprite;
	private List<Vector2f> points;
	private AxisAlignedBB aabb;

	protected WorldMapPath(Sprite sprite, Vector2f... points) {
		this.sprite = sprite;
		this.points = new ArrayList<Vector2f>();
		for (int i = 0; i < points.length; i++) {
			if (i < points.length - 1) {
				this.points.add(points[i]);
				this.points.add(points[i + 1]);
			}
		}
		double x = 0;
		double y = 0;
		double toX = 0;
		double toY = 0;
		for (int i = 0; i < points.length; i++) {
			Vector2f point = points[i];
			if (i == 0) {
				x = point.x;
				y = point.y;
			}
			if (point.x < x) {
				x = point.x;
			}
			if (point.x > toX) {
				toX = point.x;
			}
			if (point.y < y) {
				y = point.y;
			}
			if (point.y > toY) {
				toY = point.y;
			}
		}
		this.aabb = new AxisAlignedBB(x - 8, y - 8, toX - x + 16, toY - y + 16);
	}

	protected void update() {
	}

	protected void render(Gui gui, Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		GL11.glLineWidth(1.5f);
		GlStateManager.color(1, 0, 0, 1);
		for (int i = 0; i < points.size() - 1; i++) {
			Vector2f point = points.get(i);
			Vector2f nextPoint = points.get(i + 1);
			this.drawLine(point, nextPoint);
		}
		GlStateManager.color(1, 1, 1, 1);
		this.sprite.render(this.aabb.getX(), this.aabb.getY(), this.aabb.getWidth(), this.aabb.getHeight());
		Lib.drawCollisionBox(this.aabb, 0xffffffff);
		GlStateManager.color(1, 1, 1, 1);
	}

	private static void drawLine(Vector2f point, Vector2f nextPoint) {
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