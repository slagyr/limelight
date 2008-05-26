#- Copyright 2008 8th Light, Inc.
#- Limelight and all included source files are distributed under terms of the GNU LGPL.

module Copyrights

  ENDL = "\n"

  class Copyright

    attr_reader :file_ext, :text_filename, :comment_prefix

    def glob(dir)
      return Dir.glob(File.join(dir, "**", "*.#{file_ext}"))  
    end

    def text
      if @text.nil?
        @text = IO.read(File.join(File.expand_path(File.dirname(__FILE__)), text_filename)).strip       
      end
      return @text
    end

  end

  class Ruby < Copyright

    def initialize
      @file_ext = "rb"
      @text_filename = "ruby.txt"
      @comment_prefix = "#-"
    end

  end

  class Java < Copyright

    def initialize
      @file_ext = "java"
      @text_filename = "java.txt"
      @comment_prefix = "//-"
    end

  end

  class SourceFile

    attr_reader :copyright

    def initialize(filename, comment_prefix)
      @filename = filename
      @comment_prefix = comment_prefix
      @lines = IO.readlines(filename, ENDL)
      find_copyright
    end

    def has_copyright?
      @copyright.length > 0
    end

    def find_copyright
      @copyright = ''
      @lines.each do |line|
        if has_comment_prefix(line)
          @copyright << line
        else
          break
        end
      end
      @copyright.strip!
    end

    def has_comment_prefix(line)
      return false if line.nil?
      return line[0...@comment_prefix.length] == @comment_prefix
    end

    def remove_copyright
      while has_comment_prefix(@lines[0])
        @lines.shift
      end
    end

    def add_copyright(text)
      @lines.insert(0, ENDL) if @lines[0].to_s.strip.length > 0
      @lines.insert(0, text, ENDL)
    end

    def save!
      File.open(@filename, "w") do |file|
         @lines.each do |line|
           file.write line
         end
      end
    end
  end


  def self.add(lang_name, dir)
    lang = eval("#{lang_name}.new")
    lang.glob(dir).each do |filename|
      source_file = SourceFile.new(filename, lang.comment_prefix)
      if source_file.has_copyright?
        if source_file.copyright != lang.text
          puts "invalid copyright: #{filename}"
          source_file.remove_copyright
          source_file.add_copyright(lang.text)
          source_file.save!
        else
          puts "already has valid copyright: #{filename}"
        end
      else
        puts "missing copyright: #{filename}"
        source_file.add_copyright(lang.text)
        source_file.save!
      end
    end
  end

end