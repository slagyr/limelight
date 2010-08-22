package limelight.ui.model;

import limelight.ui.Panel;
import limelight.ui.model.inputs.InputPanel;

import java.awt.event.KeyEvent;

public class RootKeyListener implements java.awt.event.KeyListener
{
  private Panel focusedPanel;

  public RootKeyListener(RootPanel rootPanel)
  {
    focusedPanel = rootPanel;
  }

  public void keyTyped(KeyEvent e)
  {
    focusedPanel.keyTyped(e);
  }

  public void keyPressed(KeyEvent e)
  {
    focusedPanel.keyPressed(e);
  }

  public void keyReleased(KeyEvent e)
  {
    focusedPanel.keyReleased(e);
  }

  public Panel getFocusedPanel()
  {
    return focusedPanel;
  }

  public void focusOn(Panel panel)
  {
    if(panel == null || panel == focusedPanel)
      return;

    Panel previouslyFocusPanel = focusedPanel;
    focusedPanel = panel;

    if(previouslyFocusPanel != null)
      previouslyFocusPanel.focusLost(null);

    panel.focusGained(null);
  }

  public void focusOnNextInput()
  {
    focusOn(nextInputPanel(focusedPanel));
  }

  public void focusOnPreviousInput()
  {
    focusOn(previousInputPanel(focusedPanel));
  }

  public static InputPanel nextInputPanel(Panel start)
  {
    InputPanel next = null;
    InputPanel first = null;
    boolean foundMe = false;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(foundMe)
        {
          next = (InputPanel) panel;
          break;
        }
        else if(panel == start)
          foundMe = true;
        if(first == null)
          first = (InputPanel) panel;
      }
    }

    if(next != null)
      return next;
    else
      return first;
  }

  public static InputPanel previousInputPanel(Panel start)
  {
    InputPanel previous = null;

    for(Panel panel : start.getRoot())
    {
      if(panel instanceof InputPanel)
      {
        if(panel == start && previous != null)
        {
          break;
        }
        else
        {
          previous = (InputPanel) panel;
        }
      }
    }

    return previous;
  }
}
