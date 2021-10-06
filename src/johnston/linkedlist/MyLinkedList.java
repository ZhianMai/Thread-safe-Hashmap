package johnston.linkedlist;

import java.util.List;

/**
 * Interface for singly linked list.
 */
public interface MyLinkedList<V> {
  public int size();
  public boolean isEmpty();

  public boolean contains(V v);
  public V get(int index);
  public V get(V v);
  public int getIndex(V v);
  public List<V> getAll();

  public MyLinkedList addLast(V v);
  public MyLinkedList addFirst(V v);
  public boolean set(V v, int index);

  public boolean remove(V v);
  public MyLinkedList removeAll();
}
