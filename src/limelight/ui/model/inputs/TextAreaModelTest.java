package limelight.ui.model.inputs;

import limelight.ui.TypedLayout;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TextAreaModelTest
{
  TextModel modelInfo;
  TextAreaModel areaInfo;
  TextInputPanel panel;

  @Before
  public void setUp()
  {
    panel = new TextArea2Panel();
    modelInfo = panel.getModelInfo();
    areaInfo = new TextAreaModel(panel);
    modelInfo.setText("I took the one less traveled by.  And that has made all the difference");
  }

  @Test
  public void shouldBeAbleToFindNewLineIndices()
  {
    ArrayList<Integer> indices = areaInfo.findNewLineIndices("this\nIs\nA new \rline");

    assertEquals(3, indices.size());
    assertEquals(4, (int)indices.get(0));
    assertEquals(7, (int)indices.get(1));
    assertEquals(14, (int)indices.get(2));
  }

  @Test
  public void canSpiltTextIntoMultipleLinesBasedOffReturnKeys()
  {
    ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts("this is the first line\nthis is the second line");

    assertEquals(2, textLayouts.size());
    assertEquals("this is the first line\n", textLayouts.get(0).getText());
    assertEquals("this is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void canSplitTextIntoMultipleLinesFromThePanelWidth()
  {
   ArrayList<TypedLayout> textLayouts = areaInfo.parseTextForMultipleLayouts("This here is the first full line. This is the second line");

    assertEquals(2, textLayouts.size());
    assertEquals("This here is the first full line. ", textLayouts.get(0).getText());
    assertEquals("This is the second line", textLayouts.get(1).getText());
  }

  @Test
  public void willStoreATextLayoutForEachLine()
  {
    areaInfo.setText("This text is more than 1 line\rand should be 2 lines");

    ArrayList<TypedLayout> textLayouts = areaInfo.getTextLayouts();
    assertEquals(2, textLayouts.size());
    assertEquals("This text is more than 1 line\r", textLayouts.get(0).getText());
    assertEquals("and should be 2 lines", textLayouts.get(1).getText());
  }

  @Test
  public void canCalculateTheTextModelsDimensions()
  {
    modelInfo.setText("line 1\nline 2");
    Dimension dim = modelInfo.calculateTextDimensions();
    assertEquals(29, dim.width);
    assertEquals(28, dim.height);
  }

  @Test
  public void canTellIfTheTextPanelIsFull()
  {
    modelInfo.setText("line\nline\nline\nline\nline\nline\nline\nline\n");
    assertTrue(modelInfo.isBoxFull());
  }


}
