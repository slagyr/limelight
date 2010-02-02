package limelight.ui;

import java.awt.*;
import java.awt.font.TextHitInfo;
import java.awt.geom.Rectangle2D;

public interface TypedLayout
{

  public abstract void draw(Graphics2D graphics, float x, float y);
  public abstract boolean hasDrawn();
  public abstract String toString();
  public abstract String getText();
  public abstract float getAscent();
  public abstract float getDescent();
  public abstract float getLeading();
  public abstract Rectangle2D getBounds();

  public TextHitInfo hitTestChar(float x, float y);
}
