package johnston.hashmap;

public class MapPair<K, V> {
  public final K key;
  private V val;

  public MapPair(K key, V val) {
    this.key = key;
    this.val = val;
  }

  public V getV() {
    return val;
  }

  public void setV(V val) {
    this.val = val;
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }

    if (!(o instanceof MapPair)) {
      return false;
    }

    return ((MapPair<K, V>) o).key.equals(this.key);
  }
}
