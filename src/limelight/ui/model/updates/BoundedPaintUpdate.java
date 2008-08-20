package limelight.ui.model.updates;

import limelight.util.Box;
import limelight.ui.Panel;
import limelight.ui.model.Update;
import limelight.ui.model.inputs.TextBoxPanel;

import java.awt.*;

public class BoundedPaintUpdate extends PaintUpdate
{
  private Box bounds;

  public BoundedPaintUpdate(Box bounds)
  {
    super(3);
    this.bounds = bounds;
  }

  public BoundedPaintUpdate(Rectangle bounds)
  {
    this(new Box(bounds));
  }

  public BoundedPaintUpdate(int x, int y, int width, int height)
  {
    this(new Box(x, y, width, height));
  }

  public void performUpdate(Panel panel)
  {
    if(panel instanceof TextBoxPanel)
      ;
    super.performUpdate(panel);
  }

  protected Box getAbsoluteBounds(Panel panel)
  {
    Point location = panel.getAbsoluteLocation();
    Box box = new Box(bounds.x + location.x, bounds.y + location.y, bounds.width, bounds.height);    
    return box;
  }

  public Update prioritize(Update other)
  {
    if(other instanceof BoundedPaintUpdate)
    {
      Box newBounds = new Box(bounds);
      newBounds.add(((BoundedPaintUpdate)other).getRelativeBounds());
      return new BoundedPaintUpdate(newBounds);
    }
    else
      return super.prioritize(other);
  }

  public Box getRelativeBounds()
  {
    return bounds;
  }


}
