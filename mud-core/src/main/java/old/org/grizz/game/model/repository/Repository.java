package old.org.grizz.game.model.repository;

/**
 * Created by Grizz on 2015-04-21.
 */
public interface Repository<T> {
    void add(T t);

    T get(String id);
}
