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
}
