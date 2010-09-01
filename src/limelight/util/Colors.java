//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

import limelight.LimelightError;

import java.awt.*;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Colors
{
  public static final Color TRANSPARENT = new Color(0, 0, 0, 0);

  private static final Pattern fullHex = Pattern.compile("#?([0-9aAbBcCdDeEfF]{6})");
  private static final Pattern shortHex = Pattern.compile("#?([0-9aAbBcCdDeEfF]{3})");
  private static final Pattern fullHexWithAlpha = Pattern.compile("#?([0-9aAbBcCdDeEfF]{8})");
  private static final Pattern shortHexWithAlpha = Pattern.compile("#?([0-9aAbBcCdDeEfF]{4})");
  private static final Pattern validName = Pattern.compile("[a-z_]+");
  private static final HashMap<String, Color> namedColors = new HashMap<String, Color>();

  static
  {
    namedColors.put("alice_blue", Color.decode("#F0F8FF"));
    namedColors.put("antique_white", Color.decode("#FAEBD7"));
    namedColors.put("aqua", Color.decode("#00FFFF"));
    namedColors.put("aquamarine", Color.decode("#7FFFD4"));
    namedColors.put("azure", Color.decode("#F0FFFF"));
    namedColors.put("beige", Color.decode("#F5F5DC"));
    namedColors.put("bisque", Color.decode("#FFE4C4"));
    namedColors.put("black", Color.decode("#000000"));
    namedColors.put("blanched_almond", Color.decode("#FFEBCD"));
    namedColors.put("blue", Color.decode("#0000FF"));
    namedColors.put("blue_violet", Color.decode("#8A2BE2"));
    namedColors.put("brown", Color.decode("#A52A2A"));
    namedColors.put("burly_wood", Color.decode("#DEB887"));
    namedColors.put("cadet_blue", Color.decode("#5F9EA0"));
    namedColors.put("chartreuse", Color.decode("#7FFF00"));
    namedColors.put("chocolate", Color.decode("#D2691E"));
    namedColors.put("coral", Color.decode("#FF7F50"));
    namedColors.put("cornflower_blue", Color.decode("#6495ED"));
    namedColors.put("cornsilk", Color.decode("#FFF8DC"));
    namedColors.put("crimson", Color.decode("#DC143C"));
    namedColors.put("cyan", Color.decode("#00FFFF"));
    namedColors.put("dark_blue", Color.decode("#00008B"));
    namedColors.put("dark_cyan", Color.decode("#008B8B"));
    namedColors.put("dark_golden_rod", Color.decode("#B8860B"));
    namedColors.put("dark_gray", Color.decode("#A9A9A9"));
    namedColors.put("dark_grey", Color.decode("#A9A9A9"));
    namedColors.put("dark_green", Color.decode("#006400"));
    namedColors.put("dark_khaki", Color.decode("#BDB76B"));
    namedColors.put("dark_magenta", Color.decode("#8B008B"));
    namedColors.put("dark_olive_green", Color.decode("#556B2F"));
    namedColors.put("darkorange", Color.decode("#FF8C00"));
    namedColors.put("dark_orchid", Color.decode("#9932CC"));
    namedColors.put("dark_red", Color.decode("#8B0000"));
    namedColors.put("dark_salmon", Color.decode("#E9967A"));
    namedColors.put("dark_sea_green", Color.decode("#8FBC8F"));
    namedColors.put("dark_slate_blue", Color.decode("#483D8B"));
    namedColors.put("dark_slate_gray", Color.decode("#2F4F4F"));
    namedColors.put("dark_slate_grey", Color.decode("#2F4F4F"));
    namedColors.put("dark_turquoise", Color.decode("#00CED1"));
    namedColors.put("dark_violet", Color.decode("#9400D3"));
    namedColors.put("deep_pink", Color.decode("#FF1493"));
    namedColors.put("deep_sky_blue", Color.decode("#00BFFF"));
    namedColors.put("dim_gray", Color.decode("#696969"));
    namedColors.put("dim_grey", Color.decode("#696969"));
    namedColors.put("dodger_blue", Color.decode("#1E90FF"));
    namedColors.put("fire_brick", Color.decode("#B22222"));
    namedColors.put("floral_white", Color.decode("#FFFAF0"));
    namedColors.put("forest_green", Color.decode("#228B22"));
    namedColors.put("fuchsia", Color.decode("#FF00FF"));
    namedColors.put("gainsboro", Color.decode("#DCDCDC"));
    namedColors.put("ghost_white", Color.decode("#F8F8FF"));
    namedColors.put("gold", Color.decode("#FFD700"));
    namedColors.put("golden_rod", Color.decode("#DAA520"));
    namedColors.put("gray", Color.decode("#808080"));
    namedColors.put("grey", Color.decode("#808080"));
    namedColors.put("green", Color.decode("#00FF00"));
    namedColors.put("green_yellow", Color.decode("#ADFF2F"));
    namedColors.put("honey_dew", Color.decode("#F0FFF0"));
    namedColors.put("hot_pink", Color.decode("#FF69B4"));
    namedColors.put("indian_red", Color.decode("#CD5C5C"));
    namedColors.put("indigo", Color.decode("#4B0082"));
    namedColors.put("ivory", Color.decode("#FFFFF0"));
    namedColors.put("khaki", Color.decode("#F0E68C"));
    namedColors.put("lavender", Color.decode("#E6E6FA"));
    namedColors.put("lavender_blush", Color.decode("#FFF0F5"));
    namedColors.put("lawn_green", Color.decode("#7CFC00"));
    namedColors.put("lemon_chiffon", Color.decode("#FFFACD"));
    namedColors.put("light_blue", Color.decode("#ADD8E6"));
    namedColors.put("light_coral", Color.decode("#F08080"));
    namedColors.put("light_cyan", Color.decode("#E0FFFF"));
    namedColors.put("light_golden_rod_yellow", Color.decode("#FAFAD2"));
    namedColors.put("light_gray", Color.decode("#D3D3D3"));
    namedColors.put("light_grey", Color.decode("#D3D3D3"));
    namedColors.put("light_green", Color.decode("#90EE90"));
    namedColors.put("light_pink", Color.decode("#FFB6C1"));
    namedColors.put("light_salmon", Color.decode("#FFA07A"));
    namedColors.put("light_sea_green", Color.decode("#20B2AA"));
    namedColors.put("light_sky_blue", Color.decode("#87CEFA"));
    namedColors.put("light_slate_gray", Color.decode("#778899"));
    namedColors.put("light_slate_grey", Color.decode("#778899"));
    namedColors.put("light_steel_blue", Color.decode("#B0C4DE"));
    namedColors.put("light_yellow", Color.decode("#FFFFE0"));
    namedColors.put("lime", Color.decode("#00FF00"));
    namedColors.put("lime_green", Color.decode("#32CD32"));
    namedColors.put("limelight_green", Color.decode("#BBD453"));
    namedColors.put("linen", Color.decode("#FAF0E6"));
    namedColors.put("magenta", Color.decode("#FF00FF"));
    namedColors.put("maroon", Color.decode("#800000"));
    namedColors.put("medium_aqua_marine", Color.decode("#66CDAA"));
    namedColors.put("medium_blue", Color.decode("#0000CD"));
    namedColors.put("medium_orchid", Color.decode("#BA55D3"));
    namedColors.put("medium_purple", Color.decode("#9370D8"));
    namedColors.put("medium_sea_green", Color.decode("#3CB371"));
    namedColors.put("medium_slate_blue", Color.decode("#7B68EE"));
    namedColors.put("medium_spring_green", Color.decode("#00FA9A"));
    namedColors.put("medium_turquoise", Color.decode("#48D1CC"));
    namedColors.put("medium_violet_red", Color.decode("#C71585"));
    namedColors.put("midnight_blue", Color.decode("#191970"));
    namedColors.put("mint_cream", Color.decode("#F5FFFA"));
    namedColors.put("misty_rose", Color.decode("#FFE4E1"));
    namedColors.put("moccasin", Color.decode("#FFE4B5"));
    namedColors.put("navajo_white", Color.decode("#FFDEAD"));
    namedColors.put("navy", Color.decode("#000080"));
    namedColors.put("old_lace", Color.decode("#FDF5E6"));
    namedColors.put("olive", Color.decode("#808000"));
    namedColors.put("olive_drab", Color.decode("#6B8E23"));
    namedColors.put("orange", Color.decode("#FFA500"));
    namedColors.put("orange_red", Color.decode("#FF4500"));
    namedColors.put("orchid", Color.decode("#DA70D6"));
    namedColors.put("pale_golden_rod", Color.decode("#EEE8AA"));
    namedColors.put("pale_green", Color.decode("#98FB98"));
    namedColors.put("pale_turquoise", Color.decode("#AFEEEE"));
    namedColors.put("pale_violet_red", Color.decode("#D87093"));
    namedColors.put("papaya_whip", Color.decode("#FFEFD5"));
    namedColors.put("peach_puff", Color.decode("#FFDAB9"));
    namedColors.put("peru", Color.decode("#CD853F"));
    namedColors.put("pink", Color.decode("#FFC0CB"));
    namedColors.put("plum", Color.decode("#DDA0DD"));
    namedColors.put("powder_blue", Color.decode("#B0E0E6"));
    namedColors.put("purple", Color.decode("#800080"));
    namedColors.put("red", Color.decode("#FF0000"));
    namedColors.put("rosy_brown", Color.decode("#BC8F8F"));
    namedColors.put("royal_blue", Color.decode("#4169E1"));
    namedColors.put("saddle_brown", Color.decode("#8B4513"));
    namedColors.put("salmon", Color.decode("#FA8072"));
    namedColors.put("sandy_brown", Color.decode("#F4A460"));
    namedColors.put("sea_green", Color.decode("#2E8B57"));
    namedColors.put("sea_shell", Color.decode("#FFF5EE"));
    namedColors.put("sienna", Color.decode("#A0522D"));
    namedColors.put("silver", Color.decode("#C0C0C0"));
    namedColors.put("sky_blue", Color.decode("#87CEEB"));
    namedColors.put("slate_blue", Color.decode("#6A5ACD"));
    namedColors.put("slate_gray", Color.decode("#708090"));
    namedColors.put("slate_grey", Color.decode("#708090"));
    namedColors.put("snow", Color.decode("#FFFAFA"));
    namedColors.put("spring_green", Color.decode("#00FF7F"));
    namedColors.put("steel_blue", Color.decode("#4682B4"));
    namedColors.put("tan", Color.decode("#D2B48C"));
    namedColors.put("teal", Color.decode("#008080"));
    namedColors.put("thistle", Color.decode("#D8BFD8"));
    namedColors.put("tomato", Color.decode("#FF6347"));
    namedColors.put("turquoise", Color.decode("#40E0D0"));
    namedColors.put("violet", Color.decode("#EE82EE"));
    namedColors.put("wheat", Color.decode("#F5DEB3"));
    namedColors.put("white", Color.decode("#FFFFFF"));
    namedColors.put("white_smoke", Color.decode("#F5F5F5"));
    namedColors.put("yellow", Color.decode("#FFFF00"));
    namedColors.put("yellow_green", Color.decode("#9ACD32"));
    namedColors.put("transparent", TRANSPARENT);
  }

  public static Color resolve(String value)
  {
    Color result = convertFullHexWithAlphaColor(value);
    if(result == null)
      result = convertFullHexWithoutAlphaColor(value);
    if(result == null)
      result = convertShortHexWithAlphaColor(value);
    if(result == null)
      result = convertShortHexWithoutAlphaColor(value);
    if(result == null)
      result = convertNamedColor(value);
    if(result == null)
      throw new LimelightError("Invalid color: " + value);

    return result;
  }

  private static Color convertFullHexWithAlphaColor(String value)
  {
    Matcher matcher = fullHexWithAlpha.matcher(value);
    if(matcher.matches())
    {
      String hexValue = matcher.group(1);
      int r = hexToInt(hexValue.substring(0, 2));
      int g = hexToInt(hexValue.substring(2, 4));
      int b = hexToInt(hexValue.substring(4, 6));
      int a = hexToInt(hexValue.substring(6, 8));
      return new Color(r, g, b, a);
    }
    return null;
  }

  private static Color convertFullHexWithoutAlphaColor(String value)
  {
    Matcher matcher = fullHex.matcher(value);
    if(matcher.matches())
    {
      String decodableValue = "#" + matcher.group(1);
      return Color.decode(decodableValue);
    }
    return null;
  }

  private static Color convertShortHexWithAlphaColor(String value)
  {
    Matcher matcher = shortHexWithAlpha.matcher(value);
    if(matcher.matches())
    {
      String hexValue = matcher.group(1);
      int r = hexToInt("" + hexValue.charAt(0) + hexValue.charAt(0));
      int g = hexToInt("" + hexValue.charAt(1) + hexValue.charAt(1));
      int b = hexToInt("" + hexValue.charAt(2) + hexValue.charAt(2));
      int a = hexToInt("" + hexValue.charAt(3) + hexValue.charAt(3));
      return new Color(r, g, b, a);
    }
    return null;
  }

  private static Color convertShortHexWithoutAlphaColor(String value)
  {
    Matcher matcher = shortHex.matcher(value);
    if(matcher.matches())
    {
      String hexValue = matcher.group(1);
      String decodableValue = "#" +
          hexValue.charAt(0) + hexValue.charAt(0) +
          hexValue.charAt(1) + hexValue.charAt(1) +
          hexValue.charAt(2) + hexValue.charAt(2);

      return Color.decode(decodableValue);
    }
    return null;
  }

  private static final Pattern wordTransition = Pattern.compile("[a-z][A-Z]");
  private static Color convertNamedColor(String name)
  {
    if(!validName.matcher(name).matches())
      name = formatName(name);
    return namedColors.get(name);
  }

  private static String formatName(String name)
  {
    name = name.replace(" ", "_");
    Matcher matcher = wordTransition.matcher(name);
    while(matcher.find())
      {
        StringBuilder builder = new StringBuilder(name.substring(0, matcher.start()));
      builder.append(matcher.group().substring(0, 1));
      builder.append("_");
      builder.append(matcher.group().substring(1, 2).toLowerCase());
      builder.append(name.substring(matcher.end(), name.length()));
      name = builder.toString();
    }
    name = name.toLowerCase();
    return name;
  }

  private static int hexToInt(String hex)
  {
    return Integer.parseInt(hex, 16);
  }

  public static String toString(Color color)
  {
    String r = Integer.toHexString(color.getRed());
    String g = Integer.toHexString(color.getGreen());
    String b = Integer.toHexString(color.getBlue());
    String a = Integer.toHexString(color.getAlpha());
    StringBuffer buffer = new StringBuffer("#");

    if(r.length() == 1)
      buffer.append("0");
    buffer.append(r);

    if(g.length() == 1)
      buffer.append("0");
    buffer.append(g);

    if(b.length() == 1)
      buffer.append("0");
    buffer.append(b);

    if(a.length() == 1)
      buffer.append("0");
    buffer.append(a);

    return buffer.toString();
  }
}
