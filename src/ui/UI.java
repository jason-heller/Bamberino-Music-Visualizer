package ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

import asset.Model;
import asset.Texture;
import asset.TextureUtils;
import core.Window;
import scene.MathUtil;
import scene.Scene;

public class UI {
	private static Texture NO_TEXTURE = TextureUtils.createTexture("res/textures/flat.png");
	
	private static UIShader shader;
	public static final Model quad = quad2DModel();
	public static final int DEPTH_SEQUENTIAL = 0;
	private static List<Component> components = new ArrayList<>();
	private static Map<Matrix4f, Component> worldSpaceComponents = new LinkedHashMap<>();
	private static float opacity = 1f;
	
	public static final int width = 1920;
	public static final int height = 1080;
	
	public static boolean hideUI;

	public static void addComponent(Component component) {
		final int depth = component.getDepth();

		if (depth == UI.DEPTH_SEQUENTIAL) {
			int highestDepth = 0;

			for (int i = components.size() - 1; i >= 0; --i) {
				final int compDepth = components.get(i).getDepth();
				if (compDepth >= 0) {
					if (compDepth > highestDepth) {
						highestDepth = compDepth;
					} else {
						break;
					}
				}
			}
			component.setDepth(highestDepth);
		}

		if (depth < 0) {
			for (int i = components.size() - 1; i >= 0; --i) {
				final int compDepth = components.get(i).getDepth();
				if (compDepth > depth) {
					components.add(i + 1, component);
					return;
				}
			}
		} else {
			for (int i = 0; i < components.size(); ++i) {
				final int compDepth = components.get(i).getDepth();
				if (compDepth < 0 || compDepth > depth) {
					components.add(i, component);
					return;
				}
			}
		}

		components.add(component);
	}

	private static Model quad2DModel() {
		final Model model = Model.create();
		model.bind();
		model.createAttribute(0, new float[] { -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f }, 2);
		model.createAttribute(1, new float[] { 0, 1, 1, 1, 0, 0, 1, 0 }, 2);
		model.unbind();
		return model;
	}

	public static void clean() {
		shader.clean();
	}
	
	public static void clear() {
		components.clear();
	}

	public static void drawImage(Image image) {
		addComponent(image);
		// image.setOpacity(opacity);
		image.markAsTemporary();
	}

	public static Image drawImage(String texture, int x, int y) {
		final Image img = new Image(texture, x, y);
		img.setOpacity(opacity);
		img.markAsTemporary();
		addComponent(img);
		return img;
	}

	public static Image drawImage(String texture, int x, int y, int w, int h) {
		final Image img = new Image(texture, x, y);
		img.w = w;
		img.h = h;
		img.setOpacity(opacity);
		img.markAsTemporary();
		addComponent(img);
		return img;
	}
	
	public static Image drawImage(Texture texture, int x, int y, int w, int h, Vector3f color) {
		final Image img = new Image(texture, x, y);
		img.w = w;
		img.h = h;
		img.setColor(color);
		img.setOpacity(opacity);
		img.markAsTemporary();
		addComponent(img);
		return img;
	}

	public static Image drawRect(int x, int y, int width, int height, Vector3f color) {
		return drawImage(NO_TEXTURE, x, y, width, height, color);
	}

	public static void drawHollowRect(int x, int y, int width, int height, int thickness, Vector3f color) {
		drawImage(NO_TEXTURE, x, y, width, thickness, color);
		drawImage(NO_TEXTURE, x, y, thickness, height, color);
		drawImage(NO_TEXTURE, x, y + (height - thickness), width, thickness, color);
		drawImage(NO_TEXTURE, x + (width - thickness), y, thickness, height, color);
	}
	
	public static Image drawLine(int x1, int y1, int x2, int y2, int width, Vector3f color) {
		float dx = x2-x1;
		float dy = y2-y1;
		float len = (float)Math.sqrt(dx*dx + dy*dy);
		float dir = MathUtil.pointDirection(x1, y1, x2, y2);
		
		final Image img = new Image(NO_TEXTURE, (x1+x2)/2f, (y1+y2)/2f);
		img.w = len;
		img.h = width;
		img.setColor(color);
		img.setOpacity(opacity);
		img.setRotation(dir);
		img.markAsTemporary();
		addComponent(img);
		return img;
	}

	public static void drawCircle(int x, int y, int radius, Vector3f color) {
		drawCircle(x, y, radius, 1, 12, color);
	}
	
	public static void drawCircle(int x, int y, int radius, int width, int partitions, Vector3f color) {
		float prt = (float) (2f * Math.PI) / partitions;
		for (float i = 0; i < 2 * Math.PI; i += prt) {
			int dx = (int) (Math.cos(i) * radius);
			int dy = (int) (Math.sin(i) * radius);
			int nx = (int) (Math.cos(i + prt) * radius);
			int ny = (int) (Math.sin(i + prt) * radius);
			drawLine(x + dx, y + dy, x + nx, y + ny, width, color);
		}
	}

