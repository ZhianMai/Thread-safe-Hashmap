package johnston.linkedlist.test;

import johnston.linkedlist.MyLinkedList;
import johnston.linkedlist.MyLinkedListThreadSafeImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Same content as MyLinkedListImplTest, just init() new a different class.
 */
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySafeLinkedListCorrectnessTest {
  private MyLinkedList<String> stringList;

  @BeforeEach
  public void init() {
    stringList = new MyLinkedListThreadSafeImpl<>();
  }

  @Test
  @DisplayName("Test Junit functionality.")
  public void demoTestMethod() {
    assertTrue(true);
    assertTrue((stringList != null), "Test object init.");
  }

  @Test
  @DisplayName("Test removeAll()")
  public void testRemovAll() {
    reset();

    for (int i = 0; i < 100; i++) {
      stringList.addLast("test");
    }

    stringList.removeAll();
    int size = stringList.size();
    List<String> list = stringList.getAll();
    String str = stringList.get(0);

    assertTrue(size == 0 && list.size() == 0 && str == null);
  }

  @Test
  @DisplayName("Test size consistency")
  public void testSize() {
    reset();
    int testTime = 100;

    for (int i = 0; i < testTime; i++) {
      stringList.addLast("test");
    }

    int size = stringList.size();
    List<String> list = stringList.getAll();
    assertTrue(size == testTime && list.size() == testTime);
  }

  @Test
  @DisplayName("Test index")
  public void testIndex() {
    reset();
    int testTime = 100;
    List<String> input = buildStringInput("Test ", testTime);

    for (int i = 0; i < testTime; i++) {
      stringList.addLast(new String(input.get(i)));
    }

    boolean diff = false;

    for (int i = 0; i < testTime; i++) {
      if (!input.get(i).equals(stringList.get(i))) {
        diff = true;
        break;
      }
    }
    assertTrue(!diff);
  }

  @Test
  @DisplayName("Test ctor")
  public void testCtor() {
    String str = "Test";
    stringList = new MyLinkedListThreadSafeImpl<>(new String(str));

    assertTrue(stringList.size() == 1 && str.equals(stringList.get(0)));
  }

  @Test
  @DisplayName("Test remove element.")
  public void testRemoveElement() {
    reset();
    String bad = "Should delete";
    int testTime = 100;
    List<String> input = buildStringInput("Test ", testTime);

    for (int i = 0; i < testTime; i++) {
      stringList.addLast(new String(input.get(i)));

      if (i == testTime / 2) {
        stringList.addLast(bad);
      }
    }

    boolean success = stringList.remove(new String(bad));
    boolean diff = false;

    for (int i = 0; i < testTime; i++) {
      if (!input.get(i).equals(stringList.get(i))) {
        diff = true;
        System.out.println(input.get(i));
        System.out.println(stringList.get(i));
        break;
      }
    }
    assertTrue(!diff && success);
  }

  @Test
  @DisplayName("Test remove non-existing element.")
  public void testRemoveBadElement() {
    reset();
    int testTime = 100;
    List<String> input = buildStringInput("Test ", testTime);

    for (int i = 0; i < testTime; i++) {
      stringList.addLast(new String(input.get(i))); // Use different String obj
    }

    boolean success = stringList.remove(new String("Should delete"));
    boolean diff = false;

    for (int i = 0; i < testTime; i++) {
      if (!input.get(i).equals(stringList.get(i))) {
        diff = true;
        System.out.println(input.get(i));
        System.out.println(stringList.get(i));
        break;
      }
    }
    assertTrue(!diff && !success);
  }

  @Test
  @DisplayName("Test contains().")
  public void testContainMethod() {
    reset();
    int testTime = 100;
    List<String> input = buildStringInput("Test ", testTime);

    for (int i = 0; i < testTime; i++) {
      String str = "Test " + i;
      stringList.addLast(str); // Use different String obj
    }

    boolean existsElement = stringList.contains(input.get(testTime / 2));
    boolean noSuchElement = stringList.contains("Bad string");

    assertTrue(existsElement && !noSuchElement);
  }

  @Test
  @DisplayName("Test empty list")
  public void testEmptyList() {
    reset();

    assertTrue(stringList.isEmpty() && stringList.size() == 0 && stringList.get(0) == null &&
               !stringList.contains("Bad string") && stringList.getAll().size() == 0);
  }

  @Test
  @DisplayName("Test new node append")
  public void testNewNodeAppend() {
    reset();
    String one = "one";
    String two = "two";
    String three = "three";
    stringList.addFirst(one);
    stringList.addFirst(two);
    stringList.addFirst(three);

    assertTrue(stringList.getIndex(one) == 2 &&
        stringList.getIndex(two) == 1 &&
        stringList.getIndex(three) == 0 &&
        stringList.size() == 3);
  }

  @Test
  @DisplayName("Test set value at given idx")
  public void testSetValue() {
    reset();
    int testTime = 100;
    List<String> input = buildStringInput("Test ", testTime);
    for (String str : input) {
      stringList.addLast(str); // Use different String obj
    }

    String newString = "new string";
    stringList.set(newString, 0);
    stringList.set(newString, testTime / 2);
    stringList.set(newString, testTime - 1);

    assertTrue(newString.equals(stringList.get(0)) &&
        newString.equals(stringList.get(testTime / 2)) &&
        newString.equals(stringList.get(testTime - 1)));
  }

  @Test
  @DisplayName("Test get value index")
  public void testGetValueIndex() {
    reset();
    int testTime = 100;
    String begin = "begin";
    String middle = "mid";
    String end = "end";
    List<String> input = buildStringInput("Test ", testTime);
    input.set(0, begin);
    input.set(testTime / 2, middle);
    input.set(testTime - 1, end);

    for (String str : input) {
      stringList.addLast(str); // Use different String obj
    }

    assertTrue(stringList.getIndex(begin) == 0 &&
        stringList.getIndex(middle) == testTime / 2 &&
        stringList.getIndex(end) == testTime - 1 &&
        stringList.getIndex("Null") == -1
    );
  }

  @Test
  @DisplayName("Test return the same value as the given value")
  public void testReturnSameValue() {
    reset();
    String strA = "A";
    String strB = "B";
    String strC = "C";
    stringList.addLast(new String(strA));
    stringList.addLast(new String(strB));
    stringList.addLast(new String(strC));

    assertTrue(strA.equals(stringList.get(strA)) &&
        strB.equals(stringList.get(strB)) &&
        strC.equals(stringList.get(strC)) &&
        stringList.get("Bad string") == null);
  }

  private void reset() {
    stringList.removeAll();
  }

  private List<String> buildStringInput(String prefix, int count) {
    List<String> result = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      result.add(prefix + i);
    }
    return result;
  }
}
