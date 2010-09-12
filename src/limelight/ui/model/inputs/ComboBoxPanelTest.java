//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model.inputs;

import limelight.builtin.BuiltInStyles;
import limelight.ui.Panel;
import limelight.ui.events.*;
import limelight.ui.model.MockRootPanel;
import limelight.ui.model.PropPanel;
import limelight.ui.api.MockProp;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class ComboBoxPanelTest
{
  private ComboBoxPanel panel;
  private PropPanel parent;
  private MockRootPanel root;

  @Before
  public void setUp() throws Exception
  {
    panel = new ComboBoxPanel();
    parent = new PropPanel(new MockProp());
    parent.add(panel);

    root = new MockRootPanel();
    root.add(parent);
    root.styleStore = BuiltInStyles.all();
  }

  @Test
  public void isButton() throws Exception
  {
    assertEquals(true, AbstractButtonPanel.class.isInstance(panel));
  }

  @Test
  public void canBeBuffered() throws Exception
  {
    assertEquals(false, panel.canBeBuffered());
  }

  @Test
  public void settingParentSetsTextAccessor() throws Exception
  {
    panel.addOption("foo");
    panel.addOption("blah");
    parent.setText("blah");
    assertEquals("blah", panel.getSelectedOption().toString());
  }

  @Test
  public void settingParentSteralizesParent() throws Exception
  {
    assertEquals(true, parent.isSterilized());
  }

  @Test
  public void shouldDefaultStyles() throws Exception
  {   
    assertEquals("128", panel.getStyle().getWidth());
    assertEquals("27", panel.getStyle().getHeight());
    assertEquals("8", panel.getStyle().getLeftPadding());
    assertEquals("left", panel.getStyle().getHorizontalAlignment());
    assertEquals("center", panel.getStyle().getVerticalAlignment());
    assertEquals("Arial", panel.getStyle().getFontFace());
    assertEquals("bold", panel.getStyle().getFontStyle());
    assertEquals("12", panel.getStyle().getFontSize());
    assertEquals("#000000ff", panel.getStyle().getTextColor());
  }
  
  @Test
  public void propPainterReset() throws Exception
  {
    assertSame(ComboBoxPanel.ComboBoxPropPainter.instance, parent.getPainter());
  }

  @Test
  public void settingSelection() throws Exception
  {
    panel.setOptions(1, 2, 3, 4, 5);
    assertEquals(1, panel.getSelectedOption());

    panel.setSelectedOption(2);
    assertEquals(2, panel.getSelectedOption());
  }

  @Test
  public void cannotAddNullOption() throws Exception
  {
    panel.setOptions(1, 2, 3, null, 4);
    assertEquals(4, panel.getOptions().size());

    panel.addOption(null);
    assertEquals(4, panel.getOptions().size());
  }

  @Test
  public void clearingOptions() throws Exception
  {
    panel.setOptions(1, 2, 3, null, 4);

    panel.clear();

    assertEquals(0, panel.getOptions().size());
  }

  @Test
  public void pressingButtonOpensList() throws Exception
  {
    panel.setOptions(1, 2, 3);

    new ButtonPushedEvent(panel).dispatch(panel);

    assertEquals(2, root.getChildren().size());
    PropPanel curtains = (PropPanel) root.getChildren().get(1);
    assertEquals("limelight_builtin_curtains", curtains.getProp().getName());

    PropPanel list = (PropPanel) curtains.getChildren().get(0);
    assertEquals("limelight_builtin_combo_box_popup_list", list.getProp().getName());

    assertEquals(3, list.getChildren().size());
    final PropPanel item1 = (PropPanel) list.getChildren().get(0);
    assertEquals("limelight_builtin_combo_box_popup_list_item", item1.getProp().getName());
    assertEquals("1", item1.getText());
    final PropPanel item2 = (PropPanel) list.getChildren().get(1);
    assertEquals("limelight_builtin_combo_box_popup_list_item", item2.getProp().getName());
    assertEquals("2", item2.getText());
    final PropPanel item3 = (PropPanel) list.getChildren().get(2);
    assertEquals("limelight_builtin_combo_box_popup_list_item", item3.getProp().getName());
    assertEquals("3", item3.getText());
  }

  @Test
  public void consumedButtonPushedEventsDoNotLoadPopup() throws Exception
  {
    panel.setOptions(1, 2, 3);

    new ButtonPushedEvent(panel).consumed().dispatch(panel);

    assertEquals(1, root.getChildren().size());
  }

  @Test
  public void pressingEscapeWillClosePopupList() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_ESCAPE, 0).dispatch(panel);
    assertEquals(1, root.getChildren().size());
  }

  @Test
  public void consumedKeyPressedEventsDoNothing() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_ESCAPE, 0).consumed().dispatch(panel);
    assertEquals(2, root.getChildren().size());
  }

  @Test
  public void selectedOptionsIsHighlightedAtFirst() throws Exception
  {
    panel.setOptions(1, 2, 3);
    panel.setSelectedOption(3);
    new ButtonPushedEvent(panel).dispatch(panel);

    final PropPanel selected = panel.getPopup().getSelectedItem();
    assertEquals("3", selected.getText());
    assertEquals(root.getStylesStore().get("limelight_builtin_combo_box_popup_list_item_selected"), selected.getStyle().getScreen());
  }

  @Test
  public void pressingDownSelectsTheNextItem() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("2", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("3", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("3", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void pressingUpSelectsThePreviousItem() throws Exception
  {
    panel.setOptions(1, 2, 3);
    panel.setSelectedOption(3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("2", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("1", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("1", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void itemsIsSelectedOnMouseEnter() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);
    final Panel item3 = panel.getPopup().getPopupList().getChildren().get(2);

    new MouseEnteredEvent(item3, 0, null, 0).dispatch(item3);

    assertEquals("3", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void pressingReturnConfirmsSelection() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);
    new KeyPressedEvent(panel, 0, KeyEvent.KEY_DOWN, 0).dispatch(panel);

    new KeyPressedEvent(panel, 0, KeyEvent.KEY_ENTER, 0).dispatch(panel);

    assertEquals("2", panel.getText());
    assertEquals(false, isPopupOpen());
  }
  
  @Test
  public void popupClosesWhenFocusIsLost() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new FocusLostEvent(panel).dispatch(panel);

    assertEquals(false, isPopupOpen());
  }

  @Test
  public void consumedFocusLostEventDoesNothing() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    new FocusLostEvent(panel).consumed().dispatch(panel);

    assertEquals(true, isPopupOpen());
  }
  
  @Test
  public void clickingOnListDoesNotCloseThePopup() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    final PropPanel popupList = panel.getPopup().getPopupList();
    new MouseClickedEvent(popupList, 0, null, 0).dispatch(panel);

    assertEquals(true, isPopupOpen());
  }

  @Test
  public void acquiresFocusWhenPressed() throws Exception
  {
    panel.setOptions(1, 2, 3);
    new ButtonPushedEvent(panel).dispatch(panel);

    assertEquals(true, panel.hasFocus());
  }

  private boolean isPopupOpen()
  {
    return panel.getPopup() != null;
  }
  
  @Test
  public void valuChangedEventInvokedWhenChangingText() throws Exception
  {
    panel.setOptions(1, 2, 3);
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    panel.setSelectedOption(1);
    assertEquals(false, action.invoked);

    panel.setSelectedOption(3);
    assertEquals(true, action.invoked);
  }
}

