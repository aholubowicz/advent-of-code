(require '[clojure.string :as str])

(def in (slurp "src/advent2019/6.in"))
;(def in (slurp "src/advent2019/6bsmall.in"))

(def lines (vec (->> (str/split in #"\n"))))

(defn parse-orbit [con]
  (clojure.string/split con #"\)"))

(defn add-parent [m [parent planet]]
  (assoc m planet {:parent parent}))

(defn parents [lines]
  (reduce add-parent {} (map parse-orbit lines)))

(defn calc-orbits [m planet]
  (let [parent (:parent (m planet))
        orbit-count (:orbit-count (m planet))]
    (if orbit-count
      m
      (if parent
        (let [result (calc-orbits m parent)
              parent-orbit-count (:orbit-count (result parent))
              parent-parents (:parents (result parent))]
          (update-in
            (update-in result
                       [planet :orbit-count]
                       (constantly (inc parent-orbit-count)))
            [planet :parents]
            (constantly (conj parent-parents parent)) ))
        (assoc m planet {:orbit-count 0 :parents []})))))

(defn build-graph []
  (let [m (parents lines)]
    (reduce #(calc-orbits %1 %2) m (keys m))))

(let [m (build-graph)
      planet1 (m "YOU")
      planet2 (m "SAN")
      common-parents (clojure.set/intersection
                       (set (:parents planet1))
                       (set (:parents planet2)))
      max-common-orbit (apply max (map #(:orbit-count (m %)) common-parents))]
  (+
    (- (:orbit-count planet1) max-common-orbit)
    (- (:orbit-count planet2) max-common-orbit)
    -2))
