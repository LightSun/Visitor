package com.heaven7.java.visitor;

/**
 * the normalize visitor.
 * @param <K> the key type
 * @param <V> the main value type
 * @param <V1> the v1 type
 * @param <V2> the v2 type
 * @param <R> the result type.
 * @since 1.2.7
 */
public interface NormalizeVisitor<K, V, V1, V2, R> {


    /**
     * visit the key and values to result.
     * @param param the extra param
     * @param key the key
     * @param v the main value
     * @param v1 the first value of others
     * @param v2 the second value of others
     * @return the result.
     */
    R visit(K key, V v, V1 v1, V2 v2, Object param);
}
