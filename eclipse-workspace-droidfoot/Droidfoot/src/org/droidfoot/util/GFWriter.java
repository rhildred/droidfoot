package org.droidfoot.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.List;

public class GFWriter {
	private PrintWriter pw;

	public GFWriter(Writer writer) {
		this.pw = new PrintWriter(writer);
	}

	public void writeAll(List<String[]> list) throws IOException {
		for (String[] entry : list) {
			for (String str : entry) {
				this.pw.println(str);
			}
		}
	}

	public void testWrite() throws IOException {
		this.pw.println("test");
	}

	public void close() throws IOException {
		pw.flush();
		pw.close();
	}

}
