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
    return frame.getPanel().getOwnerOfPoint(point);
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
      frame.getPanel().mouseEntered(e);
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
    if(descendant instanceof ParentPanel)
      descendant.mouseEntered(e);
  }

  private void exit(Panel descendant, Panel ancestor, MouseEvent e)
  {
    while(descendant != ancestor)
    {
      if(descendant instanceof ParentPanel)
        descendant.mouseExited(e);
      descendant = descendant.getParent();
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
