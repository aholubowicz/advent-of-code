(require '[clojure.string :as str])

(def in (slurp "src/advent2019/5.in"))

(def numbers (vec (->> (str/split in #",") (map read-string))))

;(def numbers [1,9,10,3,2,3,11,0,99,30,40,50])

(defn operation [instruction params nums dest]
  (if (= 1 a)
    (assoc nums d (+ (nth v b) (nth v c)))
    (if (= 2 nums)
      (assoc v d (* (nth v b) (nth v c))))))

(operation {:op 2, :modes (0 1)} [4 3] [1002,4,3,4] )


(defn parse-complex-instruction [x]
  {:op (mod x 100)
   :modes (reverse (map read-string (clojure.string/split (str (quot x 100)) #"")))}
  )

(reverse [1 2 3])

;(map read-string (clojure.string/split (str (quot 1102 100)) #""))
;(mod 1102 100)
;(read-string "1")

;(parse-complex-instruction 1102)
;(parse-complex-instruction 1002)

(defn parse-instruction [x]
  (cond
    (= x 99) {:op 99 :modes []}
    (= x 1) {:op 1 :modes [0 0]}
    (= x 2) {:op 2 :modes [0 0]}
    (= x 3) {:op 3 :modes [0]}
    (= x 4) {:op 4 :modes [0]}
    :else (parse-complex-instruction x)
    ))

(parse-instruction 1002)

(loop [pos 0
       nums numbers]
  (if (or
        (empty? (rest nums))
        (> pos (count nums))
        (= 99 (nth nums pos)))
    nums
    (let [op (first nums)
          number-of-params (count (:modes op))
          params (subvec nums 1 (inc number-of-params))]
      (recur
        (+ pos number-of-params)
        (operation (parse-instruction op) params nums)))))

(subvec [1 2 3 4 5 6] 2 (+ 2 3))
(nth [1 2 3 4 5 6] 4)

