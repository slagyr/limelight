require File.expand_path(File.dirname(__FILE__) + "/lib/init")

class_content = IO.read File.expand_path(File.dirname(__FILE__) + "/src/limelight/styles/attributes/WidthAttribute.java")
test_content = IO.read File.expand_path(File.dirname(__FILE__) + "/src/limelight/styles/attributes/WidthAttributeTest.java")

#puts "class_content: #{class_content}"
#puts "test_content: #{test_content}"

Java::limelight.styles.Style::STYLE_LIST.each do |attr|
  class_name = "#{attr.name}Attribute".gsub(" ", "")
  class_filename = "#{class_name}.java"
  test_filename = "#{class_name}Test.java"

#  puts "class_filename: #{class_filename}"
#  puts "test_filename: #{test_filename}"

  new_class_content = class_content.
          gsub("WidthAttribute", class_name).
          gsub("\"Width\"", "\"#{attr.name}\"").
          gsub("dimension", attr.compiler.type).
          gsub("auto", attr.defaultValue.toString)

  new_test_content = test_content.
          gsub("WidthAttribute", class_name).
          gsub("\"Width\"", "\"#{attr.name}\"").
          gsub("dimension", attr.compiler.type).
          gsub("auto", attr.defaultValue.toString)

  new_class_filename = File.expand_path(File.dirname(__FILE__) + "/src/limelight/styles/attributes/#{class_filename}")
  new_test_filename = File.expand_path(File.dirname(__FILE__) + "/src/limelight/styles/attributes/#{test_filename}")

  if attr.name != "Width"

    File.open(new_class_filename, "w") { |f| f.write new_class_content }
    File.open(new_test_filename, "w") { |f| f.write new_test_content }

  end

end