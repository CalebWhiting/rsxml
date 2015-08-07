package com.rsxml.xml.clazz;

import com.rsxml.xml.*;
import jdk.internal.org.objectweb.asm.tree.*;

/**
 * @author Caleb Whiting
 */
public class XMLSuperclass extends XMLClassFilter {

	String name;

	@Override
	public void setTextContent(String textContent) {
		name = textContent;
	}

	@Override
	public boolean match(ClassNode node) {
		String name = this.name;
		if (name.startsWith("@") && name.endsWith("@")) {
			name = name.substring(1, name.length() - 1);
			ClassNode n = XMLProcessor.getIdClassMap().get(name);
			name = n == null ? "Unidentified" : n.name;
		}
		name = name.replace('.', '/');
		return node.superName.equals(name);
	}

}
