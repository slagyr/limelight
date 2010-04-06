#- Copyright © 2008-2010 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

require 'limelight/prop'
require 'limelight/scene'
require 'limelight/dsl/build_exception'
require 'limelight/util'

module Limelight

  # A trigger to build a Limelight::Scene using the PropBuilder DSL.
  #
  # See Limelight::PropBuilder
  #
  def self.build_props(root, options={}, &block)
    loader = options.delete(:build_loader)
    instance_variables = options.delete(:instance_variables)
    root.add_options(options)
    builder = DSL::PropBuilder.new(root)
    builder.__install_instance_variables(instance_variables)
    builder.__loader__ = loader
    builder.instance_eval(&block) if block  
    return root
  end

  module DSL

    # The basis of the DSL for building Limelight::Prop objects.
    #
    # Sample usage:
    #
    #   builder = Limelight::PropBuilder.new(a_prop)
    #   builder.instance_eval(&block)
    #
    # The prop passed into the constructor will be the root of the contructed Prop tree.
    # The block passed into instance_eval contains the DSL for building props.
    #
    # Example block/DSL:
    #
    #   parent :id => "the_parent" do
    #     child_one do
    #       grand_child_one :id => "gc_1", :styles => "grand_child"
    #       grand_child_two :id => "gc_2", :styles => "grand_child"
    #     end
    #     child_two
    #   end
    #
    # The above example will create a Limelight::Prop named 'parent' and add it to the root prop passed into the builder.
    # The 'parent' prop will contain two props named 'child_one' and 'child_two'.  'child_one' will contain two props named
    # 'grand_child_one' and 'grand_child_two'.  'child_two' has no child props nor do 'grand_child_one' or 'grand_child_two'.
    #
    # An options Hash may be passed into each prop.  The key, value pairs in the hash will be used to set properties on the prop
    # when it is added to a Limelight::Scene.
    #
    # See Limelight::Prop
    #
    class PropBuilder

      alias :__instance_variables :instance_variables
      alias :__instance_variable_get :instance_variable_get
      alias :__instance_variable_set :instance_variable_set

      Limelight::Util.lobotomize(self)

      # Returns the root prop either passed in or created by this builder.
      #
      attr_reader :__prop__
      attr_accessor :__loader__

      # Creates a new builder.  If a prop is passed it, it will be the root on which props are created.
      # If the paramter is a Hash, the Hash will be used to construct a prop that will be used as the root.
      #
      def initialize(options)
        if options.is_a?(Prop)
          @__prop__ = options
        else
          @__prop__ = Prop.new(options)
        end
      end

      # Add extra initialization options to the prop currently under construction.
      #
      #   tree :id => "stump" do
      #     __ :height => "100%", :width => "30", :background_color => :brown
      #     branch :height => "100", :width => "20"
      #     branch do
      #       __ :height => "100", :width => "20"
      #     end
      #   end
      #
      # In the above example, the 'tree' prop has the following initialization options: id, height, width, background_color.
      # The two 'branch' child props are identical.
      #
      def __(options)
        @__prop__.add_options(options)
      end

      # Installs props from another file using the prop DSL.  The path will be relative to the
      # root directory of the current production.
      #
      def __install(file, instance_variables = {})
        raise "Cannot install external props because no loader was provided" if @__loader__.nil?
        raise "External prop file: '#{file}' doesn't exist" if !@__loader__.exists?(file)
        content = @__loader__.load(file)
        begin
          self.__install_instance_variables(instance_variables)
          self.instance_eval(content)
        rescue Exception => e
          raise BuildException.new(file, content, e)
        end
      end

      # Installs instance variables, specified by the passed hash, into the PropBuilder instance.
      # The following call...
      #
      #   __install_instance_variables :one => "1", "two" => "two", :three => 3
      #
      # ...will result in the following instance variables.
      #
      #   @one   = "1"
      #   @two   = "2"
      #   @three = 3
      #
      # Instance variables are propogated to nested builders so that they need only be defined
      # on the top level builder and all children will have access to them.
      #
      def __install_instance_variables(instance_variables)
        return if instance_variables.nil?
        instance_variables.each_pair do |key, value|
          name = "@#{key}"
          __instance_variable_set(name, value)
        end
      end

      def __install_instance_variables_from_builder(source) #:nodoc:
        source.__instance_variables.each do |name|
          if !__instance_variable_get(name)
            value = source.__instance_variable_get(name)
            __instance_variable_set(name, value)
          end
        end
      end

      def method_missing(sym, options={}, &block) # :nodoc:
        options[:name] ||= sym.to_s
#        builder = PropBuilder.new(options)
#        builder.__install_instance_variables_from_builder(self)
#        builder.__loader__ = @__loader__
#        builder.instance_eval(&block) if block
#        @__prop__.add(builder.__prop__)
        current_prop = @__prop__
        @__prop__ = Prop.new(options)
        instance_eval(&block) if block
        current_prop.add(@__prop__)
        @__prop__ = current_prop
      end
    end

  end
end