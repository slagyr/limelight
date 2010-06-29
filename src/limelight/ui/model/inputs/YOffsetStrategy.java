package limelight.ui.model.inputs;

public interface YOffsetStrategy
{
  YOffsetStrategy FITTING = new FittingYOffsetStrategy();

  int calculateYOffset(TextModel model);
}
