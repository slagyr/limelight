package limelight.ui.model.updates;

import junit.framework.TestCase;

import java.awt.*;

import limelight.util.Box;
import limelight.ui.MockPanel;

public class BoundedPaintUpdateTest extends TestCase
{
  private Box bounds;
  private BoundedPaintUpdate update;

  public void setUp() throws Exception
  {
    bounds = new Box(1, 2, 3, 4);
    update = new BoundedPaintUpdate(bounds);
  }

  public void testRelativeBounds() throws Exception
  {
    assertSame(bounds, update.getRelativeBounds());
  }

  public void testAbsoluteBounds() throws Exception
  {
    MockPanel panel = new MockPanel();
    panel.stubAbsoluteLocation(new Point(11, 22));
    assertEquals(new Box(12, 24, 3, 4), update.getAbsoluteBounds(panel));
  }
  
  public void testPrioritizeWithSameBounds() throws Exception
  {
    BoundedPaintUpdate update1 = new BoundedPaintUpdate(new Box(0, 0, 10, 10));
    BoundedPaintUpdate update2 = new BoundedPaintUpdate(new Box(0, 0, 10, 10));

    assertEquals(new Box(0, 0, 10, 10), ((BoundedPaintUpdate)update1.prioritize(update2)).getRelativeBounds());
    assertEquals(new Box(0, 0, 10, 10), ((BoundedPaintUpdate)update2.prioritize(update1)).getRelativeBounds());
  }

  public void testPrioritizeWithDifferentBounds() throws Exception
  {
    BoundedPaintUpdate update1 = new BoundedPaintUpdate(new Box(0, 0, 10, 10));
    BoundedPaintUpdate update2 = new BoundedPaintUpdate(new Box(10, 10, 10, 10));

    assertEquals(new Box(0, 0, 20, 20), ((BoundedPaintUpdate)update1.prioritize(update2)).getRelativeBounds());
    assertEquals(new Box(0, 0, 20, 20), ((BoundedPaintUpdate)update2.prioritize(update1)).getRelativeBounds());
  }
}
