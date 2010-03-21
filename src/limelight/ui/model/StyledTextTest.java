//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;

public class StyledTextTest extends TestCase
{
  private StyledText styledText;
  private StyledText styledText1;
  private StyledText styledText2;

  public void testHasText() throws Exception
  {
    styledText = new StyledText("Hello there", "default");
    assertEquals(styledText.getText(), "Hello there");
  }

  public void testHasOtherText() throws Exception
  {
    styledText = new StyledText("Goodbye there", "default");
    assertEquals(styledText.getText(), "Goodbye there");
  }

  public void testHasDefaultStyle() throws Exception
  {
    styledText = new StyledText("Hello there", "default");
    assertEquals(styledText.getStyleName(), "default");
  }

  public void testHasStyle() throws Exception
  {
    styledText = new StyledText("Some text", "Some_style");
    assertEquals(styledText.getStyleName(), "Some_style");
  }

  public void testHasOtherStyle() throws Exception
  {
    styledText = new StyledText("Some text", "Other_style");
    assertEquals(styledText.getStyleName(), "Other_style");
  }

  public void testHasUsefulEquals() throws Exception
  {
    styledText1 = new StyledText("Hello", "default");
    styledText2 = new StyledText("Hello", "default");

    assertTrue(styledText1.equals(styledText2));
  }

  public void testIfDifferentTextThenNotEquals() throws Exception
  {
    styledText1 = new StyledText("hi", "default");
    styledText2 = new StyledText("bye", "default");

    assertFalse(styledText1.equals(styledText2));
  }


  public void testIfDifferentStyleThenNotEquals() throws Exception
  {
    styledText1 = new StyledText("hi", "a");
    styledText2 = new StyledText("hi", "b");

    assertFalse(styledText1.equals(styledText2));
  }

  public void testEqualsWithStyles() throws Exception
  {
    styledText1 = new StyledText("hi", "a");
    styledText2 = new StyledText("hi", "a");

    assertTrue(styledText1.equals(styledText2));
  }

  public void testEqualUsesStringEqualsMethod() throws Exception
  {
    String text1 = new String("abcd");
    String text2 = new String("abcd");
    styledText1 = new StyledText(text1, "default");
    styledText2 = new StyledText(text2, "default");

    assertTrue(styledText1.equals(styledText2));
  }

  public void testChangeText() throws Exception
  {
    styledText = new StyledText("hi", "a");
    styledText.setText("bye");

    assertEquals(styledText.getText(), "bye");
  }

  public void testChangeStyle() throws Exception
  {
    styledText = new StyledText("hi", "a");
    styledText.setStyle("bye");

    assertEquals(styledText.getStyleName(), "bye");
  }

  public void testRemoveUnusedTags() throws Exception
  {
    styledText = new StyledText("a<b>bc", "a");

    assertEquals("a<b>bc", styledText.getText());
    assertEquals("a", styledText.getStyleName());
  }

  public void testRemoveMultipleUnclosedTags() throws Exception
  {
    styledText = new StyledText("<q>a</b>b</c>c<d>", "default");

    assertEquals("<q>a</b>b</c>c<d>", styledText.getText());
    assertEquals("default", styledText.getStyleName());
  }

  public void testGetParentStyles() throws Exception
  {
    List<String> styles = new LinkedList<String>();
    styles.add("specific");
    styles.add("more_specific");
    styles.add("most_specific");

    styledText = new StyledText("Rumpelstiltskin", "mean_elf", styles);

    assertEquals("Rumpelstiltskin", styledText.getText());

    List<String> actualStyles = styledText.getParentStyles();
    assertEquals("most_specific", actualStyles.get(0));
    assertEquals("more_specific", actualStyles.get(1));
    assertEquals("specific", actualStyles.get(2));
  }

  public void testUnequalParentsNotEquals() throws Exception
  {
    List<String> styles = new LinkedList<String>();
    styles.add("specific");
    styles.add("more_specific");
    styles.add("most_specific");

    styledText = new StyledText("Rumpelstiltskin", "mean_elf", styles);

    assertFalse(styledText.equals(new StyledText("Rumpelstiltskin", "mean_elf")));
  }
}
