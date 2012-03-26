package limelight.ui.model;

import limelight.Log;
import limelight.ui.Panel;
import limelight.ui.PanelUtil;
import limelight.util.Pair;

import java.util.*;

public class LayoutJob
{
  public static void layoutPanel(Panel panel)
  {
Log.debug("layoutPanel = " + panel);
    if(panel.needsLayout())
    {
      final Layout layout = panel.resetNeededLayout();
      layout.doExpansion(panel);
      layoutChildren(panel);
      layout.doContraction(panel);
      layout.doFinalization(panel);
    }
    else
      layoutChildren(panel);
  }

  private static void layoutChildren(Panel panel)
  {
    if(panel instanceof ParentPanel)
    {
      ParentPanel parent = (ParentPanel) panel;
      for(Panel child : parent.getChildren())
        layoutPanel(child);
    }
  }
}
