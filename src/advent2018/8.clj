(require '[clojure.string :as str])

(def in (slurp "src/advent2018/8.in"))

(def numbers (map read-string (str/split in #" ")))
(def numbers [2 3 0 3 10 11 12 1 1 0 1 99 2 1 1 2])
(def numbers [0 3 10 11 12 1 1 0 1 99 2 1 1 2])
(def numbers [1 1 0 1 99 2])
(def numbers [1 0 0 1 2])
(def numbers [0 1 99])
(def numbers [3 3 0 1 10 0 1 20 0 1 30 1 2 3])

(defn dive [{:keys [rest sum]}]
  (let [[n m & r] rest]
    (println "*" n m (take 20 r))
    (if (nil? n)
      {:rest [] :sum sum}
      (if (zero? n)
         (do
           (println "zero " n m)
           {:sum (+ sum (reduce + (take m r))) :rest (drop (+ 2 n m) rest)})

         (do (println "iterating")
           (let [iterated (iterate dive {:rest r :sum sum})
                 reduced (last (take (inc n) iterated))]
             (println "n m" n m)
             ;(println "iterated" (take 5 iterated))
             (println "reduced" (:sum reduced) (take 20 (:rest reduced)))
             (println "result " {:rest [] :sum (+ (:sum reduced) (reduce + (:rest reduced)))})
             {:rest (drop m (:rest reduced)) :sum (+ (:sum reduced) (reduce + (take m (:rest reduced))))}))))))

(defn dive [{:keys [rest sum]}]
  (let [[n m & r] rest]
    (println "*" n m (take 20 r))
    (if (nil? n)
      {:rest [] :sum sum}
      (if (zero? n)
         (do
           (println "zero " n m)
           {:sum (+ sum (reduce + (take m r))) :rest (drop (+ 2 n m) rest)})

         (do (println "reducing")
             (let [reduced (reduce
                             (fn [s _] (dive s))
                             {:rest r :sum sum}
                             (range n))]
               (println "reduced" (:sum reduced) (count (:rest reduced)) (take 20 (:rest reduced)))
               {:rest (drop m (:rest reduced)) :sum (+ (:sum reduced) (reduce + (take m (:rest reduced))))}))))))

(defn dive [{:keys [rest sum]}]
  (let [[n m & r] rest]
    (println "*" n m (take 20 r))
    (if (nil? n)
      {:rest [] :sum sum}
      (if (zero? n)
         (do
           (println "zero " n m)
           {:sum (+ sum (reduce + (take m r))) :rest (drop (+ 2 n m) rest)})

         (do (println "reducing")
             (let [reduced-nodes (reduce
                                   (fn [s i] (assoc s (inc i) (dive (s i))))
                                   {0 {:rest r :sum 0}}
                                   (range n))
                   last-reduced (reduced-nodes n)
                   metadata (take m (:rest last-reduced))]
               ;(println "nodes" reduced-nodes)
               (println "metadata" metadata)
               {:rest (drop m (:rest last-reduced))
                :sum (reduce
                       #(+' %1
                           (if (and (> %2 0) (<= %2 n))
                             (do (println "tak")
                               (:sum (reduced-nodes %2)))
                             (do (println "nie") 0)))
                       0
                       metadata)}))))))

(dive {:rest numbers :sum 0})
(take 20 [])
(defn a [{:keys [a b]}] [a b])
(a {:a 1 :b 2})

(< 2 1 3)

(let [[n m & r] [1]] [n m r])

(take-while pos? [1 2 3 -1])

;zle 56119
