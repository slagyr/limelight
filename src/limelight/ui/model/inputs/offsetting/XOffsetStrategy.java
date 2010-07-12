package limelight.ui.model.inputs.offsetting;

import limelight.ui.model.inputs.TextModel;

public interface XOffsetStrategy
{
  XOffsetStrategy STATIONARY = new StationaryXOffsetStrategy();
  XOffsetStrategy CENTERED = new CenteredXOffsetStrategy();
  XOffsetStrategy FITTING = new FittingXOffsetStrategy();

  int calculateXOffset(TextModel model);
}
