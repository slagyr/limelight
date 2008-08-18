package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.updates.PaintUpdate;
import limelight.ui.model.updates.BoundedPaintUpdate;

import java.awt.*;

public class ButtonTest extends TestCase
{
  private ButtonPanel panel;
  private Button button;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());
    panel = new ButtonPanel();
    button = new Button(panel);
    root.setPanel(panel);
  }

  public void testRepaint() throws Exception
  {
    button.repaint();

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(PaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithParams() throws Exception
  {
    button.repaint(1, 2, 3, 4);

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithRectangle() throws Exception
  {
    button.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }
}
