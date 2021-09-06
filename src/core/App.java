package core;

import java.io.File;

import org.lwjgl.opengl.Display;

import scene.Scene;
import ui.UI;

public class App {
	
	public static boolean active = true;
	private static Scene scene;
	
	public static void main(String[] args) {
		initOpenGL();

		Window.create();
		UI.create();
		scene = new Scene();
		
		while ((!Display.isCloseRequested()) && active) {
			Window.update();
			scene.draw();
			UI.render(scene);
			UI.update();
		}

		scene.clean();
		UI.clean();
		Window.destroy();
		
		while(!active) {}
		
		System.exit(0);
	}

	private static void initOpenGL() {
		String ext = "";
		String operatingSystem = System.getProperty("os.name").toLowerCase();
		String nativesPath =  new File("lib").getAbsolutePath();
		if (operatingSystem.contains("win")) {
			ext = "windows";
		} else if (operatingSystem.contains("lin")) {
			ext = "linux";
		} else if (operatingSystem.contains("mac")) {
			ext = "macosx";
		} else if (operatingSystem.contains("sol")) {
			ext = "solaris";
		} else {
			//throw new Exception("Unsupported operating system: " + operatingSystem);
			System.exit(-1);
		}

		System.setProperty("org.lwjgl.librarypath", nativesPath + "/" + ext);
	}

	public static void handleCrash(Exception e) {
		if (active) {
			active = false;
			new ErrorWindow(e);
		}
	}
}
