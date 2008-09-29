package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class VerticalAlignmentAttributeTest extends TestCase
{
  private VerticalAlignmentAttribute top;
  private VerticalAlignmentAttribute center;
  private VerticalAlignmentAttribute bottom;

  public void setUp() throws Exception
  {
    top = new VerticalAlignmentAttribute("top");
    center = new VerticalAlignmentAttribute("center");
    bottom = new VerticalAlignmentAttribute("bottom");
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
    assertEquals(true, top.equals(new VerticalAlignmentAttribute("top")));
    assertEquals(false, top.equals(bottom));
    assertEquals(false, top.equals(null));
  }
}
