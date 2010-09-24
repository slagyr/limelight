package limelight.ui.model.inputs;

import limelight.events.Event;
import limelight.events.EventAction;
import limelight.styles.RichStyle;
import limelight.ui.Panel;
import limelight.ui.SimplePropProxy;
import limelight.ui.events.panel.MouseClickedEvent;
import limelight.ui.events.panel.MouseEnteredEvent;
import limelight.ui.events.panel.PanelEvent;
import limelight.ui.model.Prop;
import limelight.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ComboBoxPopup
{
  private final ComboBoxPanel comboBox;
  private Prop curtains;
  private Prop popupList;
  private Prop selectedItem;
  private Map<String, RichStyle> stylesStore;

  public ComboBoxPopup(final ComboBoxPanel comboBoxPanel)
  {
    comboBox = comboBoxPanel;

    stylesStore = comboBox.getRoot().getStylesStore();
    createCurtains();
    createList();
    createListItems();

    curtains.add(popupList);

    comboBox.setPopup(this);
  }

  private void createListItems()
  {
    EventAction itemChosenAction = new EventAction()
    {
      public void invoke(Event e)
      {
        PanelEvent event = (PanelEvent)e;
        choose((Prop) event.getRecipient());
      }
    };

    EventAction itemSelectedAction = new EventAction()
    {
      public void invoke(Event e)
      {
        PanelEvent event = (PanelEvent)e;
        select((Prop) event.getRecipient());
      }
    };

    for(Object option : comboBox.getOptions())
    {
      Prop listItem = new Prop(new SimplePropProxy(), Util.toMap("name", "limelight_builtin_combo_box_popup_list_item"));
      listItem.getStyle().addExtension(stylesStore.get("limelight_builtin_combo_box_popup_list_item"));
      listItem.getEventHandler().add(MouseClickedEvent.class, itemChosenAction);
      listItem.getEventHandler().add(MouseEnteredEvent.class, itemSelectedAction);
      listItem.setText(option.toString());

      if(option.equals(comboBox.getSelectedOption()))
        select(listItem);

      popupList.add(listItem);
    }
  }

  private void select(Prop listItem)
  {
    if(selectedItem == listItem)
      return;

    if(selectedItem != null)
      selectedItem.getStyle().removeScreen();

    selectedItem = listItem;
    selectedItem.getStyle().removeScreen();
    selectedItem.getStyle().applyScreen(stylesStore.get("limelight_builtin_combo_box_popup_list_item_selected"));

    // TODO - MDM - Need a better way to handle this... screens conflicts with hover.
  }

  private void createList()
  {
    popupList = new Prop(new SimplePropProxy(), Util.toMap("name", "limelight_builtin_combo_box_popup_list"));
    popupList.getStyle().addExtension(stylesStore.get("limelight_builtin_combo_box_popup_list"));
    popupList.getStyle().setX(comboBox.getParent().getAbsoluteLocation().x - comboBox.getRoot().getX());
    popupList.getStyle().setY(comboBox.getParent().getAbsoluteLocation().y - comboBox.getRoot().getY());
    popupList.getStyle().setWidth(comboBox.getParent().getWidth());
    popupList.getEventHandler().add(MouseClickedEvent.class, new EventAction()
    {
      public void invoke(Event event)
      {
        // eat the event so the curtains won't get it
      }
    });
  }

  private void createCurtains()
  {
    curtains = new Prop(new SimplePropProxy(), Util.toMap("name", "limelight_builtin_curtains"));
    curtains.getStyle().addExtension(stylesStore.get("limelight_builtin_curtains"));
    curtains.getEventHandler().add(MouseClickedEvent.class, new EventAction()
    {
      public void invoke(Event event)
      {
        close();
      }
    });
  }

  public void open()
  {
    comboBox.getRoot().add(curtains);
  }

  public void close()
  {
    curtains.getParent().remove(curtains);
    comboBox.setPopup(null);
  }

  public void choose(Prop item)
  {
    if(item != null)
      comboBox.setText(item.getText());
    close();
  }

  public Prop getSelectedItem()
  {
    return selectedItem;
  }

  public void selectNext()
  {
    final List<Prop> items = getListItems();
    int selectedIndex = selectedItem == null ? -1 : selectedIndex(items);
    if(selectedIndex < (items.size() - 1))
      select(items.get(selectedIndex + 1));
  }

  private List<Prop> getListItems()
  {
    List<Prop> items = new ArrayList<Prop>();
    for(Panel panel : popupList.getChildren())
    {
      if(panel instanceof Prop)
        items.add((Prop)panel);
    }
    return items;
  }

  public void selectPrevious()
  {
    final List<Prop> items = getListItems();
    int selectedIndex = selectedItem == null ? items.size() : selectedIndex(items);
    if(selectedIndex > 0)
      select(items.get(selectedIndex - 1));
  }

  private int selectedIndex(List<Prop> items)
  {
    int selectedIndex = 0;
    for(int i = 0; i < items.size(); i++)
    {
      if(selectedItem == items.get(i))
      {
        selectedIndex = i;
        break;
      }
    }
    return selectedIndex;
  }

  public Prop getPopupList()
  {
    return popupList;
  }
}
