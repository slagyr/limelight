package limelight.ui.model.updates;

import java.awt.*;

public class BoundedShallowPaintUpdate extends ShallowPaintUpdate
{
  private Rectangle bounds;

  public BoundedShallowPaintUpdate(Rectangle bounds)
  {
    super(2);
    this.bounds = bounds;
  }

  public Rectangle getBounds()
  {
    return bounds;
  }

  protected void modifyGraphics(Graphics2D g)
  {
    g.clipRect(bounds.x, bounds.y, bounds.width, bounds.height);
  }
}
