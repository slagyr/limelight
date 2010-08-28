//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;


import limelight.styles.ScreenableStyle;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.inputs.InputPanel;
import limelight.util.Box;

import java.awt.*;

// TODO MDM Delete me!
public class KeyboardFocusManagerTest extends TestCase
{
  private KeyboardFocusManager manager;
  private MockInputPanel panel;

  public void setUp() throws Exception
  {
    manager = new KeyboardFocusManager();
    manager.install();
    panel = new MockInputPanel();

    MockRootPanel root = new MockRootPanel();
    root.add(panel);
  }

  public void testInstall() throws Exception
  {
    assertSame(manager, java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager());
  }
//
//  public void testFocusPanel() throws Exception
//  {
//    manager.focusPanel(panel);
//
//    assertEquals(panel, manager.getFocusedPanel());
//    assertEquals(true, panel.hasFocus);
//  }

//  public void testFocusWhenComponentIsAlreadyFocused() throws Exception
//  {
//    manager.focusPanel(panel);
//    manager.focusPanel(panel);
//
//    assertEquals(panel, manager.getFocusedPanel());
//  }
  
//  public void testFocusingAnewComponent() throws Exception
//  {
//    MockInputPanel panel2 = new MockInputPanel();
//    manager.focusPanel(panel);
//    manager.focusPanel(panel2);
//
//    assertEquals(panel2, manager.getFocusedPanel());
//    assertEquals(false, panel.hasFocus);
//    assertEquals(true, panel2.hasFocus);
//  }

//  public void testFocusNextComponent() throws Exception
//  {
//    ScenePanel root = new ScenePanel(new MockProp());
//    root.setFrame(new MockPropFrame());
//    Panel panel = new MockPanel();
//    root.add(panel);
//    MockInputPanel input1 = new MockInputPanel();
//    MockInputPanel input2 = new MockInputPanel();
//    panel.add(input1);
//    panel.add(input2);
//    manager.focusPanel(input1);
//
//    manager.focusNextComponent();
//
//    assertSame(input2, manager.getFocusedPanel());
//  }
//
//  public void testFocusPreviousComponent() throws Exception
//  {
//    ScenePanel root = new ScenePanel(new MockProp());
//    root.setFrame(new MockPropFrame());
//    Panel panel = new MockPanel();
//    root.add(panel);
//    MockInputPanel input1 = new MockInputPanel();
//    MockInputPanel input2 = new MockInputPanel();
//    panel.add(input1);
//    panel.add(input2);
//    manager.focusPanel(input2);
//
//    manager.focusPreviousComponent();
//
//    assertSame(input1, manager.getFocusedPanel());
//  }

  private class MockInputPanel extends InputPanel
  {
    public boolean hasFocus;

    public Box getBoxInsidePadding()
    {
      return null;
    }

    public Box getChildConsumableArea()
    {
      return null;
    }

    public void paintOn(Graphics2D graphics)
    {
    }

    public ScreenableStyle getStyle()
    {
      return null;
    }
  }
}
