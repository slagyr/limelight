//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.ui.Panel;
import limelight.ui.model.*;
import limelight.util.Box;
import limelight.styles.Style;
import limelight.Context;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class AwtInputPanel extends BasePanel implements InputPanel
{
  private final Component component;
  private boolean inPaintOn;

  protected AwtInputPanel()
  {
    component = createComponent();
//    component.setBackground(TRANSPARENT);
    component.addKeyListener(new InputPanelKeyListener(this));
    if(component instanceof AbstractButton)
      ((AbstractButton)component).addActionListener(new ButtonActionListener(this));
  }

  protected abstract Component createComponent();
  protected abstract TextAccessor createTextAccessor();
  protected abstract void setDefaultStyles(Style style);

  public Component getComponent()
  {
    return component;
  }

  public boolean canBeBuffered()
  {
    return false;
  }

  public void setParent(limelight.ui.Panel panel)
  {
    if(panel == null)
      Context.instance().keyboardFocusManager.focusFrame((StageFrame)getRoot().getStageFrame());
    super.setParent(panel);
    if(panel instanceof PropPanel)
    {
      PropPanel propPanel = (PropPanel) panel;
      propPanel.sterilize();
      propPanel.setTextAccessor(createTextAccessor());
      setDefaultStyles(propPanel.getStyle());
    }
  }

  public void doLayout()
  {
    InputPanelLayout.instance.doLayout(this);
  }

  public Layout getDefaultLayout()
  {
    return InputPanelLayout.instance;
  }

  public void setSize(int w, int h)
  {
    super.setSize(w, h);
    component.setSize(w, h);
  }

  public void setLocation(int x, int y)
  {
    super.setLocation(x, y);
    component.setLocation(x, y);
  }

  public Box getChildConsumableArea()
  {
    return getBoundingBox();
  }

  public Box getBoxInsidePadding()
  {
    return getBoundingBox();
  }

  public Style getStyle()
  {
    return getParent().getStyle();
  }

  public int getWidth()
  {
    return component.getWidth();
  }

  public int getHeight()
  {
    return component.getHeight();
  }

  public void paintOn(Graphics2D graphics)
  {
    inPaintOn = true;
    component.paint(graphics);
    inPaintOn = false;
  }

  public boolean isInPaintOn()
  {
    return inPaintOn;
  }

  public void mousePressed(MouseEvent e)
  {
    e = translatedEvent(e);
    e.setSource(component);
    for(MouseListener mouseListener : component.getMouseListeners())
      mouseListener.mousePressed(e);
    getParent().mousePressed(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    e = translatedEvent(e);
    e.setSource(component);
    for(MouseListener mouseListener : component.getMouseListeners())
      mouseListener.mouseReleased(e);
    getParent().mouseReleased(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    e = translatedEvent(e);
    Context.instance().keyboardFocusManager.focusPanel(this);
    for(MouseListener mouseListener : component.getMouseListeners())
      mouseListener.mouseClicked(e);
    getParent().mouseClicked(e);
  }

  public void mouseDragged(MouseEvent e)
  {
    e = translatedEvent(e);
    e.setSource(component);
    for(MouseMotionListener mouseListener : component.getMouseMotionListeners())
      mouseListener.mouseDragged(e);
  }

  private static class InputPanelKeyListener implements KeyListener
  {
    private final AwtInputPanel panel;

    public InputPanelKeyListener(AwtInputPanel inputPanel)
    {
      this.panel = inputPanel;
    }

    public void keyTyped(KeyEvent e)
    {
      panel.keyTyped(e);
    }

    public void keyPressed(KeyEvent e)
    {
      panel.keyPressed(e);
    }

    public void keyReleased(KeyEvent e)
    {
      panel.keyReleased(e);
    }
  }

  private static class ButtonActionListener implements ActionListener
  {
    private final AwtInputPanel panel;

    public ButtonActionListener(AwtInputPanel inputPanel)
    {
      this.panel = inputPanel;
    }

    public void actionPerformed(ActionEvent e)
    {
      panel.buttonPressed(e);
    }
  }
}
