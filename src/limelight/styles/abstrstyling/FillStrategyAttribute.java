package limelight.styles.abstrstyling;

import limelight.ui.painting.ImageFillStrategy;

public interface FillStrategyAttribute extends StyleAttribute
{
  ImageFillStrategy getStrategy();
  String getName();
}
