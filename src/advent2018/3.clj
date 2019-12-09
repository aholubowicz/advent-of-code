(require '[clojure.string :as str])

(def in (slurp "src/advent2018/3.in"))

(def lines (->> (str/split in #"\n")))

(def line (get lines 0))

(defn line->square [line] (let [[_ n x y a b] (re-find #"#(.*) @ (.*),(.*): (.*)x(.*)" line)]
                            {:n (read-string n) :x (read-string x) :y (read-string y) :a (read-string a) :b (read-string b)}))

(line->square line)

(def squares (map line->square lines))
(def square {:n 9 :x 1 :y 2 :a 2 :b 2})
(def squares [{:n 9 :x 2 :y 2 :a 2 :b 2} {:n 8 :x 0 :y 0 :a 2 :b 2}])

(def area-size 2000)
(def area (vec (map (fn [_ ] (vec (replicate area-size 0))) (range area-size))))


(defn update-field [a x y nv] (update-in a [x y] (fn [v] (if (= v 0) nv "x"))))
(update-field (update-field area 1 2 1) 1 3 2)

(defn fields [square]
  (for [x (range (:x square) (+ (:x square) (:a square)))
        y (range (:y square) (+ (:y square) (:b square)))] {:x x :y y :n (:n square)}))

(fields square)

(defn paint [area square]
  (reduce #(update-field %1 (:x %2) (:y %2) (:n %2)) area (fields square)))

(def painted-area (reduce #(paint %1 %2) area squares))

(defn count-value [row value] (count (filter #(= % value) row)))
(count-value [0 0 "x" "x" 0] "x")
(reduce #(+ %1 (count-value %2 "x")) 0 [[0 0 "x" "x" 0] [0 0 "x" "x" 0]])

(defn count-values [painted-area v]
  (reduce #(+ %1 (count-value %2 v)) 0 painted-area))

(count-values painted-area 8)

(filter #(not= % 0)
        (map #(if
                (= (count-values painted-area (:n %)) (* (:a %) (:b %)))
                (:n %) 0) squares))

