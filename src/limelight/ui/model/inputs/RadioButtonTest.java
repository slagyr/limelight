package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.MockFrame;
import limelight.ui.model.RootPanel;
import limelight.ui.model.updates.BoundedPaintUpdate;
import limelight.ui.model.updates.PaintUpdate;

import java.awt.*;

public class RadioButtonTest extends TestCase
{
  private RadioButtonPanel panel;
  private RadioButton radioButton;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());
    panel = new RadioButtonPanel();
    radioButton = new RadioButton(panel);
    root.setPanel(panel);
  }

  public void testRepaint() throws Exception
  {
    radioButton.repaint();

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(PaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithParams() throws Exception
  {
    radioButton.repaint(1, 2, 3, 4);

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithRectangle() throws Exception
  {
    radioButton.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }
}
