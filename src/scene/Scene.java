package scene;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import audio.AudioHandler;
import scene.kitchen.CabinetMeshes;
import scene.kitchen.CutleryMesh;
import scene.kitchen.DrawerMeshes;
import scene.kitchen.FlameMeshes;
import scene.kitchen.StaticMeshes;
import scene.kitchen.TomatoMesh;
import ui.Colors;
import ui.Text;
import ui.UI;

public class Scene {
	private Map<String, IRender> renderables = new HashMap<>();
	
	private Camera camera;
	
	private FlatShader shader;
	
	private AudioHandler audioHandler;
	
	private StaticMeshes staticMeshes = new StaticMeshes();
	private CutleryMesh cutleryMesh = new CutleryMesh();
	private DrawerMeshes drawerMeshes = new DrawerMeshes();
	private CabinetMeshes cabinetMeshes = new CabinetMeshes();
	private TomatoMesh tomatoMesh = new TomatoMesh();
	private FlameMeshes flameMeshes = new FlameMeshes();
	
	private Text title;
	private Text subTitle;
	
	public Scene() {
		camera = new Camera();
		shader = new FlatShader();
		
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		
		addRenderable("", staticMeshes);
		addRenderable("cutlery", cutleryMesh);
		addRenderable("drawer", drawerMeshes);
		addRenderable("tomato", tomatoMesh);
		addRenderable("cabinet", cabinetMeshes);
		addRenderable("burner", flameMeshes);
		
		audioHandler = new AudioHandler("song/song.txt", renderables);

		//title = new Text(Globals.title, Globals.titleX, Globals.titleY, .6f, true);
		//subTitle = new Text(Globals.subTitle, Globals.titleX, Globals.titleY + 40, .3f, true);
		
		//UI.addComponent(title);
		//UI.addComponent(subTitle);
	}
	
	public void addRenderable(String reactable, IRender renderable) {
		renderables.put(reactable, renderable);
	}
	
	public void draw() {
		camera.update();
		audioHandler.update();
		
		shader.start();
		shader.viewProj.loadMatrix(camera.getProjectionView());
		shader.lightVec.loadVec3(camera.getDirectionVector());
		
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(Globals.bgColor.x, Globals.bgColor.y, Globals.bgColor.z, 1f);
		
		/*Vector3f color = Colors.rgbToHsv(Globals.bgColor.x, Globals.bgColor.y, Globals.bgColor.z);
		color.x = (color.x + Window.deltaTime * .05f) % 1f;
		Globals.bgColor.set(Colors.hsvToRgb(color.x, color.y, color.z));*/

		for(IRender renderable : renderables.values()) {
			renderable.draw(shader);
		}
		
		shader.stop();
		
		//title.setOpacity(Globals.titleOpacity);
		//subTitle.setOpacity(Globals.titleOpacity);
		
		UI.drawString(Globals.title, Globals.titleX, Globals.titleY, .6f, true).setOpacity(Globals.titleOpacity);
		UI.drawString(Globals.subTitle, Globals.titleX, Globals.titleY + 40, .3f, true).setOpacity(Globals.titleOpacity);
		
		// Song duration
		int s = audioHandler.getRemainingSongDuration();
		String songTime = String.format("%02d:%02d", (s % 3600) / 60, (s % 60));
		
		// Timer
		UI.drawString("#c" + songTime, 1168, 529, .25f, false);
	}
	
	public void clean() {
		shader.clean();
		audioHandler.clean();
		
		//UI.removeComponent(title);
		//UI.removeComponent(subTitle);
		
		for(IRender renderable : renderables.values()) {
			renderable.clean();
		}
	}
}
