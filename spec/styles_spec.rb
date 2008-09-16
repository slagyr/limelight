require File.expand_path(File.dirname(__FILE__) + "/spec_helper")
require 'limelight/styles'

describe Limelight::Styles do

  def const_format(name)
    return name.gsub(" ", "")
  end

  def method_format(name)
    return name.downcase.gsub(" ", "_")
  end

  it "should have all the styles" do
    Limelight::Styles::Style::STYLE_LIST.each do |descriptor|
      const_name = const_format(descriptor.name)
      Limelight::Styles.const_get(const_name)
    end
  end

#  it "should generate code" do
#    Limelight::Styles::Style::STYLE_LIST.each do |descriptor|
#      const_name = const_format(descriptor.name)
#      method_name = method_format(descriptor.name)
#      spaces = 40 - const_name.length
#      puts "# Specifies the #{descriptor.name} of a prop."
#      puts "#"
#      puts "#   style.#{method_name} = <value>"
#      puts "#"
#      puts "#{const_name} #{'' * spaces} = Limelight::Styles::Style::STYLE_LIST.get(#{descriptor.index})"
#      puts ""
#    end
#  end


end