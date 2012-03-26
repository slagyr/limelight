package limelight.ui;

public class PanelUtil
{
  public static int depthOf(Panel panel)
  {
    if(panel == null)
      return -1;
    else
      return depthOf(panel, 0);
  }

  private static int depthOf(Panel panel, int depth)
  {
    if(panel.getParent() == null)
      return depth;
    else
      return depthOf(panel.getParent(), depth + 1);
  }
}
