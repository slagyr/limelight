(ns limelight.test.prop
  (:use
    [limelight.prop]
    [lazytest.describe :only (describe testing it with)]
    [lazytest.context :only (fn-context)]))

(describe "creating a Prop"
  (it (some #{limelight.model.api.PropProxy} (supers limelight.prop.Prop))))


