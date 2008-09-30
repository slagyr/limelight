package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.util.Aligner;

public class SimpleVerticalAlignmentAttributeTest extends TestCase
{
  private SimpleVerticalAlignmentAttribute top;
  private SimpleVerticalAlignmentAttribute center;
  private SimpleVerticalAlignmentAttribute bottom;

  public void setUp() throws Exception
  {
    top = new SimpleVerticalAlignmentAttribute(Aligner.TOP);
    center = new SimpleVerticalAlignmentAttribute(Aligner.VERTICAL_CENTER);
    bottom = new SimpleVerticalAlignmentAttribute(Aligner.BOTTOM);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (top instanceof StyleAttribute));
    assertEquals(Aligner.TOP, top.getAlignment());
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
    assertEquals(true, top.equals(new SimpleVerticalAlignmentAttribute(Aligner.TOP)));
    assertEquals(false, top.equals(bottom));
    assertEquals(false, top.equals(null));
  }
}
