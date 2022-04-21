package com.heaven7.java.visitor;

public interface ThrowableVisitor extends Visitor<Throwable, Void>{
	
	 /**
	  * visit the throwable.
	  */
	 Void visit(Throwable t);
	 
}
