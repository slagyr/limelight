package limelight.ui.model.inputs;

import limelight.ui.model.inputs.painters.TextPanelBoxPainter;
import limelight.ui.model.inputs.painters.TextPanelCursorPainter;
import limelight.ui.model.inputs.painters.TextPanelSelectionPainter;
import limelight.ui.model.inputs.painters.TextPanelTextPainter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TextPanelPainterStoreTest
{
  TextPanelPainterStore painterStore;
  TextModel boxInfo;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextBox2Panel();
    boxInfo = new PlainTextModel(panel);
    painterStore = new TextPanelPainterStore(boxInfo);
  }

  @Test
  public void itShouldHaveACursorPainter()
  {
    assertEquals(TextPanelCursorPainter.class, painterStore.getCursorPainter().getClass());
  }

  @Test
  public void itShouldHaveABoxPainter()
  {
    assertEquals(TextPanelBoxPainter.class, painterStore.getBoxPainter().getClass());
  }

  @Test
  public void itShouldHaveASelectionPainter()
  {
    assertEquals(TextPanelSelectionPainter.class, painterStore.getSelectionPainter().getClass());
  }

  @Test
  public void itShouldHaveATextPainter()
  {
    assertEquals(TextPanelTextPainter.class, painterStore.getTextPainter().getClass());
  }
}
