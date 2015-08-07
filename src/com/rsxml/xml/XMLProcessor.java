package com.rsxml.xml;

import com.rsxml.xml.clazz.*;
import jdk.internal.org.objectweb.asm.tree.*;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;

/**
 * @author Caleb Whiting
 */
public class XMLProcessor {

	private static final Logger log = Logger.getLogger("XMLProcessor");

	private static final Map<String, ClassNode> ID_CLASS_MAP = new HashMap<>();
	private static final Map<String, ClassNode> LIBRARY = new HashMap<>();

	public static void parseProcessors(URL url) throws Exception {
		try (InputStream in = url.openStream()) {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			NodeList classes = document.getElementsByTagName("class");
			XMLUtility.forEach(classes, XMLProcessor::findClass);
		}
	}

	private static void findClass(Node node) {
		List<ClassNode> matches = new ArrayList<>();
		String key = XMLUtility.getNodeProperty(node, "key");
		NodeList identifier = node.getChildNodes();
		List<XMLClassFilter> identifiers = new LinkedList<>();
		XMLUtility.forEach(identifier, process -> {
			String tag = process.getNodeName();
			if (tag.startsWith("#"))
				return;
			try {
				String className = "com.rsxml.xml.clazz.XML" + XMLUtility.toCamelCase(tag);
				Class c = Class.forName(className);
				XMLClassFilter ci = (XMLClassFilter) c.newInstance();
				ci.setAttributes(process.getAttributes());
				ci.setTextContent(process.getTextContent());
				identifiers.add(ci);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		LIBRARY.values().forEach(c -> {
			// assign the given class as the identified class so that the identifier
			// can look for instances of its self
			ID_CLASS_MAP.put(key, c);
			for (XMLClassFilter i : identifiers) {
				if (!i.match(c)) {
					return;
				}
			}
			matches.add(c);
		});
		if (matches.size() != 1) {
			throw BadMatcherException.newInstance(key, matches, c -> c.name);
		}
		ID_CLASS_MAP.put(key, matches.get(0));
		log.info(key + " matches " + matches.get(0).name);
	}

	public static Map<String, ClassNode> getIdClassMap() {
		return ID_CLASS_MAP;
	}

	public static Map<String, ClassNode> getLibrary() {
		return LIBRARY;
	}

}
