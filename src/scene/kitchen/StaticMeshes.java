package scene.kitchen;

import org.joml.Matrix4f;

import asset.Model;
import asset.ObjLoader;
import scene.FlatShader;
import scene.IRender;

public class StaticMeshes implements IRender {
	
	private Model kitchen;
	private Matrix4f matrix;
	
	public StaticMeshes() {
		kitchen = ObjLoader.load("res/models/kitchen.obj");
		matrix = new Matrix4f();
	}

	@Override
	public void draw(FlatShader shader) {
		shader.modelView.loadMatrix(matrix);
		kitchen.draw();
	}

	@Override
	public void setReactionValue(int index, float value) {
		// Nah
	}

	@Override
	public void clean() {
		kitchen.clean();
	}
}
