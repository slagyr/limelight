package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.StyleAttribute;

public class HorizontalAlignmentAttributeTest extends TestCase
{
  private HorizontalAlignmentAttribute left;
  private HorizontalAlignmentAttribute center;
  private HorizontalAlignmentAttribute right;

  public void setUp() throws Exception
  {
    left = new HorizontalAlignmentAttribute("left");
    center = new HorizontalAlignmentAttribute("center");
    right = new HorizontalAlignmentAttribute("right");
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (left instanceof StyleAttribute));
    assertEquals("left", left.getAlignment());
  }

  public void testToString() throws Exception
  {
    assertEquals("left", left.toString());
    assertEquals("center", center.toString());
    assertEquals("right", right.toString());
  }

  public void testEquality() throws Exception
  {
    assertEquals(true, left.equals(left));
    assertEquals(true, left.equals(new HorizontalAlignmentAttribute("left")));
    assertEquals(false, left.equals(right));
    assertEquals(false, left.equals(null));
  }
}
