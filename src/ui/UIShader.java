package ui;

import shader.ShaderProgram;
import shader.UniformBoolean;
import shader.UniformFloat;
import shader.UniformVec3;
import shader.UniformVec4;

public class UIShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shader/ui.vert";
	private static final String FRAGMENT_FILE = "shader/ui.frag";

	public UniformVec4 translation = new UniformVec4("translation");
	public UniformVec4 offset = new UniformVec4("offset");
	public UniformVec3 color = new UniformVec3("color");
	public UniformBoolean centered = new UniformBoolean("centered");
	public UniformFloat opacity = new UniformFloat("opacity");
	public UniformFloat rotation = new UniformFloat("rot");

	public UIShader() {
		super(VERTEX_FILE, FRAGMENT_FILE, "in_vertices", "in_uvs");
		super.storeAllUniformLocations(color, translation, offset, centered, opacity, rotation);
	}

}
