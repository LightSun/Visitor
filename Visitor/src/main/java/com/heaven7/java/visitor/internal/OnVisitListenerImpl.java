package com.heaven7.java.visitor.internal;

public class OnVisitListenerImpl implements OnVisitListener{

	@Override
	public void onVisitStart(OperateInterceptor interceptor) {
		interceptor.begin();
	}

	@Override
	public void onVisitEnd(OperateInterceptor interceptor) {
		interceptor.end();
	}

}
