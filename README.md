# Thread-safe Linked List and Hash Map (in Java)

This repo contains implementations of thread-safe linked list, hash map and their Junit tests.

## Update

### version 1.2
- Improved the multi-threading read method to show that on heavy reading situations, the read-write lock does have significant better performance than synchronized keyword.
  - The old method was to run <i>hashMap.contains(key)</i> a lot of times, perhaps the bottleneck is the memory R/W speed making read-write lock has the same runtime as synchronized keyword.
  - New heavy read method simply makes the current sleep 20 milli seconds, so no such memory R/W speed bottleneck.
  - The multi-threading heavy read test starts 6 threads, and the result is:
    - Basic benchmark (no thread-safety): 30 sec;
    - Synchronized keyword: 3 min;
    - Read-write lock: 30 sec.
  - It proves that the synchronized area only allows one reading thread entered at a time, while read-write lock allows all reading thread to enter so the time is as fast as the benchmark!
  
### version 1.1 
 - Rename hash map testing method interface to <b><i>MyHashMapTesting</i></b>, and it extends <b><i>MyHashMap</i></b> interface. Now all hash map implementation classes are implemented <b>MyhashMapTesting</b> only.

 - Create a factory class for hash map object creation. Use enum <b><i>ThreadSafePolicy</i></b> to decide which types of implementation objects to get:
   - <i>NoSync</i>: return <i>MyHashMapImpl</i> object.
   - <i>SyncKeyword</i>: return <i>MyHashMapSyncedImpl</i> object.
   - <i>ReadWriteLock</i>: return <i>MyHashMapReentrantImpl</i> object.

 Example:
 ```Java
 // Hash map for general use (no debugging methods)
 private MyHashMap<ClassA, ClassB> hashMap;
 
 // Create a MyHashMap object without thread-safety policy
 hashMap = MyHashMapFactory.getMyHashMap(ThreadSafePolicy.NoSync);
 
 // Create a MyHashMap object without thread-safety policy and given capacity and loadFactor
 hashMap = MyHashMapFactory.getMyHashMap(ThreadSafePolicy.NoSync, 666, 0.4f);
 
 // Create a MyHashMap object with thread-safety using synchronized keyword
 hashMap = MyHashMapFactory.getMyHashMap(ThreadSafePolicy.SyncKeyword);
 
 // Createa MyHashMap object with thread-safety using ReentrantReadWriteLock
 hashMap = MyHashMapFactory.getMyHashMap(ThreadSafePolicy.ReadWriteLock);
 
 // Hash map for debugging (interface MyHashMapTesting)
 private MyHashMapTesting<ClassA, ClassB> hashMap;
 
 hashMap = MyHashMapFactory.MyHashMapTesting(ThreadSafePolicy.NoSync);
 hashMap = MyHashMapFactory.MyHashMapTesting(ThreadSafePolicy.NoSync, 666, 0.4f);
 // Same usage as the MyHashMap above, but with different method names.
 ```
 
 - Applied factory method to Junit testing to reduce redundent codes.
 
 <p align="center">
  <img src="/cover%20img/57ecc01c4c5cd160aa630b58238a86b.jpg" style="width:300px;height:400px;"/>
  <br />
  <i>Disclaimer: run multi-threading test on one's own risk ;)</i>
</p>
 
## Linked List

:link:[link](src/johnston/linkedlist/)

Implementation contains:
- An implementation of basic singly linked list;
- An implementation of thread-safe singly linked list based on basic linked list, using read-write lock provided by <i>Reentrantreadwritelock</i>.

Test contains:
- Linked list correctness test cases;
- Linked list multi-threading test cases.

Interface <i>MyHashMap</i> provides methods:
 - int size();
 - boolean isEmpty();
 - boolean contains(V v);
 - boolean get(int index);
 - V get(V v);
 - int getIndex(V v);
 - MyLinkedList addFirst(V v);
 - MyLinkedList addLast(V v);
 - boolean set(V v, int index);
 - boolean remove(V v);
 - MyLinkedList removeAll();

## Hash Map

:link:[link](src/johnston/hashmap/)

Implementation contains:
- Basic hash map;
- Thread-safe hash map based on basic hash map, using <i>synchronized</i> keyword.
- Thread-safe hash map based on basic hash map, using read-write lock provided by Reentrantreadwritelock.

Test contains:
- Hash map correctness test cases;
- Hash map multi-threading test cases.

Interface <i>MyLinkedList</i> provides methods:
- int size();
- boolean isEmpty();
- boolean isSameHash(K one, K two);
- V get(K k);
- boolean containsKey(K k);
- void put(K k, V v);
- void removeAll();
- boolean remove(K k);

For hash collision, these implementations use separate chaining, and the hash map bucket uses MyLinkedList. 

## Multi-threading test cases

The multi-threading test cases contain write, read-write, write-delete tests, and heavy read performance test. The basic implementations can cause data racing and would eventually fail these tests at some point.

## Notes on multi-threading

In general, the ReentrantReadWriteLock has better flexibility than synchronized keywords, such as avoiding starvation, supporting priority, and spearating read-write operation.

But the performance test on a single machine shows that ReentrantReadWriteLock does not have significant speedup than synchronized keyword. Maybe the runtime overhead is higher than traditional synchronized keyword. I tried minimizing the critical sections for read-write look, but the performance has no significant improvement, and it's easier to cause errors than locking the whole method.

The hash map buckets use basic or thread-safe linked-list can both pass the multi-threading tests. Since all write operations are locked, so the bucket list is guaranteed only one thread enter & write/delte at the same time.
