package limelight.ui;

public class ScrollViewLayout extends PanelLayout
{
  private ScrollViewPanel view;

  public ScrollViewLayout(ScrollViewPanel panel)
  {
    super(panel);
    view = panel;
  }

  public void doLayout()
  {                   
    panel.snapToSize();

    if(panel.getChildren().size() == 0)
			return;

    doLayoutOnChildren();
    rebuildRows();

    boolean abort = adjustScrollBars();

    if(!abort)
    {
      Aligner aligner = buildAligner(area);
      layoutRows(aligner);
    }
  }

  private void rebuildRows()
  {
    area = panel.getChildConsumableArea();
    reset();
    buildRows();
  }

  private boolean adjustScrollBars()
  {
    boolean verticalOn = view.getScrollPanel().isVerticalScrollBarOn();
    boolean horizontalOn = view.getScrollPanel().isHorizontalScrollBarOn();
    if(verticalOn && consumedHeight <= (area.height + ScrollBarPanel.SCROLL_BAR_WIDTH))
    {
      view.getScrollPanel().disableVerticalScrollBar();
      panel.setWidth(panel.getWidth() + ScrollBarPanel.SCROLL_BAR_WIDTH);
      rebuildRows();
    }
    else if(!view.getScrollPanel().isVerticalScrollBarOn() && consumedHeight > area.height)
    {
      view.getScrollPanel().enableVerticalScrollBar();
      panel.setWidth(panel.getWidth() - ScrollBarPanel.SCROLL_BAR_WIDTH);
      rebuildRows();
    }

    if(horizontalOn && consumedWidth <= area.width)
    {
      view.getScrollPanel().disableHorizontalScrollBar();
      panel.setHeight(panel.getHeight() + ScrollBarPanel.SCROLL_BAR_WIDTH);
    }
    else if(!view.getScrollPanel().isHorizontalScrollBarOn() && consumedWidth > area.width)
    {
      view.getScrollPanel().enableHorizontalScrollBar();
      panel.setHeight(panel.getHeight() - ScrollBarPanel.SCROLL_BAR_WIDTH);
    }

    if(!view.getScrollPanel().isVerticalScrollBarOn() && !view.getScrollPanel().isHorizontalScrollBarOn())
    {
      revertToNonScrollView();
      return true;
    }
    return false;  
  }

  private void revertToNonScrollView()
  {
    try
    {
      ParentPanel parent = view.getScrollPanel().getParent();
      parent.clearChildren();
      for(Panel child: view.getChildren())
        parent.add(child);
      parent.doLayout();
    }
    catch (SterilePanelException e)
    {
      e.printStackTrace();
    }
  }

  public int getConsumedWidth()
  {
    return consumedWidth;
  }

  public int getConsumedHeight()
  {
    return consumedHeight;
  }
}
