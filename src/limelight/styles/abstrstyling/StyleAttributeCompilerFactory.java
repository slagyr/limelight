//- Copyright © 2008-2009 8th Light, Inc. All Rights Reserved.
//- Limelight and all included source files are distributed under terms of the GNU LGPL.

package limelight.styles.abstrstyling;

import limelight.styles.styling.RealStyleAttributeCompilerFactory;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

public interface StyleAttributeCompilerFactory
{
  RealStyleAttributeCompilerFactory compilerFactory = null;

  StyleAttributeCompiler compiler(String name);

}
