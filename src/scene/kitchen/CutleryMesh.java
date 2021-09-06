package scene.kitchen;

import org.joml.Matrix4f;

import asset.Model;
import asset.ObjLoader;
import core.Window;
import scene.FlatShader;
import scene.IRender;

public class CutleryMesh implements IRender {
	
	private Model cutlery;
	private Matrix4f matrix;
	
	private float rotationY = 0f;
	private float rotDelta = 0f;
	
	private final float ROT_DECAY_SPEED = 2;
	
	public CutleryMesh() {
		cutlery = ObjLoader.load("res/models/cutlery.obj");
		matrix = new Matrix4f();
	}

	@Override
	public void draw(FlatShader shader) {
		rotationY += rotDelta * Window.deltaTime;
		rotDelta = rotDelta + (Window.deltaTime * ROT_DECAY_SPEED * -rotDelta);
		
		matrix.identity();
		matrix.translate(-0.15f, 2.743f, 0.707f);
		matrix.rotateY(rotationY);
		shader.modelView.loadMatrix(matrix);
		cutlery.draw();
	}
	
	@Override
	public void setReactionValue(int index, float value) {
		rotDelta = value * 800f;
	}


	@Override
	public void clean() {
		cutlery.clean();
	}
}
