//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import junit.framework.TestCase;
import limelight.util.Aligner;

public class AlignerTest extends TestCase
{
  public void testTopLeft() throws Exception
  {
    Box area = new Box(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.LEFT, Aligner.TOP);

    assertEquals(0, aligner.startingX(0));
    assertEquals(0, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(0, aligner.startingX(50));
    assertEquals(0, aligner.startingY());
  }

  public void testCenterAlignments() throws Exception
  {
    Box area = new Box(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.HORIZONTAL_CENTER, Aligner.VERTICAL_CENTER);

    assertEquals(50, aligner.startingX(0));
    assertEquals(50, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(25, aligner.startingX(50));
    assertEquals(25, aligner.startingY());
  }

  public void testBottomRightAlignment() throws Exception
  {
    Box area = new Box(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.RIGHT, Aligner.BOTTOM);

    assertEquals(100, aligner.startingX(0));
    assertEquals(100, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(50, aligner.startingX(50));
    assertEquals(50, aligner.startingY());
  }

  public void testTopLeftNotStartingAtZero() throws Exception
  {
    Box area = new Box(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.LEFT, Aligner.TOP);

    assertEquals(5, aligner.startingX(0));
    assertEquals(5, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(5, aligner.startingX(50));
    assertEquals(5, aligner.startingY());
  }

  public void testCenterAlignmentsNotStartingAtZero() throws Exception
  {
    Box area = new Box(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.HORIZONTAL_CENTER, Aligner.VERTICAL_CENTER);

    assertEquals(55, aligner.startingX(0));
    assertEquals(55, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(30, aligner.startingX(50));
    assertEquals(30, aligner.startingY());
  }

  public void testBottomRightAlignmentNotStartingAtZero() throws Exception
  {
    Box area = new Box(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, Aligner.RIGHT, Aligner.BOTTOM);

    assertEquals(105, aligner.startingX(0));
    assertEquals(105, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(55, aligner.startingX(50));
    assertEquals(55, aligner.startingY());
  }
}
