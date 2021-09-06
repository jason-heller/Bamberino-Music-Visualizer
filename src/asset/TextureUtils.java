package asset;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GLContext;

import core.App;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

// TODO: Clean me!

class TextureData {

	public int type;
	private final int width;
	private final int height;
	private final ByteBuffer buffer;

	private boolean clampEdges = false;
	private boolean mipmap = false;
	private boolean anisotropic = false;
	private boolean nearest = true;
	private boolean transparent;
	private float bias = 0f;
	private int numRows = 1;
	private int format = GL11.GL_RGBA;

	public TextureData(ByteBuffer buffer, int width, int height) {
		this.buffer = buffer;
		this.width = width;
		this.height = height;
	}
	
	public int getFormat() {
		return format;
	}

	public float getBias() {
		return bias;
	}

	public ByteBuffer getBuffer() {
		return buffer;
	}

	public int getHeight() {
		return height;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getWidth() {
		return width;
	}

	public boolean isAnisotropic() {
		return anisotropic;
	}

	public boolean isClampEdges() {
		return clampEdges;
	}

	public boolean isMipmap() {
		return mipmap;
	}

	public boolean isNearest() {
		return nearest;
	}

	public boolean isTransparent() {
		return transparent;
	}

	public void setAnisotropic(boolean anisotropic) {
		this.anisotropic = anisotropic;
	}

	public void setBias(float bias) {
		this.bias = bias;
	}

	public void setClampEdges(boolean clampEdges) {
		this.clampEdges = clampEdges;
	}

	public void setMipmap(boolean mipmap) {
		this.mipmap = mipmap;
	}

	public void setNearest(boolean nearest) {
		this.nearest = nearest;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public void setTransparent(boolean transparent) {
		this.transparent = transparent;
	}
}

public class TextureUtils {

	public static Texture createTexture(String path) {
		final TextureData textureData = readTextureData(path);
		if (textureData == null) {
			return null;
		}
		textureData.type = GL11.GL_TEXTURE_2D;
		final int textureId = loadTextureToOpenGL(textureData);
		return new Texture(textureId, textureData.getWidth(), textureData.getHeight());
	}
	
	protected static TextureData readTextureData(String path) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			final InputStream in = new BufferedInputStream(new FileInputStream(path));
			final PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.BGRA);
			buffer.flip();
			in.close();
		} catch (final Exception e) {
			App.handleCrash(new Exception("Failed to load texture " + path));
		}
		return new TextureData(buffer, width, height);
	}

	private static int loadTextureToOpenGL(TextureData data) {
		final int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(data.type, texID);
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		if (data.type == GL13.GL_TEXTURE_CUBE_MAP) {
			for (int i = 0; i < 6; i++) {
				GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGB, data.getWidth(),
						data.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			}
		} else {
			GL11.glTexImage2D(data.type, 0, data.getFormat(), data.getWidth(), data.getHeight(), 0, GL12.GL_BGRA,
					GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}

		if (data.isMipmap()) {
			if (data.getNumRows() <= 1) {
				GL30.glGenerateMipmap(data.type);
			}
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);// GL11.
			GL11.glTexParameterf(data.type, GL14.GL_TEXTURE_LOD_BIAS, data.getBias());
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 5);
			if (data.isAnisotropic() && GLContext.getCapabilities().GL_EXT_texture_filter_anisotropic) {
				GL11.glTexParameterf(data.type, GL14.GL_TEXTURE_LOD_BIAS, data.getBias());
				GL11.glTexParameterf(data.type, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, 4.0f);
			}
			
			if (data.getNumRows() > 1) {
				GL11.glTexImage2D(data.type, 1, GL11.GL_RGBA, data.getWidth()/2, data.getHeight()/2, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
				GL11.glTexImage2D(data.type, 2, GL11.GL_RGBA, data.getWidth()/4, data.getHeight()/4, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
				GL11.glTexImage2D(data.type, 3, GL11.GL_RGBA, data.getWidth()/8, data.getHeight()/8, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
				GL11.glTexImage2D(data.type, 4, GL11.GL_RGBA, data.getWidth()/16, data.getHeight()/16, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
				GL11.glTexImage2D(data.type, 5, GL11.GL_RGBA, data.getWidth()/32, data.getHeight()/32, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
				//GL11.glTexImage2D(data.type, 6, GL11.GL_RGBA, data.getWidth()/12, data.getHeight()/12, 0, GL12.GL_BGRA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
			}
		} else if (data.isNearest()) {
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}
		
		if (data.isClampEdges()) {
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		} else {
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(data.type, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}
		GL11.glBindTexture(data.type, 0);
		return texID;
	}

	public static void unbindTexture() {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
}
