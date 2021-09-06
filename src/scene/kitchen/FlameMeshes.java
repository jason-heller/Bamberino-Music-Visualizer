package scene.kitchen;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import asset.Model;
import asset.ObjLoader;
import core.Window;
import scene.FlatShader;
import scene.IRender;
import scene.MathUtil;
import scene.kitchen.util.BouncingObject;

public class FlameMeshes implements IRender {
	
	private Model flames, pan, tofu;
	private Matrix4f matrix;
	
	private float[] scale = new float[4];
	
	private BouncingObject bouncingObj;
	
	private final float GAIN_CUTOFF = .5f;
	private final float MAX_TOFU_RAD = .21f;
	
	private Vector3f tofuPos;
	
	private final float SEP = -0.872f;
	private final float X = 1.49f + SEP;
	private final float Z = -0.69f + SEP;
	private final Vector3f[] POSITIONS = new Vector3f[] { new Vector3f(X, 2, Z), new Vector3f(SEP + X, 2, Z),
			new Vector3f(X, 2, SEP + Z), new Vector3f(SEP + X, 2, SEP + Z) };
	
	private final Vector3f PAN_POSITION = new Vector3f(0.555f, 2.1f, -2.5f);
	
	public FlameMeshes() {
		flames = ObjLoader.load("res/models/flame.obj");
		pan = ObjLoader.load("res/models/pan.obj");
		tofu = ObjLoader.load("res/models/tofu.obj");
		matrix = new Matrix4f();
		
		bouncingObj = new BouncingObject();
		
		tofuPos = new Vector3f();
	}

	@Override
	public void draw(FlatShader shader) {
		for(int i = 0; i < 4; i++) {
			matrix.identity();
			matrix.translate(POSITIONS[i]);
			matrix.scale(scale[i]);
			shader.modelView.loadMatrix(matrix);
			flames.draw();
		}
		
		boolean animatePan = scale[2] > .2f;
		bouncingObj.update(!animatePan);
		
		// Pan
		matrix.identity();
		matrix.translate(PAN_POSITION);
		Matrix4f.mul(matrix, bouncingObj.getMatrix(), matrix);
		shader.modelView.loadMatrix(matrix);
		pan.draw();
		
		// Tofu
		tofuPos.y = 0f;
		tofuPos.x += Window.deltaTime * bouncingObj.getRotationX() / 50f;
		tofuPos.z += Window.deltaTime * bouncingObj.getRotationZ() / 50f;
		
		if (tofuPos.lengthSquared() > MAX_TOFU_RAD * MAX_TOFU_RAD) {
			tofuPos.setLength(MAX_TOFU_RAD);
		}
		
		tofuPos.y = .1f + bouncingObj.getY() * 2f;
		
		matrix.identity();
		matrix.translate(Vector3f.add(PAN_POSITION, tofuPos));
		Matrix4f.mul(matrix, bouncingObj.getMatrix(), matrix);
		matrix.scale(.75f);
		shader.modelView.loadMatrix(matrix);
		tofu.draw();
	}
	
	@Override
	public void setReactionValue(int index, float value) {
		if (value < GAIN_CUTOFF) {
			scale[index] = MathUtil.lerp(scale[index], 0, 10f * Window.deltaTime);
		} else {
			if (scale[index] < .8f) {
				scale[index] = 1.2f;
			} else {
				scale[index] = MathUtil.lerp(scale[index], .8f + (float)(Math.random()*0.4), 10f * Window.deltaTime);
			}
		}
	}

	@Override
	public void clean() {
		flames.clean();
		pan.clean();
		tofu.clean();
	}
}
