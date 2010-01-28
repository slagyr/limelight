package limelight.ui.model.inputs;

import limelight.ui.model.inputs.keyProcessors.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.awt.event.KeyEvent;

import static org.junit.Assert.assertEquals;

@RunWith(Suite.class)
@Suite.SuiteClasses({KeyProcessorSuite.StandardKeyProcessorTest.class, KeyProcessorSuite.CmdKeyProcessorTest.class, KeyProcessorSuite.ShiftKeyProcessorTest.class,
    KeyProcessorSuite.ShiftCmdKeyProcessorTest.class, KeyProcessorSuite.AltKeyProcessorTest.class, KeyProcessorSuite.AltCmdKeyProcessorTest.class,
    KeyProcessorSuite.AltShiftKeyProcessorTest.class, KeyProcessorSuite.AltShiftCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionKeyProcessorTest.class,
    KeyProcessorSuite.SelectionCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionShiftKeyProcessorTest.class, KeyProcessorSuite.SelectionShiftCmdKeyProcessorTest.class,
    KeyProcessorSuite.SelectionAltKeyProcessorTest.class, KeyProcessorSuite.SelectionAltCmdKeyProcessorTest.class, KeyProcessorSuite.SelectionAltShiftKeyProcessorTest.class,
    KeyProcessorSuite.SelectionAltShiftCmdKeyProcessorTest.class})
public class KeyProcessorSuite
{
  public static final int SELECTION_START_INDEX = 4;

  public static TextModel boxInfo;
  public static TextInputPanel boxPanel;
  public static KeyProcessor processor;
  public static TextModelAsserter asserter;


  public static void standardSetUp()
  {
    setUp();
    boxInfo.setSelectionIndex(0);
    boxInfo.selectionOn = false;
  }

  private static void setUp()
  {
    boxPanel = new TextBox2Panel();
    boxInfo = boxPanel.getBoxInfo();
    asserter = new TextModelAsserter(boxInfo);

    boxInfo.setText("Here are four words");
    boxInfo.setCursorIndex(1);
  }

  private static void selectionSetUp()
  {
    setUp();
    boxInfo.setSelectionIndex(SELECTION_START_INDEX);
    boxInfo.selectionOn = true;
  }

  public static class TextModelAsserter
  {
    TextModel boxInfo;

    public TextModelAsserter(TextModel boxInfo)
    {
      this.boxInfo = boxInfo;
    }


    public void assertSelection(int cursorIndex, int selectionIndex, boolean selectionOn)
    {
      assertEquals(cursorIndex, boxInfo.getCursorIndex());
      assertEquals(selectionIndex, boxInfo.getSelectionIndex());
      assertEquals(selectionOn, boxInfo.selectionOn);
    }

    public void assertTextState(int cursorIndex, String text)
    {
      assertEquals(cursorIndex, boxInfo.getCursorIndex());
      assertEquals(text, boxInfo.text.toString());
      if (!boxInfo.selectionOn)
        assertEquals(0, boxInfo.getSelectionIndex());
    }
  }

