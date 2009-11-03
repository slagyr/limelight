package limelight.ui.model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class MockPropFrame implements PropFrame
{
  private Container contentPane;
  private RootPanel root;
  public boolean visible;
  private JFrame window;

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
  }

  public Frame getWindow()
  {
    if(window == null)
      window = new JFrame();
    return window;
  }

  public boolean isVisible()
  {
    return visible;
  }

  public boolean isVital()
  {
    return false;
  }

  public void activated(WindowEvent e)
  {
  }

  public void setRoot(RootPanel root)
  {
    this.root = root;
  }
}
