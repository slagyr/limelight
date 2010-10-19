(ns limelight.test.production
  (:use
    [limelight.production]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Production"
  (testing "lineage"
    (it (some #{limelight.model.api.ProductionProxy} (supers limelight.production.Production))))

  (testing "attributes"
    (given [production (limelight.production.Production. :peer :theater :casting-director)]
      (it "has peer" (= :peer (.peer production)))
      (it "has casting director" (= :casting-director (.casting-director production)))
      (it "has theater" (= :theater (.theater production))))))

(describe "Building a new production"
  (given [production (new-production :peer)]
    (it "have the peer" (= :peer (.peer production)))
    (it "has a real theater" (= limelight.theater.Theater (type (.theater production))))
    (it "has a real casting director" (= limelight.casting-director.CastingDirector (type (.casting-director production))))))