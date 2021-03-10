package com.tapi.connectview

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.TypedValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tapi.connectview.speedview.components.Section
import com.tapi.speedtest.ui.speedview.view.Gauge
import java.net.InetAddress
import java.net.NetworkInterface
import java.util.*


typealias OnSpeedChangeListener = (gauge: Gauge, isSpeedUp: Boolean, isByTremble: Boolean) -> Unit
typealias OnSectionChangeListener = (previousSection: Section?, newSection: Section?) -> Unit
typealias OnPrintTickLabelListener = (tickPosition: Int, tick: Float) -> CharSequence?


fun Gauge.doOnSections(action: (section: Section) -> Unit) {
    val sections = ArrayList(this.sections)
    // this will also clear observers.
    this.clearSections()
    sections.forEach { action.invoke(it) }
    this.addSections(sections)
}

fun getRoundAngle(a: Float, d: Float): Float {
    return (a * .5f * 360 / (d * Math.PI)).toFloat()
}

object Utils {
    fun dpToPx(context: Context, dp: Float): Int {
        return (dp * context.resources.displayMetrics.density + 0.5f).toInt()
    }


    fun pxToDp(context: Context, px: Int): Int {
        return (px / context.resources.displayMetrics.density).toInt()
    }

    fun spToPx(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            context.resources.displayMetrics
        )
            .toInt()
    }


//    fun shareFileAudio(context: Context, text: String, pkgName: String?): Boolean {
//        return try {
//            val intent = Intent()
//            if (pkgName != null) {
//                intent.`package` = pkgName
//            }
//            intent.action = Intent.ACTION_SEND
//            intent.putExtra(Intent.EXTRA_STREAM, text)
//            intent.type = "audio/*"
//            context.startActivity(Intent.createChooser(intent, context.resources.getString(R.string.Choose_in_app_inten)))
//            true
//        } catch (e: Exception) {
//            e.printStackTrace()
//            false
//        }
//    }


    fun getIPAddress(useIPv4: Boolean): String {
        try {
            val interfaces: List<NetworkInterface> =
                Collections.list(NetworkInterface.getNetworkInterfaces())
            for (intf in interfaces) {
                val addrs: List<InetAddress> = Collections.list(intf.inetAddresses)
                for (addr in addrs) {
                    if (!addr.isLoopbackAddress) {
                        val sAddr = addr.hostAddress
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        val isIPv4 = sAddr.indexOf(':') < 0
                        if (useIPv4) {
                            if (isIPv4) return sAddr
                        } else {
                            if (!isIPv4) {
                                val delim = sAddr.indexOf('%') // drop ip6 zone suffix
                                return if (delim < 0) sAddr.toUpperCase(Locale.ROOT) else sAddr.substring(
                                    0,
                                    delim
                                ).toUpperCase(Locale.ROOT)
                            }
                        }
                    }
                }
            }
        } catch (ex: Exception) {
        } // for now eat exceptions
        return ""
    }


    fun <X, Y, Z> map(
        sourceX: LiveData<X>,
        sourceY: LiveData<Y>,
        mapFunc: (x: X?, y: Y?) -> Z
    ): MutableLiveData<Z> {

        val result = MediatorLiveData<Z>()
        result.addSource(sourceX) {
            result.value = mapFunc(it, sourceY.value)
        }
        result.addSource(sourceY) {
            result.value = mapFunc(sourceX.value, it)
        }
        return result


    }

    fun shareMutilpleFile(context: Context, listUri: ArrayList<Uri>, pkgName: String?) {
        val shareIntent = Intent()
        if (pkgName != null) {
            shareIntent.`package` = pkgName
        }
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, listUri)
        shareIntent.type = "audio/*"
        context.startActivity(
            Intent.createChooser(
                shareIntent,
                context.resources.getString(R.string.Choose_in_app_inten)
            )
        )
    }


}