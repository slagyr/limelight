//- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the MIT License.

package limelight.model.api;

import limelight.ui.model.Prop;
import java.util.Map;

public interface PropProxy
{
  Map<String, Object> applyOptions(Map<String, Object> options);

  Prop getPeer();
}
