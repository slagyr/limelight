#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/util'

module Limelight

  # A trigger to define Style objects using the StyleBuilder DSL.
  #
  # See Limelight::Stylesbuilder, Limelight::Stylebuilder
  #
  def self.build_styles(options = {}, &block)
    builder = DSL::StylesBuilder.new(options)
    builder.instance_eval(&block) if block
    return builder.__styles
  end

  def self.build_styles_from_file(filename, options = {})
    content = IO.read(filename)
    styles = Limelight.build_styles(options) do
      begin
        eval content
      rescue Exception => e
        raise Limelight::DSL::BuildException.new(filename, content, e)
      end
    end
    return styles
  end

  module DSL

    # The basis of the DSL for building Style objects.
    #
    # Sample StyleBuilder DSL
    #
    #  sandbox {
    #    width "100%"
    #    height "100%"
    #    vertical_alignment :top
    #  }
    #
    #  sample {
    #    width 320
    #    height 320
    #    gradient :on
    #  }
    #
    #  spinner {
    #    extends :sample
    #    background_color :green
    #    secondary_background_color :blue
    #    gradient_angle 0
    #    gradient_penetration 100
    #  }
    #
    # This exmple builds three styles: sandbox, sample, spinner.  Within each style block, the individual attributes of
    # the style may be set.
    #
    # See Limelight::Styles
    #
    class StylesBuilder

      Limelight::Util.lobotomize(self)

      attr_reader :__styles, :__extendable_styles

      def initialize(options = {})
        @__extendable_styles = options[:extendable_styles] || {}
        @__styles = options[:styles] || {}
      end

      def method_missing(sym, &block) #:nodoc:
        __add_style(sym.to_s, &block)
      end

      def __add_style(name, &block) #:nodoc:
        style = @__styles[name]
        if style == nil
          style = Styles::RichStyle.new
          extension = @__extendable_styles[name]
          style.add_extension(extension) if extension
        end
        builder = StyleBuilder.new(name, style, self)
        builder.instance_eval(&block) if block
        @__styles[name] = style
      end
    end

    # The basis of the DSL for defining a Style object.
    #
    class StyleBuilder

      Limelight::Util.lobotomize(self)

      attr_reader :__style  #:nodoc:

      def initialize(name, style, styles_builder, options = {})  #:nodoc:
        @__name = name
        @__style = style
        @__styles_builder = styles_builder
      end

      # Used to define a hover style.  Hover styles are appiled when the mouse passed over a prop using the specified style.
      #
      #   spinner {
      #     width 50
      #     height 50
      #     hover {
      #       text_color :white
      #     }
      #   }
      #
      # The text color of props using the 'spinner' style will become white when the mouse hovers over them.
      #
      def hover(&block)
        @__styles_builder.__add_style("#{@__name}.hover", &block)
      end

      # Styles may extend other styles.
      #
      #    base {
      #      background_color :red
      #    }
      #
      #    cell {
      #      extends :base
      #      text_color :black
      #    }
      #
      # The 'cell' style now has all attributes defined in 'base'.  Therefore any prop using the 'cell' style
      # will have a red background.  Styles may override attributes aquired through extension.
      #
      def extends(*style_names)
        style_names.each do |style_name|
          extension = @__styles_builder.__styles[style_name.to_s] || @__styles_builder.__extendable_styles[style_name.to_s]
          raise StyleBuilderException.new("Can't extend missing style: '#{style_name}'") if extension.nil?
          @__style.add_extension(extension)
        end
      end

      def method_missing(sym, value) #:nodoc:
        setter_sym = "#{sym}=".to_s
        raise StyleBuilderException.new("'#{sym}' is not a valid style") if !@__style.respond_to?(setter_sym)
        @__style.send(setter_sym, value.to_s)
      end

      # Kernel.y was added by the yaml library in JRuby 1.4.  Not sure what to do about this type of problem.
      def y(*args) #:nodoc:
        method_missing(:y, *args)
      end
    end

    # Exception thrown by StyleBuilder when an error is encountered.
    #
    class StyleBuilderException < Exception
      def initialize(message)
        super(message)
      end
    end

  end
end