//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight;

import junit.framework.TestCase;


import limelight.ui.Panel;
import limelight.ui.model.inputs.InputPanel;
import limelight.ui.model.RootPanel;
import limelight.ui.model.MockFrame;
import limelight.ui.model.TextAccessor;
import limelight.ui.MockPanel;
import limelight.styles.Style;
import limelight.styles.styling.RealStyleAttributeCompilerFactory;

import java.awt.*;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

public class KeyboardFocusManagerTest extends TestCase
{
  private KeyboardFocusManager manager;
  private MockComponent component;
  private MockInputPanel panel;

  public void setUp() throws Exception
  {
    manager = new KeyboardFocusManager();
    manager.install();
    panel = new MockInputPanel();
    component = panel.mockComponent;
  }
  
  public void testInstall() throws Exception
  {
    assertSame(manager, java.awt.KeyboardFocusManager.getCurrentKeyboardFocusManager());
  }

  public void testFocusPanel() throws Exception
  {
    manager.focusPanel(panel);

    assertEquals(1, component.mockFocusListener.gained);
    assertEquals(panel, manager.getFocusedPanel());
    assertEquals(component, manager.getFocuedComponent());
    assertEquals(true, panel.hasFocus);
  }

  public void testFocusWhenComponentIsAlreadyFocused() throws Exception
  {
    manager.focusPanel(panel);
    manager.focusPanel(panel);

    assertEquals(1, component.mockFocusListener.gained);
    assertEquals(panel, manager.getFocusedPanel());
  }
  
  public void testFocusingAnewComponent() throws Exception
  {
    MockInputPanel panel2 = new MockInputPanel();
    manager.focusPanel(panel);
    manager.focusPanel(panel2);

    assertEquals(1, component.mockFocusListener.gained);
    assertEquals(1, component.mockFocusListener.lost);
    assertEquals(1, panel2.mockComponent.mockFocusListener.gained);
    assertEquals(panel2, manager.getFocusedPanel());
    assertEquals(false, panel.hasFocus);
    assertEquals(true, panel2.hasFocus);
  }

  public void testFocusNextComponent() throws Exception
  {
    RootPanel root = new RootPanel(new MockFrame());
    Panel panel = new MockPanel();
    root.setPanel(panel);
    MockInputPanel input1 = new MockInputPanel();
    MockInputPanel input2 = new MockInputPanel();
    panel.add(input1);
    panel.add(input2);
    manager.focusPanel(input1);

    manager.focusNextComponent();

    assertSame(input2, manager.getFocusedPanel());
  }

  public void testFocusPreviousComponent() throws Exception
  {
    RootPanel root = new RootPanel(new MockFrame());
    Panel panel = new MockPanel();
    root.setPanel(panel);
    MockInputPanel input1 = new MockInputPanel();
    MockInputPanel input2 = new MockInputPanel();
    panel.add(input1);
    panel.add(input2);
    manager.focusPanel(input2);

    manager.focusPreviousComponent();

    assertSame(input1, manager.getFocusedPanel());
  }         

  private static class MockComponent extends Component
  {
    public final MockFocusListener mockFocusListener = new MockFocusListener();

    public synchronized FocusListener[] getFocusListeners()
    {
      return new FocusListener[] {mockFocusListener};
    }
  }

  private static class MockFocusListener implements FocusListener
  {
    public int gained;
    public int lost;

    public void focusGained(FocusEvent e)
    {
      gained += 1;
    }

    public void focusLost(FocusEvent e)
    {
      lost += 1;
    }
  }

  private class MockInputPanel extends InputPanel
  {
    public MockComponent mockComponent;
    public boolean hasFocus;

    protected Component createComponent()
    {
      return mockComponent = new MockComponent();
    }

    protected TextAccessor createTextAccessor()
    {
      return null;
    }

    protected void setDefaultStyles(Style style)
    {
    }

    public void focusLost(FocusEvent e)
    {
      hasFocus = false;
    }

    public void focusGained(FocusEvent e)
    {
      hasFocus = true;
    }
  }
}
