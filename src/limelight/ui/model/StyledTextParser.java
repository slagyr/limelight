//- Copyright 2008 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.LinkedList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class StyledTextParser
{

  private static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)>(.*)</(\\1)>");

  public LinkedList<StyledText> parse(String text)
  {
    return parse(text, "default", new LinkedList<StyledText>());
  }

  private LinkedList<StyledText> parse(String text, String defaultStyle, LinkedList<StyledText> list)
  {
    Matcher matcher = TAG_REGEX.matcher(text);

    int index = 0;
    while (matcher.find())
    {
      String prefix = escape(text.substring(index, matcher.start()));
      String styleName = matcher.group(1);
      String styledText = matcher.group(2);

      if (prefix.length() > 0)
        list.add(new StyledText(prefix, defaultStyle));

      parse(styledText, styleName, list);

      index = matcher.end();
    }

    String trailingText = escape(text.substring(index));
    if (trailingText.length() > 0)
      list.add(new StyledText(trailingText, defaultStyle));

    return list;
  }

  private String escape(String text)
  {
    return text.replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&amp;","&");
  }
}
