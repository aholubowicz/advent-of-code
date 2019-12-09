(ns messagecentrereceiver.advent.6)

(require '[clojure.string :as str])

(def in (slurp "src/advent2018/6.in"))
(def in (slurp "src/advent2018/6-small.in"))

(def points (map
              #(map read-string (str/split % #", "))
              (str/split in #"\n")))

(def width (inc (apply max (map first points))))
(def height (inc (apply max (map second points))))
(def area (vec (map (fn [_ ] (vec (replicate width ""))) (range height))))

(def objs (map #(hash-map :x (first %) :y (second %) :n 0) points))
(def obj (nth objs 1))

(defn paint-obj [area obj]
  (let [prev (get-in area [(:y obj) (:x obj)])
        new (if (= prev (:n obj)) "." (:n obj))]
      (assoc-in area [(:y obj) (:x obj)] new)))

(paint-obj area obj)

(def painted (reduce paint-obj area objs))

(paint-obj painted {:y 6, :n 0, :x 1})
; malowanie kropki jesli wartosc jest ta sama


