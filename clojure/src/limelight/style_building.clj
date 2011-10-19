;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.style-building
  (:use
    [limelight.util :only (read-src)]))

(declare *styles*)
(declare *extendable-styles*)

(defn find-or-create [name]
  (or (@*styles* name)
    (limelight.styles.RichStyle.)))

(defn write-to-map [name style]
  (swap! *styles* assoc name style))

(defn- extract-calls [body]
  (loop [calls [] body body]
    (if (list? (first body))
      (recur (conj calls (first body)) (rest body))
      [body calls])))

(defn- extract-attributes [body]
  (loop [attrs {} body body]
    (cond
      (not (seq body)) attrs
      (map? (first body)) (recur (merge attrs (first body)) (rest body))
      :else (merge attrs (apply hash-map body)))))

(defn extends [style & ext-names]
  (doseq [ext-name ext-names]
    (let [ext-name (name ext-name)
          extension (or (@*styles* ext-name) (*extendable-styles* ext-name))]
      (if extension
        (.addExtension style extension)
        (throw (limelight.LimelightException. (format "Can't extend missing style: '%s'" (name ext-name)))))))
  style)

(defmacro style [name & body]
  (let [[body calls] (extract-calls body)
        attributes (extract-attributes body)]
    `(list
       (let [name# (clojure.core/name ~name)
             style# (find-or-create name#)]
         (-> style# ~@calls)
         (limelight.util.Options/apply style# (limelight.util.Opts. ~attributes))
         (write-to-map name# style#)))))

(defn build-styles
  ([styles src path] (build-styles styles src path {}))
  ([styles src path extendable-styles]
    (binding [*ns* (the-ns 'limelight.style-building)
              *styles* (atom styles)
              *extendable-styles* extendable-styles]
      (read-src path src)
      @*styles*)))
