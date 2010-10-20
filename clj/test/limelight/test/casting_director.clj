(ns limelight.test.casting-director
  (:use
    [limelight.casting-director]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.casting-director CastingDirector]))

(describe "CastingDirector"
  (testing "lineage"
    (it "inherits from API" (isa? CastingDirector limelight.model.api.CastingDirector)))
  (testing "construction"
    (given [director (CastingDirector. :loader)]
      (it "set the loader" (= :loader (.loader director))))))


