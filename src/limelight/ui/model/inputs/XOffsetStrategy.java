package limelight.ui.model.inputs;

public interface XOffsetStrategy
{
  XOffsetStrategy CENTERED = new CenteredXOffsetStrategy(); 

  int calculateXOffset(TextModel model);
}
