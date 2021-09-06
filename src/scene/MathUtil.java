package scene;

public class MathUtil {
	public static float angleLerp(float start, float end, float amount) {
		start += 360;
		end += 360;
		start %= 360;
		end %= 360;

		if (end - start < 180) {
			return lerp(start, end, amount);
		} else {
			return lerp(start + 360, end, amount);
		}
	}
	
	public static float lerp(float s, float t, float amount) {
		return s * (1f - amount) + t * amount;
	}

	public static float pointDirection(float x1, float y1, float x2, float y2) {
		float dx, dy;
		dy = y2 - y1;
		dx = x2 - x1;

		return (float) -(Math.atan2(dy, dx));
	}
}
