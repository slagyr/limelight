package limelight.ui;

import java.awt.*;

public interface TypedLayout
{

  public abstract void draw(Graphics2D graphics, int x, int y);
  public abstract boolean hasDrawn();
  public abstract String getText();
  public abstract int getX();
  public abstract int getY();

}
