package com.heaven7.java.visitor.collection;

/**
 * the iterate control used to control the order of all operate ( {@linkplain VisitService#OP_FILTER}} and etc.).
 * But {@linkplain VisitService#OP_INSERT} only used for List.
 * <p>
 * the default order of iterate is :<br> 
 * first({@linkplain VisitService#OP_DELETE} ).<br> 
 * second({@linkplain VisitService#OP_FILTER} ).<br> 
 * then({@linkplain VisitService#OP_UPDATE} ).<br> 
 * last({@linkplain VisitService#OP_INSERT} ).<br> 
 * </p>
 * <pre>
 	public void testIterationControl(){
		mService.beginIterateControl()
		.first(OP_UPDATE)
		.second(OP_FILTER)
		.then(OP_DELETE)
		.last(OP_INSERT)
		.end();
	}</br>after call this the order is : <br> OP_UPDATE ->OP_FILTER -> OP_DELETE -> OP_INSERT
 * </pre>
 * <pre>
 	public void testIterationControl2(){
		mService.beginIterateControl()
		.first(OP_UPDATE) // 4
		.first(OP_FILTER) //3
		.first(OP_DELETE) //2
		.first(OP_INSERT) //1
		.end();
	}</br>after call this the order is : <br> OP_INSERT ->OP_DELETE -> OP_FILTER -> OP_UPDATE
 * </pre>
 * @author heaven7
 *
 * @param <T> the type 
 * @see {@linkplain VisitService#OP_DELETE}
 * @see {@linkplain VisitService#OP_FILTER}
 * @see {@linkplain VisitService#OP_UPDATE}
 * @see {@linkplain VisitService#OP_INSERT}
 */
public abstract class IterateControl<T> {
	
	IterateControl(){}

	/**
	 *  end the iterate control and return the original object.
	 * @return the original object
	 */
	public abstract T end();

	/**
	 * intercept the target operate if visit success.
	 * @param operate the operate. see {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public abstract IterateControl<T> interceptIfSuccess(int operate);

	/**
	 * define the target operate run the first in current iteration, but it may change. such as recall {@linkplain #first(int)}}.the more to see in demo.
	 * @param operate the operate. see {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public abstract IterateControl<T> first(int operate);

	/**
	 * define the target operate run the second in current iteration. but may change, the more to see in demo.
	 * @param operate the operate. see {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public abstract IterateControl<T> second(int operate);

	/**
	 * define the target operate run after the {@linkplain #second(int)} in current iteration, but may change, the more to see in demo.
	 * @param operate the operate. see {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public abstract IterateControl<T> then(int operate);

	/**
	 * define the target operate run the last in current iteration, but may change, the more to see in demo.
	 * @param operate the operate. see {@linkplain CollectionVisitServiceImpl#OP_DELETE}} and etc.
	 * @return this.
	 */
	public abstract IterateControl<T> last(int operate);

}