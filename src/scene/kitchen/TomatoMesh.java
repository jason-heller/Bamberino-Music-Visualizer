package scene.kitchen;

import org.joml.Matrix4f;

import asset.Model;
import asset.ObjLoader;
import core.Window;
import scene.FlatShader;
import scene.IRender;

public class TomatoMesh implements IRender {
	
	private static final float JUMP_HEIGHT = 6.5f;

	private Model tomato;
	private float reactValue = 0f;
	
	private Matrix4f matrix;
	
	private boolean isJumping = false;
	private float y = 0;
	private float ySpeed = 0f;
	private float gravity = .5f;
	
	public TomatoMesh() {
		tomato = ObjLoader.load("res/models/tomato.obj");
		matrix = new Matrix4f();
	}

	@Override
	public void draw(FlatShader shader) {
		matrix.identity();
		
		if (isJumping) {
			ySpeed -= gravity * Window.deltaTime;
			y += ySpeed;
			
			if (y <= 0) {
				isJumping = false;
				y = 0;
			}
		} else if (reactValue != 0f) {
			isJumping = true;
			ySpeed = JUMP_HEIGHT * Window.deltaTime;
		}
		
		matrix.translate(0, y, 0);
		
		shader.modelView.loadMatrix(matrix);
		tomato.draw();
	}

	@Override
	public void setReactionValue(int index, float value) {
		reactValue = value;
	}

	@Override
	public void clean() {
		tomato.clean();
	}
}
