package limelight;

import junit.framework.TestCase;

public class AlignerTest extends TestCase
{
  public void testDefaultAlignmentIsSameAsTopLeft() throws Exception
  {
    Rectangle area = new Rectangle(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, null, null);

    assertEquals(0, aligner.startingX(0));
    assertEquals(0, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(0, aligner.startingX(50));
    assertEquals(0, aligner.startingY());
  }

  public void testCenterAlignments() throws Exception
  {
    Rectangle area = new Rectangle(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, "center", "center");

    assertEquals(50, aligner.startingX(0));
    assertEquals(50, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(25, aligner.startingX(50));
    assertEquals(25, aligner.startingY());
  }

  public void testBottomRightAlignment() throws Exception
  {
    Rectangle area = new Rectangle(0, 0, 100, 100);
    Aligner aligner = new Aligner(area, "right", "bottom");

    assertEquals(100, aligner.startingX(0));
    assertEquals(100, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(50, aligner.startingX(50));
    assertEquals(50, aligner.startingY());
  }

  public void testTopLeftNotStartingAtZero() throws Exception
  {
    Rectangle area = new Rectangle(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, null, null);

    assertEquals(5, aligner.startingX(0));
    assertEquals(5, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(5, aligner.startingX(50));
    assertEquals(5, aligner.startingY());
  }

  public void testCenterAlignmentsNotStartingAtZero() throws Exception
  {
    Rectangle area = new Rectangle(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, "center", "center");

    assertEquals(55, aligner.startingX(0));
    assertEquals(55, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(30, aligner.startingX(50));
    assertEquals(30, aligner.startingY());
  }

  public void testBottomRightAlignmentNotStartingAtZero() throws Exception
  {
    Rectangle area = new Rectangle(5, 5, 100, 100);
    Aligner aligner = new Aligner(area, "right", "bottom");

    assertEquals(105, aligner.startingX(0));
    assertEquals(105, aligner.startingY());

    aligner.addConsumedHeight(50);
    assertEquals(55, aligner.startingX(50));
    assertEquals(55, aligner.startingY());
  }
}
