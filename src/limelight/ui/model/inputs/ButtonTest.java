//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockPropFrame;

import java.awt.*;

public class ButtonTest extends TestCase
{
  private ButtonPanel panel;
  private Button button;
  private RootPanel root;

  public void setUp() throws Exception
  {
    root = new RootPanel(new MockPropFrame());
    panel = new ButtonPanel();
    button = new Button(panel);
    root.setPanel(panel);
  }

  public void testRepaint() throws Exception
  {
    panel.setSize(100, 100);
    button.repaint();

    assertEquals(true, root.dirtyRegionsContains(panel.getAbsoluteBounds()));
  }

  public void testRapaintWithParams() throws Exception
  {
    button.repaint(1, 2, 3, 4);

    assertEquals(true, root.dirtyRegionsContains(new Rectangle(1, 2, 3, 4)));
  }

  public void testRapaintWithRectangle() throws Exception
  {
    button.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.dirtyRegionsContains(new Rectangle(1, 2, 3, 4)));
  }
}
