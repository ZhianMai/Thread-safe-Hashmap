# Thread-safe Linked list and Hash Map (in Java)

This repo contains the implementation of thread-safe linked list and hash map and Junit test cases.

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

Interface <i>MyHashMap</i> provides methods:
- int size();
- boolean isEmpty();
- boolean isSameHash(K one, K two);
- V get(K k);
- boolean containsKey(K k);
- void put(K k, V v);
- void removeAll();
- boolean remove(K k);

## Multi-threading test cases

The multi-threading test cases contain write, read-write, write-delete tests, and heavy read performance test. The basic implementations can cause data racing and would eventually fail these tests at some point.

## Notes on multi-threading

In general, the ReentrantReadWriteLock has better flexibility than synchronized keywords, such as avoiding starvation, supporting priority, and spearating read-write operation.

But the performance test on a single machine shows that ReentrantReadWriteLock does not have significant speedup than synchronized keyword. Maybe the runtime overhead is higher than traditional synchronized keyword. I tried minimizing the critical sections for read-write look, but the performance has no significant improvement, and it's easier to cause errors than locking the whole method. 

