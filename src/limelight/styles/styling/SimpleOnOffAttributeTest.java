package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleOnOffAttributeTest extends TestCase
{
  private SimpleOnOffAttribute on;
  private SimpleOnOffAttribute off;

  public void setUp() throws Exception
  {
    on = new SimpleOnOffAttribute(true);
    off = new SimpleOnOffAttribute(false);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (on instanceof StyleAttribute));
    assertEquals(true, on.isOn());
    assertEquals(false, off.isOn());
    assertEquals(true, off.isOff());
    assertEquals(false, on.isOff());
  }

  public void testToString() throws Exception
  {
    assertEquals("on", on.toString());
    assertEquals("off", off.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, on.equals(on));
    assertEquals(true, on.equals(new SimpleOnOffAttribute(true)));
    assertEquals(false, on.equals(off));
    assertEquals(false, on.equals(null));
  }
}
