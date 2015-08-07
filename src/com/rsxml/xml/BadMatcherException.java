package com.rsxml.xml;

import java.util.*;
import java.util.function.*;

/**
 * @author Caleb Whiting
 */
public class BadMatcherException extends RuntimeException {

	public BadMatcherException(String message) {
		super(message);
	}

	public static <T> BadMatcherException newInstance(String key, List<T> matches, Function<T, String> func) {
		String message = "Bad matcher '" + key + "' [";
		for (int i = 0; i < matches.size(); i++) {
			if (i > 0) message += ", ";
			T o = matches.get(i);
			message += func == null ? String.valueOf(o) : func.apply(o);
		}
		message += "]";
		return new BadMatcherException(message);
	}

}
