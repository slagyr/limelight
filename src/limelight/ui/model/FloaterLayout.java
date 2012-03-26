//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model;

import limelight.ui.Panel;
import limelight.util.Box;

import java.util.Map;

public class FloaterLayout extends SimpleLayout
{
  public static FloaterLayout instance = new FloaterLayout();

  //TODO Floater need to change position when scrolled too.
  public void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout)
  {
//    PanelBase panel = (PanelBase)thePanel;
//    if(panel.isFloater())
//    {
//      Box area = panel.getParent().getChildConsumableBounds();
//      int newX = panel.getStyle().getCompiledX().getX(0, area);
//      int newY = panel.getStyle().getCompiledY().getY(0, area);
//      panel.markAsDirty();
//      panel.setLocation(newX, newY);
//      panel.markAsDirty();
//    }
  }

  @Override
  public void doFinalization(Panel thePanel)
  {
    PanelBase panel = (PanelBase)thePanel;
    if(panel.isFloater())
    {
      Box area = panel.getParent().getChildConsumableBounds();
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

  public void doLayout(Panel thePanel, Map<Panel, Layout> panelsToLayout, boolean topLevel)
  {
    doLayout(thePanel, null);
  }
}
