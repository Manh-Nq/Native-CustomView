package com.tapi.connectview.linespeedview.objs

data class DataNetwork(var percent: Float = 0f, var transferRate: Float = 0f) {
    override fun toString(): String {
        return "percent: $percent  - transferRateBit: $transferRate"
    }
}
