package limelight.ui.model.inputs.offsetting;

import limelight.ui.model.inputs.TextModel;

public interface XOffsetStrategy
{
  XOffsetStrategy CENTERED = new CenteredXOffsetStrategy();

  int calculateXOffset(TextModel model);
}
