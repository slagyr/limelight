//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.awt.*;
import java.awt.event.*;

public interface PropFrame
{
  Point getLocationOnScreen();

  RootPanel getRoot();
  
  void close(WindowEvent e);

  Frame getWindow();

  boolean isVisible();

  boolean isVital();

  void activated(WindowEvent e);

  boolean shouldAllowClose();

  void closed(WindowEvent e);

  void iconified(WindowEvent e);

  void deiconified(WindowEvent e);

  void deactivated(WindowEvent e);

  void addKeyListener(KeyListener listener);
  void addMouseListener(MouseListener listener);
  void addMouseMotionListener(MouseMotionListener listener);
  void addMouseWheelListener(MouseWheelListener listener);
  void removeKeyListener(KeyListener listener);
  void removeMouseListener(MouseListener listener);
  void removeMouseMotionListener(MouseMotionListener listener);
  void removeMouseWheelListener(MouseWheelListener listener);
  MouseListener[] getMouseListeners();
  MouseMotionListener[] getMouseMotionListeners();
  MouseWheelListener[] getMouseWheelListeners();
  KeyListener[] getKeyListeners();

  int getWidth();

  int getHeight();

  Graphics getGraphics();

  Cursor getCursor();

  void setCursor(Cursor cursor);

  Insets getInsets();
}
