#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/util/map_hash'

describe Limelight::Util::MapHash do

  before do
    @map = Java::java.util.HashMap.new
    @hash = Limelight::Util::MapHash.new(@map)
  end

  it "puts entries" do
    @hash["one"] = 1

    @map.get("one").should == 1
  end

  it "puts symbols as strings" do
    @hash[:one] = 1

    @map.get("one").should == 1
  end

  it "gets entries" do
    @map.put("one", 1)

    @hash["one"].should == 1
  end

  it "gets symbold as strings" do
    @map.put("one", 1)

    @hash[:one].should == 1
  end

  it "can check equality" do
    @map.put("one", 1)

    (@hash == {"one" => 1}).should == true
    @hash.eql?({"one" => 1}).should == true
    (@hash == :blah).should == false
    @hash.eql?(:blah).should == false
    (@hash == {:two => 2}).should == false
    (@hash == {:one => 1, :two => 2}).should == false
    (@hash == {:one => 2}).should == false

#    ({:one => 1} == @hash).should == true
#    ({:one => 1}.eql?(@hash)).should == true
  end

  it "clears" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    @hash.clear

    @map.size.should == 0
  end

  it "has default" do
    @hash.default.should == nil

    @hash.default = "blah"

    @hash.default.should == "blah"
    @hash["foo"].should == "blah"
  end

  it "has no default_proc" do
    @hash.default_proc.should == nil
  end

  it "deletes elements" do
    @map.put("one", 1)

    result = @hash.delete(:one)

    result.should == 1
    @map.isEmpty.should == true
  end

  it "delete_if" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    @hash.delete_if { |key, value| value < 3 }

    @map.size.should == 1
    @map.get("three").should == 3
  end

  it "is enumerable" do
    @hash.is_a?(Enumerable).should == true
  end

  it "supports enumeration" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    keys = []
    values = []
    @hash.each do |key, value|
      keys << key
      values << value
    end

    keys.sort.should == ["one", "three", "two"]
    values.sort.should == [1, 2, 3]
  end

  it "supports handles each_key" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    keys = []
    @hash.each_key { |key| keys << key }

    keys.sort.should == ["one", "three", "two"]
  end

  it "supports handles each_key" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    values = []
    @hash.each_value { |value| values << value }

    values.sort.should == [1, 2, 3]
  end

  it "handles id empty" do
    @hash.empty?.should == true

    @map.put("one", 1)

    @hash.empty?.should == false
  end

  it "fetches values" do
    @map.put("one", 1)

    @hash.fetch("one").should == 1
    @hash.fetch("blah", "foo").should == "foo"
    @hash.fetch("blah") { |key| key }.should == "blah"
    lambda { @hash.fetch("blah") }.should raise_error(IndexError, "key not found")
  end

  it "has_key?" do
    @map.put("one", 1)

    @hash.has_key?("one").should == true
    @hash.has_key?(:one).should == true
    @hash.has_key?(:blah).should == false
  end

  it "has_value?" do
    @map.put("one", 1)

    @hash.has_value?(1).should == true
    @hash.value?(1).should == true
    @hash.has_value?(2).should == false
    @hash.value?(2).should == false
  end

  it "can get index" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    @hash.index(1).should == "one"
    @hash.index(2).should == "two"
    @hash.index(3).should == "three"
    @hash.index(4).should == nil
  end

  it "can invert" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    result = @hash.invert

    result.class.should == Limelight::Util::MapHash
    result.should_not be(@hash)
    result[1].should == "one"
    result[2].should == "two"
    result[3].should == "three"
  end

  it "handle key?, member?, include?" do
    @map.put("one", 1)

    @hash.key?("one").should == true
    @hash.key?(:blah).should == false
    @hash.member?("one").should == true
    @hash.member?(:blah).should == false
    @hash.include?("one").should == true
    @hash.include?(:blah).should == false
  end

  it "give keys" do
    @map.put("one", 1)

    @hash.keys.should == ["one"]
  end

  it "give length and size" do
    @hash.length.should == 0
    @hash.size.should == 0

    @map.put("one", 1)

    @hash.length.should == 1
    @hash.size.should == 1
  end

  it "can merge" do
    @map.put("one", "1")

    result = @hash.merge({:one => 1, :two => 2})

    result.class.should == Limelight::Util::MapHash
    result.should_not be(@hash)
    result["one"].should == 1
    result["two"].should == 2
  end

  it "can be merged" do
    @map.put("one", "1")

    result = {:one => 1, :two => 2}.merge(@hash)

    result.class.should == Hash
    result["one"].should == "1"
    result[:one].should == 1
    result[:two].should == 2
  end

  it "can merge!" do
    @map.put("one", "1")

    @hash.merge!({:one => 1, :two => 2})

    @hash["one"].should == 1
    @hash["two"].should == 2
  end

  it "can merge! using block" do
    @map.put("one", "1")

    @hash.merge!({:one => 1, :two => 2}) { |key, old, new| old }

    @hash["one"].should == "1"
    @hash["two"].should == 2
  end

  it "can be replaced" do
    @map.put("one", 1)

    @hash.replace({:two => 2, :three => 3})

    @map.contains_key?("one").should == false
    @map.get("two").should == 2
    @map.get("three").should == 3
  end

  it "selects" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    result = @hash.select { |key, value| value < 3 }
    result.sort { |a, b| a[0] <=> b[0] }

    result[0].should == ["two", 2]
    result[1].should == ["one", 1]
  end

  it "shifts" do
    @map.put("one", 1)
    @map.put("two", 2)

    result = @hash.shift
    key = result[0]
    if key == "one"
      result[1].should == 1
    else
      result[1].should == 2
    end

    @map.size.should == 1
    @map.contains_key(key).should == false
  end

  it "go to_hash" do
    @hash["one"] = 1

    result = @hash.to_hash

    result.class.should == Hash
    result["one"].should == 1
  end

  it "gets values" do
    @map.put("one", 1)

    @hash.values.should == [1]
  end

  it "gets values_at" do
    @map.put("one", 1)
    @map.put("two", 2)
    @map.put("three", 3)

    result = @hash.values_at("one", "three")

    result.should == [1, 3]
  end

#  ==, eql?, pretty_print, pretty_print_cycle, sort, to_a, to_s, to_yaml, yaml_initialize

end