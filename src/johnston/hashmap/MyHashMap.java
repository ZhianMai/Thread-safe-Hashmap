package johnston.hashmap;

public interface MyHashMap<K, V> {
  public static final int DEFAULT_CAPACITY = 16;
  public static final float DEFAULT_LOAD_FACTOR = 0.5f;

  public int size();
  public boolean isEmpty();

  public V get(K k);
  public V containsKey(K k);

  public void put(K k, V v);

  public void removeAll();
  public boolean remove(K k);
}
