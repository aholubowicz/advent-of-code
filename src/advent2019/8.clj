(require '[clojure.string :as str])

(def in (slurp "src/advent2019/8.in"))
(def in "003456789012")

;(partition (* 3 2) in)

;(filter #{\0} (list \1 \2 \3 \4 \0 \0))
;(count (filter #{\0} (list \7 \8 \9 \0 \1 \2)))

(def layer
  (second
    (first
      (sort-by first
        (map #(vector (count (filter #{\0} %)) %) (partition (* 3 2) in))))))

(* (count (filter #{\0} layer)) (count (filter #{\1} layer)))
