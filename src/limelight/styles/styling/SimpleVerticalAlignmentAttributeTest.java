package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleVerticalAlignmentAttributeTest extends TestCase
{
  private SimpleVerticalAlignmentAttribute top;
  private SimpleVerticalAlignmentAttribute center;
  private SimpleVerticalAlignmentAttribute bottom;

  public void setUp() throws Exception
  {
    top = new SimpleVerticalAlignmentAttribute("top");
    center = new SimpleVerticalAlignmentAttribute("center");
    bottom = new SimpleVerticalAlignmentAttribute("bottom");
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (top instanceof StyleAttribute));
    assertEquals("top", top.getAlignment());
  }

  public void testToString() throws Exception
  {
    assertEquals("top", top.toString());
    assertEquals("center", center.toString());
    assertEquals("bottom", bottom.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, top.equals(top));
    assertEquals(true, top.equals(new SimpleVerticalAlignmentAttribute("top")));
    assertEquals(false, top.equals(bottom));
    assertEquals(false, top.equals(null));
  }
}
