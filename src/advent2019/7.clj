(require '[clojure.string :as str])
(require '[clojure.math.combinatorics :as combo])

(def in (slurp "src/advent2019/7.in"))

(def numbers (vec (->> (str/split in #",") (map read-string))))
;(def numbers [3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0])
;(def numbers [3,23,3,24,1002,24,10,24,1002,23,-1,23,
;              101,5,23,23,1,24,23,23,4,23,99,0,0))
;(def numbers [3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,
;              1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0))

(defn make-computer [numbers input]
  {:memory (into {} (map-indexed #(vector %1 %2) numbers))
   :input  input
   :output []})

(def computer (make-computer numbers [1]))

(defn mem-value [memory mode pos]
  (let [result (case mode
                 0 (memory (memory pos))
                 1 (memory pos))]
    (if (nil? result) 0 result)))

(defn update-memory [{op :op modes :modes} pos c]
  (case op
    1 (update c :memory
              #(assoc %
                 (mem-value % 1 (+ 3 pos))
                 (+ (mem-value % (nth modes 0) (+ pos 1))
                    (mem-value % (nth modes 1) (+ pos 2)))))
    2 (update c :memory
              #(assoc %
                 (mem-value % 1 (+ 3 pos))
                 (* (mem-value % (nth modes 0) (+ pos 1))
                    (mem-value % (nth modes 1) (+ pos 2)))))
    3 (update
        (update c :memory
                #(assoc %
                   (mem-value % 1 (+ 1 pos))
                   (first (:input c))))
        :input rest)
    4 (update c :output
                 #(conj % (mem-value (:memory c) (nth modes 0) (+ 1 pos))))
    7 (update c :memory
              #(if (< (mem-value % (nth modes 0) (+ pos 1)) (mem-value % (nth modes 1) (+ pos 2)))
                 (assoc % (mem-value % 1 (+ 3 pos)) 1)
                 (assoc % (mem-value % 1 (+ 3 pos)) 0)))
    8 (update c :memory
              #(if (= (mem-value % (nth modes 0) (+ pos 1)) (mem-value % (nth modes 1) (+ pos 2)))
                 (assoc % (mem-value % 1 (+ 3 pos)) 1)
                 (assoc % (mem-value % 1 (+ 3 pos)) 0)))
    c))

(defn operation [instruction pos c]
  (update-memory instruction pos c))


(def num-of-params {1 3, 2 3, 3 1, 4 1, 5 2, 6 2, 7 3, 8 3, 99 0})

(defn parse-instruction [x]
  (let [op (mod x 100)
        modes (map #(mod % 10) (take (num-of-params op) (iterate #(quot % 10) (quot x 100))))]
    {:op op :modes modes}))

(defn calculate-pos [{op :op modes :modes} pos params-count {memory :memory}]
  (case op
    5 (if (not= (mem-value memory (nth modes 0) (+ pos 1)) 0) (mem-value memory (nth modes 1) (+ 2 pos)) (+ 3 pos))
    6 (if (= (mem-value memory (nth modes 0) (+ pos 1)) 0) (mem-value memory (nth modes 1) (+ 2 pos)) (+ 3 pos))
    (+ pos params-count 1)))

(defn compute [numbers input]
  (loop [pos 0
         c (make-computer numbers input)]
    (let [mem (:memory c)]
      (if (= 99 (mem pos))
        c
        (let [op (mem pos)
              instruction (parse-instruction op)
              params-count (count (:modes instruction))]
          (recur
            (calculate-pos instruction pos params-count c)
            (operation instruction pos c)))))))

;(def result (compute numbers [3 0]))
;(println (:output result))

(defn get-output [in]
  (let [c in
        res1 (compute numbers [(get c 0) 0])
        ;_ (println (last res1))
        res2 (compute numbers [(get c 1) (last (:output res1))])
        res3 (compute numbers [(get c 2) (last (:output res2))])
        res4 (compute numbers [(get c 3) (last (:output res3))])
        res5 (compute numbers [(get c 4) (last (:output res4))])]
    (last (:output res5))))

;(get-output [0 1 2 3 4])
;(let [c [0 1 2 3 4]]
;  (println (get c 0))
;  (compute numbers [(get c 0) 0]))

(apply max (map get-output (combo/permutations [0 1 2 3 4])))
