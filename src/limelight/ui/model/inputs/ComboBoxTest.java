package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.updates.PaintUpdate;
import limelight.ui.model.updates.BoundedPaintUpdate;

import java.awt.*;

public class ComboBoxTest extends TestCase
{
  private ComboBoxPanel panel;
  private ComboBox comboBox;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());
    panel = new ComboBoxPanel();
    comboBox = new ComboBox(panel);
    root.setPanel(panel);
  }

  public void testRepaint() throws Exception
  {
    comboBox.repaint();

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(PaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithParams() throws Exception
  {
    comboBox.repaint(1, 2, 3, 4);

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithRectangle() throws Exception
  {
    comboBox.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }

}
