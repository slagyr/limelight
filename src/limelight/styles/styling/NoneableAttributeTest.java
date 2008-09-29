package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class NoneableAttributeTest extends TestCase
{
  private NoneableAttribute<IntegerAttribute> none;
  private NoneableAttribute<IntegerAttribute> fifty;

  public void setUp() throws Exception
  {
    none = new NoneableAttribute<IntegerAttribute>(null);
    fifty = new NoneableAttribute<IntegerAttribute>(new IntegerAttribute(50));
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (none instanceof StyleAttribute));
    assertEquals(true, none.isNone());
    assertEquals(50, fifty.getAttribute().getValue());
  }

  public void testToString() throws Exception
  {
    assertEquals("none", none.toString());
    assertEquals("50", fifty.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, none.equals(none));
    assertEquals(true, none.equals(new NoneableAttribute<IntegerAttribute>(null)));
    assertEquals(false, none.equals(fifty));
    assertEquals(true, fifty.equals(fifty));
    assertEquals(true, fifty.equals(new NoneableAttribute<IntegerAttribute>(new IntegerAttribute(50))));
    assertEquals(false, none.equals(null));
  }
}
