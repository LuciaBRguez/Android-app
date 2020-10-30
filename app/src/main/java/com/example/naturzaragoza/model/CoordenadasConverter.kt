package com.example.naturzaragoza.model

import uk.me.jstott.jcoord.UTMRef

// Clase para pasar de coordenadas UTM a longitud y latitud
class CoordenadasConverter(val x: Double, val y: Double) {

    private val l: uk.me.jstott.jcoord.LatLng
    init {
        val coords: UTMRef = UTMRef(x, y, 'N', 30)
        l = coords.toLatLng()
    }

    public fun converterLatitud(): Double
    {
        return l.getLat();
    }

    public fun converterLongitud(): Double
    {
        return l.getLng();
    }
}