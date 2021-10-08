package johnston.hashmap;

/**
 * This enum is for factory class caller to decide the policy of thread-safety, namely
 * no thread-safety, synchronized keyword, or Reentrant read-write lock.
 */
public enum ThreadSafePolicy {
  NoSync,
  SyncKeyword,
  ReadWriteLock
}
