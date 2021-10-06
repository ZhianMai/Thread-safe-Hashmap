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

public class MyHashMapThreadSafeImplCorrectnessTest {
  private MyHashMapThreadSafeImpl<String, Integer> hashMap;
  private int globalTestTime;

  @BeforeEach
  public void init() {
    hashMap = new MyHashMapThreadSafeImpl<String, Integer>();
    globalTestTime = 100;
  }

  @Test
  @DisplayName("Test Junit functionality.")
  public void demoTestMethod() {
    assertTrue(true);
    assertTrue((hashMap != null), "Test object init.");
  }

  
}
