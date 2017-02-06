package com.heaven7.java.visitor.internal;

import static com.heaven7.java.visitor.collection.Operation.*;

public final class InternalUtil {

	public static String op2String(int op) {
		switch (op) {
		case OP_DELETE:
			return "OP_DELETE";

		case OP_FILTER:
			return "OP_FILTER";

		case OP_UPDATE:
			return "OP_UPDATE";

		case OP_INSERT:
			return "OP_INSERT";

		case OP_TRIM:
			return "OP_TRIM";
		default:
			return null;
		}
	}

}
