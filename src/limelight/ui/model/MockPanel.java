//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.model.Panel;
import limelight.ui.api.MockProp;
import limelight.util.Box;

import java.awt.*;

public class MockPanel extends Panel
{
  public static int paintCount = 0;

  public Box boxInsideMargin;
  public Box boxInsidePadding;
  private int prepWidth;
  private int prepHeight;
  public boolean wasLaidOut;
  private boolean dimensionsArePrepared;
  public int paintIndex;

  public MockPanel()
	{
		super(new MockProp());
	}

  public Box getBoxInsideMargins()
  {
    if(boxInsideMargin != null)
      return boxInsideMargin;
    return super.getBoxInsideMargins();
  }

  public Box getBoxInsidePadding()
  {
    if(boxInsidePadding != null)
      return boxInsidePadding;
    return super.getBoxInsidePadding();
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
