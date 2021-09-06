package scene.kitchen.util;

import org.joml.Matrix4f;

import core.Window;
import scene.MathUtil;

public class BouncingObject {
	private Matrix4f matrix;
	
	private float y;
	private float ySpeed;
	private float rx, rz;
	private float targetRx, targetRz;
	
	private static final float JUMP_HEIGHT = 1f;
	private static final float GRAVITY = 0.1f;
	
	public BouncingObject() {
		matrix = new Matrix4f();
	}
	
	public void update(boolean atRest) {
		if (y > 0f) {
			ySpeed -= GRAVITY;
			if (atRest) {
				rx = MathUtil.lerp(rx, 0, 2f * Window.deltaTime);
				rz = MathUtil.lerp(rz, 0, 2f * Window.deltaTime);
			} else {
				rx = MathUtil.lerp(rx, targetRx, 2f * Window.deltaTime);
				rz = MathUtil.lerp(rz, targetRz, 2f * Window.deltaTime);
			}
		} else {
			if (atRest) {
				y = 0f;
				ySpeed = 0f;
				rx = MathUtil.lerp(rx, 0, 2f * Window.deltaTime);
				rz = MathUtil.lerp(rz, 0, 2f * Window.deltaTime);
			} else {
				ySpeed = JUMP_HEIGHT;
				targetRx = (float)(Math.random() - 0.5) * 60f;
				targetRz = (float)(Math.random() - 0.5) * 60f;
			}
		}
		
		y += ySpeed * Window.deltaTime;
		
		matrix.identity();
		matrix.translate(0, y, 0);
		matrix.rotateX(rx);
		matrix.rotateZ(rz);
	}
	
	public Matrix4f getMatrix() {
		return matrix;
	}

	public float getY() {
		return y;
	}

	public float getRotationX() {
		return rx;
	}
	
	public float getRotationZ() {
		return rz;
	}
}
