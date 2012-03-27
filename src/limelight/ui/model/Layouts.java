package limelight.ui.model;

import limelight.Log;
import limelight.ui.Panel;

public class Layouts
{
  public static void on(Panel panel)
  {
    layoutPanel(panel);
  }

  public static void on(Panel panel, Layout layout)
  {
    panel.resetNeededLayout();
    panel.markAsNeedingLayout(layout);
    layoutPanel(panel);
  }

  private static void layoutPanel(Panel panel)
  {
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
        on(child);
    }
  }
}
