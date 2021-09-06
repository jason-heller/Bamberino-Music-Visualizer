package scene;

public interface IRender {
	public void draw(FlatShader shader);
	public void setReactionValue(int index, float value);
	public void clean();
}
