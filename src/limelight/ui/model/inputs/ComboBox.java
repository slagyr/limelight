//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.Context;

import javax.swing.*;
import java.awt.*;

public class ComboBox extends JComboBox
{
  private final ComboBoxPanel panel;
  private boolean inPaintChildren;

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
    Point frameLocation = panel.getRoot().getFrame().getLocationOnScreen();
    Point panelLocation = panel.getAbsoluteLocation();
    return new Point(frameLocation.x + panelLocation.x, frameLocation.y + panelLocation.y + 44);
    //TODO This magic 44 is to get the popup displayed in the right location.  I doubt the constant works on all operating systems.
  }

  public void repaint()
  {
    if(panel != null && !inPaintChildren && !panel.isInPaintOn() && panel.getRoot() != null)
      panel.getRoot().addDirtyRegion(panel.getAbsoluteBounds());
  }

  public void repaint(long tm, int x, int y, int width, int height)
  {
    repaint(new Rectangle(x, y, width, height));
  }

  public void repaint(Rectangle dirtyBounds)
  {
    if(panel != null && panel.getRoot() != null)
    {
      Point location = panel.getAbsoluteLocation();
      dirtyBounds.translate(location.x, location.y);
      panel.getRoot().addDirtyRegion(dirtyBounds);
    }
  }

  public void setSize(int width, int height)
  {
    super.setSize(width, height);
    doLayout();
  }

  protected void paintChildren(Graphics g)
  {
    inPaintChildren = true;
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
    inPaintChildren = false;
  }

  public boolean isShowing()
  {
    return true;
  }

}
