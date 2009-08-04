package limelight.styles.abstrstyling;

import java.awt.*;

public interface YCoordinateAttribute extends StyleAttribute
{
  int getY(int consumed, Rectangle area);
}
