package hr.markic.budgetmanager.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.content.getSystemService
import hr.markic.budgetmanager.model.Item

fun View.startAnimation(animationId: Int)
        = startAnimation(AnimationUtils.loadAnimation(context, animationId))

inline fun<reified T : Activity> Context.startActivity()
        = startActivity(Intent(this, T::class.java).apply {
    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
})
inline fun<reified T: BroadcastReceiver> Context.sendBroadcast()
        = sendBroadcast(Intent(this, T::class.java))

fun Context.setBooleanPreference(key: String, value: Boolean) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .edit()
        .putBoolean(key, value)
        .apply()


fun Context.getBooleanPreference(key: String) =
    PreferenceManager.getDefaultSharedPreferences(this)
        .getBoolean(key, false)


fun Context.isOnline() : Boolean {
    val connectivityManager = getSystemService<ConnectivityManager>()
    connectivityManager?.activeNetwork?.let { network ->
        connectivityManager.getNetworkCapabilities(network)?.let { networkCapabilities ->
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        }
    }
    return false
}

fun callDelayed(delay: Long, function: Runnable) {
    Handler(Looper.getMainLooper()).postDelayed(
        function,
        delay
    )
}

fun Context.fetchItems() : MutableList<Item> {
    val items = mutableListOf<Item>()
    val cursor = contentResolver?.query( Uri.parse("write that"),
        null,
        null,
        null,
        null)
    while (cursor != null && cursor.moveToNext()) {
        items.add(Item(
            cursor.getLong(cursor.getColumnIndexOrThrow(Item::_id.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::title.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::explanation.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::picturePath.name)),
            cursor.getString(cursor.getColumnIndexOrThrow(Item::date.name)),
            cursor.getInt(cursor.getColumnIndexOrThrow(Item::read.name)) == 1
        ))
    }
    return items
}