package limelight.ui.model.updates;

import junit.framework.TestCase;

import java.awt.*;

import limelight.ui.MockGraphics;

public class BoundedShallowPaintUpdateTest extends TestCase
{
  private Rectangle bounds;
  private BoundedShallowPaintUpdate update;

  public void setUp() throws Exception
  {
    bounds = new Rectangle(1, 2, 3, 4);
    update = new BoundedShallowPaintUpdate(bounds);
  }

  public void testHasRectangle() throws Exception
  {
    assertEquals(bounds, update.getBounds());
  }
  
  public void testModifyGraphics() throws Exception
  {
    MockGraphics graphics = new MockGraphics();
    update.modifyGraphics(graphics);

    Rectangle clip = graphics.clippedRectangle;
    assertEquals(1, clip.x);
    assertEquals(2, clip.y);
    assertEquals(3, clip.width);
    assertEquals(4, clip.height);
  }
}
