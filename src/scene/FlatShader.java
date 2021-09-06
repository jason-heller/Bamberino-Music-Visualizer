package scene;

import shader.ShaderProgram;
import shader.UniformMat4;
import shader.UniformSampler;
import shader.UniformVec3;

public class FlatShader extends ShaderProgram {

	private static final String VERTEX_SHADER = "shader/flat.vert";
	private static final String FRAGMENT_SHADER = "shader/flat.frag";

	public UniformMat4 viewProj = new UniformMat4("viewProj");
	public UniformMat4 modelView = new UniformMat4("modelView");
	
	public UniformVec3 lightVec = new UniformVec3("lightVec");

	protected UniformSampler albedoTexture = new UniformSampler("albedoTexture");

	public FlatShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER, "position", "textureCoords", "normals", "colors");
		super.storeAllUniformLocations(viewProj, modelView, albedoTexture, lightVec);
		super.bindFragOutput(0, "finalColor");
	}
}
