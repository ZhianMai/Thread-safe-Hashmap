package johnston.hashmap;

import johnston.linkedlist.MyLinkedListImpl;
import johnston.linkedlist.MyLinkedListThreadSafeImpl;
import johnston.linkedlist.MyLinkedList;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This is the basic hash map implementation without thread safety.
 * <p>
 * The bucket uses thread-safe singly linked list.
 */
public class MyHashMapThreadSafeImpl<K, V> implements MyHashMap<K, V>, HashMapTestSupport<K, V> {
  private int size;
  private int capacity;
  private MyLinkedList<MapPair>[] bucketList;
  private final float loadFactor;

  public static final int DEFAULT_CAPACITY = 16;
  public static final int REHASH_FACTOR = 2;
  public static final float DEFAULT_LOAD_FACTOR = 0.5f;

  private ReentrantReadWriteLock reentrantReadWriteLock;
  private Lock readLock;
  private Lock writeLock;

  public MyHashMapThreadSafeImpl(int capacity, float loadFactor) {
    this.capacity = capacity;
    this.size = 0;
    this.loadFactor = loadFactor;
    this.bucketList = (MyLinkedList<MapPair>[]) (new MyLinkedList[capacity]);

    // Init read-write lock.
    reentrantReadWriteLock = new ReentrantReadWriteLock();
    readLock = reentrantReadWriteLock.readLock();
    writeLock = reentrantReadWriteLock.writeLock();
  }

  public MyHashMapThreadSafeImpl() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  /**
   * Return size of the hash table.
   * <p>
   * Read lock required.
   */
  @Override
  public int size() {
    readLock.lock();

    try {
      return this.size;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return if the current hash table is empty.
   * <p>
   * Read lock required.
   */
  @Override
  public boolean isEmpty() {
    readLock.lock();

    try {
      return this.size == 0;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return if two given keys have the same hash value. No need to lock.
   */
  @Override
  public boolean isSameHash(K one, K two) {
    return hash(one) == hash(two);
  }

  /**
   * Return the value by given key. If no such key, return null.
   * <p>
   * Read lock required.
   */
  @Override
  public V get(K k) {
    readLock.lock();

    try {
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
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return true if key exists, otherwise false.
   * <p>
   * Read lock required.
   */
  @Override
  public boolean containsKey(K k) {
    readLock.lock();

    try {
      int bucketIdx = getIndex(k);
      if (bucketList[bucketIdx] == null) {
        return false;
      }
      MapPair<K, V> dummy = new MapPair<>(k, null);

      return bucketList[bucketIdx].contains(dummy);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * If the key exists, update the value, otherwise insert a new pair.
   * <p>
   * Write lock required.
   */
  @Override
  public void put(K k, V v) {
    writeLock.lock();

    try {
      rehash();
      int bucketIdx = getIndex(k);
      MapPair<K, V> newPair = new MapPair<>(k, null);

      if (bucketList[bucketIdx] == null) {
        bucketList[bucketIdx] = getNewLinkedList();
        bucketList[bucketIdx].addFirst(newPair);
        size++;
        return;
      }

      MapPair<K, V> oldPair = bucketList[bucketIdx].get(newPair);
      if (oldPair == null) { // No such pair, add to the bucket at index 0.
        bucketList[bucketIdx].addFirst(newPair);
        size++;
      } else { // Update old value
        oldPair.setV(v);
      }
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Empty the hash table.
   *
   * Write lock required.
   */
  @Override
  public void removeAll() {
    writeLock.lock();

    try {
      size = 0;
      Arrays.fill(bucketList, null);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Remove the pair by the given key and return true. If no such keys, return false.
   *
   * Write lock required.
   */
  @Override
  public boolean remove(K k) {
    writeLock.lock();

    try {
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
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Return the hash value of the given key. Use the key's hashcode() and make it
   * positive. No need to lock.
   */
  private int hash(K k) {
    if (k == null) {
      return 0;
    }
    return k.hashCode() & 0x7FFFFFFF; // Ensure > 0
  }

  /**
   * Return bucket index of the given key. No need to lock.
   */
  private int getIndex(K k) {
    int hash = hash(k);
    return hash % bucketList.length;
  }

  /**
   * Double the capacity of the hash table if the load factor is > 0.5. All pairs are guaranteed
   * to be found by given keys after rehashing.
   *
   * No need to lock since all caller functions are locked by write lock.
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
          bucketList[bucketIdx] = getNewLinkedList();
        }
        bucketList[bucketIdx].addFirst(pair);
      }
    }
  }

  private MyLinkedList<MapPair> getNewLinkedList() {
    return new MyLinkedListImpl<>();
  }
  /**
   * Method for testing only, won't expose to MyHashMap interface.
   */
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

  /**
   * Method for testing only, won't expose to MyHashMap interface.
   */
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
    writeLock.lock();

    try {
      put(k, v);
      remove(k);
    } finally {
      writeLock.unlock();
    }
  }
}
