package limelight.builtin.players;

import limelight.model.api.FakePropProxy;
import limelight.ui.events.panel.CastEvent;
import limelight.ui.model.PropPanel;
import limelight.util.Util;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DropDownTest
{                       
  public DropDown dropDown;
  public PropPanel propPanel;

  @Before
  public void setUp() throws Exception
  {
    dropDown = new DropDown();
    propPanel = new PropPanel(new FakePropProxy());
    dropDown.install(new CastEvent(propPanel));
  }

  @Test
  public void installation() throws Exception
  {
    assertEquals(propPanel, dropDown.getPropPanel());
    assertNotNull(dropDown.getDropDownPanel());
    assertEquals(dropDown.getDropDownPanel(), propPanel.getChildren().get(0));
    assertEquals(true, propPanel.isSterilized());
    assertNotNull(propPanel.getStagehands().get("drop-down"));
    assertEquals(DropDown.class, propPanel.getStagehands().get("drop-down").getClass());
  }

  @Test
  public void choices() throws Exception
  {
    dropDown.setChoices(Arrays.asList("one", "two", "three"));

    assertEquals("one", dropDown.getDropDownPanel().getChoices().get(0));
    assertEquals("one", dropDown.getChoices().get(0));
    assertEquals("two", dropDown.getDropDownPanel().getChoices().get(1));
    assertEquals("two", dropDown.getChoices().get(1));
    assertEquals("three", dropDown.getDropDownPanel().getChoices().get(2));
    assertEquals("three", dropDown.getChoices().get(2));
  }

  @Test
  public void settingValue() throws Exception
  {
    dropDown.setChoices(Arrays.asList("one", "two", "blue"));
    dropDown.setValue("blue");
    assertEquals("blue", dropDown.getValue());

    dropDown.setValue("red");
    assertEquals("blue", dropDown.getValue());
  }

}
