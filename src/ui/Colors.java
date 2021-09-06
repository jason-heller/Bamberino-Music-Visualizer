package ui;

import java.awt.Color;
import java.util.Random;

import org.joml.Vector3f;

public class Colors {

	public static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

	private static final long MARQUEE_SPEED_MS = 500;
	private static final long MARQUEE_COLOR_SIZE = 10;
	private static final long SPECTRUM_SPEED_MS = 3000;

	public static final Vector3f RED = new Vector3f(1, 0, 0);
	public static final Vector3f ORANGE = new Vector3f(.99f, .44f, 0);
	public static final Vector3f YELLOW = new Vector3f(1, 1f, 0);
	public static final Vector3f GREEN = new Vector3f(0, 1f, 1f);
	public static final Vector3f CYAN = new Vector3f(0.61f, .9f, .9f);
	public static final Vector3f BLUE = new Vector3f(.078f, 0.596f, 1);
	public static final Vector3f PINK = new Vector3f(1, .4f, .7f);
	public static final Vector3f INDIGO = new Vector3f(0.54f, 0.16f, .88f); // 138-43-226
	public static final Vector3f VIOLET = new Vector3f(0.93f, .51f, 0.94f); // 238-130-238
	public static final Vector3f PURPLE = new Vector3f(0.5f, 0f, 0.5f);
	public static final Vector3f DK_VIOLET = new Vector3f(0.58f, 0f, 0.82f);
	public static final Vector3f BLACK = new Vector3f(0, 0, 0);
	public static final Vector3f WHITE = new Vector3f(1, 1, 1);
	public static final Vector3f LT_SILVER = new Vector3f(.8f, .8f, .8f);
	public static final Vector3f SILVER = new Vector3f(0.6f, 0.6f, 0.6f);
	public static final Vector3f GREY = new Vector3f(0.37f, 0.37f, 0.37f);
	public static final Vector3f DK_GREY = new Vector3f(0.2f, 0.2f, 0.2f);
	public static final Vector3f GOLD = new Vector3f(.83f, .68f, .21f);

	public static final Vector3f TEAL = hexToRgb("008080");

	public static final Vector3f GUI_BORDER_COLOR = DK_GREY;
	public static final Vector3f GUI_BACKGROUND_COLOR = BLACK;
	public static final Vector3f GUI_ACCENT_COLOR = DK_VIOLET;

	public static Vector3f alertColor() {
		final float alpha = 0.5f + (float) Math.sin(System.currentTimeMillis() % 1000 / 250f) * 0.5f;
		return new Vector3f(alpha, alpha / 4f, alpha / 9f);
	}

	public static Vector3f getColor(char c, int pos) {
		switch (c) {
		case 'r':
			return RED;
		case 'o':
			return ORANGE;
		case 'y':
			return YELLOW;
		case 'g':
			return GREEN;
		case 'c':
			return CYAN;
		case 'b':
			return BLUE;
		case 'p':
			return PINK;
		case 'i':
			return INDIGO;
		case 'v':
			return VIOLET;
		case '0':
			return BLACK;
		case 'R':
			return hsvToRgb(pos % 20 / 20.1f, .85f, 1f);
		case '*':
			return spectrum(.85f, 1f);
		case 'M':
			return scrollColor(pos);
		case 'A':
			return alertColor();
		case 's':
			return LT_SILVER;
		case 'S':
			return SILVER;
		case 'G':
			return GOLD;
		case 'w':
		default:
			return WHITE;
		}
	}

	private static Vector3f spectrum(float saturation, float value) {
		float hue = (System.currentTimeMillis() % SPECTRUM_SPEED_MS / (float) SPECTRUM_SPEED_MS);
		return hsvToRgb(hue, saturation, value);
	}

	public static Vector3f random(int seed) {
		Random r = new Random();
		r.setSeed(seed);
		return new Vector3f(r.nextFloat(), r.nextFloat(), r.nextFloat());
	}

	public static Vector3f hexToRgb(String hex) {
		String rHex = hex.substring(0, 2);
		String gHex = hex.substring(2, 4);
		String bHex = hex.substring(4);

		float red = Integer.parseInt(rHex, 16) / 255f;
		float green = Integer.parseInt(gHex, 16) / 255f;
		float blue = Integer.parseInt(bHex, 16) / 255f;

		return new Vector3f(red, green, blue);
	}

	public static Vector3f hsvToRgb(float hue, float saturation, float value) {
		final int h = (int) (hue * 6);
		final float f = hue * 6 - h;
		final float p = value * (1 - saturation);
		final float q = value * (1 - f * saturation);
		final float t = value * (1 - (1 - f) * saturation);

		switch (h) {
		case 0:
			return new Vector3f(value, t, p);
		case 1:
			return new Vector3f(q, value, p);
		case 2:
			return new Vector3f(p, value, t);
		case 3:
			return new Vector3f(p, q, value);
		case 4:
			return new Vector3f(t, p, value);
		case 5:
			return new Vector3f(value, p, q);
		default:
			throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", "
					+ saturation + ", " + value);
		}
	}

	public static Vector3f scrollColor(int pos) {
		pos += MARQUEE_COLOR_SIZE;
		final int scrollPos = (int) (System.currentTimeMillis() % MARQUEE_SPEED_MS / (float) MARQUEE_SPEED_MS
				* MARQUEE_COLOR_SIZE);
		final float alpha = (pos - scrollPos % MARQUEE_COLOR_SIZE) % MARQUEE_COLOR_SIZE / (float) MARQUEE_COLOR_SIZE;
		return new Vector3f(alpha, alpha, alpha);
	}

	public static Vector3f rgbToHsv(float r, float g, float b) {
		float[] hsv = new float[3];
		Color.RGBtoHSB((int)(r * 255), (int)(g * 255), (int)(b * 255), hsv);
		return new Vector3f(hsv[0], hsv[1], hsv[2]);
	}
}
