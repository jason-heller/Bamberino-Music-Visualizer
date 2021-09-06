package core;

import java.awt.GridLayout;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ErrorWindow {
	private JFrame window;
	private JTextArea textArea;
	
	public ErrorWindow(Exception e) {
		window = new JFrame();
		window.setLayout(new GridLayout(0,1));
		textArea = new JTextArea();
		
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String sStackTrace = sw.toString();
		try {
			sw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		pw.close();
		
		textArea.setText("The application has crashed. Stack trace:\n\n" + sStackTrace);
		textArea.setRows(40);
		textArea.setColumns(80);
		window.add(textArea);
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		
		window.addWindowListener(new java.awt.event.WindowAdapter() {
		    @Override
		    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
		        App.active = true;
		    }
		});
	}
}
