//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.util.Box;

public class FloaterLayout implements Layout
{
  public static FloaterLayout instance = new FloaterLayout();

  //TODO Floater need to change position when scrolled too.
  public void doLayout(Panel thePanel)
  {
    BasePanel panel = (BasePanel)thePanel;
    if(panel.isFloater())
    {
      panel.resetLayout();
      Box area = panel.getParent().getChildConsumableArea();
      int newX = panel.getStyle().getCompiledX().getX(0, area);
      int newY = panel.getStyle().getCompiledY().getY(0, area);
      panel.markAsDirty();
      panel.setLocation(newX, newY);
      panel.markAsDirty();
    }
  }

  public boolean overides(Layout other)
  {
    return other != PropPanelLayout.instance;
  }
}
