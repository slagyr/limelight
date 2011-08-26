//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.styles;

import limelight.styles.abstrstyling.StyleValue;

public interface StyleObserver
{
  void styleChanged(StyleAttribute attribute, StyleValue value);
}
