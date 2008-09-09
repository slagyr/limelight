package limelight.ui.model.updates;

import junit.framework.TestCase;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.Update;
import limelight.ui.MockPanel;
import limelight.ui.Panel;
import limelight.Context;
import limelight.BufferedImagePool;
import limelight.caching.SimpleCache;

import java.awt.image.BufferedImage;

public class ScrollChangedUpdateTest extends TestCase
{
  private Update update;
  private RootPanel root;
  private MockPanel panel;

  public void setUp() throws Exception
  {
    Context.instance().bufferedImageCache = new SimpleCache<Panel, BufferedImage>();
    Context.instance().bufferedImagePool = new BufferedImagePool(1);
    update = new LayoutAndPaintUpdate(5);
    root = new RootPanel(new MockFrame());
    panel = new MockPanel();
    root.setPanel(panel);
  }

  public void testPerformUpdate() throws Exception
  {
    update.performUpdate(panel);

    assertEquals(true, panel.wasPainted);
  }
}