	public static Text drawString(Font font, String text, int x, int y, float fontSize, int lineWidth, boolean centered,
			int... offsets) {
		final Text txt = new Text(font, text, x, y, fontSize, lineWidth, centered, offsets);
		txt.setOpacity(opacity);
		txt.markAsTemporary();
		addComponent(txt);
		return txt;
	}

	public static Text drawString(String text, int x, int y) {
		final Text txt = new Text(text, x, y);
		txt.setOpacity(opacity);
		txt.markAsTemporary();
		addComponent(txt);
		return txt;
	}

	public static Text drawString(String text, int x, int y, boolean centered) {
		final Text txt = new Text(Font.defaultFont, text, x, y, Font.defaultSize, Window.displayWidth / 2 - 40,
				centered);
		txt.setOpacity(opacity);
		txt.markAsTemporary();
		addComponent(txt);
		return txt;
	}

	public static Text drawString(String text, int x, int y, float fontSize, boolean centered) {
		final Text txt = new Text(Font.defaultFont, text, x, y, fontSize, Window.displayWidth / 2 - 40, centered);
		txt.setOpacity(opacity);
		txt.markAsTemporary();
		addComponent(txt);
		return txt;
	}

	public static Text drawString(String text, int x, int y, float fontSize, int lineWidth, boolean centered) {
		final Text txt = new Text(Font.defaultFont, text, x, y, fontSize, lineWidth, centered);
		txt.setOpacity(opacity);
		txt.markAsTemporary();
		addComponent(txt);
		return txt;
	}

	public static Text drawString(Text text) {
		addComponent(text);
		text.markAsTemporary();
		return text;
	}
	
	public static Text drawString(String text, float fontSize, boolean centered,
			Matrix4f worldMatrix) {
		final Text txt = new Text(Font.defaultFont, text, 0, 0, fontSize, Integer.MAX_VALUE, centered);
		txt.setOpacity(opacity);
		worldSpaceComponents.put(worldMatrix, txt);
		return txt;
	}
	
	public static Image drawImage(String texture, int x, int y, int w, int h, Matrix4f worldMatrix) {
		final Image img = new Image(texture, x, y);
		img.w = w;
		img.h = h / Window.getAspectRatio();
		img.setOpacity(opacity);
		worldSpaceComponents.put(worldMatrix, img);
		return img;
	}

	public static float getOpacity() {
		return opacity;
	}

	public static void create() {
		shader = new UIShader();
	}

	public static void removeComponent(Component component) {
		components.remove(component);
	}
	
	public static void update() {
		Iterator<Component> iter = components.iterator();
		while(iter.hasNext()) {
			if (iter.next().isTemporary()) {
				iter.remove();
			}
		}
		
		iter = worldSpaceComponents.values().iterator();
		while(iter.hasNext()) {
			if (iter.next().isTemporary()) {
				iter.remove();
			}
		}
	}

	public static void render(Scene scene) {

		prepare();
		
		shader.start();

		quad.bind(0, 1);

		for (final Component component : components) {
			if (component instanceof Image) {
				final Image image = (Image) component;
				image.gfx.bind(0);
				shader.color.loadVec3(image.getColor());
				shader.opacity.loadFloat(image.getOpacity());
				shader.translation.loadVec4(image.getTransform());
				shader.offset.loadVec4(image.getUvOffset());
				shader.centered.loadBoolean(image.isCentered());
				shader.rotation.loadFloat(image.getRotation());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
			} else {
				// GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_COLOR);
				final Text text = (Text) component;
				float dx = text.offsetX / (float)width;
				float dy = text.offsetY / (float)height;
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, text.getFont().getTexture().id);
				for (int i = 0; i < text.getLetters().length; i++) {
					final Image image = text.getLetters()[i];
					
					Vector4f transform = new Vector4f(image.getTransform());
					transform.x += dx;
					transform.y += dy;
					
					shader.color.loadVec3(image.getColor());
					shader.opacity.loadFloat(text.getOpacity());
					shader.translation.loadVec4(transform);
					shader.offset.loadVec4(image.getUvOffset());
					shader.centered.loadBoolean(image.isCentered());
					shader.rotation.loadFloat(image.getRotation());
					GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
				} // GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			
		}

		quad.unbind(0, 1);

		shader.stop();
		finish();
	}
	
	private static void prepare() {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
	}
	
	private static void finish() {
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public static void setOpacity(float newOpacity) {
		opacity = newOpacity;
	}

	public static void updateDepth(Component component) {
		if (components.contains(component)) {
			components.remove(component);
			addComponent(component);
		}
	}

	
}
