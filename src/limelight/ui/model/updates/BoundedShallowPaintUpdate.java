package limelight.ui.model.updates;

import limelight.ui.model.Update;
import limelight.util.Box;

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

  public Update prioritize(Update other)
  {
    if(other instanceof BoundedShallowPaintUpdate)
    {
      Box newBounds = new Box(bounds);
      newBounds.add(((BoundedShallowPaintUpdate)other).getBounds());
      return new BoundedShallowPaintUpdate(newBounds);
    }
    else
      return super.prioritize(other);
  }
}
