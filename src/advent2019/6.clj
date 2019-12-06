(require '[clojure.string :as str])

(def in (slurp "src/advent2019/6.in"))
;(def in (slurp "src/advent2019/6small.in"))

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
              parent-orbit-count (:orbit-count (result parent))]
          (update-in result
                     [planet :orbit-count]
                     (constantly (inc parent-orbit-count))))
        (assoc m planet {:orbit-count 0})))))

(defn calc-orbits-counts [m]
  (reduce #(calc-orbits %1 %2) m (keys m)))

(apply + (map :orbit-count (vals (calc-orbits-counts (parents lines)))))

