package limelight.ui.model2;

import limelight.ui.Panel;
import java.awt.event.*;
import java.awt.*;

public class EventListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
  private Panel panel;
  public Panel pressedPanel;
  public Panel hooveredPanel;

  public EventListener(Panel panel)
  {
    this.panel = panel;
  }

  private Panel panelFor(Point point)
  {
    return panel.getOwnerOfPoint(point);
  }

  public void mouseClicked(MouseEvent e)
  {
    // IGNORE
  }

  public void mousePressed(MouseEvent e)
  {
    pressedPanel = panelFor(e.getPoint());
    pressedPanel.mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    Panel releasedPanel = panelFor(e.getPoint());
    releasedPanel.mouseReleased(e);
    if(releasedPanel == pressedPanel)
      releasedPanel.mouseClicked(e);
  }

  public void mouseEntered(MouseEvent e)
  {
    // IGNORE
  }

  public void mouseExited(MouseEvent e)
  {
    // IGNORE
  }

  public void mouseDragged(MouseEvent e)
  {
    Panel panel = panelFor(e.getPoint());
    if(panel != hooveredPanel)
      transition(panel, e);
    panel.mouseDragged(e);
  }

  private void transition(Panel panel, MouseEvent e)
  {
    if(hooveredPanel == null)
    {
      panel.mouseEntered(e);
      enter(panel, panel, e);
    }
    else if(hooveredPanel.isAncestor(panel))
      exit(hooveredPanel, panel, e);
    else if(panel.isAncestor(hooveredPanel))
      enter(panel, hooveredPanel, e);
    else
    {
      Panel ancestor = hooveredPanel.getClosestCommonAncestor(panel);
      exit(hooveredPanel, ancestor, e);
      enter(panel, ancestor, e);
    }
    hooveredPanel = panel;
  }

  private void enter(Panel descendant, Panel ancestor, MouseEvent e)
  {
System.err.println("enter");    
    if(descendant == ancestor || descendant == null)
      return;
    enter(descendant.getParentPanel(), ancestor, e);
//    if(descendant instanceof ParentPanel)
    if(descendant != null)
      descendant.mouseEntered(e);
  }

  private void exit(Panel descendant, Panel ancestor, MouseEvent e)
  {
    while(descendant != ancestor && !(descendant instanceof RootPanel))
    {
//      if(descendant instanceof ParentPanel)
      if(descendant != null)
        descendant.mouseExited(e);
      descendant = descendant.getParentPanel();
    }
  }

  public void mouseMoved(MouseEvent e)
  {
    Panel panel = panelFor(e.getPoint());
    if(panel != hooveredPanel)
      transition(panel, e);
    panel.mouseMoved(e);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    panelFor(e.getPoint()).mouseWheelMoved(e);
  }

  public void keyTyped(KeyEvent e)
  {
  }

  public void keyPressed(KeyEvent e)
  {
  }

  public void keyReleased(KeyEvent e)
  {
  }
}
