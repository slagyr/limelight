//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.styling;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleAttribute;
import limelight.styles.HorizontalAlignment;
import limelight.util.Box;

public class SimpleHorizontalAlignmentAttributeTest extends TestCase
{
  private SimpleHorizontalAlignmentAttribute left;
  private SimpleHorizontalAlignmentAttribute center;
  private SimpleHorizontalAlignmentAttribute right;

  public void setUp() throws Exception
  {
    left = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT);
    center = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.CENTER);
    right = new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.RIGHT);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (left instanceof StyleAttribute));
    assertEquals(HorizontalAlignment.LEFT, left.getAlignment());
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
    assertEquals(true, left.equals(new SimpleHorizontalAlignmentAttribute(HorizontalAlignment.LEFT)));
    assertEquals(false, left.equals(right));
    assertEquals(false, left.equals(null));
  }

  public void testStartingX() throws Exception
  {
    Box area = new Box(0, 0, 100, 100);
    assertEquals(0, left.getX(100, area));
    assertEquals(0, left.getX(1, area));

    assertEquals(0, center.getX(100, area));
    assertEquals(25, center.getX(50, area));
    assertEquals(37, center.getX(25, area));
    assertEquals(49, center.getX(1, area));

    assertEquals(0, right.getX(100, area));
    assertEquals(50, right.getX(50, area));
    assertEquals(99, right.getX(1, area));
  }
}
