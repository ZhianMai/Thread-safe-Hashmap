package johnston.hashmap.test;

import johnston.hashmap.MapPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MapPairTest {
  private MapPair<String, Integer> pairOne;
  private MapPair<String, Integer> pairTwo;

  @BeforeEach
  public void init() {
    pairOne = new MapPair<>("pair A", 0);
    pairTwo = new MapPair<>("pair B", 1);
  }

  @Test
  @DisplayName("Test Junit functionality.")
  public void checkJunit() {
    assertTrue(true);
    assertTrue((pairOne != null) && (pairTwo != null), "Test object init.");
  }

  @Test
  @DisplayName("Test MapPair equals() method")
  public void checkMapPairEquals() {
    MapPair<String, Integer> copyOne = new MapPair<>(new String(pairOne.key), 2);
    MapPair<String, Integer> copyTwo = new MapPair<>(new String(pairTwo.key), 2);
    MapPair<String, Integer> badOne = new MapPair<>(new String("Bad"), 2);
    MapPair<Integer, Integer> badTwo = new MapPair<>(1, 2);

    assertTrue(copyOne.equals(pairOne));
    assertTrue(copyTwo.equals(pairTwo));

    assertTrue(pairOne.equals(copyOne));

    assertTrue(pairOne.equals(pairOne));

    assertTrue(!copyOne.equals(badOne));
    assertTrue(!copyOne.equals(badTwo));
  }
}
