(ns beam-diagnostic-system.util.miMath)

(defn abs 
	"calcula el valor absoluto de un numero"
	[n]
	(if (< n 0)
		(* -1 n)
		n))

(defn avg
	"retorna el promedio de dos numeros"
	[a b]
	(/ (+ a b) 2))


(defn good-enough?
	"Tests if a guess is close enough to the real square root"
	[number guess]
	(let [diff (- (* guess guess) number)]
		(if (< (abs diff) 0.001)
		true
		false)))

(defn sqrt 
	"devuelve la raiz cuadrada"
	([number] (sqrt number 1.0))
	([number guess]
		(if (good-enough? number guess)
			guess
			(sqrt number (avg guess (/ number guess))))))