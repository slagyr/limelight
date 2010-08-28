package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class CmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = CmdKeyProcessor.instance;
    modifiers = 4;
  }

  @Test
  public void canSelectAll()
  {
    model.setText("Bob");

    processor.processKey(press(KeyEvent.KEY_A), model);

    assertSelection(3, 0, true);
  }

  @Test
  public void canPasteAtCursor()
  {
    model.setText("Bob");
    model.copyText(" Dole");

    processor.processKey(press(KeyEvent.KEY_V), model);

    assertTextState(model.getText().length(), 0, "Bob Dole");
  }

  @Test
  public void canProcessRightArrow()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(model.getText().length(), 0, false);
  }

  @Test
  public void canProcessLeftArrow()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessUpArrow()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(0, 0, false);
  }

  @Test
  public void canProcessDownArrow()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(model.getText().length(), 0, false);
  }
}
