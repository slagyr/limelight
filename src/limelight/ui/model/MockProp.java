//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.ui.PaintablePanel;
import limelight.model.api.MockPropProxy;
import limelight.ui.painting.PaintAction;
import limelight.ui.painting.Border;
import limelight.model.api.PropProxy;
import limelight.util.Box;

public class MockProp extends MockParentPanel implements Prop, PaintablePanel
{
  public final MockPropProxy prop;
  public Box childConsumableBounds;
  private int prepForSnapWidth;
  private int prepForSnapHeight;
  public boolean wasLaidOut;
  public boolean wasFloatLaidOut;
  public Box boxInsideMargins = new Box(0, 0, 100, 100);
  public Box boxInsideBorders = new Box(0, 0, 100, 100);
  public String name;

  public MockProp()
  {
    prop = new MockPropProxy();
  }

  public MockProp(String name)
  {
    this();
    this.name = name;
  }

  public Box getChildConsumableBounds()
  {
    if(childConsumableBounds != null)
      return childConsumableBounds;
    else
      return getBounds();
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

  public TextAccessor getTextAccessor()
  {
    throw new RuntimeException("MockPropablePanel.getTextAccessor() called");
  }

  public void setTextAccessor(TextAccessor accessor)
  {
    throw new RuntimeException("MockPropablePanel.setTextAccessor() called");
  }

  public Box getBorderedBounds()
  {
    return boxInsideBorders;
  }

  public Box getMarginedBounds()
  {
    return boxInsideMargins;
  }

  public Border getBorderShaper()
  {
    return new Border(getStyle(), boxInsideMargins);
  }

  public PropProxy getProxy()
  {
    return prop;
  }

  public void doLayout()
  {
    super.doLayout();
    setSize(prepForSnapWidth, prepForSnapHeight);
    for(Panel child : children)
      child.doLayout();
    wasLaidOut = true;
  }

  public void doFloatLayout()
  {
    super.doFloatLayout();
    wasFloatLaidOut = true;
  }

  public void prepForSnap(int width, int height)
  {
    prepForSnapWidth = width;
    prepForSnapHeight = height;
  }

  public String toString()
  {
    return super.toString() + ":" + name;
  }
}
