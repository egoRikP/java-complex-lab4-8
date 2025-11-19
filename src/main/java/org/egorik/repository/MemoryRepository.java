package org.egorik.repository;

import java.util.ArrayList;
import java.util.List;

public class MemoryRepository<T> implements Repository<T> {

    private final List<T> objects = new ArrayList<>();

    @Override
    public void add(T obj) {
        objects.add(obj);
    }

    @Override
    public List<T> getAll() {
        return objects;
    }

    @Override
    public void delete(int ind) {
        objects.remove(ind);
    }

    @Override
    public boolean isEmpty() {
        return objects.isEmpty();
    }
}