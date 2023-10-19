package ru.greenatom.DAO;

import java.util.Collection;
import java.util.Optional;

public interface Dao<E, I> {
    Optional<I> create(E model);
    void update(E model);
    void delete(E model);
    Optional<E> get(int id);
    Collection<E> getAll();
    <F, V> Collection<E> getAll(F field, V value);

}
