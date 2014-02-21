package ui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MenifestGenerator {
	public static void main(String[] args) throws IOException {
		gen();
	}
	
	public static void gen() throws IOException {
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("META-INF/MANIFEST.MF")));
		bw.write("Manifest-Version: 1.0\n");
		bw.write("Class-Path: ");
		final File libDir = new File("lib");
		File[] files = libDir.listFiles();
		for (File file : files) {
			bw.write(file.getPath() + " ");
		}
		bw.write("\n");
		bw.write("Main-Class: ");
		bw.write(Main.class.getName() + "\n\n");
		bw.close();
	}
}
