package com.codepath.marcferna.mapexercise;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements
  GooglePlayServicesClient.ConnectionCallbacks,
  GooglePlayServicesClient.OnConnectionFailedListener,
  GoogleMap.OnMapLongClickListener {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_maps);
      setUpMapIfNeeded();
  }

  @Override
  protected void onResume() {
      super.onResume();
      setUpMapIfNeeded();
  }

  /**
   * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
   * installed) and the map has not already been instantiated.. This will ensure that we only ever
   * call {@link #setUpMap()} once when {@link #mMap} is not null.
   * <p>
   * If it isn't installed {@link SupportMapFragment} (and
   * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
   * install/update the Google Play services APK on their device.
   * <p>
   * A user can return to this FragmentActivity after following the prompt and correctly
   * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
   * have been completely destroyed during this process (it is likely that it would only be
   * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
   * method in {@link #onResume()} to guarantee that it will be called.
   */
  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
              .getMap();
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
          setUpMap();
      }
    }
  }

  /**
   * This is where we can add markers or lines, add listeners or move the camera. In this case, we
   * just add a marker near Africa.
   * <p>
   * This should only be called once and when we are sure that {@link #mMap} is not null.
   */
  private void setUpMap() {
    mMap.setOnMapLongClickListener(this);
    mMap.setInfoWindowAdapter(new CustomWindowAdapter(this.getLayoutInflater()));
  }

  @Override
  public void onConnected(Bundle bundle) {

  }

  @Override
  public void onDisconnected() {

  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {

  }

  @Override
  public void onMapLongClick(LatLng latLng) {
    Toast.makeText(getApplicationContext(), "Long Press", Toast.LENGTH_LONG).show();
    showAlertDialogForPoint(latLng);
  }

  // Display the alert that adds the marker
  private void showAlertDialogForPoint(final LatLng point) {
    // inflate message_item.xml view
    View messageView = LayoutInflater.from(MapsActivity.this).inflate(R.layout.message_item, null);
    // Create alert dialog builder
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    // set message_item.xml to AlertDialog builder
    alertDialogBuilder.setView(messageView);

    // Create alert dialog
    final AlertDialog alertDialog = alertDialogBuilder.create();

    // Configure dialog button (OK)
    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
      new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          // Define color of marker icon
          BitmapDescriptor defaultMarker =
          BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
          // Extract content from alert dialog
          String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).getText().toString();
          String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).getText().toString();
          // Creates and adds marker to the map
          Marker marker = mMap.addMarker(new MarkerOptions()
                                       .position(point)
                                       .title(title)
                                       .snippet(snippet)
                                       .icon(defaultMarker));
          dropPinEffect(marker);
        }
      });

    // Configure dialog button (Cancel)
    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
    new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
    });

    // Display the dialog
    alertDialog.show();
  }

  private void dropPinEffect(final Marker marker) {
    // Handler allows us to repeat a code block after a specified delay
    final android.os.Handler handler = new android.os.Handler();
    final long start = SystemClock.uptimeMillis();
    final long duration = 1500;

    // Use the bounce interpolator
    final android.view.animation.Interpolator interpolator =
      new BounceInterpolator();

    // Animate marker with a bounce updating its position every 15ms
    handler.post(new Runnable() {
      @Override
      public void run() {
        long elapsed = SystemClock.uptimeMillis() - start;
        // Calculate t for bounce based on elapsed time
        float t = Math.max(1 - interpolator.getInterpolation((float) elapsed / duration), 0);
        // Set the anchor
        marker.setAnchor(0.5f, 1.0f + 14 * t);

        if (t > 0.0) {
          // Post this event again 15ms from now.
          handler.postDelayed(this, 15);
        } else { // done elapsing, show window
          marker.showInfoWindow();
        }
      }
    });
  }
}
