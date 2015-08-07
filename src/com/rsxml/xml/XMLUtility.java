package com.rsxml.xml;

import org.w3c.dom.*;

import java.util.function.*;

/**
 * @author Caleb Whiting
 */
public class XMLUtility {

	public static String getNodeProperty(Node node, String key) {
		NamedNodeMap attributes = node.getAttributes();
		Node item = attributes.getNamedItem(key);
		return item == null ? null : item.getTextContent();
	}

	public static void forEach(NodeList list, Consumer<Node> consumer) {
		for (int i = 0; i < list.getLength(); i++) {
			consumer.accept(list.item(i));
		}
	}

	public static String toCamelCase(String tag) {
		String camel = "";
		for (int j = 0; j < tag.length(); j++) {
			char ch = tag.charAt(j);
			if (j == 0) {
				camel += Character.toUpperCase(ch);
			} else if (ch == '-') {
				camel += Character.toUpperCase(tag.charAt(j + 1));
				j++;
			} else {
				camel += ch;
			}
		}
		return camel;
	}

}

