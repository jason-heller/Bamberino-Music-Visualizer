package scene;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.Display;

public class Camera {
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f viewDirection;
	
	private Matrix4f projection, view;

	private int fov = 90;
	
	public static final float FAR_PLANE = 1000f;
	public static final float NEAR_PLANE = 1f;
	
	public Camera() {
		projection = new Matrix4f();
		view = new Matrix4f();
		
		position = new Vector3f(1f,2.5f,3.85f);
		rotation = new Vector3f(10, -90, 0);
		viewDirection = new Vector3f();
		
		final float aspectRatio = Display.getWidth() / Display.getHeight();
		final float y_scale = (float) (1f / Math.tan(Math.toRadians((fov) / 2f)));
		final float x_scale = y_scale / aspectRatio;
		final float frustum_length = FAR_PLANE - NEAR_PLANE;

		projection.m00 = x_scale;
		projection.m11 = y_scale;
		projection.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		projection.m23 = -1;
		projection.m32 = -(2 * NEAR_PLANE * FAR_PLANE / frustum_length);
		projection.m33 = 0;
	}
	
	public void update() {
		view.identity();
		view.translate(-position.x, -position.y, -position.z);
		view.rotate(rotation);
		

		viewDirection.x = -view.m02;
		viewDirection.y = -view.m12;
		viewDirection.z = -view.m22;
	}
	
	public Matrix4f getProjectionView() {
		return Matrix4f.mul(projection, view, null);
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.set(x,y,z);
	}
	
	public void setRotation(float pitch, float yaw, float roll) {
		this.rotation.set(pitch, yaw, roll);
	}

	public Vector3f getDirectionVector() {
		return viewDirection;
	}
}
