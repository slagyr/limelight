package limelight.ui;

public class FakeRadioButtonGroupMember implements RadioButtonGroupMember
{
  public boolean selected;
  public RadioButtonGroup radioButtonGroup;

  public boolean isSelected()
  {
    return selected;
  }

  public void setButtonGroup(RadioButtonGroup radioButtonGroup)
  {
    this.radioButtonGroup = radioButtonGroup;
  }

  public void setSelected(boolean value)
  {
    selected = value;
  }
}
