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
    area = panel.getChildConsumableArea();

    if(panel.getChildren().size() == 0)
			return;

    doLayoutOnChildren();
    reset();
    buildRows();

    boolean abort = adjustScrollBars();

    if(!abort)
    {
      Aligner aligner = buildAligner(area);
      layoutRows(aligner);
    }
  }

  private boolean adjustScrollBars()
  {
    boolean layoutRequired = false;
    if(view.getScrollPanel().isHorizontalScrollBarOn() && consumedWidth <= (area.width + ScrollBarPanel.SCROLL_BAR_WIDTH))
    {
      view.getScrollPanel().disableHorizontalScrollBar();
      panel.setHeight(panel.getHeight() + ScrollBarPanel.SCROLL_BAR_WIDTH);
    }
    else if(!view.getScrollPanel().isHorizontalScrollBarOn() && consumedWidth > area.width)
    {
      view.getScrollPanel().enableHorizontalScrollBar();
      panel.setHeight(panel.getHeight() - ScrollBarPanel.SCROLL_BAR_WIDTH);
    }
    if(view.getScrollPanel().isVerticalScrollBarOn() && consumedHeight <= (area.height + ScrollBarPanel.SCROLL_BAR_WIDTH))
    {
      view.getScrollPanel().disableVerticalScrollBar();
      panel.setWidth(panel.getWidth() + ScrollBarPanel.SCROLL_BAR_WIDTH);
      layoutRequired = true;
    }
    else if(!view.getScrollPanel().isVerticalScrollBarOn() && consumedHeight > area.height)
    {
      view.getScrollPanel().enableVerticalScrollBar();
      panel.setWidth(panel.getWidth() - ScrollBarPanel.SCROLL_BAR_WIDTH);
      layoutRequired = true;
    }

    if(!view.getScrollPanel().isVerticalScrollBarOn() && !view.getScrollPanel().isHorizontalScrollBarOn())
    {
      revertToNonScrollView();
      return true;
    }
    else if(layoutRequired)
    {
      area = panel.getChildConsumableArea();
      reset();
      buildRows();
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
