//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.awt.*;
import java.awt.event.WindowEvent;

public interface PropFrame
{
  Container getContentPane();

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
}
