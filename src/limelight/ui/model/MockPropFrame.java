package limelight.ui.model;

import java.awt.*;

public class MockPropFrame implements PropFrame
{
  private Container contentPane;

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
}
