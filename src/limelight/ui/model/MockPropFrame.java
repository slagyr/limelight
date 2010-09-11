//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.MockGraphics;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyListener;
import java.util.LinkedList;

public class MockPropFrame implements PropFrame
{
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
  private LinkedList<KeyListener> keyListeners = new LinkedList<KeyListener>();
  private LinkedList<MouseListener> mouseListeners = new LinkedList<MouseListener>();
  private LinkedList<MouseMotionListener> mouseMotionListeners = new LinkedList<MouseMotionListener>();
  private LinkedList<MouseWheelListener> mouseWheelListeners = new LinkedList<MouseWheelListener>();
  private Cursor cursor = Cursor.getDefaultCursor();
  private Dimension size = new Dimension(0, 0);

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

  public void addKeyListener(KeyListener listener)
  {
    this.keyListener = listener;
    keyListeners.add(listener);
  }

  public void addMouseListener(MouseListener listener)
  {
    mouseListeners.add(listener);
  }

  public void addMouseMotionListener(MouseMotionListener listener)
  {
    mouseMotionListeners.add(listener);
  }

  public void addMouseWheelListener(MouseWheelListener listener)
  {
    mouseWheelListeners.add(listener);
  }

  public void removeKeyListener(KeyListener listener)
  {
    keyListeners.remove(listener);
  }

  public void removeMouseListener(MouseListener listener)
  {
    mouseListeners.remove(listener);
  }

  public void removeMouseMotionListener(MouseMotionListener listener)
  {
    mouseMotionListeners.remove(listener);
  }

  public void removeMouseWheelListener(MouseWheelListener listener)
  {
    mouseWheelListeners.remove(listener);
  }

  public KeyListener[] getKeyListeners()
  {
    return keyListeners.toArray(new KeyListener[keyListeners.size()]);
  }

  public int getWidth()
  {
    return size.width;
  }

  public int getHeight()
  {
    return size.height;
  }

  public Graphics getGraphics()
  {
    return new MockGraphics();
  }

  public Cursor getCursor()
  {
    return cursor;
  }

  public void setCursor(Cursor cursor)
  {
    this.cursor = cursor;
  }

  public Insets getInsets()
  {
    return new Insets(0, 0, 0, 0);
  }

  public MouseListener[] getMouseListeners()
  {
    return mouseListeners.toArray(new MouseListener[mouseListeners.size()]);
  }

  public MouseMotionListener[] getMouseMotionListeners()
  {
    return mouseMotionListeners.toArray(new MouseMotionListener[mouseMotionListeners.size()]);
  }

  public MouseWheelListener[] getMouseWheelListeners()
  {
    return mouseWheelListeners.toArray(new MouseWheelListener[mouseWheelListeners.size()]);
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }

  public void setVital(boolean vital)
  {
    this.vital = vital;
  }

  public void setSize(int width, int height)
  {
    size = new Dimension(width, height);
  }

  private static class MockPropFrameWindow extends Frame implements PropFrameWindow
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
