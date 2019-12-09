(require '[clojure.string :as str])

(def in (slurp "src/advent2018/2.in"))

(def words (->> (str/split in #"\n")))
;(def words ["fghij" "fguij"])
;(def words ["abcde" "fghij" "klmno" "pqrst" "fguij" "axcye" "wvxyz"])

(count words)
(def word "abcde")

(def has-letter-occuring (fn [w n] (filter (fn [[k v]] (= v n)) (frequencies w))))
(has-letter-occuring word 3)

(count (filter #(seq (has-letter-occuring % 2)) words))

(* 32 248)


(defn without [w n] (str (subs w 0 n) (subs w (+ n 1) (count w))))
(defn list-without [w] (for [i (range 0 (count w))] (without w i)))

(list-without "abcde")
(without "abcd" 3)
(subs "abcd" 0 0)


(def pairs (for [x words y words :when (not= x y)] [x y]))
(def lists (map (fn [[x y]] [(list-without x) (list-without y)]) pairs))
(def lists (map (fn [[x y]] (map vector (list-without x) (list-without y))) pairs))

(filter seq (map (fn [pairs] (filter #(= (first %) (second %)) pairs)) lists))
