;- Copyright © 2008-2011 8th Light, Inc. All Rights Reserved.
;- Limelight and all included source files are distributed under terms of the MIT License.

(ns limelight.clojure.style-building
  (:use
    [limelight.clojure.util :only (read-src ->options)]))

;(declare *styles*)
;(declare *extendable-styles*)
;
;(defn find-or-create [name]
;  (or (@*styles* name)
;    (limelight.styles.RichStyle.)))
;
;(defn write-to-map [name style]
;  (swap! *styles* assoc name style))
;
;(defn- extract-calls [body]
;  (loop [calls [] body body]
;    (if (list? (first body))
;      (recur (conj calls (first body)) (rest body))
;      [body calls])))
;
;(defn- extract-attributes [body]
;  (loop [attrs {} body body]
;    (cond
;      (not (seq body)) attrs
;      (map? (first body)) (recur (merge attrs (first body)) (rest body))
;      :else (merge attrs (apply hash-map body)))))
;
;(defn extends [style & ext-names]
;  (doseq [ext-name (map name ext-names)]
;    (let [extension (or (@*styles* ext-name) (*extendable-styles* ext-name))]
;      (if extension
;        (.addExtension style extension)
;        (throw (limelight.LimelightException. (format "Can't extend missing style: '%s'" ext-name))))))
;  style)
;
;(defn- extract-hover-style [name attributes]
;  (if-let [hover-attrs (:hover attributes)]
;    (let [hover-name (str name ".hover")
;          style (find-or-create hover-name)]
;      (limelight.util.Options/apply style (limelight.util.Opts. hover-attrs))
;      (write-to-map hover-name style)))
;  (dissoc attributes :hover))
;
;(defmacro style [name & body]
;  (let [[body calls] (extract-calls body)
;        attributes (extract-attributes body)]
;    `(list
;       (let [name# (clojure.core/name ~name)
;             style# (find-or-create name#)
;             attributes# (extract-hover-style name# ~attributes)]
;         (-> style# ~@calls)
;         (limelight.util.Options/apply style# (limelight.util.Opts. attributes#))
;         (write-to-map name# style#)))))
(declare ->style)

(defn- src->data [src path ns]
  (binding [*ns* ns]
    (read-src path (str "[" src "]"))))

(defn- apply-extensions [extendable-styles styles style attributes]
  (if-let [extensions (:extends attributes)]
    (doseq [ext-name (map name (flatten (vector extensions)))]
      (if-let [extension (or (styles ext-name) (extendable-styles ext-name))]
        (.addExtension style extension)
        (throw (limelight.LimelightException. (format "Can't extend missing style: '%s'" ext-name))))))
  (dissoc attributes :extends))

(defn- apply-hover-style [extendable-styles styles name attributes]
  [(dissoc attributes :hover)
   (if-let [hover-attributes (:hover attributes)]
     (->style extendable-styles styles [(str name ".hover") hover-attributes])
     styles)])

(defn- ->style [extendable-styles styles [name & options]]
  (let [name (clojure.core/name name)
        style (or (get styles name) (limelight.styles.RichStyle.))
        attributes (->options options)
        attributes (apply-extensions extendable-styles styles style attributes)
        [attributes styles] (apply-hover-style extendable-styles styles name attributes)]
    (limelight.util.Options/apply style (limelight.util.Opts. attributes))
    (assoc styles name style)))

(defn build-styles
  ([styles src path ns] (build-styles styles src path ns {}))
  ([styles src path ns extendable-styles]
    (let [data (src->data src path ns)]
      (reduce (partial ->style extendable-styles) styles data))))
