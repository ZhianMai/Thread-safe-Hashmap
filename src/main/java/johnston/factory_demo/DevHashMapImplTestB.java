package johnston.factory_demo;

public class DevHashMapImplTestB implements DevHashMapTest {

  public DevHashMapImplTestB() {
    System.out.println("DevHashMapImplTestB default ctor");
  }

  public DevHashMapImplTestB(int val) {
    System.out.println("DevHashMapImplTestB param ctor");
  }

  @Override
  public int get(int index) {
    System.out.println("DevHashMapImplTestB get()");
    return 1;
  }

  @Override
  public void put(int index, int value) {
    System.out.println("DevHashMapImplTestB put()");
  }

  @Override
  public boolean test() {
    return false;
  }

  @Override
  public void printVal() {
    System.out.println("DevHashMapImplTestB printVal()");
  }
}
