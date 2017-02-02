package com.heaven7.java.visitor.internal;

public interface OnVisitListener {
	
	void onVisitStart(OperateInterceptor interceptor);

	void onVisitEnd(OperateInterceptor interceptor);
}
