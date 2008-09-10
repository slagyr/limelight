//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.updates.PaintUpdate;
import limelight.ui.model.updates.BoundedPaintUpdate;

import java.awt.*;

public class CheckBoxTest extends TestCase
{
  private CheckBoxPanel panel;
  private CheckBox checkBox;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockFrame());
    panel = new CheckBoxPanel();
    checkBox = new CheckBox(panel);
    root.setPanel(panel);
  }

  public void testRepaint() throws Exception
  {
    checkBox.repaint();

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(PaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithParams() throws Exception
  {
    checkBox.repaint(1, 2, 3, 4);

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }

  public void testRapaintWithRectangle() throws Exception
  {
    checkBox.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.changedPanelsContains(panel));
    assertEquals(BoundedPaintUpdate.class, panel.getNeededUpdate().getClass());
  }
}
