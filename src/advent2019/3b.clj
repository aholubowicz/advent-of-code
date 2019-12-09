(require '[clojure.string :as str])

(def in (slurp "src/advent2019/3.in"))

;(def in "R8,U5,L5,D3\nU7,R6,D4,L4")
;(def in "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83")
;(def in "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

(def lines (vec (->> (str/split in #"\n"))))

(defn parse-instruction [x]
  {:distance  (read-string (subs x 1))
   :direction (subs x 0 1)})

(defn parse-line [l]
  (->> (str/split l #",") (map parse-instruction)))

(def moves1 (parse-line (lines 0)))
(def moves2 (parse-line (lines 1)))

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

(next-point {:x 10 :y 2} {:distance 1 :direction "L"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "R"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "U"})
;(next-point {:x 10 :y 2} {:distance 1 :direction "D"})

(defn points [moves]
  (loop [p {:x 0 :y 0}
         m moves
         result [{:x 0 :y 0}]]
    (if (empty? m)
      result
      (let [next-p (next-point p (first m))]
        (recur next-p (rest m) (conj result next-p))))))

(partition 2 1 (points moves1))
(partition 2 1 (points moves2))

(second (list 1 2 3))

(defn to-rect [line]
  {:x1 (min (:x (first line)) (:x (second line)))
   :y1 (min (:y (first line)) (:y (second line)))
   :x2 (max (:x (first line)) (:x (second line)))
   :y2 (max (:y (first line)) (:y (second line)))})

(to-rect '({:x 3, :y 5} {:y 2, :x 3}))
(to-rect '({:y 3, :x 6} {:x 2, :y 3}))
(to-rect '({:y 5, :x 8} {:x 3, :y 5}))
(to-rect '({:x 6, :y 7} {:y 3, :x 6}))

(defn x-intersect? [r1 r2]
  (let [left (if (<= (:x1 r1) (:x1 r2)) r1 r2)
        right (if (<= (:x1 r1) (:x1 r2)) r2 r1)]
    (if (<= (:x1 right) (:x2 left))
      (:x1 right))))
      ;[(:x1 right) (min (:x2 left) (:x1 right))])))

(defn y-intersect? [r1 r2]
  (let [left (if (<= (:y1 r1) (:y1 r2)) r1 r2)
        right (if (<= (:y1 r1) (:y1 r2)) r2 r1)]
    (if (<= (:y1 right) (:y2 left))
      (:y1 right))))

(x-intersect? {:x1 3, :y1 2, :x2 3, :y2 5} {:x1 2, :y1 3, :x2 6, :y2 3})
(x-intersect? {:x1 2, :y1 3, :x2 6, :y2 3} {:x1 3, :y1 2, :x2 3, :y2 5})
(y-intersect? {:x1 3, :y1 2, :x2 3, :y2 5} {:x1 2, :y1 3, :x2 6, :y2 3})
(y-intersect? {:x1 2, :y1 3, :x2 6, :y2 3} {:x1 3, :y1 2, :x2 3, :y2 5})

(x-intersect? {:x1 3, :y1 5, :x2 8, :y2 5} {:x1 6, :y1 3, :x2 6, :y2 7})
(x-intersect? {:x1 6, :y1 3, :x2 6, :y2 7} {:x1 3, :y1 5, :x2 8, :y2 5})
(y-intersect? {:x1 3, :y1 5, :x2 8, :y2 5} {:x1 6, :y1 3, :x2 6, :y2 7})
(y-intersect? {:x1 6, :y1 3, :x2 6, :y2 7} {:x1 3, :y1 5, :x2 8, :y2 5})

;(filter #(and (comp not nil) (first %))))
(defn intersections []
  (for [m1 (partition 2 1 (points moves1))
        m2 (partition 2 1 (points moves2))
        :let [x-int (x-intersect? (to-rect m1) (to-rect m2))
              y-int (y-intersect? (to-rect m1) (to-rect m2))]
        :when (and x-int y-int)]
    [x-int y-int]))

(defn abs [n] (max n (- n)))

(second (sort (map #(+ (abs (first %)) (abs (second %))) (intersections))))

