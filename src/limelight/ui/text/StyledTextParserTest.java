//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.util.StringUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

public class StyledTextParserTest extends Assert
{
  private StyledTextParser parser;
  private List<StyledText> texts;
  private List<String> styles;

  @Before
  public void setUp() throws Exception
  {
    parser = new StyledTextParser();
    styles = new LinkedList<String>();
    styles.add("default");
  }

  private void checkStyledText(StyledText styledText, String text, String... styles)
  {
    assertEquals(text, styledText.getText());

    String expectedStylesString = StringUtil.join(", ", styles);
    String actualStylesString = StringUtil.join(", ", styledText.getStyleNames().toArray(new String[styledText.getStyleNames().size()]));
    assertEquals(expectedStylesString, actualStylesString);
  }

  @Test
  public void shouldParseUnstyledText() throws Exception
  {
    texts = parser.parse("Hello There");

    checkStyledText(texts.get(0), "Hello There");
  }

  @Test
  public void shouldParseOtherUnstyledText() throws Exception
  {
    texts = parser.parse("Howdy");

    checkStyledText(texts.get(0), "Howdy");
  }

  @Test
  public void shouldParseStyledText() throws Exception
  {
    texts = parser.parse("<b>I am serious</b>");

    checkStyledText(texts.get(0), "I am serious", "b");
  }

  @Test
  public void shouldParseMultipleStyledText() throws Exception
  {
    texts = parser.parse("<b>I am</b><c> serious</c>");

    checkStyledText(texts.get(0), "I am", "b");
    checkStyledText(texts.get(1), " serious", "c");
  }

  @Test
  public void shouldParseUnstyledTextBetweenStyledText() throws Exception
  {
    texts = parser.parse("<b>I am</b> not really<c> serious</c>");

    checkStyledText(texts.get(0), "I am", "b");
    checkStyledText(texts.get(1), " not really");
    checkStyledText(texts.get(2), " serious", "c");
  }

  @Test
  public void shouldParseTrailingUnstyledText() throws Exception
  {
    texts = parser.parse("<b>styled</b> unstyled");

    checkStyledText(texts.get(0), "styled", "b");
    checkStyledText(texts.get(1), " unstyled");
  }

  @Test
  public void shouldParseAlternatingStylingAndUnstyling() throws Exception
  {
    texts = parser.parse("unstyled1<b>styled1</b>unstyled2<c>styled2</c>unstyled3");

    checkStyledText(texts.get(0), "unstyled1");
    checkStyledText(texts.get(1), "styled1", "b");
    checkStyledText(texts.get(2), "unstyled2");
    checkStyledText(texts.get(3), "styled2", "c");
    checkStyledText(texts.get(4), "unstyled3");
  }

  @Test
  public void shouldParseNestedTags() throws Exception
  {
    texts = parser.parse("default<b>b<c>c<d>d<e>e</e>d</d>c</c>b</b>default");

    checkStyledText(texts.get(0), "default");
    checkStyledText(texts.get(1), "b", "b");
    checkStyledText(texts.get(2), "c", "c", "b");
    checkStyledText(texts.get(3), "d", "d", "c", "b");
    checkStyledText(texts.get(4), "e", "e", "d", "c", "b");
    checkStyledText(texts.get(5), "d", "d", "c", "b");
    checkStyledText(texts.get(6), "c", "c", "b");
    checkStyledText(texts.get(7), "b", "b");
    checkStyledText(texts.get(8), "default");
  }

  @Test
  public void shouldParseMultipleNestedAndAlternatingTags() throws Exception
  {
    texts = parser.parse("default<a>a<b>b</b>a</a>default<c>c<d>d</d>c</c>default");

    checkStyledText(texts.get(0), "default");
    checkStyledText(texts.get(1), "a", "a");
    checkStyledText(texts.get(2), "b", "b", "a");
    checkStyledText(texts.get(3), "a", "a");
    checkStyledText(texts.get(4), "default");
    checkStyledText(texts.get(5), "c", "c");
    checkStyledText(texts.get(6), "d", "d", "c");
    checkStyledText(texts.get(7), "c", "c");
    checkStyledText(texts.get(8), "default");
  }

  @Test
  public void shouldParseUnclosedTags() throws Exception
  {
    texts = parser.parse("default<a>a</b>b</a>default");

    checkStyledText(texts.get(0), "default");
    checkStyledText(texts.get(1), "a</b>b", "a");
    checkStyledText(texts.get(2), "default");
  }

  @Test
  public void shouldParseMisMatchedTags() throws Exception
  {
    texts = parser.parse("sky<a>wal</b>ker");

    checkStyledText(texts.get(0), "sky<a>wal</b>ker");
  }

  @Test
  public void shouldParseMultipleInnerStlyes() throws Exception
  {
    texts = parser.parse("before <a>a <b>b</b> <c>c</c> a</a> after");

    assertEquals(7, texts.size());
    checkStyledText(texts.get(0), "before ");
    checkStyledText(texts.get(1), "a ", "a");
    checkStyledText(texts.get(2), "b", "b", "a");
    checkStyledText(texts.get(3), " ", "a");
    checkStyledText(texts.get(4), "c", "c", "a");
    checkStyledText(texts.get(5), " a", "a");
    checkStyledText(texts.get(6), " after");
  }

