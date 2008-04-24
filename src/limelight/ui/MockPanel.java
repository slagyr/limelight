package limelight.ui;

import limelight.ui.Rectangle;
import limelight.ui.Panel;
import limelight.ui.MockProp;

import java.awt.*;

public class MockPanel extends Panel
{
  public static int paintCount = 0;

  public Rectangle rectangleInsideMargin;
  public Rectangle rectangleInsidePadding;
  private int prepWidth;
  private int prepHeight;
  public boolean wasLaidOut;
  private boolean dimensionsArePrepared;
  public int paintIndex;

  public MockPanel()
	{
		super(new MockProp());
	}

  public Rectangle getRectangleInsideMargins()
  {
    if(rectangleInsideMargin != null)
      return rectangleInsideMargin;
    return super.getRectangleInsideMargins();
  }

  public Rectangle getRectangleInsidePadding()
  {
    if(rectangleInsidePadding != null)
      return rectangleInsidePadding;
    return super.getRectangleInsidePadding();
  }

  public void prepForSnap(int width, int height)
  {
    this.prepWidth = width;
    this.prepHeight = height;
    getProp().getStyle().setWidth(width + "");
    getProp().getStyle().setHeight(height + "");
    dimensionsArePrepared = true;
  }

  public Dimension getMaximumSize()
  {
    if(dimensionsArePrepared)
      return new Dimension(prepWidth, prepHeight);
    else
      return super.getMaximumSize();
  }

  public void doLayout()
  {  
    super.doLayout();
    wasLaidOut = true;
  }

  public void paint(Graphics graphics)
  {   
    paintIndex = paintCount++;
//    super.paint(graphics);
  }
}
