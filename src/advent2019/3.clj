(require '[clojure.string :as str])

(def in (slurp "src/advent2019/3.in"))

(def in "R8,U5,L5,D3\nU7,R6,D4,L4")

(def lines (vec (->> (str/split in #"\n"))))

(defn parse-instruction [x]
  {:distance (read-string (subs x 1))
   :direction (subs x 0 1)})

(defn parse-line [l]
  (->> (str/split l #",") (map parse-instruction)))

(def moves1 (parse-line (lines 0)))

(defn next-point [p instruction]
  (case (:direction instruction)
    "L" {:x (- (:x p) (:distance instruction))
         :y (:y p)}
    "R" {:x (+ (:x p) (:distance instruction))
         :y (:y p)}
    "U" {:y (+ (:y p) (:distance instruction))
         :x (:x p)}
    "D" {:y (- (:y p) (:distance instruction))
         :x (:x p)}
  ))

;(next-point {:x 10 :y 2} {:distance 1 :direction "L"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "R"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "U"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "D"})

(loop [p {:x 0 :y 0}
       m moves1
       result []]
  (if (empty? m)
    result
    (let [next-p (next-point p (first m))]
      (recur next-p (rest m) (conj result next-p)))))

