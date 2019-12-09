(require '[clojure.string :as str])

(def in (slurp "src/advent2018/1.in"))

(def numbers (->> (str/split in #"\n") (map read-string)))
(def numbers [1 2])
(def numbers (concat numbers numbers))


(def sums (reduce (fn [s n] (conj s (+ (or (last s) 0) n))) [] numbers))

(:found (reduce
          (fn [s n]
            (if (:found s)
              s
              (if (contains? (:visited s) n)
                (assoc-in s [:found] n)
                (assoc-in s [:visited] (conj (:visited s) n)))))
          {:visited #{0} :found nil}
          sums))

(println "done")
