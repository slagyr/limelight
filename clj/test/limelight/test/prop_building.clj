(ns limelight.test.prop-building
  (:use
    [limelight.prop-building]
    [limelight.scene :only (new-scene)]
    [limelight.prop :only (children)]
    [lazytest.describe :only (describe testing it with given before using)]
    [lazytest.context :only (fn-context)]))

(describe "Illuminated prop-building"
  (given [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two))")
          _ (.setProduction @(.peer scene) (limelight.model.MockProduction. "Mock Production"))
          _ (.illuminate @(.peer scene))]
    (it "has a name"
      (= "one" (.getName @(.peer (first (children scene))))))
    (it "applies options"
      (= "Number ONE!" (.getText @(.peer (first (children scene))))))))

(describe "Building props"
  (testing "with no props"
    (given [scene (build-props (new-scene {}) "")]
      (it "has no children" (= 0 (count (children scene))))))

  (testing "with one prop"
    (given [scene (build-props (new-scene {}) "(! 'one)")]
      (it "has one child" (= 1 (count (children scene))))))

  (testing "with two prop"
    (given [scene (build-props (new-scene {}) "(! 'one)(! 'two)")]
      (it "has one child" (= 2 (count (children scene))))))

  (testing "nested props"
    (given [scene (build-props (new-scene {}) "(! 'one (! 'two))")]
      (it "has one child at each level"
        (and (= 1 (count (children scene)))
             (= 1 (count (children (first (children scene)))))))))

  (testing "with options and a child prop"
    (given [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two))")]
      (it "has one child at each level"
        (and (= 1 (count (children scene)))
             (= 1 (count (children (first (children scene)))))))))


  (testing "with two children"
    (given [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two) (! 'three))")]
      (it "has two children of the root prop"
        (= 2 (count (children (first (children scene))))))))
  )