(require '[clojure.string :as str])

(def in (slurp "src/advent2018/7.in"))
;(def in (slurp "src/advent2018/7-small.in"))

(def lines (->> (str/split in #"\n")))

(def line (nth lines 0))

(defn parse [line] (let [[_ a b] (re-find #"Step (.*) must.*step (.*) can" line)]
                     {:a a :b b}))

(parse line)
(def points (map parse lines))

(def graph
  (reduce (fn [g p] (update-in g [(:b p)] conj (:a p)))
          (reduce (fn [g p] (conj g [(:a p) []])) {} points)
          points))

(defn next-to-delete [graph]
  (first (first (sort-by first (filter (comp empty? second) graph)))))

(next-to-delete graph)

(defn delete [node graph]
  (map
    (fn [[k v]] [k (remove #{node} v)])
    (filter (fn [[k _]] (not= k node)) graph)))

(next-to-delete (delete "C" graph))
(delete "A" (delete "C" graph))


(loop [g graph
       s ""]
  (let [to-remove (next-to-delete g)]
    (if (empty? g)
      s
      (recur (delete to-remove g) (str s to-remove)))))

(next-to-delete {})


(nth [] -1)
