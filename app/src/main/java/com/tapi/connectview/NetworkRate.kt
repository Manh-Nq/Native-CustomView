package com.tapi.connectview

data class NetworkRate(val percent: Float, val rate: Float) {
    override fun toString(): String {
        return "percent : $percent rate :$rate"
    }
}