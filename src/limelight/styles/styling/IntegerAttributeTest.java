package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class IntegerAttributeTest extends TestCase
{
  private IntegerAttribute attr50;
  private IntegerAttribute attr100;
  private IntegerAttribute attr999;

  public void setUp() throws Exception
  {
    attr50 = new IntegerAttribute(50);
    attr100 = new IntegerAttribute(100);
    attr999 = new IntegerAttribute(999);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (attr50 instanceof StyleAttribute));
    assertEquals(50, attr50.getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("50", attr50.toString());
    assertEquals("100", attr100.toString());
    assertEquals("999", attr999.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, attr50.equals(attr50));
    assertEquals(true, attr50.equals(new IntegerAttribute(50)));
    assertEquals(false, attr50.equals(attr999));
  }
}
