(require '[clojure.string :as str])

(def in (slurp "src/advent2019/3.in"))

;(def in "R8,U5,L5,D3\nU7,R6,D4,L4")
;(def in "R8\nU7,R8,D7")
;(def in "R75,D30,R83,U83,L12,D49,R71,U7,L72\nU62,R66,U55,R34,D71,R55,D58,R83")
(def in "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51\nU98,R91,D20,R16,D67,R40,U7,R15,U6,R7")

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

(defn denormalize [x]
  (map #(update-in % [:distance] (constantly 1)) (repeat (:distance x) x)))

;(denormalize {:distance 3, :direction "R"})

(defn path [m]
  (reduce (fn [x y] (conj x (next-point (last x) y))) [{:x 0 :y 0}]
    (mapcat denormalize m)))

(def path1 (path moves1))
(def path2 (path moves2))

(defn extend-path [path1 path2]
  (concat path1 (repeat (- (count path2) (count path1)) (last path1))))

(extend-path path1 path2)

(def intersections
  (filter #(= (first (second %)) (second (second %)))
    (map-indexed #(vector %1 %2)
      (partition 2 (interleave (extend-path path1 path2) (extend-path path2 path1))))))

(second intersections)
(count (extend-path path1 path2))
(count (extend-path path2 path1))
(count path1)
(count path2)

(nth (path moves1) 15)
(nth (path moves2) 15)

(map vector [1 2 3 4] [10 11])
