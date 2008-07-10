//- Copyright 2008 8th Light, Inc.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles;

public interface StyleObserver
{
  void styleChanged(StyleDescriptor descriptor, String value);
}
