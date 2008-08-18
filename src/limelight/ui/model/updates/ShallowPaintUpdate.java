package limelight.ui.model.updates;

import limelight.ui.Panel;
import limelight.ui.model.Update;

import java.awt.*;

public class ShallowPaintUpdate extends Update
{
  protected ShallowPaintUpdate(int severity)
  {
    super(severity);
  }

  //TODO MDM This is a bit too optimistic.  Scrollbars/Inputs maybe covered by floaters so we can't just paint it directly
  public void performUpdate(Panel panel)
  {
    Rectangle ab = panel.getAbsoluteBounds();
    Graphics2D rootGraphics = panel.getRoot().getGraphics();
    if(rootGraphics != null)
    {
      Graphics2D g = (Graphics2D) rootGraphics.create(ab.x, ab.y, ab.width, ab.height);
      modifyGraphics(g);
      panel.paintOn(g);
      g.dispose();
    }
  }

  protected void modifyGraphics(Graphics2D g)
  {
  }
}
