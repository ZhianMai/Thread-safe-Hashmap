package johnston.hashmap;

import johnston.linkedlist.MyLinkedList;
import johnston.linkedlist.MyLinkedListImpl;
import johnston.linkedlist.MyLinkedListReentrantLockImpl;

import java.util.Arrays;
import java.util.List;

/**
 * This is the basic hash map implementation without thread safety.
 *
 * The bucket uses the basic (not thread-safe) singly linked list.
 */
public class MyHashMapImpl<K, V> implements MyHashMapTesting<K, V> {
  private int size;
  private int capacity;
  private MyLinkedList<MapPair>[] bucketList;
  private final float loadFactor;

  public static final int DEFAULT_CAPACITY = 16;
  public static final int REHASH_FACTOR = 2;
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

  /**
   * Return the value by given key. If no such key, return null.
   */
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

  /**
   * Return true if key exists, otherwise false.
   */
  @Override
  public boolean containsKey(K k) {
    int bucketIdx = getIndex(k);
    if (bucketList[bucketIdx] == null) {
      return false;
    }
    MapPair<K, V> dummy = new MapPair<>(k, null);

    return bucketList[bucketIdx].contains(dummy);
  }

  /**
   * If the key exists, update the value, otherwise insert a new pair.
   */
  @Override
  public void put(K k, V v) {
    rehash();
    int bucketIdx = getIndex(k);
    MapPair<K, V> newPair = new MapPair<>(k, null);

    if (bucketList[bucketIdx] == null) {
      bucketList[bucketIdx] = new MyLinkedListImpl<>();
      // Using thread-safe linked list would not prevent data racing.
      // bucketList[bucketIdx] = new MyLinkedListThreadSafeImpl<>();
      bucketList[bucketIdx].addFirst(newPair);
      size++;
      return;
    }

    MapPair<K, V> oldPair = bucketList[bucketIdx].get(newPair);
    if (oldPair == null) { // No such pair
      bucketList[bucketIdx].addFirst(newPair);
      size++;
    } else { // Update old value
      oldPair.setV(v);
    }
  }

  @Override
  public void removeAll() {
    size = 0;
    Arrays.fill(bucketList, null);
  }

  /**
   * Remove the pair by the given key and return true. If no such keys, return false.
   */
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

  /**
   * Double the capacity of the hash table if the load factor is > 0.5. All pairs are guaranteed
   * to be found by given keys after rehashing.
   */
  private void rehash() {
    if (this.size * 1.0f / this.capacity < loadFactor) {
      return;
    }

    capacity *= REHASH_FACTOR;
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
          bucketList[bucketIdx] = new MyLinkedListImpl<>();
          // Using thread-safe linked list would not prevent data racing.
          bucketList[bucketIdx] = new MyLinkedListReentrantLockImpl<>();
        }
        bucketList[bucketIdx].addFirst(pair);
      }
    }
  }

  @Override
  public int[] getAllBucketSize() {
    int[] result = new int[bucketList.length];

    for (int i = 0; i < result.length; i++) {
      if (bucketList[i] != null) {
        result[i] = bucketList[i].size();
      }
    }
    return result;
  }

  @Override
  public int getTotalPairCount() {
    int result = 0;

    for (int i = 0; i < bucketList.length; i++) {
      if (bucketList[i] != null) {
        result += bucketList[i].size();
      }
    }
    return result;
  }

  @Override
  public void addAndDelete(K k, V v) {
    put(k, v);
    remove(k);
  }
}
