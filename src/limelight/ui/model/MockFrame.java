package limelight.ui.model;

import limelight.ui.api.MockStage;
import limelight.ui.MockGraphics;

import java.awt.*;

public class MockFrame extends Frame
{
  private Container contentPanel;

  public MockFrame()
  {
    setStage(new MockStage());
    contentPanel = new Container(){
      public Graphics getGraphics()
      {
        return new MockGraphics();
      }
    };
  }

  public Container getContentPane()
  {
    return contentPanel;
  }
}
