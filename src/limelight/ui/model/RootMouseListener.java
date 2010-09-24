//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.model.Stage;
import limelight.ui.Panel;
import limelight.ui.events.panel.*;

import java.awt.event.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class RootMouseListener implements MouseListener, MouseMotionListener, MouseWheelListener
{
  private RootPanel panel; // TODO MDM remove this member
  private Stage stage;
  public Panel pressedPanel;
  public Panel hooveredPanel;

  public RootMouseListener(RootPanel panel)
  {
    this.panel = panel;
  }

  public RootMouseListener(Stage stage)
  {
    this.stage = stage;
  }

  public RootPanel getRoot()
  {
    if(panel == null)
      return stage.getRoot();
    else
      return panel;
  }

  public void reset()
  {
    panel = null;
    pressedPanel = null;
    hooveredPanel = null;
  }

  private Panel panelFor(Point point)
  {
    return getRoot().getOwnerOfPoint(point);
  }

  public void mouseClicked(MouseEvent e)
  {
    // IGNORE
  }

  public void mousePressed(MouseEvent e)
  {
    if(getRoot() == null)
      return;

    pressedPanel = panelFor(e.getPoint());
    new MousePressedEvent(pressedPanel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(pressedPanel);
  }

  public void mouseReleased(MouseEvent e)
  {
    if(getRoot() == null)
      return;

    Panel releasedPanel = panelFor(e.getPoint());
    new MouseReleasedEvent(releasedPanel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(releasedPanel);
    if(releasedPanel == pressedPanel)
    {
      new MouseClickedEvent(releasedPanel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(releasedPanel);
    }
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
    if(getRoot() == null)
      return;

    Panel panel = panelFor(e.getPoint());
    if(panel != hooveredPanel)
      transition(panel, e);
    if(pressedPanel != null)
    {
      new MouseDraggedEvent(pressedPanel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(pressedPanel);
    }
  }

  public void mouseMoved(MouseEvent e)
  {
    if(getRoot() == null)
      return;

    Panel panel = panelFor(e.getPoint());
    if(panel != hooveredPanel)
      transition(panel, e);
    new MouseMovedEvent(panel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(panel);
  }

  public void mouseWheelMoved(MouseWheelEvent e)
  {
    if(getRoot() == null)
      return;
    
    final Panel panel = panelFor(e.getPoint());  
    new limelight.ui.events.panel.MouseWheelEvent(panel, e.getModifiers(), e.getPoint(), e.getClickCount(), e.getScrollType(), e.getScrollAmount(), e.getWheelRotation()).dispatch(panel);
  }

  private void transition(Panel panel, MouseEvent e)
  {
    if(hooveredPanel == null)
    {
      new MouseEnteredEvent(panel, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(panel);
      enter(panel, panel, e);
    }
    else if(hooveredPanel.isDescendantOf(panel))
      exit(hooveredPanel, panel, e);
    else if(panel.isDescendantOf(hooveredPanel))
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
    new MouseEnteredEvent(descendant, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(descendant);
  }

  private void exit(Panel descendant, Panel ancestor, MouseEvent e)
  {
    while(descendant != ancestor && !(descendant instanceof Scene))
    {
      if(descendant != null)
      {
        new MouseExitedEvent(descendant, e.getModifiers(), e.getPoint(), e.getClickCount()).dispatch(descendant);
        descendant = descendant.getParent();
      }
    }
  }
}
