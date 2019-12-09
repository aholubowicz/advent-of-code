(require '[clojure.string :as str])

(def in (slurp "src/advent2018/4.in"))
;(def in (slurp "src/advent2018/4-small.in"))

(def lines (sort (->> (str/split in #"\n"))))

(def line (nth lines 1))

(defn guard-begins-line [line]
  (re-find #".*Guard #(.*) begins.*" line))

(defn parse [line]
  (let [guard (guard-begins-line line)
        wakes (re-find #".*:(.*)].*wakes.*" line)
        asleep (re-find #".*:(.*)].*asleep.*" line)]
    (cond
      guard (let [[_ n] guard] {:guard (Integer. n) :begins true})
      wakes (let [[_ n] wakes] {:wakes (Integer. n)})
      asleep (let [[_ n] asleep] {:asleep (Integer. n)}))))

(parse line)

(map parse lines)

;get rid of not sleeping guards)
(def lines (map first (filter #(not (and (guard-begins-line (first %)) (guard-begins-line (second %))))
                              (partition 2 1 (concat lines ["empty line"])))))

(def by-guard
  (map (fn [[k v]] [(first k) (filter #(not (:begins %)) (flatten v))])
    (group-by #(first %)
      (partition 2 (partition-by :guard (map parse lines))))))

(defn count-sleep [actions]
  (reduce
    (fn [s e] (+ s (- (:wakes (second e)) (:asleep (first e)))))
    0
    (partition 2 2 actions)))

(count-sleep '({:asleep 40} {:wakes 50} {:asleep 36} {:wakes 46} {:asleep 45} {:wakes 55}))


(defn get-time [e]
  (let [[_ v] (first e)] v))

(get-time {{:guard 10, :begins true} 50})

(sort-by get-time
  (map (fn [[k v]] {k (count-sleep v)}) by-guard))
;winner  {{:guard 421, :begins true} 495})

(def most-sleeping [{:asleep 20} {:wakes 37} {:asleep 15} {:wakes 30} {:asleep 36} {:wakes 40} {:asleep 46} {:wakes 53} {:asleep 35} {:wakes 43} {:asleep 47} {:wakes 57} {:asleep 27} {:wakes 54} {:asleep 27} {:wakes 28} {:asleep 15} {:wakes 49} {:asleep 55} {:wakes 58} {:asleep 11} {:wakes 30} {:asleep 7} {:wakes 24} {:asleep 37} {:wakes 44} {:asleep 20} {:wakes 51} {:asleep 8} {:wakes 28} {:asleep 36} {:wakes 44} {:asleep 21} {:wakes 57} {:asleep 9} {:wakes 51} {:asleep 55} {:wakes 57} {:asleep 8} {:wakes 40} {:asleep 31} {:wakes 35} {:asleep 12} {:wakes 58} {:asleep 22} {:wakes 52} {:asleep 2} {:wakes 34} {:asleep 38} {:wakes 48} {:asleep 17} {:wakes 50}])
(def most-sleeping [{:asleep 5} {:wakes 25} {:asleep 30} {:wakes 55} {:asleep 24} {:wakes 29}])
(def most-sleeping [{:asleep 40} {:wakes 50} {:asleep 36} {:wakes 46} {:asleep 45} {:wakes 55}])


(defn is-in [m r]
  (and
    (>= m (:asleep (first r)))
    (< m (:wakes (second r)))))

(is-in 36 '({:asleep 20} {:wakes 37}))

(defn overlaps [minute events]
  (count (filter (partial is-in minute) (partition 2 events))))

(overlaps 20 most-sleeping)

(defn best-minute [events]
  (last
   (sort-by second
     (for [m (range 0 60)] [m (overlaps m events)]))))

(best-minute most-sleeping)


(sort-by (comp second second)
  (map (fn [[k v]] [k (best-minute v)]) by-guard))
(* 421 27)
;11367

(* 1153 32)
