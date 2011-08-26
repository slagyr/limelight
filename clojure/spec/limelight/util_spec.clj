;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.util-spec
  (:use
    [speclj.core]
    [limelight.util])
  (:import
    [limelight.clojure ProxyMap]
    [limelight.util OptionsMap]))

(describe "Limelight Util"

  (context "maps"

    (it "creates a map for Clojure"
      (should= {} (map-for-clojure nil))
      (should= ProxyMap (class (map-for-clojure (ProxyMap.))))
      (should= :foo (map-for-clojure :foo)))

    (it "creates a map for Clojure from java Map"
      (let [jmap (OptionsMap.)
            result (map-for-clojure jmap)]
        (should= ProxyMap (class result))
        (should= jmap (.getMap result))))

    (it "creates a map for Java"
      (should= OptionsMap (class (map-for-java nil)))
      (should= OptionsMap (class (map-for-java (OptionsMap.)))))

    (it "uses the wrapped map from ProxyMap for Java"
      (let [jmap (OptionsMap.)
            pmap (ProxyMap. jmap)]
        (should-be-same jmap (map-for-java pmap))))

    (it "convers a clojure map to Java"
      (let [cmap (hash-map :foo "bar" "fizz" "bang")
            result (map-for-java cmap)]
        (should= OptionsMap (class result))
        (should= "bar" (.get result "foo"))
        (should= "bang" (.get result "fizz"))))
    )
  )
(run-specs)

