package com.heaven7.java.visitor.collection;

import com.heaven7.java.visitor.IterateVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.anno.Nullable;

import java.util.List;

/*public*/ abstract class WrappedOperateManager<T> extends CollectionVisitService.OperateManager<T> {

    private final CollectionVisitService.OperateManager<T> mOm;

    public WrappedOperateManager(CollectionVisitService.OperateManager<T> om) {
        super();
        this.mOm = om;
    }

    @Override
    public ListVisitService<T> endAsList() throws UnsupportedOperationException {
        return mOm.endAsList();
    }

    @Override
    public CollectionVisitService.OperateManager<T> cache() {
        return mOm.cache();
    }

    @Override
    public CollectionVisitService<T> end() {
        return mOm.end();
    }

    @Override
    public CollectionVisitService.OperateManager<T> filter(@Nullable Object param, PredicateVisitor<? super T> filter) {
        return mOm.filter(param, filter);
    }

    @Override
    public CollectionVisitService.OperateManager<T> delete(@Nullable Object param, PredicateVisitor<? super T> delete) {
        return mOm.delete(param, delete);
    }

    @Override
    public CollectionVisitService.OperateManager<T> update(T newT, @Nullable Object param, PredicateVisitor<? super T> update) {
        return mOm.update(newT, param, update);
    }

    @Override
    public CollectionVisitService.OperateManager<T> insert(List<T> list, @Nullable Object param, IterateVisitor<? super T> insert) {
        return mOm.insert(list, param, insert);
    }

    @Override
    public CollectionVisitService.OperateManager<T> insert(T newT, @Nullable Object param, IterateVisitor<? super T> insert) {
        return mOm.insert(newT, param, insert);
    }

    @Override
    public CollectionVisitService.OperateManager<T> insertFinally(T newT, @Nullable Object param, IterateVisitor<? super T> insert) {
        return mOm.insertFinally(newT, param, insert);
    }

    @Override
    public CollectionVisitService.OperateManager<T> insertFinally(List<T> list, @Nullable Object param, IterateVisitor<? super T> insert) {
        return mOm.insertFinally(list, param, insert);
    }

    @Override
    public CollectionVisitService.OperateManager<T> insertFinallyIfNotExist(T newT) {
        return mOm.insertFinallyIfNotExist(newT);
    }
}
