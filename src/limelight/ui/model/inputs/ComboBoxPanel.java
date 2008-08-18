package limelight.ui.model.inputs;

import limelight.ui.model.PropPanel;
import limelight.ui.model.TextAccessor;

import java.awt.*;

public class ComboBoxPanel extends InputPanel
{
  private ComboBox comboBox;
  private ContainerStub container;

  public ComboBoxPanel()
  {
    super();
    setSize(120, comboBox.getPreferredSize().height);
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

  protected TextAccessor createTextAccessor()
  {
    return new ComboBoxTextAccessor(comboBox);
  }

  private static class ComboBoxTextAccessor implements TextAccessor
  {
    private ComboBox comboBox;

    public ComboBoxTextAccessor(ComboBox comboBox)
    {
      this.comboBox = comboBox;
    }

    public void setText(String text)
    {
      comboBox.setSelectedItem(text);
    }

    public String getText()
    {
      return comboBox.getSelectedItem().toString();
    }
  }
}
