package shader;

import java.util.logging.Logger;

import org.lwjgl.opengl.GL20;

public abstract class Uniform {

	private static final int NOT_FOUND = -1;

	private final String name;
	private int location;

	protected Uniform(String name) {
		this.name = name;
	}

	public int getLocation() {
		return location;
	}

	protected void storeUniformLocation(int programID) {
		location = GL20.glGetUniformLocation(programID, name);
		if (location == NOT_FOUND) {
			Logger.getGlobal().warning("No uniform variable called " + name + " found!");
		}
	}

}
