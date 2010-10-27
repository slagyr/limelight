(ns limelight.production-test
  (:use
    [limelight.production]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)])
  (:import [limelight.production Production]
           [limelight.casting-director CastingDirector]))

(describe "Production"
  (testing "lineage"
    (it (isa? Production limelight.model.api.ProductionProxy)))

  (testing "attributes"
    (given [production (Production. :peer :theater :casting-director)]
      (it "has peer" (= :peer (.peer production)))
      (it "has casting director" (= :casting-director (.casting-director production)))
      (it "has theater" (= :theater (.theater production))))))

(describe "Building a new production"
  (given [peer-production (limelight.model.FakeProduction. "Mock")
          production (new-production peer-production)]
    (testing "the peer"
      (it "is connected" (= peer-production (.peer production)))
      (it "has reverse connection" (= production (.getProxy (.peer production)))))
    (testing "creates a theater"
      (it "implementing the Theater API" (= limelight.theater.Theater (type @(.theater production))))
      (it "with the peer theater" (= (.getTheater peer-production) (.peer @(.theater production))))
      (it "with the production" (= production (.production @(.theater production)))))
    (testing "creates a casting director"
      (it "implementing the CastingDirector API" (= CastingDirector (type (.casting-director production)))))))

(describe "Loading stages"
  (given [loader (limelight.util.MockResourceLoader.)
          _ (set! (.readTextResult loader) "(stage \"One\" {})")
          peer-production (limelight.model.FakeProduction. "Mock" loader)
          theater (.getTheater peer-production)
          production (new-production peer-production)
          _ (.loadStages production)]
    (it "adds stages"
      (= 1 (count (.getStages theater))))
    (it "uses stages.clj as the path to read"
      (= "stages.clj" (.pathToReadText loader)))))