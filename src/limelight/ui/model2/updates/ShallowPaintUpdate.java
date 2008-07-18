package limelight.ui.model2.updates;

import limelight.ui.Panel;
import limelight.ui.model2.Update;

import java.awt.*;

public class ShallowPaintUpdate extends Update
{
  protected ShallowPaintUpdate(int severity)
  {
    super(severity);
  }

  //TODO MDM This is a bit too optimistic.  Scrollbars maybe covered by floaters so we can't just paint it directly
  public void performUpdate(Panel panel)
  {
    limelight.util.Box ab = panel.getAbsoluteBounds();
    Graphics2D g = (Graphics2D) panel.getRoot().getGraphics().create(ab.x, ab.y, ab.width, ab.height);
    panel.paintOn(g);
    g.dispose();
  }
}
