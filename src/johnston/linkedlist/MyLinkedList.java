package johnston.linkedlist;

import java.util.List;

/**
 * Interface for singly linked list.
 */
public interface MyLinkedList<V> {
  public int size();
  public boolean isEmpty();
  public MyLinkedList add(V v);
  public boolean contains(V v);
  public V get(int index);
  public boolean set(V v, int index);
  public List<V> getAll();
  public boolean remove(V v);
  public MyLinkedList removeAll();
  /**
   * This method is for multi-testing only.
   */
  public int getNodeLength();
}
