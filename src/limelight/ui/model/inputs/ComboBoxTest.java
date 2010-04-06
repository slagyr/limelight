//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import junit.framework.TestCase;
import limelight.ui.api.MockProp;
import limelight.ui.model.ScenePanel;
import limelight.ui.model.MockPropFrame;

import java.awt.*;

public class ComboBoxTest extends TestCase
{
  private ComboBoxPanel panel;
  private ComboBox comboBox;
  private ScenePanel root;

  public void setUp() throws Exception
  {
    root = new ScenePanel(new MockProp());
    root.setFrame(new MockPropFrame());
    panel = new ComboBoxPanel();
    comboBox = new ComboBox(panel);
    root.add(panel);
  }

  public void testRepaint() throws Exception
  {
    panel.setSize(100, 100);
    comboBox.repaint();

    assertEquals(true, root.dirtyRegionsContains(panel.getAbsoluteBounds()));
  }

  public void testRapaintWithParams() throws Exception
  {
    comboBox.repaint(1, 2, 3, 4);

    assertEquals(true, root.dirtyRegionsContains(new Rectangle(1, 2, 3, 4)));
  }

  public void testRapaintWithRectangle() throws Exception
  {
    comboBox.repaint(new Rectangle(1, 2, 3, 4));

    assertEquals(true, root.dirtyRegionsContains(new Rectangle(1, 2, 3, 4)));
  }

}
