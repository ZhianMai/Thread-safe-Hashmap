package johnston.hashmap;

import johnston.linkedlist.MyLinkedList;
import johnston.linkedlist.MyLinkedListBasicImpl;
import java.math.BigInteger;
import org.apache.commons.codec.digest.MurmurHash3;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * This is the basic hash map implementation without thread safety.
 *
 * The bucket uses the basic (not thread-safe) singly linked list.
 */
public class MyHashMapBasicImpl<K, V> implements MyHashMapTesting<K, V> {
  private int size;
  private int capacity;
  private MyLinkedList<MapPair>[] bucketList;
  private final float loadFactor;

  private static final int DEFAULT_CAPACITY = 16;
  private static final int REHASH_FACTOR = 2;
  private static final float DEFAULT_LOAD_FACTOR = 0.5f;
  private static final int THREAD_SLEEP_MILLI_SEC = 20;

  public MyHashMapBasicImpl(int capacity, float loadFactor) {
    this.capacity = capacity;
    this.size = 0;
    this.loadFactor = loadFactor;
    this.bucketList = (MyLinkedList<MapPair>[]) (new MyLinkedList[capacity]);
  }

  public MyHashMapBasicImpl() {
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
    return get(k) != null;
  }

  /**
   * If the key exists, update the value, otherwise insert a new pair.
   */
  @Override
  public void put(K k, V v) {
    rehash();
    int bucketIdx = getIndex(k);
    MapPair<K, V> newPair = new MapPair<>(k, v);

    if (bucketList[bucketIdx] == null) {
      bucketList[bucketIdx] = getNewLinkedList();
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

  /**
   * Return hashcode of the given key. Using MurmurHash function here to avoid
   * primary clustering. MurmurHash is a performance efficient non-cryptographic hash function.
   */
  private int hash(K k) {
    if (k == null) {
      return 0;
    }
    byte[] temp = BigInteger.valueOf(k.hashCode()).toByteArray();
    return MurmurHash3.hash32(temp) & 0x7FFFFFFF; // Ensure > 0
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
    if (this.size * 1.0f / this.capacity < this.loadFactor) {
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

  @Override
  public Iterator<MapPair> iterator() {
    return new MyHashMapIterator<>(this.bucketList);
  }

  /**
   * Iterator class for hash map.
   */
  static class MyHashMapIterator<MapPair> implements Iterator<MapPair> {
    MyLinkedList<MapPair>[] bucketList;
    int bucketIndex;
    Iterator<MapPair> listIterator;

    public MyHashMapIterator(MyLinkedList<MapPair>[] bucketList) {
      this.bucketList = bucketList;
      if (bucketList == null || bucketList.length == 0) {
        bucketIndex = -1;
      } else {
        bucketIndex = 0;
      }
      listIterator = null;
    }

    @Override
    public boolean hasNext() {
      updateIterator();
      return listIterator == null ? false : listIterator.hasNext();
    }

    @Override
    public MapPair next() {
      updateIterator();
      return listIterator.next();
    }

    private void updateIterator() {
      if (bucketIndex == -1) { // No elements
        listIterator = null;
        return;
      } else if (bucketIndex == bucketList.length) { // The last bucket, no more update.
        return;
      } else if (listIterator != null && listIterator.hasNext()) { // Current iterator not end
        return;
      }
      for (; bucketIndex < bucketList.length; bucketIndex++) { // Get the next bucket iterator
        if (bucketList[bucketIndex] != null) {
          listIterator = bucketList[bucketIndex].iterator();
          if (listIterator.hasNext()) {
            bucketIndex++;
            return;
          }
        }
      }
    }
  }

  /**
   * Place different linked list implementations here.
   */
  private MyLinkedList<MapPair> getNewLinkedList() {
    return new MyLinkedListBasicImpl<>();
    // return new MyLinkedListThreadSafeImpl<>();
  }

  /**
   * Methods below are for testing.
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

  /**
   *  Simulates heavy time-consuming read data work.
   */
  @Override
  public void heavyRead() throws InterruptedException {
    Thread.sleep(THREAD_SLEEP_MILLI_SEC);
  }
}
