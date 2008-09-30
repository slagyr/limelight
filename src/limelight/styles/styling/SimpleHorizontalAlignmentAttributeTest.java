package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;

public class SimpleHorizontalAlignmentAttributeTest extends TestCase
{
  private SimpleHorizontalAlignmentAttribute left;
  private SimpleHorizontalAlignmentAttribute center;
  private SimpleHorizontalAlignmentAttribute right;

  public void setUp() throws Exception
  {
    left = new SimpleHorizontalAlignmentAttribute("left");
    center = new SimpleHorizontalAlignmentAttribute("center");
    right = new SimpleHorizontalAlignmentAttribute("right");
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
    assertEquals(true, left.equals(new SimpleHorizontalAlignmentAttribute("left")));
    assertEquals(false, left.equals(right));
    assertEquals(false, left.equals(null));
  }
}
