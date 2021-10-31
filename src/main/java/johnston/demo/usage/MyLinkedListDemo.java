package johnston.demo.usage;

import johnston.linkedlist.MyLinkedList;
import johnston.linkedlist.MyLinkedListBasicImpl;
import johnston.linkedlist.MyLinkedListReentrantLockImpl;

public class MyLinkedListDemo {
  public static void main(String[] args) {
    MyLinkedList<Integer> list = new MyLinkedListReentrantLockImpl<>();

    // Append to the tail
    for (int i = 0; i < 10; i++) {
      list.addLast(i * i);
    }

    System.out.println("Size: " + list.size()); // size

    // Get element by index
    for (int index = 0; index < list.size(); index++) {
      System.out.print(list.get(index) + ",");
    }
    System.out.println();

    // Get index by element
    for (int i = 0; i < 10; i++) {
      System.out.print(list.getIndex(i * i) + ",");
    }
    System.out.println();

    // Set value by index
    for (int index = 0; index < list.size(); index++) {
      list.set(-index, index);
    }

    // Iteration
    for (int i : list) {
      System.out.print(i + ",");
    }

    System.out.println(list);

    // Remove element, O(n)
    int size = list.size();
    for (int index = 0; index < size; index++) {
      list.remove(-index);
    }

    System.out.println("\nSize: " + list.size()); // size
  }
}
