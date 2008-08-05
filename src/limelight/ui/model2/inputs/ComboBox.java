package limelight.ui.model2.inputs;

import limelight.ui.model2.updates.Updates;
import limelight.ui.model2.RootPanel;
import limelight.Context;

import javax.swing.*;
import java.awt.*;

public class ComboBox extends JComboBox
{
  private ComboBoxPanel panel;

  public ComboBox(ComboBoxPanel panel)
  {
    super();
    this.panel = panel;
    Component[] components = getComponents();
    addNotify();
    for(Component component : components)
      component.addNotify();
  }

  public boolean requestFocusInWindow()
  {
    Context.instance().keyboardFocusManager.focusPanel(panel);
    return true;
  }

  public void requestFocus()
  {
    requestFocusInWindow();
  }

  public Point getLocationOnScreen()
  {
    Point frameLocation = ((RootPanel) panel.getRoot()).getFrame().getLocation();
    Point panelLocation = panel.getAbsoluteLocation();
    return new Point(frameLocation.x + panelLocation.x, frameLocation.y + panelLocation.y);
  }

  public void repaint()
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void repaint(Rectangle r)
  {
    if(panel != null)
      panel.setNeededUpdate(Updates.shallowPaintUpdate);
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    doLayout();
  }

  protected void paintChildren(Graphics g)
  {
    for(Component comp : getComponents())
    {
      comp.setVisible(true);
      Rectangle cr = comp.getBounds();

      boolean hitClip = g.hitClip(cr.x, cr.y, cr.width, cr.height);

      if(hitClip)
      {
        Graphics cg = g.create(cr.x, cr.y, cr.width,
            cr.height);
        cg.setColor(comp.getForeground());
        cg.setFont(comp.getFont());
        try
        {
          comp.paint(cg);
        }
        finally
        {
          cg.dispose();
        }
      }
    }
  }

  public boolean isShowing()
  {
    return true;
  }

}
