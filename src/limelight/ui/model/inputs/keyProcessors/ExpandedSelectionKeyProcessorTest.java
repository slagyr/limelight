package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExpandedSelectionKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpMultiLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = ExpandedSelectionOnKeyProcessor.instance;
    modifiers = 0;
  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertEquals(TextLocation.at(0, 1), model.getCaretLocation());
    assertEquals("H are four words", model.getText());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessRightArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(TextLocation.at(0, 2), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(TextLocation.at(0, 0), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessUpArrowAndEndSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(1, 3));

    processor.processKey(press(KeyEvent.KEY_UP), model);

    assertEquals(TextLocation.at(0, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessDownArrowAndEndSelection()
  {
    model.setText("This is\nMulti lined.");
    model.setCaretLocation(TextLocation.at(0, 3));

    processor.processKey(press(KeyEvent.KEY_DOWN), model);

    assertEquals(TextLocation.at(1, 3), model.getCaretLocation());
    assertEquals(TextLocation.at(0, 4), model.getSelectionLocation());
    assertEquals(false, model.isSelectionActivated());
  }
}
