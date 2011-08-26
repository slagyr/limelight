//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui;

import junit.framework.TestCase;


import limelight.styles.Style;
import limelight.ui.model.FakeScene;
import limelight.ui.model.inputs.InputPanel;

import java.awt.*;

// TODO MDM Delete me!
public class KeyboardFocusManagerTest extends TestCase
{
  private limelight.ui.KeyboardFocusManager manager;
  private MockInputPanel panel;

  public void setUp() throws Exception
  {
    manager = new limelight.ui.KeyboardFocusManager();
    manager.install();
    panel = new MockInputPanel();

    FakeScene root = new FakeScene();
    root.add(panel);
  }

  public void testInstall() throws Exception
  {
    assertSame(manager, java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager());
  }

  private class MockInputPanel extends InputPanel
  {
    public boolean hasFocus;

    public void paintOn(Graphics2D graphics)
    {
    }

    @Override
    protected void setDefaultStyles(Style style)
    {
    }

    public void setText(String text)
    {
    }

    public String getText()
    {
      return null;
    }
  }
}
