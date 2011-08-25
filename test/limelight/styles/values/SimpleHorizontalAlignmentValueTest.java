//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.values;

import junit.framework.TestCase;
import limelight.styles.abstrstyling.StyleValue;
import limelight.styles.HorizontalAlignment;
import limelight.util.Box;

public class SimpleHorizontalAlignmentValueTest extends TestCase
{
  private SimpleHorizontalAlignmentValue left;
  private SimpleHorizontalAlignmentValue center;
  private SimpleHorizontalAlignmentValue right;

  public void setUp() throws Exception
  {
    left = new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT);
    center = new SimpleHorizontalAlignmentValue(HorizontalAlignment.CENTER);
    right = new SimpleHorizontalAlignmentValue(HorizontalAlignment.RIGHT);
  }

  public void testCreation() throws Exception
  {
    assertEquals(true, (left instanceof StyleValue));
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
    assertEquals(true, left.equals(new SimpleHorizontalAlignmentValue(HorizontalAlignment.LEFT)));
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
