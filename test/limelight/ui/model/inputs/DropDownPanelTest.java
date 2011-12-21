//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.ui.model.inputs;

import limelight.model.FakeProduction;
import limelight.builtin.BuiltInStyles;
import limelight.model.CastingDirector;
import limelight.model.api.FakePropProxy;
import limelight.ui.Panel;
import limelight.ui.events.panel.*;
import limelight.ui.model.FakeScene;
import limelight.ui.model.MockStage;
import limelight.ui.model.PropPanel;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertSame;

public class DropDownPanelTest
{
  private DropDownPanel panel;
  private PropPanel parent;
  private FakeScene root;

  @Before
  public void setUp() throws Exception
  {
    panel = new DropDownPanel();
    parent = new PropPanel(new FakePropProxy());
    parent.add(panel);

    root = new FakeScene();
    root.add(parent);
    root.styleStore = BuiltInStyles.all();

    root.setProduction(new FakeProduction());
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
    panel.addChoice("foo");
    panel.addChoice("blah");
    parent.setText("blah");
    assertEquals("blah", panel.getSelectedChoice().toString());
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
    assertSame(DropDownPanel.DropDownPropPainter.instance, parent.getPainter());
  }

  @Test
  public void settingSelection() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3, 4, 5);
    assertEquals(1, panel.getSelectedChoice());

    panel.setSelectedChoice(2);
    assertEquals(2, panel.getSelectedChoice());
  }

  @Test
  public void cannotAddNullOption() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3, null, 4);
    assertEquals(4, panel.getChoices().size());

    panel.addChoice(null);
    assertEquals(4, panel.getChoices().size());
  }

  @Test
  public void clearingOptions() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3, null, 4);

    panel.clear();

    assertEquals(0, panel.getChoices().size());
  }

  @Test
  public void pressingButtonOpensList() throws Exception
  {
    CastingDirector.installed();
    root.illuminate();
    panel.setChoicesVargs(1, 2, 3);

    new ButtonPushedEvent().dispatch(panel);

    assertEquals(2, root.getChildren().size());
    PropPanel curtains = (PropPanel) root.getChildren().get(1);
    assertEquals("limelight_builtin_curtains", curtains.getName());

    PropPanel list = (PropPanel) curtains.getChildren().get(0);
    assertEquals("limelight_builtin_drop_down_popup_list", list.getName());

    assertEquals(3, list.getChildren().size());
    final PropPanel item1 = (PropPanel) list.getChildren().get(0);
    assertEquals("limelight_builtin_drop_down_popup_list_item", item1.getName());
    assertEquals("1", item1.getText());
    final PropPanel item2 = (PropPanel) list.getChildren().get(1);
    assertEquals("limelight_builtin_drop_down_popup_list_item", item2.getName());
    assertEquals("2", item2.getText());
    final PropPanel item3 = (PropPanel) list.getChildren().get(2);
    assertEquals("limelight_builtin_drop_down_popup_list_item", item3.getName());
    assertEquals("3", item3.getText());
  }

  @Test
  public void consumedButtonPushedEventsDoNotLoadPopup() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);

    new ButtonPushedEvent().consumed().dispatch(panel);

    assertEquals(1, root.getChildren().size());
  }

  @Test
  public void pressingEscapeWillClosePopupList() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    new KeyPressedEvent(0, KeyEvent.KEY_ESCAPE, 0).dispatch(panel);
    assertEquals(1, root.getChildren().size());
  }

  @Test
  public void consumedKeyPressedEventsDoNothing() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    new KeyPressedEvent(0, KeyEvent.KEY_ESCAPE, 0).consumed().dispatch(panel);
    assertEquals(2, root.getChildren().size());
  }

  @Test
  public void selectedOptionsIsHighlightedAtFirst() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    panel.setSelectedChoice(3);
    new ButtonPushedEvent().dispatch(panel);

    final PropPanel selected = panel.getPopup().getSelectedItem();
    assertEquals("3", selected.getText());
    assertEquals(root.getStyles().get("limelight_builtin_drop_down_popup_list_item_selected"), selected.getStyle().getScreen());
  }

  @Test
  public void pressingDownSelectsTheNextItem() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    new KeyPressedEvent(0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("2", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("3", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(0, KeyEvent.KEY_DOWN, 0).dispatch(panel);
    assertEquals("3", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void pressingUpSelectsThePreviousItem() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    panel.setSelectedChoice(3);
    new ButtonPushedEvent().dispatch(panel);

    new KeyPressedEvent(0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("2", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("1", panel.getPopup().getSelectedItem().getText());

    new KeyPressedEvent(0, KeyEvent.KEY_UP, 0).dispatch(panel);
    assertEquals("1", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void itemsIsSelectedOnMouseEnter() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);
    final Panel item3 = panel.getPopup().getPopupList().getChildren().get(2);

    new MouseEnteredEvent(0, null, 0).dispatch(item3);

    assertEquals("3", panel.getPopup().getSelectedItem().getText());
  }

  @Test
  public void pressingReturnConfirmsSelection() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);
    new KeyPressedEvent(0, KeyEvent.KEY_DOWN, 0).dispatch(panel);

    new KeyPressedEvent(0, KeyEvent.KEY_ENTER, 0).dispatch(panel);

    assertEquals("2", panel.getText());
    assertEquals(false, isPopupOpen());
  }

  @Test
  public void popupClosesWhenFocusIsLost() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    new FocusLostEvent().dispatch(panel);

    assertEquals(false, isPopupOpen());
  }

  @Test
  public void consumedFocusLostEventDoesNothing() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    new FocusLostEvent().consumed().dispatch(panel);

    assertEquals(true, isPopupOpen());
  }

  @Test
  public void clickingOnListDoesNotCloseThePopup() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    final PropPanel popupList = panel.getPopup().getPopupList();
    new MouseClickedEvent(0, null, 0).dispatch(panel);

    assertEquals(true, isPopupOpen());
  }

  @Test
  public void acquiresFocusWhenPressed() throws Exception
  {
    final MockStage stage = new MockStage();
    root.setStage(stage);

    panel.setChoicesVargs(1, 2, 3);
    new ButtonPushedEvent().dispatch(panel);

    assertEquals(true, panel.hasFocus());
  }

  private boolean isPopupOpen()
  {
    return panel.getPopup() != null;
  }

  @Test
  public void valueChangedEventInvokedWhenChangingText() throws Exception
  {
    panel.setChoicesVargs(1, 2, 3);
    final MockEventAction action = new MockEventAction();
    panel.getEventHandler().add(ValueChangedEvent.class, action);

    panel.setSelectedChoice(1);
    assertEquals(false, action.invoked);

    panel.setSelectedChoice(3);
    assertEquals(true, action.invoked);
  }
}