  public static class StandardKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPKey(boxInfo);
    }

    @Test
    public void canProcessCharacters()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(2, "Haere are four words");
    }

    @Test
    public void canProcessBackSpace()
    {
      processor.processKey(KeyEvent.VK_BACK_SPACE);

      asserter.assertTextState(0, "ere are four words");
    }

    @Test
    public void canProcessRightArrow()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(2, 0, false);
    }

    @Test
    public void canProcessLeftArrow()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, 0, false);
    }
  }

  public static class CmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPCMD(boxInfo);
    }

    @Test
    public void canSelectAll()
    {
      boxInfo.setText("Bob");

      processor.processKey(KeyEvent.VK_A);

      asserter.assertSelection(3, 0, true);
    }

    @Test
    public void canPasteAtCursor()
    {
      boxInfo.setText("Bob");
      boxInfo.copyText(" Dole");

      processor.processKey(KeyEvent.VK_V);

      asserter.assertTextState(boxInfo.text.length(), "Bob Dole");
    }

    @Test
    public void canProcessRightArrow()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(boxInfo.text.length(), 0, false);
    }

    @Test
    public void canProcessLeftArrow()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, 0, false);
    }
  }

  public static class ShiftKeyProcessorTest
  {

    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPShift(boxInfo);
    }

    @Test
    public void canProcessCharacters()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(2, "HAere are four words");
    }

    @Test
    public void canProcessRightArrowAndBeingSelection()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(2, 1, true);
    }

    @Test
    public void canProcessLeftArrowAndBeginSelection()
    {
      processor.processKey(KeyEvent.VK_LEFT);
      asserter.assertSelection(0, 1, true);
    }
  }

  public static class ShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPShiftCMD(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndSelectToTheRightEdge()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(boxInfo.text.length(), 1, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToTheLeftEdge()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, 1, true);
    }
  }

  public static class AltKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPAlt(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndJumpToTheNextWord()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      assertEquals(5, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessRightArrowAndSkipOverExtraSpacesToNextWord()
    {
      boxInfo.setText("Here are    many  spaces");
      boxInfo.setCursorIndex(5);

      processor.processKey(KeyEvent.VK_RIGHT);

      assertEquals(12, boxInfo.getCursorIndex());
    }

    @Test
    public void  canProcessRightArrowAndJumpToTheNextWordInATinyString()
    {
      boxInfo.setText("H s");
      boxInfo.setCursorIndex(0);

      processor.processKey(KeyEvent.VK_RIGHT);

      assertEquals(2, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessLeftArrowAndJumpToThePreviousWord()
    {
      boxInfo.setCursorIndex(9);

      processor.processKey(KeyEvent.VK_LEFT);

      assertEquals(5, boxInfo.getCursorIndex());
    }

    @Test
    public void canProcessLeftArrowAndSkipOverExtraSpacesToPreviousWord()
    {
      boxInfo.setText("Here are    many  spaces");
      boxInfo.setCursorIndex(12);

      processor.processKey(KeyEvent.VK_LEFT);

      assertEquals(5, boxInfo.getCursorIndex());
    }
  }

  public static class AltCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPAltCMD(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndNothingHappens()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(1, 0, false);
    }

    @Test
    public void canProcessCharacterAndNothingHappens()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(1, "Here are four words");
    }
  }

  public static class AltShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPAltShift(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndSelectToTheNextWord()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(5, 1, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToThePreviousWord()
    {
      boxInfo.setCursorIndex(9);

      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(5, 9, true);
    }
  }

  public static class AltShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      standardSetUp();
      processor = new KPAltShiftCMD(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndNothingHappens()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(1, 0, false);
    }

    @Test
    public void canProcessCharacterAndNothingHappens()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(1, "Here are four words");
    }
  }

  public static class SelectionKeyProcessorTest
  {

    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSel(boxInfo);
    }


    @Test
    public void canProcessCharactersAndReplaceSelection()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(2, "Ha are four words");
    }

    @Test
    public void canProcessBackSpaceAndDeleteSelection()
    {
      processor.processKey(KeyEvent.VK_BACK_SPACE);

      asserter.assertTextState(1, "H are four words");
    }

    @Test
    public void canProcessRightArrowAndEndSelection()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(2, SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndEndSelection()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, SELECTION_START_INDEX, false);
    }
  }

  public static class SelectionCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelCMD(boxInfo);
    }

    @Test
    public void canSelectAll()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertSelection(boxInfo.text.length(), 0, true);
    }

    @Test
    public void canPasteAtCursor()
    {
      boxInfo.copyText("oot");

      processor.processKey(KeyEvent.VK_V);

      asserter.assertTextState(SELECTION_START_INDEX, "Hoot are four words");
      asserter.assertSelection(SELECTION_START_INDEX, 0, false);
    }

    @Test
    public void canCopySelectedText()
    {
      processor.processKey(KeyEvent.VK_C);

      asserter.assertTextState(1, "Here are four words");
      asserter.assertSelection(1, SELECTION_START_INDEX, true);
      assertEquals("ere", boxInfo.getClipboardContents());
    }

    @Test
    public void canCutSelectedText()
    {
      processor.processKey(KeyEvent.VK_X);

      asserter.assertTextState(1, "H are four words");
      asserter.assertSelection(1, 0, false);
      assertEquals("ere", boxInfo.getClipboardContents());
    }

    @Test
    public void canProcessRightArrowAndJumpToRightEdgeAndEndSelection()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(boxInfo.text.length(), SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndJumpToLeftEdgeAndEndSelection()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, SELECTION_START_INDEX, false);
    }

  }

  public static class SelectionShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelShift(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndContinueSelectionToTheRight()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(2, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndContinueSelectionToTheLeft()
    {
      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessCharacterAndPlaceUppercaseCharAtCursorIndex()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertTextState(2, "HA are four words");
    }
  }

  public static class SelectionShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelShiftCMD(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndContinueSelectionToTheRightEdge()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(boxInfo.getText().length(), SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndContinueSelectionToTheLeftEdge()
    {
      boxInfo.setCursorIndex(2);

      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(0, SELECTION_START_INDEX, true);
    }
  }

  public static class SelectionAltKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelAlt(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndMoveToStartOfNextWord()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(5, SELECTION_START_INDEX, false);
    }

    @Test
    public void canProcessLeftArrowAndMoveToEndOfPreviousWord()
    {
      boxInfo.setCursorIndex(9);

      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(5, 4, false);
    }
  }

  public static class SelectionAltCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelAltCMD(boxInfo);
    }


    @Test
    public void canProcessCharacterAndDoNothing()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertSelection(1, SELECTION_START_INDEX, true);
    }
  }

  public static class SelectionAltShiftKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelAltShift(boxInfo);
    }

    @Test
    public void canProcessRightArrowAndSelectToStartOfNextWord()
    {
      processor.processKey(KeyEvent.VK_RIGHT);

      asserter.assertSelection(5, SELECTION_START_INDEX, true);
    }

    @Test
    public void canProcessLeftArrowAndSelectToEndOfPreviousWord()
    {
      boxInfo.setCursorIndex(9);

      processor.processKey(KeyEvent.VK_LEFT);

      asserter.assertSelection(5, 4, true);
    }
  }

  public static class SelectionAltShiftCmdKeyProcessorTest
  {
    @Before
    public void setUp()
    {
      selectionSetUp();
      processor = new KPSelAltShiftCMD(boxInfo);
    }


    @Test
    public void canProcessCharacterAndDoNothing()
    {
      processor.processKey(KeyEvent.VK_A);

      asserter.assertSelection(1, SELECTION_START_INDEX, true);
    }
  }

}
