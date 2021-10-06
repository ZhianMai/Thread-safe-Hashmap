package johnston.linkedlist;

/**
 * This interface provides methods for multi-threading testing.
 */
public interface LinkedListConcurrencyTestSupport<V> {
  /**
   * This method is for multi writes data racing test.
   */
  public int getNodeLength();

  /**
   * This method is for multi read and write data racing test.
   */
  public void addAndDelete(V v);
}
