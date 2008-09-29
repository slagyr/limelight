package limelight.styles.styling;

import junit.framework.TestCase;

public class AutoDimensionAttributeTest extends TestCase
{
  private AutoDimensionAttribute auto;

  public void setUp() throws Exception
  {
    auto = new AutoDimensionAttribute();
  }

  public void testToString() throws Exception
  {
    assertEquals("auto", auto.toString());
  }
  
  public void testEquals() throws Exception
  {
    assertEquals(true, auto.equals(auto));
    assertEquals(true, auto.equals(new AutoDimensionAttribute()));
    assertEquals(false, auto.equals(null));
  }

}
