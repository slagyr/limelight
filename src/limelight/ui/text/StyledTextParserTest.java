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
  private List<StyledText> parsedTextList;
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
    String actualStylesString = StringUtil.join(", ", styledText.getStyles().toArray(new String[styledText.getStyles().size()]));
    assertEquals(expectedStylesString, actualStylesString);
  }
  
  @Test
  public void shouldParseUnstyledText() throws Exception
  {
    parsedTextList = parser.parse("Hello There");

    checkStyledText(parsedTextList.get(0), "Hello There");
  }

  @Test
  public void shouldParseOtherUnstyledText() throws Exception
  {
    parsedTextList = parser.parse("Howdy");

    checkStyledText(parsedTextList.get(0), "Howdy");
  }

  @Test
  public void shouldParseStyledText() throws Exception
  {
    parsedTextList = parser.parse("<b>I am serious</b>");

    checkStyledText(parsedTextList.get(0), "I am serious", "b");
  }

  @Test
  public void shouldParseMultipleStyledText() throws Exception
  {
    parsedTextList = parser.parse("<b>I am</b><c> serious</c>");

    checkStyledText(parsedTextList.get(0), "I am", "b");
    checkStyledText(parsedTextList.get(1), " serious", "c");
  }

  @Test
  public void shouldParseUnstyledTextBetweenStyledText() throws Exception
  {
    parsedTextList = parser.parse("<b>I am</b> not really<c> serious</c>");

    checkStyledText(parsedTextList.get(0), "I am", "b");
    checkStyledText(parsedTextList.get(1), " not really");
    checkStyledText(parsedTextList.get(2), " serious", "c");
  }

  @Test
  public void shouldParseTrailingUnstyledText() throws Exception
  {
    parsedTextList = parser.parse("<b>styled</b> unstyled");

    checkStyledText(parsedTextList.get(0), "styled", "b");
    checkStyledText(parsedTextList.get(1), " unstyled");
  }

  @Test
  public void shouldParseAlternatingStylingAndUnstyling() throws Exception
  {
    parsedTextList = parser.parse("unstyled1<b>styled1</b>unstyled2<c>styled2</c>unstyled3");

    checkStyledText(parsedTextList.get(0), "unstyled1");
    checkStyledText(parsedTextList.get(1), "styled1", "b");
    checkStyledText(parsedTextList.get(2), "unstyled2");
    checkStyledText(parsedTextList.get(3), "styled2", "c");
    checkStyledText(parsedTextList.get(4), "unstyled3");
  }

  @Test
  public void shouldParseNestedTags() throws Exception
  {
    parsedTextList = parser.parse("default<b>b<c>c<d>d<e>e</e>d</d>c</c>b</b>default");

    checkStyledText(parsedTextList.get(0), "default");
    checkStyledText(parsedTextList.get(1), "b", "b");
    checkStyledText(parsedTextList.get(2), "c", "c", "b");
    checkStyledText(parsedTextList.get(3), "d", "d", "c", "b");
    checkStyledText(parsedTextList.get(4), "e", "e", "d", "c", "b");
    checkStyledText(parsedTextList.get(5), "d", "d", "c", "b");
    checkStyledText(parsedTextList.get(6), "c", "c", "b");
    checkStyledText(parsedTextList.get(7), "b", "b");
    checkStyledText(parsedTextList.get(8), "default");
  }

  @Test
  public void shouldParseMultipleNestedAndAlternatingTags() throws Exception
  {
    parsedTextList = parser.parse("default<a>a<b>b</b>a</a>default<c>c<d>d</d>c</c>default");

    checkStyledText(parsedTextList.get(0), "default");
    checkStyledText(parsedTextList.get(1), "a", "a");
    checkStyledText(parsedTextList.get(2), "b", "b", "a");
    checkStyledText(parsedTextList.get(3), "a", "a");
    checkStyledText(parsedTextList.get(4), "default");
    checkStyledText(parsedTextList.get(5), "c", "c");
    checkStyledText(parsedTextList.get(6), "d", "d", "c");
    checkStyledText(parsedTextList.get(7), "c", "c");
    checkStyledText(parsedTextList.get(8), "default");
  }

  @Test
  public void shouldParseUnclosedTags() throws Exception
  {
    parsedTextList = parser.parse("default<a>a</b>b</a>default");

    checkStyledText(parsedTextList.get(0), "default");
    checkStyledText(parsedTextList.get(1), "a</b>b", "a");
    checkStyledText(parsedTextList.get(2), "default");
  }

  @Test
  public void shouldParseMisMatchedTags() throws Exception
  {
    parsedTextList = parser.parse("sky<a>wal</b>ker");

    checkStyledText(parsedTextList.get(0), "sky<a>wal</b>ker");
  }

  @Test
  public void shouldParseMultipleInnerStlyes() throws Exception
  {
    parsedTextList = parser.parse("before <a>a <b>b</b> <c>c</c> a</a> after");

    assertEquals(7, parsedTextList.size());
    checkStyledText(parsedTextList.get(0), "before ");
    checkStyledText(parsedTextList.get(1), "a ", "a");
    checkStyledText(parsedTextList.get(2), "b", "b", "a");
    checkStyledText(parsedTextList.get(3), " ", "a");
    checkStyledText(parsedTextList.get(4), "c", "c", "a");
    checkStyledText(parsedTextList.get(5), " a", "a");
    checkStyledText(parsedTextList.get(6), " after");
  }

  @Test
  public void shouldParseEscapedTag() throws Exception
  {
    parsedTextList = parser.parse("&lt;a&gt;&amp;intentional&lt;/a&gt;");

    assertEquals(1, parsedTextList.size());
    checkStyledText(parsedTextList.get(0), "<a>&intentional</a>");
  }

  @Test
  public void shouldParseTagRegex() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123</abc>");
    assertEquals(true, matcher.find());
    assertEquals("<abc>123</abc>", matcher.group(0));
    assertEquals("abc", matcher.group(1));
    assertEquals("123", matcher.group(2));
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
    assertEquals("123\n456", matcher.group(2));
  }

  @Test
  public void shouldParseReluctantCapturing() throws Exception
  {
    Matcher matcher = StyledTextParser.TAG_REGEX.matcher("<abc>123</abc><abc>456</abc>");
    assertEquals(true, matcher.find());
    assertEquals("123", matcher.group(2));
  }

}
