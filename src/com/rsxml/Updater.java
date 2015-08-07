package com.rsxml;

import com.rspsbot.*;
import com.rspsbot.util.*;

import com.rsxml.xml.*;

import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.jar.*;
import java.util.logging.*;

/**
 * @author Caleb Whiting
 */
public class Updater {

	private static final Logger log = Logger.getLogger("Updater");

	public static void main(String[] args) {
		Log.apply();
		try {
			log.info("Attempting to update jar...");
			update();
			log.info("Completed update process");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static void update() throws Exception {
		File local = new File(Configuration.getHome(), "client.jar");
		JarUtility.update(local, new URL(Application.EXTERNAL_PACK));
		Map<String, ClassNode> library = new HashMap<>();
		try (JarInputStream j = new JarInputStream(new FileInputStream(local))) {
			for (JarEntry e; (e = j.getNextJarEntry()) != null; ) {
				if (e.getName().endsWith(".class")) {
					ClassNode cn = new ClassNode();
					ClassReader reader = new ClassReader(j);
					reader.accept(cn, ClassReader.SKIP_DEBUG);
					library.put(cn.name, cn);
				}
			}
		}
		XMLProcessor.getLibrary().putAll(library);
		URL url = new File("updater.xml").toURI().toURL();
		XMLProcessor.parseProcessors(url);
	}

}
