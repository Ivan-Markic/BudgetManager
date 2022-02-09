package hr.markic.budgetmanager.repository

import android.content.Context
import android.location.Geocoder
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.SplashScreenActivity
import hr.markic.budgetmanager.fragments.LoginFragment
import hr.markic.budgetmanager.framework.startActivity
import hr.markic.budgetmanager.model.Bill
import hr.markic.budgetmanager.model.Location
import hr.markic.budgetmanager.model.User
import java.time.LocalDateTime
import java.time.ZoneOffset

val auth:FirebaseAuth = Firebase.auth
val database = FirebaseDatabase.getInstance("https://budgetmanager-b7e7a-default-rtdb.europe-west1.firebasedatabase.app/")
lateinit var usersDB : DatabaseReference
lateinit var billDB : DatabaseReference
lateinit var locationDB : DatabaseReference
val bills = ArrayList<Bill>()
val locations = ArrayList<Location>()

class AppRepository {

    init {
        usersDB = database.getReference("Users")
        billDB = database.getReference("Bills")
        locationDB = database.getReference("Location")
    }

    fun registerUser(username: String, email: String, password: String, context:Context, activity:FragmentActivity) {

        usersDB.child(username).get().addOnSuccessListener {

            if (!it.exists()){
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener() { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context
                                , "Authentication successed.",
                                Toast.LENGTH_SHORT
                            ).show()

                            val user = User(username, email)
                            usersDB.child(user.username).setValue(user)

                            val userProfileChangeRequest = userProfileChangeRequest {
                                this.displayName = username
                            }
                            auth.currentUser!!.updateProfile(userProfileChangeRequest)

                            activity.supportFragmentManager.beginTransaction()
                                .replace(R.id.frameLayout, LoginFragment.newInstance(email, password))
                                .commit()


                        } else {

                            Toast.makeText(
                                context, task.exception?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

            } else {
                Toast.makeText(context, "User with this username exist, try different", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loginUser(email: String, password: String, context: Context) {

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() { task ->
                if (task.isSuccessful) {

                    context.startActivity<SplashScreenActivity>()
                } else {

                    Toast.makeText(
                        context, task.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

    }

    fun addBill(bill: Bill) {

        billDB.child(bill.billID.toString()).setValue(bill)
        bills.add(bill)
    }

    fun addLocation(location: Location) {
        locationDB.child(location.billID.toString()).setValue(location)
        locations.add(location)
    }

    fun loadMarkers(mMap: GoogleMap, context: Context) {


        if (locations.size > 0) {
            mMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(
                        locations[0].latitude,
                        locations[0].longitude
                    ), 10f
                )
            )
            val gc = Geocoder(context)

            for (i in 0 until locations.size) {

                val addresses = gc.getFromLocation(locations[i].latitude, locations[i].longitude, 1)

                if (addresses.size > 0) {
                    val address = addresses[0]
                    val new = MarkerOptions()
                        .position(LatLng(address.latitude, address.longitude))
                        .flat(true)
                        .snippet(address.getAddressLine(0))
                        .rotation(0f)
                        .title(locations[i].title)
                        .icon(
                            BitmapDescriptorFactory
                                .defaultMarker(locations[i].colorID)
                        )
                    mMap.addMarker(new)
                }
            }
        }
    }

    fun getLocationsForBills() {

        for (bill in bills){

            locationDB.get().addOnSuccessListener {
                if (it.exists()){
                    for (child in it.children) {

                        val billId: Long = child.child("billID").value as Long

                        if (billId == bill.billID) {

                            val title: String = child.child("title").value as String
                            val latitude: Double = child.child("latitude").value as Double
                            val longitude: Double = child.child("longitude").value as Double
                            val colorName: String = child.child("colorName").value as String
                            val colorID: Long = child.child("colorID").value as Long

                            locations.add(
                                Location(
                                    title,
                                    latitude,
                                    longitude,
                                    colorName,
                                    colorID.toFloat(),
                                    billId
                                )
                            )

                        }
                    }
                }
            }
        }
    }

    private fun getBillsForUser(username: String){

        bills.filter { bill -> bill.username == username }

    }

    fun getBills() {

        billDB.get().addOnSuccessListener {
            if (it.exists()) {
                for (child in it.children) {

                    val billId: Long = child.child("billID").value as Long
                    val title: String = child.child("title").value as String
                    val username: String = child.child("username").value as String
                    val amount: Long = child.child("amount").value as Long
                    val time: LocalDateTime =
                        LocalDateTime.ofEpochSecond(billId, 0, ZoneOffset.UTC)

                    bills.add(Bill(billId, title, amount.toDouble(), time, username))
                }
                getBillsForUser(auth.currentUser!!.displayName.toString())
            }
        }
    }
}