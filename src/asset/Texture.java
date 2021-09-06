package asset;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class Texture {
	public final int id;
	public int width, height;

	private final int type;

	public Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.type = GL11.GL_TEXTURE_2D;
	}
	
	public void bind(int unit) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + unit);
		GL11.glBindTexture(type, id);
	}
	
	public void unbind(int unit) {
		GL11.glBindTexture(type, 0);
	}
	
	public void delete() {
		GL11.glDeleteTextures(id);
	}
}
