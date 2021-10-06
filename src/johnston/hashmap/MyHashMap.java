package johnston.hashmap;

public interface MyHashMap<K, V> {
  public int size();
  public boolean isEmpty();
  public boolean isSameHash(K one, K two);

  public V get(K k);
  public boolean containsKey(K k);

  public void put(K k, V v);

  public void removeAll();
  public boolean remove(K k);
}
