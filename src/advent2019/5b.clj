(require '[clojure.string :as str])

(def in (slurp "src/advent2019/5.in"))
;(def in (slurp "src/advent2019/2.in"))

(def numbers (vec (->> (str/split in #",") (map read-string))))

;(def numbers [1,9,10,3,2,3,11,0,99,30,40,50])
;(def numbers [1,1,1,4,99,5,6,0,99])
;(def numbers [1002,4,3,4,33])
;(def numbers [1101,100,-1,4,0])
;(def numbers [3,0,4,0,99])

(defn make-computer [numbers input]
  {:memory (into {} (map-indexed #(vector %1 %2) numbers))
   :input  input
   :output []})

(def computer (make-computer numbers [1]))

(defn mem-value [memory mode pos]
  (case mode
    0 (memory (memory pos))
    1 (memory pos)))

;(mem-value {0 1, 1 9, 2 10, 3 3} 0 3)

(defn operation [{op :op modes :modes} pos c]
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
              #(conj % (mem-value (:memory c) 0 (+ 1 pos))))))



;(operation {:op 2, :modes '(0 0)} 0 computer)
;(def computer (make-computer [4,0,99] [7] ))
;(operation {:op 4, :modes '(1)} 0 computer)

(def num-of-params {1 3, 2 3, 3 1, 4 1, 99 0})

(defn parse-instruction [x]
  (let [op (mod x 100)
        modes (map #(mod % 10) (take (num-of-params op) (iterate #(quot % 10) (quot x 100))))]
    {:op op :modes modes}))

;(parse-instruction 2302)
;(parse-instruction 10002)

(defn calculate-pos [pos params-count]
  (+ pos params-count 1))

(defn compute [numbers input]
  (loop [pos 0
         c (make-computer numbers input)]
    (let [mem (:memory c)]
      (if (= 99 (mem pos))
        c
        (let [op (mem pos)
              instruction (parse-instruction op)
              params-count (count (:modes instruction))]
          (println pos instruction params-count)
          (recur
            (calculate-pos pos params-count)
            (operation instruction pos c)))))))

(def result (compute numbers [1]))
;(map second (sort-by first (:memory (compute numbers []))))
