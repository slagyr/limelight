require 'limelight/templates/templater_logger'

module Limelight
  module Templates

    class Templater
      
      def self.source_dir
        return File.join(File.dirname(__FILE__), "sources")
      end

      def self.clarify(path)
        return path if path[0..0] == '.'
        return path if path == File.expand_path(path)
        return File.join(".", path)
      end

      attr_reader :target_root, :source_root
      attr_accessor :logger

      def initialize(target_root, source_root)
        @logger = TemplaterLogger.new
        @target_root = Templater.clarify(target_root)
        @source_root = source_root
      end

      def directory(path)
        full_path = File.join(@target_root, path)
        establish_directory(full_path)
      end

      def file(target, source, tokens = {})
        target_path = File.join(@target_root, target)
        source_source = File.join(@source_root, source)

        establish_directory(File.dirname(target_path))

        if File.exists?(target_path)
          @logger.file_already_exists(target_path)
        else
          @logger.creating_file(target_path)
          content = IO.read(source_source)
          content = replace_tokens(content, tokens)
          File.open(target_path, 'w') { |file| file.write content }
        end
      end

      private

      def establish_directory(full_path)
        return if File.exists?(full_path)
        parent_path = File.dirname(full_path)
        while (!File.exists?(parent_path))
          establish_directory(parent_path)
        end

        @logger.creating_directory(full_path)
        Dir.mkdir(full_path)
      end

      def replace_tokens(content, tokens)
        return content.gsub(/!-(\w+)-!/) do |value|
          token_name = value[2...-2]
          token = tokens[token_name] || tokens[token_name.to_sym] || "UNKNOWN TOKEN"
          token
        end
      end

    end

  end
end