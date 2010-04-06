//- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.util;

public class Util
{
  public static boolean equal(Object o1, Object o2)
  {
    if(o1 != null)
      return o1.equals(o2);
    else if(o2 != null)
      return o2.equals(o1);
    else
      return true;
  }
}
