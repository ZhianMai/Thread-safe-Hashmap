package johnston.hashmap;

/**
 * Factory class for MyHashMap interface. Caller can decide thread-safe policy and
 * hash map object with debug methods or not.
 */
public class MyHashMapFactory {
  // Generate hash map without debug methods
  // Call hash map default ctor
  public static MyHashMap newMyHashMap(ThreadSafePolicy policy) {
    return newMyHashMapTesting(policy);
  }

  // Call hash map ctor with parameter
  public static MyHashMap newMyHashMap(ThreadSafePolicy policy,
                                       int capacity, float loadFactor) {
    return newMyHashMapTesting(policy, capacity, loadFactor);
  }

  // Generate hash map with debug methods
  // Call hash map default ctor
  public static MyHashMapTesting newMyHashMapTesting(ThreadSafePolicy policy) {
    return getMyHashMapTestingDefault(policy);
  }

  // Call hash map ctor with parameter
  public static MyHashMapTesting newMyHashMapTesting(ThreadSafePolicy policy,
                                                     int capacity, float loadFactor) {
    return getMyHashMapTestingWithParam(policy, capacity, loadFactor);
  }

  // Return hash map object based on given enum types and parameter
  private static MyHashMapTesting getMyHashMapTestingWithParam(ThreadSafePolicy policy,
                                                               int capacity, float loadFactor) {
    switch (policy) {
      case NoSync:
        return new MyHashMapBasicImpl(capacity, loadFactor);
      case SyncKeyword:
        return new MyHashMapSyncedImpl(capacity, loadFactor);
      case ReadWriteLock:
        return new MyHashMapReentrantImpl(capacity, loadFactor);
      default:
        return null;
    }
  }

  // Return hash map object based on given enum, no parameter
  private static MyHashMapTesting getMyHashMapTestingDefault(ThreadSafePolicy policy) {
    switch (policy) {
      case NoSync:
        return new MyHashMapBasicImpl();
      case SyncKeyword:
        return new MyHashMapSyncedImpl();
      case ReadWriteLock:
        return new MyHashMapReentrantImpl();
      default:
        return null;
    }
  }
}
