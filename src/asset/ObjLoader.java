package asset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import core.App;

public class ObjLoader {
	public static Model load(String path) {

		final LinkedHashMap<String, float[]> colors = getMaterials(path);

		final List<float[]> vertices = new ArrayList<float[]>();
		final List<float[]> uvs = new ArrayList<float[]>();
		final List<float[]> normals = new ArrayList<float[]>();

		final List<Index> indices = new ArrayList<Index>();
		final List<Integer> indexOrder = new ArrayList<Integer>();

		String currentColor = "None";

		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(path));
			String line = reader.readLine();

			while (line != null) {
				final String[] data = line.split(" ");

				if (data.length > 1) {
					if (data[0].equals("v")) {
						final float[] vertex = new float[] { Float.parseFloat(data[1]), Float.parseFloat(data[2]),
								Float.parseFloat(data[3]) };
						vertices.add(vertex);
					} else if (data[0].equals("vt")) {
						final float[] uvCoord = new float[] { Float.parseFloat(data[1]), Float.parseFloat(data[2]) };
						uvs.add(uvCoord);
					} else if (data[0].equals("vn")) {
						final float[] normal = new float[] { Float.parseFloat(data[1]), Float.parseFloat(data[2]),
								Float.parseFloat(data[3]) };
						normals.add(normal);
					} else if (data[0].equals("usemtl")) {
						currentColor = data[1];
					} else if (data[0].equals("f")) {
						for (byte i = 1; i < data.length; i++) {
							final String[] faceData = data[i].split("/");

							final int vertexIndex = Integer.parseInt(faceData[0]) - 1;
							final int uvIndex = faceData[1].equals("") ? 0 : Integer.parseInt(faceData[1]) - 1;
							final int normalIndex = Integer.parseInt(faceData[2]) - 1;
							final Index newIndex = new Index(vertexIndex, uvIndex, normalIndex, currentColor);

							int indexPosition = -1;

							for (int j = 0; j < indices.size(); j++) {
								final Index index = indices.get(j);
								if (index.equals(newIndex)) {
									indexPosition = j;
									break;
								}
							}

							if (indexPosition == -1) {
								indices.add(newIndex);
								indexOrder.add(indices.size() - 1);
							} else {
								indexOrder.add(indexPosition);
							}
						}

					}
				}

				line = reader.readLine();
			}

			reader.close();

			if (uvs.size() == 0) {
				uvs.add(new float[] { 0, 0 });
			}

			final float[] vertexArray = new float[indices.size() * 3];
			final float[] uvArray = new float[indices.size() * 2];
			final float[] normalArray = new float[indices.size() * 3];
			final float[] colorArray = new float[indices.size() * 3];
			final int[] indexArray = new int[indexOrder.size()];

			for (int i = 0; i < indexArray.length; i++) {
				indexArray[i] = indexOrder.get(i);
			}

			for (int i = 0; i < indices.size(); i++) {
				final float[] vertex = vertices.get(indices.get(i).vertexId);
				vertexArray[i * 3 + 0] = vertex[0];
				vertexArray[i * 3 + 1] = vertex[1];
				vertexArray[i * 3 + 2] = vertex[2];

				final float[] uv = uvs.get((int) indices.get(i).uvId);
				uvArray[i * 2 + 0] = uv[0];
				uvArray[i * 2 + 1] = 1 - uv[1];

				final float[] normal = normals.get((int) indices.get(i).normalId);
				normalArray[i * 3 + 0] = normal[0];
				normalArray[i * 3 + 1] = normal[1];
				normalArray[i * 3 + 2] = normal[2];

				final float[] color = colors.get(indices.get(i).color);
				colorArray[i * 3 + 0] = color[0];
				colorArray[i * 3 + 1] = color[1];
				colorArray[i * 3 + 2] = color[2];

			}

			final Model model = Model.create();
			model.bind();
			model.createIndexBuffer(indexArray);
			model.createAttribute(0, vertexArray, 3);
			model.createAttribute(1, uvArray, 2);
			model.createAttribute(2, normalArray, 3);
			model.createAttribute(3, colorArray, 3);
			model.unbind();

			return model;
		} catch (final FileNotFoundException e) {
			App.handleCrash(new IOException("Failed to find obj file: " + path));
		} catch (final IOException e) {
			App.handleCrash(new IOException("Could not read obj file " + path + ", problem with line " + e.getStackTrace()[0].getLineNumber()));
		} catch (final Exception e) {
			App.handleCrash(e);
		}

		return null;
	}

	private static LinkedHashMap<String, float[]> getMaterials(String path) {
		LinkedHashMap<String, float[]> mats = new LinkedHashMap<>();

		String mtlPath = path.substring(0, path.lastIndexOf('.')) + ".mtl";
		try {
			List<String> lines = Files.readAllLines(new File(mtlPath).toPath());
			String mat = null;

			for (String line : lines) {
				String[] data = line.split(" ");
				if (data[0].equals("newmtl")) {
					mat = data[1];
				}

				if (data[0].equals("Kd")) {
					mats.put(mat, new float[] { Float.parseFloat(data[1]), Float.parseFloat(data[2]),
							Float.parseFloat(data[3]) });
					mat = null;
				}
			}

		} catch (IOException e) {
			App.handleCrash(new IOException("Failed to load .mtl file: " + path));
		}
		return mats;
	}
}

class Index {
	public int vertexId, uvId, normalId;
	public String color;

	public Index(int vertexIndex, int uvIndex, int normalIndex, String currentColor) {
		this.vertexId = vertexIndex;
		this.uvId = uvIndex;
		this.normalId = normalIndex;
		this.color = currentColor;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Index) {
			Index i = (Index) obj;
			return this.vertexId == i.vertexId && this.uvId == i.uvId && this.normalId == i.normalId
					&& this.color.equals(i.color);
		}

		return false;
	}
}
