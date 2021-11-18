package johnston.linkedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This thread-safe singly linked list class is based on the class MyLinkedListImpl.
 * <p>
 * To improve performance, this class uses read-write lock instead of synchronized keyword.
 * <p>
 * Read-write lock can ensure:
 * -> All read threads do not mutually exclude each other.
 * -> Read threads mutually exclude write threads.
 * -> Write threads mutually exclude each other.
 * <p>
 * I choose not to inherit from MyLinkedList class because setting up critical section should be
 * specific, meaning it would override almost all methods.
 */
public class MyLinkedListReentrantLockImpl<V> implements MyLinkedList<V>,
    MyLinkedListTesting<V> {
  /**
   * List node as an inner class for linked list.
   */
  static class ListNode<V> {
    ListNode<V> next;
    V v;

    ListNode(V v) {
      this(null, v);
    }

    ListNode(ListNode<V> next, V v) {
      this.next = next;
      this.v = v;
    }
  }

  private int size;
  private ListNode<V> dummy;
  private ListNode<V> end; // End of linked list
  private ReadWriteLock readWriteLock;
  private Lock readLock;
  private Lock writeLock;

  public MyLinkedListReentrantLockImpl() {
    this(null);
  }

  public MyLinkedListReentrantLockImpl(V v) {
    this.dummy = new ListNode<>(null);
    this.size = 0;

    if (v != null) {
      this.size++;
      this.dummy.next = new ListNode<>(v);
    }

    // Init read-write lock.
    readWriteLock = new ReentrantReadWriteLock();
    readLock = readWriteLock.readLock();
    writeLock = readWriteLock.writeLock();
  }

  /**
   * Read lock required
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
   * Read lock is not required since size() has read lock.
   */
  @Override
  public boolean isEmpty() {
    return this.size() == 0;
  }

  /**
   * Add the node to the end of array, return linked list itself.
   * <p>
   * Write lock required
   */
  @Override
  public MyLinkedListReentrantLockImpl addLast(V v) {
    writeLock.lock();
    try {
      updateEnd();
      this.end.next = new ListNode<>(v);
      this.end = this.end.next;
      this.size++;

      return this;
    } finally {
      writeLock.unlock();
    }
  }

  @Override
  public MyLinkedList addFirst(V v) {
    writeLock.lock();
    try {
      ListNode<V> newNode = new ListNode<>(v);
      newNode.next = dummy.next;
      dummy.next = newNode;
      this.size++;

      return this;
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Return true if found, otherwise false.
   * <p>
   * Read lock required.
   */
  @Override
  public boolean contains(V v) {
    ListNode curr = this.dummy.next; // No need to lock this

    readLock.lock();
    try {
      while (curr != null) {
        if (curr.v.equals(v)) {
          return true;
        }
        curr = curr.next;
      }
      return false;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return the value at the given index.
   * <p>
   * Read lock required.
   */
  @Override
  public V get(int index) {
    ListNode curr;
    readLock.lock();

    try {
      if (index >= this.size) {
        return null;
      }
      curr = this.dummy.next;

      while (index-- > 0) {
        curr = curr.next;
      }
      return (V) curr.v;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return the value which is equals to the given value. This is for hashMap key-val matching.
   * <p>
   * Read lock required.
   */
  @Override
  public V get(V v) {
    ListNode curr;
    readLock.lock();

    try {
      if (this.size == 0) {
        return null;
      }
      curr = this.dummy.next;

      while (curr != null && !curr.v.equals(v)) {
        curr = curr.next;
      }
      return curr == null ? null : (V) curr.v;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Return the index of the given value. If no such value, return -1.
   * <p>
   * Read lock required
   */
  @Override
  public int getIndex(V v) {
    int index = 0;
    ListNode curr;
    readLock.lock();

    try {
      curr = this.dummy.next;

      while (curr != null && !curr.v.equals(v)) {
        curr = curr.next;
        index++;
      }

      if (size == 0 || index == size) {
        return -1;
      }
      return index;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Assign the new value to the node at the given index.
   * <p>
   * Write lock required
   */
  @Override
  public boolean set(V v, int index) {
    ListNode curr;
    writeLock.lock();

    try {
      if (index >= this.size || index < 0) {
        return false;
      }
      curr = this.dummy.next;

      while (index-- > 0) {
        curr = curr.next;
      }

      curr.v = v;
      return true;
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Return a list of all elements.
   * <p>
   * Read lock required.
   */
  @Override
  public List<V> getAll() {
    List<V> result = new ArrayList<>(); // No need to lock these two lines.
    ListNode curr;
    readLock.lock();

    try {
      curr = this.dummy.next;

      while (curr != null) {
        result.add((V) curr.v);
        curr = curr.next;
      }
      return result;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Remove the target element. If the element does not exist, return false.
   * <p>
   * Write lock reauired.
   */
  @Override
  public boolean remove(V v) {
    ListNode curr;
    writeLock.lock();

    try {
      if (v == null || isEmpty()) {
        return false;
      }
      curr = this.dummy;

      while (curr.next != null && !v.equals(curr.next.v)) {
        curr = curr.next;
      }

      if (curr.next == null) { // No such value
        return false;
      }

      curr.next = curr.next.next;
      this.size--;
      return true;
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Empty the linked list.
   * <p>
   * Write lock required.
   */
  @Override
  public MyLinkedListReentrantLockImpl removeAll() {
    writeLock.lock();

    try {
      this.dummy.next = null;
      size = 0;
      return this;
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Return an iterator of linked list object. The readlock and writelock needs to
   * pass to the iterator class since it's a static inner class.
   * <p>
   * Read lock required.
   */
  @Override
  public Iterator<V> iterator() {
    readLock.lock();
    try {
      return new MyLinkedListIterator<V>(this.dummy.next, readLock, writeLock);
    } finally {
      readLock.unlock();
    }
  }

  /**
   * Iterator of MyLinkedListReentrantLockImpl.
   *
   * Iterator should not be thread-safe, because iterator cannot decide when to finish
   * iterating. If the caller holds it for a long time, then starvation on write thread
   * occurs.
   */
  class MyLinkedListIterator<V> implements Iterator<V> {
    ListNode<V> curr;
    Lock readLock;
    Lock writeLock;

    public MyLinkedListIterator(ListNode<V> node, Lock readLock, Lock writeLock) {
      this.curr = node;
      this.readLock = readLock;
      this.writeLock = writeLock;
    }

    /**
     * Return if next element is available.
     */
    @Override
    public boolean hasNext() {
      return curr != null;
    }

    /**
     * Return next element if is available.
     */
    @Override
    public V next() {
      V v = curr.v;
      curr = curr.next;
      return v;
    }

    /**
     * Overriding is not allowed.
     */
    @Override
    public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    result.append("MyLinkedListReentrantLockImpl{");

    for (V v : this) {
      result.append(v);
      result.append(",");
    }

    result.append("}");

    return result.toString();
  }

  /**
   * For multi-threading test only.
   */
  @Override
  public int getNodeLength() {
    int count = 0;
    ListNode curr;
    readLock.lock();

    try {
      curr = dummy;

      while (curr.next != null) {
        count++;
        curr = curr.next;
      }
      return count;
    } finally {
      readLock.unlock();
    }
  }

  /**
   * For multi-threading test only.
   */
  @Override
  public void addAndDelete(V v) {
    writeLock.lock();

    try {
      addFirst(v);
      remove(v);

      addLast(v);
      remove(v);
    } finally {
      writeLock.unlock();
    }
  }

  /**
   * Helper method to locate the end of the linked list.
   * <p>
   * This private method does not need to lock, because all caller methods are locked, so no data
   * racing here.
   */
  private void updateEnd() {
    end = dummy;

    while (end.next != null) {
      end = end.next;
    }
  }
}
