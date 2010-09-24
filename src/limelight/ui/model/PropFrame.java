//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.events.EventHandler;

import java.awt.*;

public interface PropFrame
{
  RootPanel getRoot();
  
  void close();

  boolean isVisible();

  boolean isVital();

  boolean shouldAllowClose();

//  void addKeyListener(KeyListener listener);
//  void addMouseListener(MouseListener listener);
//  void addMouseMotionListener(MouseMotionListener listener);
//  void addMouseWheelListener(MouseWheelListener listener);
//  void addWindowStateListener(WindowStateListener alertFrameManager);
//  void addWindowFocusListener(WindowFocusListener alertFrameManager);
//  void addWindowListener(WindowListener alertFrameManager);
//  void removeKeyListener(KeyListener listener);
//  void removeMouseListener(MouseListener listener);
//  void removeMouseMotionListener(MouseMotionListener listener);
//  void removeMouseWheelListener(MouseWheelListener listener);
//  MouseListener[] getMouseListeners();
//  MouseMotionListener[] getMouseMotionListeners();
//  MouseWheelListener[] getMouseWheelListeners();
//  KeyListener[] getKeyListeners();
//  WindowFocusListener[] getWindowFocusListeners();

  int getWidth();

  int getHeight();

  Graphics getGraphics();

  Cursor getCursor();

  void setCursor(Cursor cursor);

  Insets getInsets();

  EventHandler getEventHandler();

  RootKeyListener getKeyListener();
  RootMouseListener getMouseListener();
}
