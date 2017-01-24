package com.heaven7.java.visitor.collection;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.heaven7.java.visitor.MapPredicateVisitor;
import com.heaven7.java.visitor.MapResultVisitor;
import com.heaven7.java.visitor.anno.Nullable;

public interface MapVisitService<K, V> extends VisitService{
	

	<R> Set<R> visitForResultSet(Object param, MapPredicateVisitor<? super K, ? super V> predicate,
	         MapResultVisitor<? super K, ? super V, R> resultVisitor);
	
	<R> Set<R> visitForResultSet( MapPredicateVisitor<? super K, ? super V> predicate,
			MapResultVisitor<? super K, ? super V, R> resultVisitor);
	
	<R> R visitForResult(Object param, MapPredicateVisitor<? super K, ? super V> predicate,
			         MapResultVisitor<? super K, ? super V, R> resultVisitor);
	
	<R> R visitForResult(MapPredicateVisitor<? super K, ? super V> predicate,
			MapResultVisitor<? super K, ? super V, R> resultVisitor);
	
	Set<KeyValuePair<K, V>> visitForQuerySet(Object param, MapPredicateVisitor<? super K, ? super V> predicate);
	
	Set<KeyValuePair<K, V>> visitForQuerySet(MapPredicateVisitor<? super K, ? super V> predicate);

	KeyValuePair<K, V> visitForQuery(Object param, MapPredicateVisitor<? super K, ? super V> predicate);

	KeyValuePair<K, V> visitForQuery(MapPredicateVisitor<? super K, ? super V> predicate);
	
	public static abstract class MapOperateInterceptor<K,V>{

		/**
		 * intercept the current iteration.
		 * @param map the map
		 * @param entry the entry
		 * @param param the parameter, may be null.
		 * @param info the IterationInfo.
		 * @return true if intercept success, this means the loop of 'for' will be 'continue'. 
		 */
		public abstract boolean intercept(Map<K, V> map, Entry<K, V> entry, @Nullable Object param, IterationInfo info);
	}
	
	public static abstract class MapOperateManager<K, V>{
		
		public abstract MapVisitService<K, V> end() ;
		
	}

}
