package com.rsxml.xml.clazz;

import com.rsxml.xml.*;
import jdk.internal.org.objectweb.asm.*;
import jdk.internal.org.objectweb.asm.tree.*;
import org.w3c.dom.*;

import java.util.*;

/**
 * @author Caleb Whiting
 */
public class XMLMethodCount extends XMLClassFilter {

	private String forcedOwner;
	private String name;
	private String desc;
	private Mode mode;
	private int amount;

	@Override
	public void setAttributes(NamedNodeMap attributes) {
		Node amount = attributes.getNamedItem("amount");
		this.amount = amount == null ? -1 : Integer.parseInt(amount.getTextContent());
		Node mode = attributes.getNamedItem("mode");
		this.mode = mode == null ? Mode.EQUALS : Mode.getConstant(mode.getTextContent());
		Node desc = attributes.getNamedItem("desc");
		this.desc = desc == null ? "" : desc.getTextContent();
		Node name = attributes.getNamedItem("name");
		this.name = name == null ? "" : name.getTextContent();
		// than operators aren't allowed in xml files
		if (this.name.equals("$init")) this.name = "<init>";
		if (this.name.equals("$clinit")) this.name = "<clinit>";
		Node owner = attributes.getNamedItem("owner");
		this.forcedOwner = owner == null ? null : owner.getTextContent();
	}

	@Override
	public boolean match(ClassNode node) {
		String desc = this.desc;
		for (Map.Entry<String, ClassNode> entry : XMLProcessor.getIdClassMap().entrySet()) {
			String key = entry.getKey();
			desc = desc.replace("@" + key + "@", entry.getValue().name);
		}
		String owner = node.name;
		if (forcedOwner != null) {
			owner = forcedOwner;
			if (owner.startsWith("@") && owner.endsWith("@")) {
				owner = owner.substring(1, owner.length() - 1);
				ClassNode n = XMLProcessor.getIdClassMap().get(owner);
				owner = n == null ? "Unidentified" : n.name;
			}
			owner = owner.replace('.', '/');
		}
		int value = getMethodCount(XMLProcessor.getLibrary().get(owner), name, desc);
		return mode.isNumericMatch(value, amount);
	}

	private int getMethodCount(ClassNode c, String name, String desc) {
		int i = 0;
		for (MethodNode fn : c.methods) {
			if ((fn.access & Opcodes.ACC_STATIC) == 0 &&
			    (desc.length() == 0 || desc.equals(fn.desc)) &&
			    (name.length() == 0 || name.equals(fn.name))) {
				i++;
			}
		}
		return i;
	}

}
