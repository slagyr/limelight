//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.model;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyledTextParser
{

  public static final Pattern TAG_REGEX = Pattern.compile("<(\\w+)>(.*?)</(\\1)>", Pattern.DOTALL);

  public LinkedList<StyledText> parse(String text)
  {
    return parse(text, "default", new LinkedList<StyledText>(), new LinkedList<String>());
  }

  private LinkedList<StyledText> parse(String text, String defaultStyle, LinkedList<StyledText> list, List<String> parentStyles)
  {
    Matcher matcher = TAG_REGEX.matcher(text);

    int index = 0;
    while (matcher.find())
    {
      String prefix = escape(text.substring(index, matcher.start()));
      String styleName = matcher.group(1);
      String styledText = matcher.group(2);

      if (prefix.length() > 0)
        list.add(new StyledText(prefix, defaultStyle, parentStyles));

      List<String> newParents = new LinkedList<String>(parentStyles);
      newParents.add(defaultStyle);
      parse(styledText, styleName, list, newParents);

      index = matcher.end();
    }

    String trailingText = escape(text.substring(index));
    if (trailingText.length() > 0)
      list.add(new StyledText(trailingText, defaultStyle, parentStyles));

    return list;
  }

  private String escape(String text)
  {
    return text.replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&amp;","&");
  }
}
