package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import org.junit.Before;
import org.junit.Test;

public class ShiftCmdKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    processor = ShiftCmdKeyProcessor.instance;
    modifiers = 5;
  }

  @Test
  public void canProcessRightArrowAndSelectToTheRightEdge()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertSelection(model.getText().length(), 1, true);
  }

  @Test
  public void canProcessLeftArrowAndSelectToTheLeftEdge()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertSelection(0, 1, true);
  }

  @Test
  public void canProcessUpArrow()
  {
    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertSelection(0, 1, true);
  }

  @Test
  public void canProcessDownArrow()
  {
    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertSelection(model.getText().length(), 1, true);
  }

}
