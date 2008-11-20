package limelight.styles.abstrstyling;

import limelight.util.Box;

public interface PixelsAttribute extends StyleAttribute
{
  int pixelsFor(int max);

  int pixelsFor(Box dounds);
}
