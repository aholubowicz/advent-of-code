(require '[clojure.string :as str])

(def in (slurp "src/advent2019/1.in"))

(def numbers (->> (str/split in #"\n") (map read-string)))

(reduce #(+ %1 (- (quot %2 3) 2)) 0 numbers)
