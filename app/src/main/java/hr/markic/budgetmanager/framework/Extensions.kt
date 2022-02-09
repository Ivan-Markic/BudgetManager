package hr.markic.budgetmanager.framework

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.text.InputType
import android.text.TextUtils.isEmpty
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.getSystemService
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.model.Bill
import hr.markic.budgetmanager.model.Item
import hr.markic.budgetmanager.model.Location
import hr.markic.budgetmanager.repository.RepositoryFactory
import hr.markic.budgetmanager.repository.auth
import java.time.LocalDateTime
import java.time.ZoneOffset


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

fun Context.buildDialogAlert(it: LatLng, mMap:GoogleMap) {

    val builder = AlertDialog.Builder(this);
    builder.setTitle("Do you want to save this location?");

    val inputTitle = EditText(this)
    val title = TextView(this)
    val category = TextView(this)
    val spinner = Spinner(this)
    val inputAmount = EditText(this)
    val amount = TextView(this)
    val timePicker = TimePicker(this)

    timePicker.setIs24HourView(true)
    timePicker.hour = 12
    timePicker.minute = 0

    title.text = getString(R.string.marker_title)
    category.text = getString(R.string.bill_category)
    amount.text = getString(R.string.bill_amount)


    val categories = resources.getStringArray(R.array.categories)
    val colors = resources.getIntArray(R.array.colors_ids)

    val spinnerArrayAdapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
    spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)

    spinner.adapter = spinnerArrayAdapter

    inputTitle.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    inputAmount.inputType = InputType.TYPE_CLASS_NUMBER
    inputTitle.error = "Title is mandatory"
    inputAmount.error = "Amount is mandatory"

    val linearLayout = LinearLayout(this)

    linearLayout.orientation = LinearLayout.VERTICAL

    linearLayout.addView(title)
    linearLayout.addView(inputTitle)
    linearLayout.addView(category)
    linearLayout.addView(spinner)
    linearLayout.addView(amount)
    linearLayout.addView(inputAmount)
    linearLayout.addView(timePicker)

    builder.setView(linearLayout)

    builder.setPositiveButton("Yes") { _ , _ ->

        if (isEmpty(inputAmount.text) || isEmpty(inputTitle.text)) return@setPositiveButton

        val titleText = inputTitle.text.toString()
        val amountText = inputAmount.text.toString().toDouble()
        val colorName = spinner.selectedItem.toString()
        val colorID = colors[categories
            .indexOf(spinner.selectedItem)].toFloat()

        val gc = Geocoder(this);

        val addresses = gc.getFromLocation(it.latitude, it.longitude, 1)

        Toast.makeText(this, "Title: $titleText", Toast.LENGTH_SHORT).show()

        if (addresses.size > 0) {
            val address = addresses[0]
            val new = MarkerOptions()
                .position(it)
                .flat(true)
                .snippet(address.getAddressLine(0))
                .rotation(0f)
                .title("$titleText $amountText")
                .icon(BitmapDescriptorFactory
                    .defaultMarker(colorID))
            mMap.addMarker(new)



            var billDate = LocalDateTime.now()
                .withHour(timePicker.hour)
                .withMinute(timePicker.minute)

            val billID = billDate.toEpochSecond(ZoneOffset.UTC)

            val bill = Bill(billID, titleText,  amountText, billDate,
                auth.currentUser?.displayName.toString())

            val location = Location(titleText, it.latitude, it.longitude, colorName, colorID, bill.billID)

            RepositoryFactory.createRepository().addBill(bill)
            RepositoryFactory.createRepository().addLocation(location)
        }
    }
    builder.setNegativeButton("No") { dialog, _ ->
        dialog.dismiss()
    }

    builder.show();
}
