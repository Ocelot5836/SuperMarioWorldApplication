package com.ocelot.mod.lib.opengl3d;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.ocelot.mod.lib.opengl3d.RawModel.Face;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class OBJLoader {

	public static RawModel load(ResourceLocation location) {
		try {
			InputStreamReader is = new InputStreamReader(Minecraft.getMinecraft().getResourceManager().getResource(location).getInputStream());
			BufferedReader reader = new BufferedReader(is);
			
			String line;
			List<Vector3f> vertices = new ArrayList<Vector3f>();
			List<Vector3f> normals = new ArrayList<Vector3f>();
			List<Face> faces = new ArrayList<Face>();

			try {
				while (true) {
					line = reader.readLine();
					String[] currentLine = line.split(" ");
					if (line.startsWith("# ")) {
						continue;
					} else if (line.startsWith("v ")) {
						Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						vertices.add(vertex);
					} else if (line.startsWith("vn ")) {
						Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
						normals.add(normal);
					} else if (line.startsWith("f ")) {
						break;
					}
				}

				while (line != null) {
					if (!line.startsWith("f ")) {
						line = reader.readLine();
						continue;
					}
					String[] currentLine = line.split(" ");
					String[] vertex1 = currentLine[1].split("/");
					String[] vertex2 = currentLine[2].split("/");
					String[] vertex3 = currentLine[3].split("/");

					Vector3f vertexIndices = new Vector3f(Float.valueOf(vertex1[0]), Float.valueOf(vertex2[0]), Float.valueOf(vertex3[0]));
					Vector3f normalIndices = new Vector3f(Float.valueOf(vertex1[2]), Float.valueOf(vertex2[2]), Float.valueOf(vertex3[2]));

					faces.add(new Face(vertexIndices, normalIndices));
					
					line = reader.readLine();
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Vector3f[] verticesArray = new Vector3f[vertices.size()];
			Vector3f[] normalsArray = new Vector3f[normals.size()];
			Face[] facesArray = new Face[faces.size()];

			int vertexPointer = 0;
			for (Vector3f vertex : vertices) {
				verticesArray[vertexPointer++] = vertex;
			}
			
			int normalPointer = 0;
			for (Vector3f normal : normals) {
				normalsArray[normalPointer++] = normal;
			}
			
			int facePointer = 0;
			for (Face face : faces) {
				facesArray[facePointer++] = face;
			}

			return new RawModel(verticesArray, normalsArray, facesArray);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}