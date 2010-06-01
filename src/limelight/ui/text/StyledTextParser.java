//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.text;

import limelight.styles.RichStyle;
import limelight.styles.Style;
import limelight.styles.StyleAttribute;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyledTextParser
{

  public static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)(\\s*(?:\\w+\\s*=(?:\\s*\"[^\"]*?\"|'[^']*?'|[^>'\"\\s]+)\\s*)*)>(.*?)</(\\1)>", Pattern.DOTALL);
  public static final Pattern ATTR_REGEX = Pattern.compile("(\\w+)\\s*=\\s*(\"[^\"]*?\"|'[^']*?'|[^>'\"\\s]+)");

  public LinkedList<StyledText> parse(String text)
  {
    return parse(text, new RichStyle(), new LinkedList<String>(), new LinkedList<StyledText>());
  }

  private LinkedList<StyledText> parse(String text, RichStyle parentStyle, LinkedList<String> styleNames, LinkedList<StyledText> list)
  {
    Matcher matcher = TAG_REGEX.matcher(text);

    int index = 0;
    while(matcher.find())
    {
//for(int i = 0; i < matcher.groupCount(); i++)
//System.err.println("matcher.group("+i+") = " + matcher.group(i));
      handleUndecoratedText(parentStyle, styleNames, list, text.substring(index, matcher.start()));

      String styleName = matcher.group(1);
      String attributeContent = matcher.group(2);
      String content = matcher.group(3);
      RichStyle style = buildStyle(attributeContent);

      styleNames.addFirst(styleName);
      parse(content, style, styleNames, list);
      styleNames.removeFirst();

      index = matcher.end();
    }

    handleUndecoratedText(parentStyle, styleNames, list, text.substring(index));

    return list;
  }

  private void handleUndecoratedText(RichStyle parentStyle, LinkedList<String> styleNames, LinkedList<StyledText> list, String undecoratedText)
  {
    String prefix = escape(undecoratedText);
    if(prefix.length() > 0)
      list.add(new StyledText(prefix, parentStyle, styleNames));
  }

  private String escape(String text)
  {
    return text.replaceAll("&gt;", ">").replaceAll("&lt;", "<").replaceAll("&amp;", "&");
  }

  private RichStyle buildStyle(String text)
  {
    RichStyle style = new RichStyle();
    if(text != null && text.length() > 0)
      parseAttributes(text, style);
    return style;
  }

  private void parseAttributes(String text, RichStyle style)
  {
    Matcher matcher = ATTR_REGEX.matcher(text);
    while(matcher.find())
    {
//for(int i = 0; i < matcher.groupCount(); i++)
//System.err.println("@matcher.group("+i+") = " + matcher.group(i));
      String attributeName = matcher.group(1);
      if(attributeName != null)
      {
        String attributeValue = removeQuotes(matcher.group(2));
        StyleAttribute attribute = Style.descriptorFor(attributeName);
        style.put(attribute, attributeValue);
      }
    }
  }

  private String removeQuotes(String value)
  {
    if((value.startsWith("'") && value.endsWith("'")) || (value.startsWith("\"") && value.endsWith("\"")))
      return value.substring(1, value.length() - 1);

    return value;
  }
}
