package asset;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import audio.AudioHandler;
import audio.Source;
import core.App;
import scene.Globals;
import ui.Colors;

public class SceneFileReader {
	public static Map<String, List<Source>> load(String path) {
		Map<String, List<Source>> sources = new HashMap<>();
		
		try {
			List<String> lines = Files.readAllLines(new File(path).toPath());
			
			Source source = null;
			boolean inBrackets = false;
			
			for(String line : lines) {
				if (line.startsWith("#"))
					continue;
				
				if (inBrackets) {
					if (line.contains("}")) {
						inBrackets = false;
						source = null;
					}
					
					String[] data = parseData(line);
					
					if (source == null) {
						switch(data[0]) {
						case "title":
							Globals.title = data[1];
							break;
						case "subtitle":
							Globals.subTitle = data[1];
							break;
						case "title_x":
							Globals.titleX = Integer.parseInt(data[1]);
							break;
						case "title_y":
							Globals.titleY = Integer.parseInt(data[1]);
							break;
						case "start_time":
							AudioHandler.playTimeMs = Long.parseLong(data[1]);
							break;
						case "background_color":
							Globals.bgColor = Colors.hexToRgb(data[1].replaceFirst("#", ""));
							break;
						case "file":
							source = new Source(System.getProperty("user.dir") + "/song/" + data[1]);
							break;
						}
					} else {
						switch(data[0]) {
						case "scene_reactor":
							String[] subdata = data[1].split(" ");
							List<Source> srcs = sources.get(subdata[0]);
							if (srcs == null) {
								srcs = new ArrayList<>();
								sources.put(subdata[0], srcs);
							}
							srcs.add(source);
							
							if (subdata.length > 1)
								source.setReactionIndex(Integer.parseInt(subdata[1]));
							
							break;
						}
					}
				}
				else if (line.contains("{")) {
					inBrackets = true;
				}
			}
		} catch (IOException e) {
			App.handleCrash(new IOException("Could not read song.txt file, problem with line " + e.getStackTrace()[0].getLineNumber()));
		}
		
		return sources;
	}

	private static String[] parseData(String line) {
		List<String> data = new ArrayList<>();
		String out = "";

		boolean inQuotes = false;
		for (int i = 0; i < line.length(); i++) {
			char c = line.charAt(i);

			if (!inQuotes) {
				if (c == '\"' || c == '\'') {
					inQuotes = true;
					continue;
				}

				if (c == ' ' || c == '\t' || c == '\b')
					continue;

				if (c == '=') {
					data.add(out);
					out = "";
					continue;
				}

				out += Character.toLowerCase(c);
			} else {
				if (c == '\"' || c == '\'') {
					inQuotes = false;
					continue;
				}
				
				/*if (c == '/') {
					out += "\\";
					continue;
				}*/

				out += c;
			}
		}

		data.add(out);
		String[] finalOut = new String[data.size()];
		
		for (int i = 0; i < finalOut.length; i++)
			finalOut[i] = data.get(i);
		
		return finalOut;
	}
}
