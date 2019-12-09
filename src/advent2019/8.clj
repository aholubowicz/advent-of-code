(require '[clojure.string :as str])

(def in (slurp "src/advent2019/8.in"))
;(def in "123456789012")

(def layer
  (second
    (first
      (sort-by first
        (map #(vector (count (filter #{\0} %)) %) (partition (* 25 6) in))))))

(* (count (filter #{\1} layer)) (count (filter #{\2} layer)))
