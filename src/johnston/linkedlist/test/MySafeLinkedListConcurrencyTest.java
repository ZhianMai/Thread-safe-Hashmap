package johnston.linkedlist.test;

import johnston.linkedlist.MyLinkedListReentrantLockImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * MySafeLinkedList is modified based on MyLinkedList. This test class is focus on concurrency
 * test. Using integer as the assigned type.
 */
public class MySafeLinkedListConcurrencyTest {
   private MyLinkedListReentrantLockImpl<Integer> intList;

   // Use basic types To test multi-threading test cases correctness.
   // private MyLinkedListImpl<Integer> intList;

  /**
   * To test multi-threading test cases correctness, let the linked list init as the basic
   * linked list. It would fail at some points if the thread amount is enough and each thread
   * works significantly amount of works.
   */
  @BeforeEach
  public void init() {
    intList = new MyLinkedListReentrantLockImpl<>();

    // Use basic types To test multi-threading test cases correctness.
    // intList = new MyLinkedListImpl<>();
  }

  @Test
  @DisplayName("Test Junit functionality.")
  public void demoTestMethod() {
    assertTrue(true);
    assertTrue((intList != null), "Test object init.");
  }

  @Test
  @DisplayName("Test write data racing.")
  public void writeDataRace() {
    reset();
    int threadCount = 10;
    int testTime = 30000;

    // Let multiple threads write data at the same time.
    class ReadWriteThread extends Thread {
      public void run() {
        for (int i = 0; i < testTime; i++) {
          intList.addLast(1);
        }
        System.out.println("Write thread (id: " + this.getId() + ") finished.");
      }
    }

    // Set up threads
    Thread[] threadPool = new Thread[threadCount];
    for (int i = 0; i < threadCount; i++) {
      threadPool[i] = new ReadWriteThread();
    }

    // Run threads
    for (Thread thread : threadPool) {
      thread.start();
    }

    // Let the main thread waits until all working threads finished.
    try {
      for (Thread thread : threadPool) {
        thread.join();
      }
    } catch (InterruptedException e) {
    }

    // Basic linked list class would cause NullPtrException.
    System.out.println("List length: " + intList.getNodeLength());
    assertTrue(intList.getNodeLength() == intList.size());
  }

  @Test
  @DisplayName("Test delete data racing.")
  public void deleteDataRace() {
    reset();
    int threadCount = 10;
    int testTime = 3000;

    for (int i = 0; i <= threadCount * testTime; i++) {
      intList.addLast(1);
    }
    // Let multiple threads delete data at the same time.
    class DeletionThread extends Thread {
      public void run() {
        for (int i = 0; i < testTime; i++) {
          intList.remove(1);
        }
        System.out.println("Deletion thread (id: " + this.getId() + ") finished.");
      }
    }

    // Set up threads
    Thread[] threadPool = new Thread[threadCount];
    for (int i = 0; i < threadCount; i++) {
      threadPool[i] = new DeletionThread();
    }

    // Run threads
    for (int i = 0; i < threadCount; i++) {
      threadPool[i].start();
    }

    // Let the main thread waits until all working threads finished.
    try {
      for (Thread thread : threadPool) {
        thread.join();
      }
    } catch (InterruptedException e) {
    }

    // Basic linked-list would have size inconsistency.
    System.out.println(intList.getNodeLength());
    assertTrue(intList.getNodeLength() == intList.size());
  }

  private int finishedThread = 0;
  private boolean diff = false;

  @Test
  @DisplayName("Test read-write data racing.")
  public void testReadWriteDataRace() {
    reset();
    int testTime = 1000;
    int threadCount = 10;

    class WriteDeleteThread extends Thread {
      public void run() {
        for (int i = 0; i < testTime; i++) {
          intList.addAndDelete(1);
        }
        System.out.println("Deletion thread (id: " + this.getId() + ") finished.");
        finishedThread++;
      }
    }

    Thread[] threadPool = new Thread[threadCount];
    for (int i = 0; i < threadPool.length; i++) {
      threadPool[i] = new WriteDeleteThread();
    }

    Thread readThread = new Thread() {
      public void run() {
        while (finishedThread != threadCount) {
          int size = intList.size();
          if (size < 0 || size > 1) {
            diff = true;
            System.out.println(size + " !!!!!!!!!!!!!!!!");
            break;
          }
        }
      }
    };

    for (Thread thread : threadPool) {
      thread.start();
    }
    readThread.start();

    // Let the main thread waits until all working threads finished.
    try {
      for (Thread thread : threadPool) {
        thread.join();
      }
      readThread.join();
    } catch (InterruptedException e) {
    }
    assertTrue(!diff);
  }

  private void reset() {
    intList.removeAll();
  }
}
