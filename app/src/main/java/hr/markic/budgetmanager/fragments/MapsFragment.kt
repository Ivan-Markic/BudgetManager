package hr.markic.budgetmanager.fragments

import android.content.Context
import android.location.Geocoder
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.text.InputType
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import hr.markic.budgetmanager.R
import hr.markic.budgetmanager.databinding.FragmentMapsBinding
import hr.markic.budgetmanager.framework.buildDialogAlert
import hr.markic.budgetmanager.repository.RepositoryFactory

class MapsFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        RepositoryFactory.createRepository().loadMarkers(mMap, requireContext())

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))

        mMap.setOnMarkerClickListener{
            it.showInfoWindow()
            true
        }

        mMap.setOnMapClickListener {

            requireContext().buildDialogAlert(it, mMap)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}