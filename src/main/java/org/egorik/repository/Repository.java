package org.egorik.repository;

import java.util.List;

public interface Repository<T> {
    void add(T obj);

    List<T> getAll();

    void delete(int ind);

    boolean isEmpty();
}