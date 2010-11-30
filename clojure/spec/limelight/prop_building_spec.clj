(ns limelight.prop-building-spec
  (:use
    [speclj.core]
    [limelight.common]
    [limelight.prop-building]
    [limelight.scene :only (new-scene)]
    [limelight.production :only (new-production)]))

(describe "prop-building"

  (it "builds with no props"
    (let [scene (build-props (new-scene {}) "")]
      (should= 0 (count (child-props scene)))))

  (it "with one prop"
    (let [scene (build-props (new-scene {}) "(! 'one)")]
      (should= 1 (count (child-props scene)))))

  (it "with two prop"
    (let [scene (build-props (new-scene {}) "(! 'one)(! 'two)")]
      (should= 2 (count (child-props scene)))))

  (it "nested props"
    (let [scene (build-props (new-scene {}) "(! 'one (! 'two))")]
      (should= 1 (count (child-props scene)))
      (should= 1 (count (child-props (first (child-props scene)))))))

  (it "with options and a child prop"
    (let [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two))")]
      (should= 1 (count (child-props scene)))
      (should= 1 (count (child-props (first (child-props scene)))))))

  (it "with two children"
    (let [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two) (! 'three))")]
      (should= 2 (count (child-props (first (child-props scene)))))))

  (it "with illumination"
    (let [scene (build-props (new-scene {}) "(! 'one {:text \"Number ONE!\"} (! 'two))")
          _ (.setProduction @(.peer scene) (.peer (new-production (limelight.model.FakeProduction. "Mock Production"))))
          _ (.illuminate @(.peer scene))]
      (should= "one" (.getName @(.peer (first (child-props scene)))))
      (should= "Number ONE!" (.getText @(.peer (first (child-props scene)))))))
  )
