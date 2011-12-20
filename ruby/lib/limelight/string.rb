#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

class String

  # Converts Ruby style names to Java style camel case.
  #
  #   "four_score".camelized # => "FourScore"
  #   "and_seven_years".camelized(:lower) # => "andSevenYears"
  #
  def camelized(starting_case = :upper)
    starting_case == :upper ? Java::limelight.util.StringUtil.capitalCamelCase(self) : Java::limelight.util.StringUtil.camelCase(self)
  end

  # Converts Java camel case names to ruby style underscored names.
  #
  #   "FourScore".underscored # => "four_score"
  #   "andSevenYears".underscored # => "and_seven_years"
  #
  def underscored
    Java::limelight.util.StringUtil.snakeCase(self)
  end


  # Converts ruby style and camalcase strings into title strings where every word is capitalized and separated by a space.
  #
  #   "four_score".titleized # => "Four Score"
  #
  def titleized(starting_case = :upper)
    Java::limelight.util.StringUtil.titleCase(self)
  end
end