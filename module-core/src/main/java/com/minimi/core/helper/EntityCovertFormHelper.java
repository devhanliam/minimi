package com.minimi.core.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class EntityCovertFormHelper<E,F> {
    private E entity;
    private Function<E,F> toForm;
    private List<E> entities;

    public EntityCovertFormHelper(List<E> entities, Function<E, F> toForm) {
        this.entities = entities;
        this.toForm = toForm;
    }

    public static <E,F> EntityCovertFormHelper newInstance(E entity,Function<E,F> toForm) {
        return new EntityCovertFormHelper<E, F>(entity, toForm);
    }
    public static <E,F> EntityCovertFormHelper newInstanceForList(List<E> entities,Function<E,F> toForm) {
        return new EntityCovertFormHelper<E, F>(entities, toForm);
    }

    public F convert() {
        try {
            return convertInternal();
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    public List<F> convertList() {
        try {
            return convertListInternal();
        } catch (NullPointerException e) {
            throw new NullPointerException(e.getMessage());
        }
    }

    private F convertInternal() {
        F form = toForm(entity);
        return form;
    }

    private List<F> convertListInternal() {
        List<F> roots = new ArrayList<>();
        for (E e : entities) {
            F form = toForm(e);
            roots.add(form);
        }
        return roots;
    }

    private EntityCovertFormHelper(E entity,Function<E,F> toForm){
        this.entity = entity;
        this.toForm = toForm;
    }

    private F toForm(E e) {
        return toForm.apply(e);
    }
}
