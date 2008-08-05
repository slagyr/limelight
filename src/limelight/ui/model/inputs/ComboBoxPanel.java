package limelight.ui.model.inputs;

import java.awt.*;

public class ComboBoxPanel extends InputPanel
{
  private ComboBox comboBox;
  private ContainerStub container;

  public ComboBoxPanel()
  {
    super();
    comboBox.setSize(120, comboBox.getPreferredSize().height);
  }

  public ComboBox getComboBox()
  {
    return comboBox;
  }

  protected Component createComponent()
  {
    comboBox = new ComboBox(this);

    container = new ContainerStub(this);
    container.add(comboBox);

    return comboBox;
  }

}
