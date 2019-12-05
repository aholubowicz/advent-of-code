(require '[clojure.string :as str])

(def in (slurp "src/advent2019/2.in"))

(def numbers (vec (->> (str/split in #",") (map read-string))))

;(def numbers [1,9,10,3,2,3,11,0,99,30,40,50])

(defn operation [v [a b c d]]
  (if (= 1 a)
    (assoc v d (+ (nth v b) (nth v c)))
    (if (= 2 a)
      (assoc v d (* (nth v b) (nth v c))))))

(defn result [numbers]
  (first
    (loop [pos 0
           nums numbers]
      (if (or
            (empty? (rest nums))
            (> pos (count nums))
            (= 99 (nth nums pos)))
        nums
        (do
          (recur (+ pos 4) (operation nums (subvec nums pos))))))))

(filter (comp not nil?)
  (for [x (range 100) y (range 100)]
    (if (= 19690720 (result (vec (assoc (assoc numbers 1 x) 2 y))))
      [x y])))

