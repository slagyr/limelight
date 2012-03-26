//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.model.api.FakePropProxy;
import limelight.ui.Panel;
import limelight.ui.PaintablePanel;
import limelight.ui.painting.PaintAction;
import limelight.ui.painting.Border;
import limelight.model.api.PropProxy;
import limelight.util.Box;

import java.util.Map;

public class MockProp extends MockParentPanel implements Prop, PaintablePanel
{
  public final FakePropProxy prop;
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
    prop = new FakePropProxy();
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

  @Override
  public Layout getDefaultLayout()
  {
    return new MockPropLayout();
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

  private class MockPropLayout implements Layout
  {
    public void doExpansion(Panel panel)
    {
      setSize(prepForSnapWidth, prepForSnapHeight);
    }

    public void doContraction(Panel panel)
    {
    }

    public void doFinalization(Panel panel)
    {
    }

    public void doLayout(Panel panel, Map<Panel, Layout> panelsToLayout)
    {
      setSize(prepForSnapWidth, prepForSnapHeight);
      for(Panel child : children)
        child.getDefaultLayout().doLayout(child, null);
      wasLaidOut = true;
    }

    public boolean overides(Layout other)
    {
      return false;
    }

    public void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout, boolean topLevel)
    {
    }
  }
}
