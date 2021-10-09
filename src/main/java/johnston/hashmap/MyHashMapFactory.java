package johnston.hashmap;

/**
 * Factory class for MyHashMap interface. Caller can decide thread-safe policy and
 * hash map object with debug methods or not.
 */
public class MyHashMapFactory {
  private static int capacity;
  private static float loadFactor;
  // Generate hash map without debug methods

  // Call hash map default ctor
  public static MyHashMap newMyHashMap(ThreadSafePolicy policy) {
    return getMyHashMap(policy, false);
  }

  // Call hash map ctor with parameter
  public static MyHashMap newMyHashMap(ThreadSafePolicy policy,
                                       int capacity, float loadFactor) {
    MyHashMapFactory.capacity = capacity;
    MyHashMapFactory.loadFactor = loadFactor;
    return getMyHashMap(policy, true);
  }

  private static MyHashMap getMyHashMap(ThreadSafePolicy policy, boolean hasParam) {
    return getMyHashMapTesting(policy, hasParam);
  }

  // Generate hash map with debug methods

  // Call hash map default ctor
  public static MyHashMapTesting newMyHashMapTesting(ThreadSafePolicy policy) {
    return getMyHashMapTesting(policy, false);
  }

  // Call hash map ctor with parameter
  public static MyHashMapTesting newMyHashMapTesting(ThreadSafePolicy policy,
                                                     int capacity, float loadFactor) {
    MyHashMapFactory.capacity = capacity;
    MyHashMapFactory.loadFactor = loadFactor;
    return getMyHashMapTesting(policy, true);
  }

  // Return hash map object based on given enum types and parameter
  private static MyHashMapTesting getMyHashMapTesting(ThreadSafePolicy policy, boolean hasParam) {
    switch (policy) {
      case NoSync:
        return hasParam ? new MyHashMapBasicImpl(capacity, loadFactor) :
            new MyHashMapBasicImpl();
      case SyncKeyword:
        return hasParam ? new MyHashMapSyncedImpl(capacity, loadFactor) :
            new MyHashMapSyncedImpl();
      case ReadWriteLock:
        return hasParam ? new MyHashMapReentrantImpl(capacity, loadFactor) :
            new MyHashMapReentrantImpl();
      default:
        return null;
    }
  }
}
