package limelight.styles.abstrstyling;

import java.awt.*;

public interface XCoordinateAttribute extends StyleAttribute
{
  int getX(int consumed, Rectangle area);
}
