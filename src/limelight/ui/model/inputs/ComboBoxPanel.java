//- Copyright � 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.styles.Style;
import limelight.ui.model.TextAccessor;
import limelight.Context;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;

public class ComboBoxPanel extends AwtInputPanel
{
  private ComboBox comboBox;

  public ComboBox getComboBox()
  {
    return comboBox;
  }

  protected Component createComponent()
  {
    comboBox = new ComboBox(this);

    ContainerStub container = new ContainerStub(this);
    container.add(comboBox);

    comboBox.addItemListener(new ComboBoxPanelItemListener(this));

    return comboBox;
  }

  protected TextAccessor createTextAccessor()
  {
    return new ComboBoxTextAccessor(comboBox);
  }

  protected void setDefaultStyles(Style style)
  {
    style.setDefault(Style.WIDTH, "120");
    style.setDefault(Style.HEIGHT, "" + comboBox.getPreferredSize().height);
  }

  private static class ComboBoxTextAccessor implements TextAccessor
  {
    private final ComboBox comboBox;

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
      Object selectedItem = comboBox.getSelectedItem();
      if(selectedItem != null)
        return selectedItem.toString();
      else
        return "";
    }
  }

  private class ComboBoxPanelItemListener implements ItemListener
  {
    private final ComboBoxPanel panel;

    public ComboBoxPanelItemListener(ComboBoxPanel comboBoxPanel)
    {
      this.panel = comboBoxPanel;
    }

    public void itemStateChanged(ItemEvent e)
    {
      int deselectValue = 2;
      if(e.getStateChange() != deselectValue)
        panel.valueChanged(e);
    }
  }

  public void mousePressed(MouseEvent e)
  {
    Context.instance().keyboardFocusManager.focusPanel(this);
    e = translatedEvent(e);    
    getParent().mousePressed(e);
  }

  public void mouseClicked(MouseEvent e)
  {
    e = translatedEvent(e);
    getParent().mouseClicked(e);
  }

  public void mouseReleased(MouseEvent e)
  {
    e = translatedEvent(e);
    getParent().mouseReleased(e);
  }
}
