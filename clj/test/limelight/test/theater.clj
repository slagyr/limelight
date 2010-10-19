(ns limelight.test.theater
  (:use
    [limelight.theater]
    [lazytest.describe :only (describe testing it with given)]
    [lazytest.context :only (fn-context)]))

(describe "Theater"
  (testing "lineage"
    (it (some #{limelight.model.api.TheaterProxy} (supers limelight.theater.Theater))))
  (testing "construction"
    (given [theater (limelight.theater.Theater. :peer :production)]
      (it "sets the peer" (= :peer (.peer theater)))
      (it "sets the production" (= :production (.production theater))))))


