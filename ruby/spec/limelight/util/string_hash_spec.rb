#- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
#- Limelight and all included source files are distributed under terms of the MIT License.

require File.expand_path(File.dirname(__FILE__) + "/../../spec_helper")
require 'limelight/util/string_hash'

describe Limelight::Util::StringHash do

  before do
    @hash = Limelight::Util::StringHash.new
  end

  it "behaves like a hash" do
    @hash["one"] = 1
    @hash["two"] = 2
    @hash["three"] = 3

    @hash["one"].should == 1
    @hash["two"].should == 2
    @hash["three"].should == 3
  end

  it "store keys as strings" do
    @hash[:one] = 1
    @hash[:two] = 2
    @hash[:three] = 3

    @hash.keys.include?("one").should == true
    @hash.keys.include?("two").should == true
    @hash.keys.include?("three").should == true
  end

  it "retrieves symbol keys as strings" do
    @hash[:one] = 1
    @hash[:two] = 2
    @hash[:three] = 3

    @hash["one"].should == 1
    @hash["two"].should == 2
    @hash["three"].should == 3
  end

  it "retrieves symbol keys as symbols" do
    @hash[:one] = 1
    @hash[:two] = 2
    @hash[:three] = 3

    @hash[:one].should == 1
    @hash[:two].should == 2
    @hash[:three].should == 3
  end

  it "converts keys to string when updating" do
    @hash.update({:one => 1, :two => 2, :three => 3})

    @hash["one"].should == 1
    @hash["two"].should == 2
    @hash["three"].should == 3
  end

  it "converts keys to string when merging" do
    @hash.merge!({:one => 1, :two => 2, :three => 3})

    @hash["one"].should == 1
    @hash["two"].should == 2
    @hash["three"].should == 3
  end

  it "converts keys to string when merging into new hash" do
    @hash[:original] = "original"
    result = @hash.merge({:one => 1, :two => 2, :three => 3})

    result.class.should == Limelight::Util::StringHash
    result["original"].should == "original"
    result["one"].should == 1
    result["two"].should == 2
    result["three"].should == 3
  end

  it "handles key?" do
    @hash[:one] = 1

    @hash.key?(:one).should == true
    @hash.key?("one").should == true
    @hash.key?("blah").should == false
  end

  it "handles fetch" do
    @hash[:one] = 1

    @hash.fetch(:one).should == 1
    @hash.fetch("one").should == 1
    lambda { @hash.fetch("blah") }.should raise_error
  end

  it "converts keys to strings on delete" do
    @hash[:one] = 1

    @hash.delete(:one).should == 1

    @hash["one"].should == nil
    @hash.keys.empty?.should == true
  end


end