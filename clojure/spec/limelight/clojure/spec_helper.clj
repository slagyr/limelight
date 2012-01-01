;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.spec-helper)

(def default-boot-options {"startBackgroundThreads" false})

(defn boot-limelight
  ([] (boot-limelight {}))
  ([options]
    (limelight.Boot/reset)
    (limelight.Boot/boot (merge default-boot-options options))))

(defmacro unless-headless [& body]
  (if (not (java.awt.GraphicsEnvironment/isHeadless))
    `(list ~@body)
    []))

(when (get (System/getenv) "HEADLESS")
  (System/setProperty "java.awt.headless" "true"))

(boot-limelight)

