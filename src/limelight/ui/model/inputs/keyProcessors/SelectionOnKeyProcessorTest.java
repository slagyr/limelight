package limelight.ui.model.inputs.keyProcessors;


import limelight.ui.events.KeyEvent;
import limelight.ui.text.TextLocation;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SelectionOnKeyProcessorTest extends AbstractKeyProcessorTest
{
  @Before
  public void setUp()
  {
    setUpSingleLine();
    model.startSelection(TextLocation.at(0, 4));
    processor = SelectionOnKeyProcessor.instance;
    modifiers = 0;
  }

//  @Test
//  public void canProcessCharactersAndReplaceSelection()
//  {
//    mockEvent = new MockKeyEvent(modifiers, KeyEvent.KEY_A, 'a');
//
//    processor.processKey(mockEvent, model);
//
//    assertTextState(2, 1, "Ha are four words");
//  }

  @Test
  public void canProcessBackSpaceAndDeleteSelection()
  {
    processor.processKey(press(KeyEvent.KEY_BACK_SPACE), model);

    assertEquals(1, model.getCaretIndex());
    assertEquals("H are four words", model.getText());
    if(!model.isSelectionActivated())
      assertEquals(1, model.getSelectionIndex());
  }

  @Test
  public void canProcessRightArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_RIGHT), model);

    assertEquals(2, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

  @Test
  public void canProcessLeftArrowAndEndSelection()
  {
    processor.processKey(press(KeyEvent.KEY_LEFT), model);

    assertEquals(0, model.getCaretIndex());
    assertEquals(4, model.getSelectionIndex());
    assertEquals(false, model.isSelectionActivated());
  }

}
