package com.example.mapsfragmentdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Dot;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PatternItem;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Arrays;
import java.util.List;

import static android.graphics.Color.argb;

public class MapsFragment extends Fragment {

    //polyline styling
    private static final int PATTERN_GAP_LENGTH_PX = 20;
    private static final PatternItem DOT = new Dot();
    private static final PatternItem GAP = new Gap(PATTERN_GAP_LENGTH_PX);

    // Create a stroke pattern of a gap followed by a dot.
    private static final List<PatternItem> PATTERN_POLYLINE_DOTTED = Arrays.asList(GAP, DOT);


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            //Add polylines
            Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(-35.016, 143.321),
                            new LatLng(-34.747, 145.592),
                            new LatLng(-34.364, 147.891),
                            new LatLng(-33.501, 150.217),
                            new LatLng(-32.306, 149.248),
                            new LatLng(-32.491, 147.309)));

            // Add polygons to indicate areas on the map.
            Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                    .clickable(true)
                    .add(
                            new LatLng(-27.457, 153.040),
                            new LatLng(-33.852, 151.211),
                            new LatLng(-37.813, 144.962),
                            new LatLng(-34.928, 138.599)));
            // Store a data object with the polygon, used here to indicate an arbitrary type.
            polygon1.setTag("alpha");
            polygon1.setFillColor(argb(50, 0, 204, 0));
            polygon1.setStrokeWidth(6);
            polygon1.setStrokeColor(argb(100, 0, 204, 0));

            // Position the map's camera near Alice Springs in the center of Australia,
            // and set the zoom factor so most of Australia shows on the screen.
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-23.684, 133.903), 4));
            // add marker at destination
            LatLng destination = new LatLng(-32.491, 147.309);
            googleMap.addMarker(new MarkerOptions().position(destination).title("Marker at destination"));

            // Set listeners for click events.
            googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
                @Override
                public void onPolylineClick(Polyline polyline) {

                    // Flip from solid stroke to dotted stroke pattern.
                    if ((polyline.getPattern() == null) || (!polyline.getPattern().contains(DOT))) {
                        polyline.setPattern(PATTERN_POLYLINE_DOTTED);
                    } else {
                        // The default pattern is a solid stroke.
                        polyline.setPattern(null);
                    }
                }
            });

            //Add Circle
            // Instantiates a new CircleOptions object and defines the center and radius
            LatLng perth = new LatLng(-31.953570, 115.856978);
            Circle circle = googleMap.addCircle(new CircleOptions()
                    .center(perth)
                    .radius(100000)
                    .strokeWidth(10)
                    .strokeColor(Color.GREEN)
                    .fillColor(Color.argb(40, 255, 0, 0))
                    .clickable(true));

            googleMap.setOnCircleClickListener(new GoogleMap.OnCircleClickListener() {
                @Override
                public void onCircleClick(Circle circle) {
                    // Flip the r, g and b components of the circle's
                    // stroke color.
                    int strokeColor = circle.getStrokeColor() ^ 0x00ffffff;
                    circle.setStrokeColor(strokeColor);
                }
            });

            //Add Overlay
            LatLng operahouse = new LatLng(-33.856732, 151.215227);
            GroundOverlay sydneyGroundOverlay = googleMap.addGroundOverlay(new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.operahouse))
                    .position(operahouse, 600f)
                    .clickable(true));
            sydneyGroundOverlay.setTag("Sydney");
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}