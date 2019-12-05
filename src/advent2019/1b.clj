(require '[clojure.string :as str])

(def in (slurp "src/advent2019/1.in"))

(def numbers (->> (str/split in #"\n") (map read-string)))

(defn fuel [x] (- (quot x 3) 2))

(defn fuels [x] (take-while #(> %1 0) (rest (iterate fuel x))))

(defn total-fuel [x] (apply + (fuels x)))

(reduce #(+ %1 (total-fuel %2)) 0 numbers)



