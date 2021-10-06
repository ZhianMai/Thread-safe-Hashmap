package johnston.hashmap.test;

import johnston.hashmap.MyHashMapImpl;
import johnston.hashmap.MyHashMapThreadSafeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MyHashMapThreadSafeImplConcurrencyTest {
  private MyHashMapThreadSafeImpl<String, Integer> hashMap;
  // Use the thread-unsafe hash map would fail all tests.
  // private MyHashMapImpl<String, Integer> hashMap;
  private int globalTestTime;
  private Random random;
  private int testFactor;

  @BeforeEach
  public void init() {
    hashMap = new MyHashMapThreadSafeImpl<String, Integer>();
    // hashMap = new MyHashMapImpl<String, Integer>();
    globalTestTime = 100;
    testFactor = 500;
    random = new Random();
  }

  @Test
  @DisplayName("Test Junit functionality.")
  public void demoTestMethod() {
    assertTrue(true);
    assertTrue((hashMap != null), "Test object init.");
  }

  @Test
  @DisplayName("Test write data racing.")
  public void writeDataRace() {
    reset();
    int threadCount = 10;
    int testTime = globalTestTime * testFactor;

    // Let multiple threads write data at the same time.
    class ReadWriteThread extends Thread {
      public void run() {
        for (int i = 0; i < testTime; i++) {
          String key = String.valueOf(random.nextDouble()); // Generates unique key.
          hashMap.put(key, i);
          assertTrue(hashMap.containsKey(key));
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

    // Basic hash map class would cause NullPtrException.
    assertEquals(hashMap.getTotalPairCount(), hashMap.size());
    assertEquals(hashMap.size(), testTime * threadCount);
  }

  @Test
  @DisplayName("Test delete data racing.")
  public void deleteDataRace() {
    reset();
    int threadCount = 10;
    int testTime = globalTestTime * 10;

    // Let multiple threads delete data at the same time.
    class DeletionThread extends Thread {
      public void run() {
        List<String> keys = buildStringInput(String.valueOf(random.nextDouble()) , testTime);
        for (int i = 0; i < testTime * testFactor; i++) {
          String key = keys.get(i % testFactor);
          hashMap.put(key, 1);
          hashMap.remove(key);
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
    assertEquals(hashMap.size(), 0);
    assertEquals(hashMap.getTotalPairCount(), 0);
  }

  private void reset() {
    hashMap.removeAll();
  }

  int finishedThread = 0;
  boolean diff = false;

  @Test
  @DisplayName("Test read-write data racing.")
  public void testReadWriteDataRace() {
    reset();
    int testTime = globalTestTime * testFactor;
    int threadCount = 10;

    class WriteDeleteThread extends Thread {
      public void run() {
        for (int i = 0; i < testTime; i++) {
          hashMap.addAndDelete(String.valueOf(random.nextDouble()), 1);
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
          int size = hashMap.size();
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

  /**
  * This method is to print each bucket size to show if clustered.
  */
  private void printALlBucketSize() {
    int[] allBucketSize = hashMap.getAllBucketSize();
    for (int i = 0; i < allBucketSize.length; i++) {
      System.out.print(allBucketSize[i] + ",");

      if (i != 0 && i % 20 == 0) {
        System.out.println();
      }
    }
  }

  private List<String> buildStringInput(String prefix, int count) {
    List<String> result = new ArrayList<>();
    int max = 100;

    for (int i = 0; i < count; i++) {
      result.add(prefix + i + " " + random.nextInt(max));
    }
    return result;
  }

  private void writeSameValue(List<String> keys, int val) {
    for (String key : keys) {
      hashMap.put(key, val);
    }
  }
}
