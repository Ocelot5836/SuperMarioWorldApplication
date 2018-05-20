package com.ocelot.mod.lib.opengl3d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.GlStateManager.CullFace;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class RawModel {

	private Vector3f[] vertices;
	private Vector3f[] normals;
	private Face[] faces;

	public RawModel(Vector3f[] vertices, Vector3f[] normals, Face[] faces) {
		this.vertices = vertices;
		this.normals = normals;
		this.faces = faces;
	}

	public void render(float x, float y, float z, float rotationX, float rotationY, float rotationZ, float scaleX, float scaleY, float scaleZ) {
		try {
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_TRIANGLES, DefaultVertexFormats.POSITION);
			GlStateManager.pushMatrix();
			GlStateManager.disableTexture2D();
			GlStateManager.cullFace(CullFace.BACK);
			this.draw(buffer);
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate(rotationX, 1, 0, 0);
			GlStateManager.rotate(rotationY, 0, 1, 0);
			GlStateManager.rotate(rotationZ, 0, 0, 1);
			GlStateManager.scale(scaleX, scaleY, scaleZ);
			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.popMatrix();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void draw(BufferBuilder buffer) {
		for (int i = 0; i < faces.length; i++) {
			Face face = faces[i];
			this.renderFace(buffer, face);
		}
	}

	private void renderFace(BufferBuilder buffer, Face face) {
		Vector3f normal1 = normals[(int) face.normal.x - 1];
		buffer.normal(normal1.x, normal1.y, normal1.z);
		Vector3f vertex1 = vertices[(int) face.vertex.x - 1];
		buffer.pos(vertex1.x, vertex1.y, vertex1.z);
		buffer.endVertex();

		Vector3f normal2 = normals[(int) face.normal.y - 1];
		buffer.normal(normal2.x, normal2.y, normal2.z);
		Vector3f vertex2 = vertices[(int) face.vertex.y - 1];
		buffer.pos(vertex2.x, vertex2.y, vertex2.z);
		buffer.endVertex();

		Vector3f normal3 = normals[(int) face.normal.z - 1];
		buffer.normal(normal3.x, normal3.y, normal3.z);
		Vector3f vertex3 = vertices[(int) face.vertex.z - 1];
		buffer.pos(vertex3.x, vertex3.y, vertex3.z);
		buffer.endVertex();
	}

	public static class Face {

		private Vector3f vertex;
		private Vector3f normal;

		Face(Vector3f vertex, Vector3f normal) {
			this.vertex = vertex;
			this.normal = normal;
		}

		public Vector3f getVertex() {
			return vertex;
		}

		public Vector3f getNormal() {
			return normal;
		}
	}
}