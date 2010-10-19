(ns limelight.test.casting-director
  (:use
    [limelight.casting-director]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "CastingDirector"
  (testing "lineage"
    (it "inherits from API" (some #{limelight.model.api.CastingDirector} (supers limelight.casting-director.CastingDirector))))
  (testing "construction"
    (given [director (limelight.casting-director.CastingDirector. :loader)]
      (it "set the loader" (= :loader (.loader director))))))

