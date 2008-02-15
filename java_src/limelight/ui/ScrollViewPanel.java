package limelight.ui;

import java.awt.*;
import java.util.LinkedList;

public class ScrollViewPanel extends ParentPanel
{
  private ScrollViewLayout layout;
  private Point startingPoint;

  public ScrollViewPanel(LinkedList<Panel> children) throws SterilePanelException
  {
    super();
    layout = new ScrollViewLayout(this);
    for(Panel child : children)
      add(child);
  }

  public void snapToSize()
  {
    setSize(getParent().getWidth() - ScrollBarPanel.SCROLL_BAR_WIDTH, getParent().getHeight() - ScrollBarPanel.SCROLL_BAR_WIDTH);  
  }

  public Rectangle getChildConsumableArea()
  {
    return new Rectangle(0, 0, getWidth(), getHeight());
  }
  
  public void doLayout()
  {
    layout.doLayout();
  }

  public int getConsumedWidth()
  {
    return layout.getConsumedWidth();
  }

  public int getConsumedHeight()
  {
    return layout.getConsumedHeight();
  }

  public void paintOn(Graphics2D graphics)
  {
  }

  public void repaint()
  {
    PaintJob job = new PaintJob(getAbsoluteBounds());
    job.paint(getFrame().getPanel());
    job.applyTo(getFrame().getGraphics());
  }

  public ScrollPanel getScrollPanel()
  {
    return  (ScrollPanel)getParent();
  }

  public Point getStartingPoint()
  {
    return startingPoint;
  }

  public void update()
  {
    restoreChildLocations();
    translateChildLocations();
    repaint();
  }

  private void restoreChildLocations()
  {
    if(startingPoint == null)
      return;
    for(Panel child : children)
      child.setLocation(child.getX() + startingPoint.x, child.getY() + startingPoint.y);
  }

  private void translateChildLocations()
  {
    startingPoint = getScrollPanel().getScrollCoordinates();
    for(Panel child : children)
      child.setLocation(child.getX() - startingPoint.x, child.getY() - startingPoint.y);
  }
}
