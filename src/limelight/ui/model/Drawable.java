package limelight.ui.model;

import java.awt.*;

public interface Drawable
{
  public void draw(Graphics2D graphics2D, int x, int y, int scaledWidth, int scaledHeight);
}
