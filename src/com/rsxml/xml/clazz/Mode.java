package com.rsxml.xml.clazz;

/**
 * @author Caleb Whiting
 */
enum Mode {
	EQUALS,
	MORE,
	LESS;

	public static Mode getConstant(String name) {
		for (Mode mode : values())
			if (mode.name().equalsIgnoreCase(name))
				return mode;
		throw new IllegalArgumentException("No enum constant " + name);
	}

	public boolean isNumericMatch(int value, int target) {
		if (target == -1 && value > 0) {
			return true;
		}
		switch (this) {
			case EQUALS:
				return value == target;
			case MORE:
				return value > target;
			case LESS:
				return value < target;
		}
		throw new IllegalArgumentException("Unsupported Mode: " + this);
	}

}
