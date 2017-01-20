package com.heaven7.java.visitor.collection;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.heaven7.java.visitor.anno.IntDef;

/**
 * the constant of collections. Created by heaven7 on 2017/1/15.
 * 
 * @since 1.0
 */
public class VisitConstant {
	/**
	 * the visit rule: until success.
	 */
	public static final int VISIT_RULE_UNTIL_SUCCESS = 1;
	/**
	 * the visit rule: until failed.
	 */
	public static final int VISIT_RULE_UNTIL_FAILED = 2;
	/**
	 * the visit rule: visit all.
	 */
	public static final int VISIT_RULE_ALL = 3;
	/**
	 * the visit result rule: until null
	 */
	public static final int VISIT_RESULT_RULE_UNTIL_NULL = 4;
	/**
	 * the visit result rule: until not null.
	 */
	public static final int VISIT_RESULT_RULE_UNTIL_NOT_NULL = 5;

	public static final int OP_DELETE = 1;
	public static final int OP_UPDATE = 2;
	public static final int OP_QUERY = 3;

	public static final int OP_MASK = 0xff;

	public static final int FLAG_RESULT = 0x100;
	public static final int FLAG_LIMIT_COUNT = 0x200;
	public static final int FLAG_REVERSE = 0x400;
	public static final int FLAG_SKIP = 0x800;

	@Retention(RetentionPolicy.CLASS)
	@IntDef({ VISIT_RULE_UNTIL_SUCCESS, VISIT_RULE_UNTIL_FAILED, VISIT_RULE_ALL, })
	public @interface VisitRuleType {
	}

}
