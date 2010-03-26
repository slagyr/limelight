desc "Generate the Style Test parser statemachine"
task :generate_styled_text_parser do
  require 'statemachine'
  require 'statemachine/generate/java'
  
  sm_src = IO.read("src/limelight/ui/text/styled_text_statemachine.rb")
  sm = eval(sm_src)

  sm.to_java(:output => "src", :name => "StyledTextStatemachine", :package => "limelight.ui.text")
end