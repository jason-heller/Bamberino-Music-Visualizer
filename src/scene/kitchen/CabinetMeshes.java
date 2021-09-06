package scene.kitchen;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import asset.Model;
import asset.ObjLoader;
import core.Window;
import scene.FlatShader;
import scene.IRender;
import scene.MathUtil;

public class CabinetMeshes implements IRender {
	
	private Model cabinet;
	
	private Matrix4f matrix;
	
	private final Vector3f TRANSLATION_BASE = new Vector3f(-0.298f, 4.5f, -0.825f);
	
	private float[] rotationY = new float[3];
	private float[] rotationTarget = new float[3];
	
	public CabinetMeshes() {
		cabinet = ObjLoader.load("res/models/cabinet.obj");
		matrix = new Matrix4f();
	}

	@Override
	public void draw(FlatShader shader) {
		for(int i = 0; i < 3; i++) {
			rotationY[i] = MathUtil.lerp(rotationY[i], rotationTarget[i], 10f * Window.deltaTime);

			drawCabinet(shader, i);
		}
	}

	private void drawCabinet(FlatShader shader, int i) {
		matrix.identity();
		matrix.translate(TRANSLATION_BASE.x, TRANSLATION_BASE.y, TRANSLATION_BASE.z - (i * 2f));
		matrix.rotateY(rotationY[i] * 90f);
		
		shader.modelView.loadMatrix(matrix);
		cabinet.draw();
	}

	@Override
	public void setReactionValue(int index, float value) {
		rotationTarget[index] = value;
	}

	@Override
	public void clean() {
		cabinet.clean();
	}

}
