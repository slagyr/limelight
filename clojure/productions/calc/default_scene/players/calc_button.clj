(defn enter [lcd]
  (text= lcd (str (load-string (str (text lcd))))))

(defn press [prop lcd]
  (let [current-text (text lcd)
        middle-text (.substring current-text
      1 (- (.length current-text) 1))]
    (text= lcd (str "(" middle-text (text prop) ")"))))

(defn clear [lcd]
  (text= lcd "()"))

(on-mouse-clicked [e]
  (let [prop (.getProp e)
        lcd (find-by-id prop "lcd")
        button-text (text prop)]
    (cond
      (= "c" button-text)
      (clear lcd)
      (= "enter" button-text)
      (enter lcd)
      :else
      (press prop lcd))))

