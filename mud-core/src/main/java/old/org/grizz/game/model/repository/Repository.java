package old.org.grizz.game.model.repository;

public interface Repository<T> {
    void add(T t);

    T get(String id);
}
