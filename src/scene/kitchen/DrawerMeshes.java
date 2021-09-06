package scene.kitchen;

import org.joml.Matrix4f;

import asset.Model;
import asset.ObjLoader;
import core.Window;
import scene.FlatShader;
import scene.IRender;
import scene.MathUtil;

public class DrawerMeshes implements IRender {
	
	private Model drawer;
	private Matrix4f matrix;

	private float reactTarget = 0f;
	private float x = 0;
	
	public DrawerMeshes() {
		drawer = ObjLoader.load("res/models/drawer.obj");
		matrix = new Matrix4f();
	}

	@Override
	public void draw(FlatShader shader) {
		x = MathUtil.lerp(x, reactTarget, 10f * Window.deltaTime);;
		
		matrix.identity();
		matrix.translate(x / 2f, 0, 0);
		
		shader.modelView.loadMatrix(matrix);
		drawer.draw();
	}

	@Override
	public void setReactionValue(int index, float value) {
		reactTarget = value;
	}

	@Override
	public void clean() {
		drawer.clean();
	}
}
