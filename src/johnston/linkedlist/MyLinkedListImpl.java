package johnston.linkedlist;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the implementation of singly linked list. It's the basic linked list without thread-safety.
 */
public class MyLinkedListImpl<V> implements MyLinkedList<V>, MyLinkedListMultiThreadingTest<V> {
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
   * Return linked list itself.
   */
  @Override
  public MyLinkedList add(V v) {
    updateEnd();
    this.end.next = new ListNode<>(v);
    this.end = this.end.next;
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

    if (curr.next == null) { // Update end node if the last element is deleted
      this.end = curr;
    }
    return true;
  }

  @Override
  public MyLinkedList removeAll() {
    this.dummy.next = null;
    size = 0;
    return this;
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
    add(v);
    remove(v);
  }

  private void updateEnd() {
    if (this.end != null && this.end.next == null) { // Already the end
      return;
    }

    ListNode curr = dummy;

    while (curr.next != null) {
      curr = curr.next;
    }
    end = curr;
  }
}
