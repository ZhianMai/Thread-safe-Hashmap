package johnston.linkedlist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the implementation of singly linked list. It's the basic linked list without
 * thread-safety.
 */
public class MyLinkedListImpl<V> implements MyLinkedList<V>, MyLinkedListTesting<V> {
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

  public MyLinkedListImpl() {
    this(null);
  }

  public MyLinkedListImpl(V v) {
    this.dummy = new ListNode<>(null);
    this.size = 0;

    if (v != null) {
      this.size++;
      this.dummy.next = new ListNode<>(v);
    }
  }

  @Override
  public int size() {
    return this.size;
  }

  @Override
  public boolean isEmpty() {
    return this.size == 0;
  }

  /**
   * Add the node to the end of array, return linked list itself.
   */
  @Override
  public MyLinkedList addLast(V v) {
    updateEnd();
    this.end.next = new ListNode<>(v);
    this.end = this.end.next;
    this.size++;

    return this;
  }

  /**
   * Add the node to the begin of the linked list.
   */
  @Override
  public MyLinkedList addFirst(V v) {
    ListNode<V> newNode = new ListNode<>(v);
    newNode.next = dummy.next;
    dummy.next = newNode;
    this.size++;

    return this;
  }

  /**
   * Return true if found, otherwise false.
   */
  @Override
  public boolean contains(V v) {
    ListNode curr = this.dummy.next;

    while (curr != null) {
      if (curr.v.equals(v)) {
        return true;
      }
      curr = curr.next;
    }
    return false;
  }

  /**
   * Return the value at the given index.
   */
  @Override
  public V get(int index) {
    if (index >= this.size) {
      return null;
    }

    ListNode curr = this.dummy.next;

    while (index-- > 0) {
      curr = curr.next;
    }
    return (V) curr.v;
  }

  @Override
  public V get(V v) {
    if (this.size == 0) {
      return null;
    }

    ListNode curr = this.dummy.next;

    while (curr != null && !curr.v.equals(v)) {
      curr = curr.next;
    }
    return curr == null ? null : (V) curr.v;
  }

  /**
   * Return the index of the given value. If no such value, return -1.
   */
  @Override
  public int getIndex(V v) {
    int index = 0;
    ListNode curr = this.dummy.next;

    while (curr != null && !curr.v.equals(v)) {
      curr = curr.next;
      index++;
    }

    if (size == 0 || index == size) {
      return -1;
    }
    return index;
  }

  /**
   * Assign the new value to the node at the given index.
   */
  @Override
  public boolean set(V v, int index) {
    if (index >= this.size) {
      return false;
    }

    ListNode curr = this.dummy.next;

    while (index-- > 0) {
      curr = curr.next;
    }

    curr.v = v;
    return true;
  }

  /**
   * Return a list of all elements
   */
  @Override
  public List<V> getAll() {
    List<V> result = new ArrayList<>(); // Be lazy :) just use the java List
    ListNode curr = this.dummy.next;

    while (curr != null) {
      result.add((V) curr.v);
      curr = curr.next;
    }
    return result;
  }

  /**
   * Remove the target element. If the element does not exist, return false.
   */
  @Override
  public boolean remove(V v) {
    if (v == null || isEmpty()) {
      return false;
    }

    ListNode curr = this.dummy;

    while (curr.next != null && !v.equals(curr.next.v)) {
      curr = curr.next;
    }

    if (curr.next == null) { // No such value
      return false;
    }

    curr.next = curr.next.next;
    this.size--;
    return true;
  }

  @Override
  public MyLinkedList removeAll() {
    this.dummy.next = null;
    size = 0;
    return this;
  }

  @Override
  public Iterator<V> iterator() {
    return new MyLinkedListIterator<V>(this.dummy.next);
  }

  static class MyLinkedListIterator<V> implements Iterator<V> {
    ListNode<V> curr;

    public MyLinkedListIterator(ListNode<V> node) {
      this.curr = node;
    }
    @Override
    public boolean hasNext() {
      return curr != null;
    }

    @Override
    public V next() {
      V v = curr.v;
      curr = curr.next;
      return v;
    }

    @Override
    public void remove() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * For multi-threading test only.
   */
  @Override
  public int getNodeLength() {
    ListNode curr = dummy;
    int count = 0;

    while (curr.next != null) {
      count++;
      curr = curr.next;
    }
    return count;
  }

  @Override
  public void addAndDelete(V v) {
    addFirst(v);
    remove(v);
  }

  private void updateEnd() {
    end = dummy;

    while (end.next != null) {
      end = end.next;
    }
  }
}
