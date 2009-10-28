//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.ui.api;

public interface Production
{
  String getName();
  void setName(String name);
  boolean allowClose();
  void close();
  Object callMethod(String name, Object... args);
}
