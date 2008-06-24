package limelight.ui.model2;

import limelight.ui.MockPanel;
import limelight.ui.painting.PaintAction;
import limelight.ui.api.PropablePanel;
import limelight.ui.api.MockProp;
import limelight.ui.api.Prop;
import limelight.util.Box;
import limelight.styles.FlatStyle;
import limelight.styles.Style;

public class MockPropablePanel extends MockPanel implements PropablePanel
{
  public MockProp prop;
  public FlatStyle style;
  public Box childConsumableBox;
  public boolean floater;

  public MockPropablePanel()
  {
    prop = new MockProp();
    style = prop.getStyle();
  }

  public Box getChildConsumableArea()
  {
    if(childConsumableBox != null)
      return childConsumableBox;
    else
      return super.getChildConsumableArea();
  }

  public void paintImmediately(int x, int y, int width, int height)
  {
  }

  public void setAfterPaintAction(PaintAction action)
  {
  }

  public void setText(String text)
  {
  }

  public String getText()
  {
    return null;
  }

  public Box getBoxInsideBorders()
  {
    return null;
  }

  public Style getStyle()
  {
    return style;
  }

  public Prop getProp()
  {
    return prop;
  }

  public boolean isFloater()
  {
    return floater;
  }
}