  @Test
  public void shouldParseEscapedTag() throws Exception
  {
    texts = parser.parse("&lt;a&gt;&amp;intentional&lt;/a&gt;");

    assertEquals(1, texts.size());
    checkStyledText(texts.get(0), "<a>&intentional</a>");
  }

  @Test
  public void shouldParseTagRegex() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123</abc>");
    assertEquals(true, matcher.find());
    assertEquals("<abc>123</abc>", matcher.group(0));
    assertEquals("abc", matcher.group(1));
    assertEquals("123", matcher.group(3));
  }

  @Test
  public void shouldParseUnmatchedTagRegex() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123</def>");
    assertEquals(false, matcher.find());
  }

  @Test
  public void shouldParseMultilineContentCapture() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123\n456</abc>");
    assertEquals(true, matcher.find());
    assertEquals("123\n456", matcher.group(3));
  }

  @Test
  public void shouldParseReluctantCapturing() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123</abc><abc>456</abc>");
    assertEquals(true, matcher.find());
    assertEquals("123", matcher.group(3));
  }

  @Test
  public void textsAtTheSameLevelShareTheSameStyleObject() throws Exception
  {
    texts = parser.parse("way left<a>middle left<b>middle</b>middle right</a>way right");

    checkStyledText(texts.get(0), "way left");
    checkStyledText(texts.get(1), "middle left", "a");
    checkStyledText(texts.get(2), "middle", "b", "a");
    checkStyledText(texts.get(3), "middle right", "a");
    checkStyledText(texts.get(4), "way right");
    assertSame(texts.get(0).getStyle(), texts.get(4).getStyle());
    assertSame(texts.get(1).getStyle(), texts.get(3).getStyle());
  }

  @Test
  public void shouldParseOneAttribute() throws Exception
  {
    texts = parser.parse("<a text_color=blue>I'm blue</a>");

    checkStyledText(texts.get(0), "I'm blue", "a");
    assertEquals("#0000ffff", texts.get(0).getStyle().getTextColor());
  }

  @Test
  public void shouldParseTwoTagsWithOneAttribute() throws Exception
  {
    texts = parser.parse("<a text_color=blue>I'm blue</a> and <b font_size=42>I'm Big</b>");

    assertEquals(3, texts.size());
    checkStyledText(texts.get(0), "I'm blue", "a");
    checkStyledText(texts.get(1), " and ");
    checkStyledText(texts.get(2), "I'm Big", "b");
    assertEquals("#0000ffff", texts.get(0).getStyle().getTextColor());
    assertEquals("42", texts.get(2).getStyle().getFontSize());
  }

  @Test
  public void shouldParseAttributeWithSingleQuotes() throws Exception
  {
    texts = parser.parse("<a text_color='blue'>I'm blue</a>");

    checkStyledText(texts.get(0), "I'm blue", "a");
    assertEquals("#0000ffff", texts.get(0).getStyle().getTextColor());
  }

  @Test
  public void shouldParseAttributeWithDoubleQuotes() throws Exception
  {
    texts = parser.parse("<a text_color=\"blue\">I'm blue</a>");

    checkStyledText(texts.get(0), "I'm blue", "a");
    assertEquals("#0000ffff", texts.get(0).getStyle().getTextColor());
  }

  @Test
  public void shouldUnclosedQuotedAttribute() throws Exception
  {
    texts = parser.parse("<a text_color=\"blue>I'm blue</a>");
    checkStyledText(texts.get(0), "<a text_color=\"blue>I'm blue</a>");
    
    texts = parser.parse("<a text_color='blue>I'm blue</a>");
    checkStyledText(texts.get(0), "<a text_color='blue>I'm blue</a>");
  }

  @Test
  public void shouldHandleTwoAttributesInTheSameTag() throws Exception
  {
    texts = parser.parse("<a text_color=blue font_size='42'>hi</a>");

    checkStyledText(texts.get(0), "hi", "a");
    assertEquals("#0000ffff", texts.get(0).getStyle().getTextColor());
    assertEquals("42", texts.get(0).getStyle().getFontSize());
  }

//  private static String sampleText = "<opener><limey><big_n_embellished>L</big_n_embellished>imelight</limey> " +
//      "is <sparkly_red>Ruby</sparkly_red> on the desktop.</opener>  " +
//      "<plain>Build applications with multiple windows, or just one window.  " +
//      "Take control of the desktop, or play nicely with the desktop.  " +
//      "Create fun animated games, or productive business apps.  Develop " +
//      "rich internet applications, or unwired apps to make you rich.  " +
//      "Publish your apps on the internet, or keep them for you and your friends.   " +
//      "Do all this, writing nothing but <sparkly_red>Ruby</sparkly_red> code, in <limey>Limelight</limey>.</plain>";
//
//  @Test
//  public void shouldGetPerformanceMetrics() throws Exception
//  {
//    long total = 0;
//    for(int i = 0; i < 100; i++)
//    {
//      long start = System.currentTimeMillis();
//      parser.parse(sampleText);
//      total += System.currentTimeMillis() - start;
//    }
//
//    double average = total / 100.0;
//    System.out.println("total = " + total + " average = " + average);
//  }

}
