package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.anno.Independence;
import com.heaven7.java.visitor.collection.CollectionVisitService.OperateManager;

/**
 * a super visit service of collection or map.
 * @author heaven7
 * @since 1.1.1
 */
public interface VisitService<T extends VisitService<T>> {
	
	/**
	 *  a flag operate manager flag , correspond to {@linkplain OperateManager}, often used for reset.
	 */
	int FLAG_OPERATE_MANAGER          = 0x00000001;
	
	/**
	 *  a flag , named iterate control, correspond to {@linkplain IterateControl},  
	 */
	int FLAG_OPERATE_ITERATE_CONTROL  = 0x00000002;
	
	
	/**
	 * contains the all flags.
	 * @since 1.1.2
	 */
	int FLAG_ALL = FLAG_OPERATE_MANAGER | FLAG_OPERATE_ITERATE_CONTROL;
	
	
	/**
	 * reset current visit service by target flags.
	 * @param flags {@linkplain #FLAG_OPERATE_MANAGER} or {@linkplain #FLAG_OPERATE_ITERATE_CONTROL}.
	 * @return this.
	 */
	@Independence
	T reset(int flags);
	
	/**
	 * reset all operations. a convenient method for call {@linkplain #reset(int)} that use all flags.
	 * @return this.
	 */
	@Independence
	T resetAll();

	/**
	 * clear the collection or map without any other operation.
	 * @return this.
	 * @since 1.1.8
	 */
	@Independence
	T clear();

}
