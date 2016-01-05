package org.droidfoot.util;

import greenfoot.UserInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class GFReader {
	private BufferedReader br;

	public GFReader(Reader reader) {
		br = new BufferedReader(reader);
	}

	List<String[]> readAll() throws IOException {
		ArrayList<String[]> entries = new ArrayList<String[]>();
		String[] entry = new String[2 + UserInfo.NUM_INTS
				+ UserInfo.NUM_STRINGS];
		entry[0] = br.readLine();
		while (entry[0] != null) {
			entry[1] = br.readLine();
			for (int i = 0; i < UserInfo.NUM_INTS; i++) {
				entry[2 + i] = br.readLine();
			}
			for (int i = 0; i < UserInfo.NUM_STRINGS; i++) {
				entry[2 + UserInfo.NUM_INTS + i] = br.readLine();
			}
			entries.add(entry);

			entry = new String[2 + UserInfo.NUM_INTS + UserInfo.NUM_STRINGS];
			entry[0] = br.readLine();
		}
		return entries;
	}

	public void close() throws IOException {
		br.close();
	}

}
