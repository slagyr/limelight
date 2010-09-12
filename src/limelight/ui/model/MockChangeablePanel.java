package limelight.ui.model;

import limelight.styles.ScreenableStyle;

import java.awt.*;

public class MockChangeablePanel extends PanelBase implements ChangeablePanel
{
  public boolean sizeChangePending;
  public boolean propagateSizeChangeUpCalled;
  public boolean propagateSizeChangeDownCalled;
  public boolean markedAsDirty;
  public boolean markAsNeedingLayoutCalled;
  public boolean borderChanged;
  public boolean clearCacheCalled;
  public TextAccessor textAccessor;

  public void paintOn(Graphics2D graphics)
  {
  }

  public ScreenableStyle getStyle()
  {
    return null;
  }

  @Override
  public void markAsDirty()
  {
    markedAsDirty = true;
  }

  @Override
  public void markAsNeedingLayout()
  {
    markAsNeedingLayoutCalled = true;
  }

  @Override
  public void markAsNeedingLayout(Layout layout)
  {
    markAsNeedingLayoutCalled = true;
    neededLayout = layout;
  }


  public void setSizeChangePending(boolean value)
  {
    sizeChangePending = value;
  }

  public void propagateSizeChangeUp()
  {
    propagateSizeChangeUpCalled = true;
  }

  public void propagateSizeChangeDown()
  {
    propagateSizeChangeDownCalled = true;
  }

  public void setBorderChanged(boolean value)
  {
    borderChanged = value;
  }

  public TextAccessor getTextAccessor()
  {
    return textAccessor;
  }

  @Override
  public void clearCache()
  {
    clearCacheCalled = true;
  }

  public Layout getNeededLayout()
  {
    return neededLayout;
  }


}
