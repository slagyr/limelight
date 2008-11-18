package limelight.styles.abstrstyling;

import limelight.styles.styling.RealStyleAttributeCompilerFactory;
import limelight.styles.abstrstyling.StyleAttributeCompiler;

public interface StyleAttributeCompilerFactory
{
  RealStyleAttributeCompilerFactory compilerFactory = null;

  StyleAttributeCompiler compiler(String name);

}
