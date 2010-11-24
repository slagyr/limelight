//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.model.Stage;
import limelight.ui.Panel;
import limelight.ui.events.panel.*;
import limelight.ui.model.inputs.InputPanel;

import java.awt.event.KeyEvent;

public class StageKeyListener implements java.awt.event.KeyListener
{
  private Panel focusedPanel;
  private Stage stage;

  public StageKeyListener(Scene rootPanel)
  {
    focusedPanel = rootPanel;
  }

  public StageKeyListener(Stage stage)
  {
    this.stage = stage;
  }

  public void reset(Scene root)
  {
    focusedPanel = root;
  }

  public Panel getFocusedPanel()
  {
    if(focusedPanel == null)
      focusedPanel = stage.getScene();
    return focusedPanel;
  }

  public void keyTyped(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new CharTypedEvent(e.getModifiers(), e.getKeyChar()).dispatch(focusedPanel);
  }

  public void keyPressed(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new KeyPressedEvent(e.getModifiers(), e.getKeyCode(), e.getKeyLocation()).dispatch(focusedPanel);
  }

  public void keyReleased(KeyEvent e)
  {
    if(getFocusedPanel() == null)
      return;

    new KeyReleasedEvent(e.getModifiers(), e.getKeyCode(), e.getKeyLocation()).dispatch(focusedPanel);
  }

  public void focusOn(Panel panel)
  {
    if(panel == null || panel == focusedPanel)
      return;

    Panel previouslyFocusPanel = focusedPanel;
    focusedPanel = panel;

    if(previouslyFocusPanel != null)
      new FocusLostEvent().dispatch(previouslyFocusPanel);

    new FocusGainedEvent().dispatch(panel);
  }

  public void focusOnNextInput()
  {
    if(getFocusedPanel() == null)
      return;

    focusOn(nextInputPanel(focusedPanel));
  }

  public void focusOnPreviousInput()
  {
    if(getFocusedPanel() == null)
      return;
    
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
