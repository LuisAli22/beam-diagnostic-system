(ns beam-diagnostic-system.model.rules)

(defn rVerifAltura [alturaMinima alturaViga] (
	if (<= alturaMinima alturaViga) true false
	))

(defn rVerifLimiteSeccionMinima [seccionMinima limiteSeccionMinima] (
	if (>= seccionMinima limiteSeccionMinima) true false
	))

(defn rVerifSeccion [seccionMinima limiteSeccionMinima seccionAcero] (
	if (<= seccionMinima seccionAcero) 
		(rVerifLimiteSeccionMinima seccionMinima limiteSeccionMinima)
		false
	))

(defn rVerifCorte [capacidaCorte corteUltimo] (
	if (>= capacidaCorte corteUltimo) true false
	))

(defn rVerifFlexion [capacidadFlexion momentoUltimo] (
	if (>= capacidadFlexion momentoUltimo) true false
	))

(defn rVerifReqDis[attrs params] (
	if (rVerifAltura (attrs "alturaMinima") (params "altura"))
		(if (rVerifSeccion (attrs "seccionMinima") (attrs "limiteSeccionMinima") (params "areaAcero"))
			(if (rVerifCorte (attrs "capacidadCorte") (params "corteUltimo"))
				(if (rVerifFlexion (attrs "capacidadFlexion") (params "momentoUltimo")) true false)
				false)
			false)
		false
	))


