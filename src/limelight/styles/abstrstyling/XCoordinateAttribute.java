package limelight.styles.abstrstyling;

import limelight.util.Box;

public interface XCoordinateAttribute extends StyleAttribute
{
  int getX(int consumed, Box area);
}
