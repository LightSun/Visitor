package com.heaven7.java.visitor.internal;

import com.heaven7.java.visitor.collection.CollectionOperation;
import com.heaven7.java.visitor.collection.MapOperation;

import java.util.LinkedList;
import java.util.List;

/**
 * the operation pool help use {@linkplain CollectionOperation} and {@linkplain MapOperation} 
 * for memory optimization. this class is synchronized.
 * @author heaven7
 * @since 1.1.8
 */
@SuppressWarnings("rawtypes")
public final class OperationPools {

    private static final int MAX_POOL_SIZE = 10;
    private static int sPOOL_SIZE_COP = 0;
    private static int sPOOL_SIZE_MOP = 0;

	private static final LinkedList<CollectionOperation> sPOOL_COLLECTION;
    private static final LinkedList<MapOperation> sPOOL_MAP;
    private static final Object sLOCK_COLLECTION;
    private static final Object sLOCK_MAP;

    static {
        sPOOL_COLLECTION = new LinkedList<CollectionOperation>();
        sPOOL_MAP = new LinkedList<MapOperation>();
        sLOCK_COLLECTION = new Object();
        sLOCK_MAP = new Object();
    }

    @SuppressWarnings("unchecked")
	public static <T> CollectionOperation<T> obtainCollectionOperation() {
        CollectionOperation co;
        synchronized (sLOCK_COLLECTION) {
            if ((co = sPOOL_COLLECTION.pollFirst()) != null) {
                sPOOL_SIZE_COP--;
            }
        }
        if (co == null) {
            return new CollectionOperation<T>() {
            };
        }
       // System.out.println("CollectionOperation: after obtainCollectionOperation(), pool size = " + sPOOL_SIZE_COP);
        return co;
    }

    public static void recycle(CollectionOperation cop) {
        if(cop != null) {
            cop.reset();
            synchronized (sLOCK_COLLECTION) {
                if (sPOOL_SIZE_COP < MAX_POOL_SIZE) {
                    sPOOL_COLLECTION.offerLast(cop);
                    sPOOL_SIZE_COP++;
                }
            }
          //  System.out.println("CollectionOperation: after recycle() pool size = " + sPOOL_SIZE_COP);
        }
    }

    public static void recycle(MapOperation mop) {
        if(mop != null) {
            mop.reset();
            synchronized (sLOCK_MAP) {
                if (sPOOL_SIZE_MOP < MAX_POOL_SIZE) {
                    sPOOL_MAP.offerLast(mop);
                    sPOOL_SIZE_MOP++;
                }
            }
          //  System.out.println("MapOperation: after recycle() pool size = " + sPOOL_SIZE_MOP);
        }
    }

    @SuppressWarnings("unchecked")
	public static <K, V> MapOperation<K, V> obtainMapOperation() {
        MapOperation mop;
        synchronized (sLOCK_MAP) {
            if ((mop = sPOOL_MAP.pollFirst()) != null) {
                sPOOL_SIZE_MOP --;
            }
        }
        if (mop == null) {
            return new MapOperation<K, V>() {
            };
        }
       // System.out.println("MapOperation: after obtainMapOperation() pool size = " + sPOOL_SIZE_MOP);
        return mop;
    }
    public static <T> void recycleAllCollectionOperation(List<CollectionOperation<T>> list){
    	//auto lock spread in jdk 1.6+
        for(CollectionOperation co : list){
            recycle(co);
        }
    }
    public static <K,V> void recycleAllMapOperation(List<MapOperation<K,V>> list){
        for(MapOperation co : list){
            recycle(co);
        }
    }

}
