package limelight.ui.model.inputs.offsetting;

import limelight.ui.model.inputs.TextModel;

public interface YOffsetStrategy
{
  YOffsetStrategy STATIONARY = new StationaryYOffsetStrategy();
  YOffsetStrategy FITTING = new FittingYOffsetStrategy();

  int calculateYOffset(TextModel model);
}
