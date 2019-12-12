(require '[clojure.string :as str])

(def in (slurp "src/advent2019/5.in"))
;(def in (slurp "src/advent2019/2.in"))

(def numbers (vec (->> (str/split in #",") (map read-string))))

;(def numbers [1,9,10,3,2,3,11,0,99,30,40,50])
;(def numbers [1,1,1,4,99,5,6,0,99])
;(def numbers [1002,4,3,4,33])
;(def numbers [1101,100,-1,4,0])
;(def numbers [3,0,4,0,99])
;(def numbers [3,9,8,9,10,9,4,9,99,-1,8])
;(def numbers [3,9,7,9,10,9,4,9,99,-1,8])
;(def numbers [3,3,1108,-1,8,3,4,3,99])
;(def numbers [3,3,1107,-1,8,3,4,3,99])
;(def numbers [3,12,6,12,15,1,13,14,13,4,13,99,-1,0,1,9])
;(def numbers [3,3,1105,-1,9,1101,0,0,12,4,12,99,1])
;(def numbers [3,21,1008,21,8,20,1005,20,22,107,8,21,20,1006,20,31,
;              1106,0,36,98,0,0,1002,21,125,20,4,20,1105,1,46,104,
;              999,1105,1,46,1101,1000,1,20,4,20,1105,1,46,98,99))

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
    4  (do
         (println "robie 4")
         ;(println pos)
         ;(println  ((:memory c) 32))
         (println (mem-value (:memory c) 1 (+ 1 pos)))
         (update c :output
                 #(conj % (mem-value (:memory c) (nth modes 0) (+ 1 pos)))))
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


;(operation {:op 2, :modes '(0 0)} 0 computer)
;(def computer (make-computer [8,2,2,0] [7]))
;(operation {:op 5, :modes '(1 1 0)} 0 computer)

(def num-of-params {1 3, 2 3, 3 1, 4 1, 5 2, 6 2, 7 3, 8 3, 99 0})

(defn parse-instruction [x]
  (let [op (mod x 100)
        modes (map #(mod % 10) (take (num-of-params op) (iterate #(quot % 10) (quot x 100))))]
    {:op op :modes modes}))

;(parse-instruction 2302)
;(parse-instruction 10002)
;(parse-instruction 1107)

(defn calculate-pos [{op :op modes :modes} pos params-count {memory :memory}]
  (case op
    5 (if (not= (mem-value memory (nth modes 0) (+ pos 1)) 0) (mem-value memory (nth modes 1) (+ 2 pos)) (+ 3 pos))
    6 (if (= (mem-value memory (nth modes 0) (+ pos 1)) 0) (mem-value memory (nth modes 1) (+ 2 pos)) (+ 3 pos))
    (+ pos params-count 1)))


(def computer (make-computer [5,0,3,99] [7]))
(calculate-pos {:op 5, :modes '(1)} 0 1 computer)

(defn compute [numbers input]
  (loop [pos 0
         c (make-computer numbers input)]
    (let [mem (:memory c)]
      (if (= 99 (mem pos))
        c
        (let [op (mem pos)
              instruction (parse-instruction op)
              ;_ (println pos)
              ;_ (println (mem 32))
              ;_ (println instruction)
              params-count (count (:modes instruction))]
          ;(println pos instruction params-count)
          (recur
            (calculate-pos instruction pos params-count c)
            (operation instruction pos c)))))))

(def result (compute numbers [5]))
(println (:output result))
;14195011

;(map second (sort-by first (:memory (compute numbers []))))
