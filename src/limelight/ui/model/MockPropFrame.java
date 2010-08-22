//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;

public class MockPropFrame implements PropFrame
{
  private Container contentPane;
  private RootPanel root;
  public boolean visible;
  private MockPropFrameWindow window;
  public boolean closed;
  public boolean activated;
  public boolean shouldAllowClose;
  public boolean wasClosed;
  public boolean iconified;
  public boolean vital = true;
  public KeyListener keyListener;

  public MockPropFrame()
  {
    contentPane = new Container();
  }

  public Container getContentPane()
  {
    return contentPane;
  }

  public Point getLocationOnScreen()
  {
    return null;
  }

  public RootPanel getRoot()
  {
    return root;
  }

  public void close(WindowEvent e)
  {
    closed = true;
  }

  public Frame getWindow()
  {
    if(window == null)
      window = new MockPropFrameWindow(this);
    return window;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public boolean isVital()
  {
    return vital;
  }

  public void activated(WindowEvent e)
  {
    activated = true;
  }

  public boolean shouldAllowClose()
  {
    return shouldAllowClose;
  }

  public void closed(WindowEvent e)
  {
    wasClosed = true;
  }

  public void iconified(WindowEvent e)
  {
    iconified = true;
  }

  public void deiconified(WindowEvent e)
  {
    iconified = false;
  }

  public void deactivated(WindowEvent e)
  {
    activated = false;
  }

  public void addKeyListener(java.awt.event.KeyListener keyListener)
  {
    this.keyListener = keyListener;
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public void setVital(boolean vital)
  {
    this.vital = vital;
  }

  private static class MockPropFrameWindow extends JFrame implements PropFrameWindow
  {
    private PropFrame frame;

    public MockPropFrameWindow(PropFrame frame) throws HeadlessException
    {
      this.frame = frame;
    }

    public PropFrame getPropFrame()
    {
      return frame;
    }
  }
}
