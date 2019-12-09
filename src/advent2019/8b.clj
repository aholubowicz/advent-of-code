(require '[clojure.string :as str])

(def in (slurp "src/advent2019/8.in"))
;(def in "0222112222120000")

;(def weight 2)
;(def height 2)
(def weight 25)
(def height 6)

(def layers-count (quot (count in) (* weight height)))

(def image
  (map first
      (map #(drop-while #{\2} %)
        (partition layers-count
                   (apply interleave (partition (* weight height) in))))))

(doseq [x (map #(apply str %)
  (partition weight (map #(if (= \0 %) \  \* ) image)))]
  (println x))
