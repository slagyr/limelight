(ns limelight.style-building
  (:use
    [limelight.util :only (read-src)]))

(declare *styles*)

(defn find-or-create [name]
  (or (@*styles* name)
      (limelight.styles.RichStyle.)))

(defn write-to-map [name style]
  (swap! *styles* assoc name style))

(defn style [namable & attributes]
  (let [name (clojure.core/name namable)
        attributes (if (and (= 1 (count attributes)) (map? (first attributes))) (first attributes) (apply hash-map attributes))
        style (find-or-create name)]
    (limelight.util.Options/apply style (limelight.util.OptionsMap. attributes))
    (write-to-map name style)))

(defn build-styles [styles src path]
   (binding [*ns* (the-ns 'limelight.style-building)
             *styles* (atom styles)]
    (read-src path src)
    @*styles*))
