package johnston.hashmap;

import johnston.linkedlist.MyLinkedList;
import johnston.linkedlist.MyLinkedListSafeImpl;

import java.util.Arrays;
import java.util.List;

public class MyHashMapImpl<K, V> implements MyHashMap<K, V> {
  private int size;
  private int capacity;
  private MyLinkedList<MapPair>[] bucketList;
  private final float loadFactor;

  public static final int DEFAULT_CAPACITY = 16;
  public static final float DEFAULT_LOAD_FACTOR = 0.5f;

  public MyHashMapImpl(int capacity, float loadFactor) {
    this.capacity = capacity;
    this.size = 0;
    this.loadFactor = loadFactor;
    this.bucketList = (MyLinkedList<MapPair>[]) (new MyLinkedList[capacity]);
  }

  public MyHashMapImpl() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  @Override
  public boolean isSameHash(K one, K two) {
    return hash(one) == hash(two);
  }

  @Override
  public V get(K k) {
    int bucketIdx = getIndex(k);
    if (bucketList[bucketIdx] == null) {
      return null;
    }

    MapPair<K, V> dummy = new MapPair<>(k, null);
    int pairIdx = bucketList[bucketIdx].getIndex(dummy);

    if (pairIdx == -1) {
      return null;
    }
    return (V) bucketList[bucketIdx].get(pairIdx).getV();
  }

  @Override
  public boolean containsKey(K k) {
    int bucketIdx = getIndex(k);
    if (bucketList[bucketIdx] == null) {
      return false;
    }
    MapPair<K, V> dummy = new MapPair<>(k, null);

    return bucketList[bucketIdx].contains(dummy);
  }

  @Override
  public void put(K k, V v) {
    rehash();
    int bucketIdx = getIndex(k);
    MapPair<K, V> newPair = new MapPair<>(k, null);

    if (bucketList[bucketIdx] == null) {
      bucketList[bucketIdx] = new MyLinkedListSafeImpl<>();
      bucketList[bucketIdx].append(newPair);
      size++;
      return;
    }

    MapPair<K, V> oldPair = bucketList[bucketIdx].get(newPair);
    if (oldPair == null) { // No such pair
      bucketList[bucketIdx].append(newPair);
    } else { // Update old value
      oldPair.setV(v);
    }
  }

  @Override
  public void removeAll() {
    size = 0;
    Arrays.fill(bucketList, null);
  }

  @Override
  public boolean remove(K k) {
    int bucketIdx = getIndex(k);
    if (bucketList[bucketIdx] == null) {
      return false;
    }
    MapPair<K, V> dummy = new MapPair<>(k, null);

    if (bucketList[bucketIdx].remove(dummy)) {
      size--;
      return true;
    } else { // No such value
      return false;
    }
  }

  private int hash(K k) {
    if (k == null) {
      return 0;
    }
    return k.hashCode() & 0x7FFFFFFF; // Ensure > 0
  }

  private int getIndex(K k) {
    int hash = hash(k);
    return hash % bucketList.length;
  }

  private void rehash() {
    if (this.capacity * 1.0 / this.size < loadFactor) {
      return;
    }

    capacity *= 2;
    MyLinkedList<MapPair>[] oldBucketList = bucketList;
    bucketList = (MyLinkedList<MapPair>[]) (new MyLinkedList[capacity]);

    for (MyLinkedList<MapPair> oldList : oldBucketList) {
      if (oldList == null) {
        continue;
      }

      List<MapPair> pairList = oldList.getAll();
      for (MapPair pair : pairList) {
        int bucketIdx = getIndex((K) pair.key);

        if (bucketList[bucketIdx] == null) {
          bucketList[bucketIdx] = new MyLinkedListSafeImpl<>();
        }
        bucketList[bucketIdx].append(pair);
      }
    }
  }
}
