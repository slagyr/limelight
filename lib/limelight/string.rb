class String

  # Converts Ruby style names to Java style camal case.
  #
  #   "four_score".camalized # => "FourScore"
  #   "and_seven_years".camalized(:lower) # => "andSevenYears"
  #
  def camalized(starting_case = :upper)
    value = self.downcase.gsub(/[_| ][a-z]/) { |match| match[-1..-1].upcase }
    value = value[0..0].upcase + value[1..-1] if starting_case == :upper
    return value
  end

  # Converts Java camel case names to ruby style underscored names.
  #
  #   "FourScore".underscored # => "four_score"
  #   "andSevenYears".underscored # => "and_seven_years"
  #
  def underscored
    value = self[0..0].downcase + self[1..-1]
    value = value.gsub(/[A-Z]/) { |cap| "_#{cap.downcase}" }
    return value
  end


  # Converts ruby style and camalcase strings into title strings where every word is capitalized and separated by a space.
  #
  #   "four_score".titleized # => "Four Score"
  #
  def titleized(starting_case = :upper)
    value = self.gsub(/[a-z0-9][A-Z]/) { |match| "#{match[0..0]} #{match[-1..-1]}" }
    value = value.gsub(/[_| ][a-z]/) { |match| " #{match[-1..-1].upcase}" }
    return value[0..0].upcase + value[1..-1]
  end
end