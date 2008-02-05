package limelight.ui;

import java.awt.*;
import java.awt.event.*;

public class FrameListener implements MouseListener, MouseMotionListener, MouseWheelListener, KeyListener
{
  private Frame frame;
  public Panel pressedPanel;
  public Panel hooveredPanel;

  public FrameListener(Frame frame)
  {
    this.frame = frame;
  }

  private Panel panelFor(Point point)
  {
    Panel result;
    if(hooveredPanel != null && hooveredPanel.containsAbsolutePoint(point))
      if(hooveredPanel.getParent() != null)
        result = hooveredPanel.getOwnerOfPoint(new Point(point.x - hooveredPanel.getParent().getX(), point.y - hooveredPanel.getParent().getY()));
      else
        result = hooveredPanel.getOwnerOfPoint(point);
    else
      result = frame.getPanel().getOwnerOfPoint(point);

    return result;
  }

  public void mouseClicked(MouseEvent e)
  {
    // IGNORE
  }

  public void mousePressed(MouseEvent e)
  {
    pressedPanel = panelFor(e.getPoint());
    pressedPanel.getBlock().mouse_pressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    Panel releasedPanel = panelFor(e.getPoint());
    releasedPanel.getBlock().mouse_released(e);
    if(releasedPanel == pressedPanel)
      releasedPanel.getBlock().mouse_clicked(e);
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
    panel.getBlock().mouse_dragged(e);
  }

  private void transition(Panel panel, MouseEvent e)
  {
    if(hooveredPanel == null)
    {
      frame.getPanel().getBlock().mouse_entered(e);
      enter(panel, frame.getPanel(), e);
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
    if(descendant == ancestor || descendant == null)
      return;
    enter(descendant.getParent(), ancestor, e);
    if(descendant instanceof BlockPanel)
    {
      descendant.getBlock().hover_on();
      descendant.getBlock().mouse_entered(e);
    }
  }

  private void exit(Panel descendant, Panel ancestor, MouseEvent e)
  {
    while(descendant != ancestor)
    {
      if(descendant instanceof BlockPanel)
      {
        descendant.getBlock().mouse_exited(e);
        descendant.getBlock().hover_off();
      }
      descendant = descendant.getParent();
    }
  }

  public void mouseMoved(MouseEvent e)
  {
    Panel panel = panelFor(e.getPoint());
    if(panel != hooveredPanel)
      transition(panel, e);
    panel.getBlock().mouse_moved(e);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
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
