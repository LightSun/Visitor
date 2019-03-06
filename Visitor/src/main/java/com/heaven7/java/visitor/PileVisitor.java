package com.heaven7.java.visitor;

/**
 * the pile visitor
 * @param <T> the main type of pile
 * @since 1.2.0
 */
public interface PileVisitor<T> extends Visitor2<Object, T, T, T> {

    /**
     * the pile visitor of 'int-add '
     * @since 1.2.5
     */
    PileVisitor<Integer> INT_ADD = new PileVisitor<Integer>() {
        @Override
        public Integer visit(Object o, Integer integer, Integer integer2) {
            return integer + integer2;
        }
    };
    /**
     * the pile visitor of 'int-sub'
     * @since 1.2.5
     */
    PileVisitor<Integer> INT_SUB = new PileVisitor<Integer>() {
        @Override
        public Integer visit(Object o, Integer integer, Integer integer2) {
            return integer - integer2;
        }
    };
    /**
     * the pile visitor of 'int-multi'
     * @since 1.2.5
     */
    PileVisitor<Integer> INT_MULTI = new PileVisitor<Integer>() {
        @Override
        public Integer visit(Object o, Integer integer, Integer integer2) {
            return integer * integer2;
        }
    };
    /**
     * the pile visitor of 'int-div'
     * @since 1.2.5
     */
    PileVisitor<Integer> INT_DIV = new PileVisitor<Integer>() {
        @Override
        public Integer visit(Object o, Integer integer, Integer integer2) {
            return integer / integer2;
        }
    };

    /**
     * the pile visitor of 'int-add '
     * @since 1.3.4
     */
    PileVisitor<Long> LONG_ADD = new PileVisitor<Long>() {
        @Override
        public Long visit(Object o, Long v1, Long v2) {
            return v1 + v2;
        }
    };
    /**
     * the pile visitor of 'int-sub'
     * @since 1.3.4
     */
    PileVisitor<Long> LONG_SUB = new PileVisitor<Long>() {
        @Override
        public Long visit(Object o, Long v1, Long v2) {
            return v1 - v2;
        }
    };
    /**
     * the pile visitor of 'int-multi'
     * @since 1.3.4
     */
    PileVisitor<Long> LONG_MULTI = new PileVisitor<Long>() {
        @Override
        public Long visit(Object o, Long v1, Long v2) {
            return v1 * v2;
        }
    };
    /**
     * the pile visitor of 'int-div'
     * @since 1.3.4
     */
    PileVisitor<Long> LONG_DIV = new PileVisitor<Long>() {
        @Override
        public Long visit(Object o, Long v1, Long v2) {
            return v1 / v2;
        }
    };

    /**
     * the pile visitor of 'float-add'
     * @since 1.2.5
     */
    PileVisitor<Float> FLOAT_ADD = new PileVisitor<Float>() {
        @Override
        public Float visit(Object o, Float f1, Float f2) {
            return f1 + f2;
        }
    };
    /**
     * the pile visitor of 'float-sub'
     * @since 1.2.5
     */
    PileVisitor<Float> FLOAT_SUB = new PileVisitor<Float>() {
        @Override
        public Float visit(Object o, Float f1, Float f2) {
            return f1 - f2;
        }
    };
    /**
     * the pile visitor of 'float-multi'
     * @since 1.2.5
     */
    PileVisitor<Float> FLOAT_MULTI = new PileVisitor<Float>() {
        @Override
        public Float visit(Object o, Float f1, Float f2) {
            return f1 * f2;
        }
    };

    /**
     * the pile visitor of 'float-div'
     * @since 1.2.5
     */
    PileVisitor<Float> FLOAT_DIV = new PileVisitor<Float>() {
        @Override
        public Float visit(Object o, Float f1, Float f2) {
            return f1 / f2;
        }
    };
    /**
     * the pile visitor of 'double-add'
     * @since 1.2.5
     */
    PileVisitor<Double> DOUBLE_ADD = new PileVisitor<Double>() {
        @Override
        public Double visit(Object o, Double f1, Double f2) {
            return f1 + f2;
        }
    };
    /**
     * the pile visitor of 'double-sub'
     * @since 1.2.5
     */
    PileVisitor<Double> DOUBLE_SUB = new PileVisitor<Double>() {
        @Override
        public Double visit(Object o, Double f1, Double f2) {
            return f1 - f2;
        }
    };
    /**
     * the pile visitor of 'double-multi'
     * @since 1.2.5
     */
    PileVisitor<Double> DOUBLE_MULTI = new PileVisitor<Double>() {
        @Override
        public Double visit(Object o, Double f1, Double f2) {
            return f1 * f2;
        }
    };
    /**
     * the pile visitor of 'double-div'
     * @since 1.2.5
     */
    PileVisitor<Double> DOUBLE_DIV = new PileVisitor<Double>() {
        @Override
        public Double visit(Object o, Double f1, Double f2) {
            return f1 / f2;
        }
    };
}
