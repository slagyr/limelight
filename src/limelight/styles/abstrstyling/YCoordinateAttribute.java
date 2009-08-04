package limelight.styles.abstrstyling;

import limelight.util.Box;

public interface YCoordinateAttribute extends StyleAttribute
{
  int getY(int consumed, Box area);
}
