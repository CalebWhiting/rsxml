package com.rsxml.xml.clazz;

import jdk.internal.org.objectweb.asm.tree.*;
import org.w3c.dom.*;

/**
 * @author Caleb Whiting
 */
public abstract class XMLClassFilter {

	public void setAttributes(NamedNodeMap attributes) {

	}

	public void setTextContent(String textContent) {

	}

	public abstract boolean match(ClassNode node);

}
