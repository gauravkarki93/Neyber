package com.clubin.neyber;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.clubin.neyber.common.logger.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class CreateGroupActivity extends ActionBarActivity
        implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    public static final String TAG = "SampleActivityBase";
    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000;
    // Keys for storing activity state in the Bundle.
    protected final static String REQUESTING_LOCATION_UPDATES_KEY = "requesting-location-updates-key";
    protected final static String LOCATION_KEY = "location-key";
    protected final static String LAST_UPDATED_TIME_STRING_KEY = "last-updated-time-string-key";
    private static final int REQUEST_PLACE_PICKER = 2;
    /**
     * Time when the location was updated represented as a String.
     */
    String categ;
    String imgName;
    String imgDecodableString;
    private static int RESULT_LOAD_IMAGE = 1;


    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
    //    private double mLatitude,mLongitude;
//    LocationListenerAdapter locationListenerAdapter;

    protected GoogleApiClient mGoogleApiClient;
    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    protected LocationRequest mLocationRequest;
    /**
     * Represents a geographical location.
     */
    protected Location mCurrentLocation;


//    private TextView mPlaceDetailsText;

//    private TextView mPlaceDetailsAttribution;
    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    protected Boolean mRequestingLocationUpdates;
    EditText establishment;
    Geocoder geoCoder;
    private PlaceAutocompleteAdapter mAdapter;
    private Place place;
    private Address bestMatch;

    /**
     * Provides the entry point to Google Play services.
     */
    private AutoCompleteTextView mAutocompleteView;
    private ImageButton mapIcon, groupImage;

    // UI Widgets.
    private RadioGroup radioGroup;
    private AutocompleteFilter filter;
    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                mapIcon.setEnabled(false);
                PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
                Intent intent = intentBuilder.build(getApplicationContext());
                // Start the Intent by requesting a result, identified by a request code.
                startActivityForResult(intent, REQUEST_PLACE_PICKER);

                // Hide the pick option in the UI to prevent users from starting the picker
                // multiple times.

            } catch (GooglePlayServicesRepairableException e) {
                GooglePlayServicesUtil
                        .getErrorDialog(e.getConnectionStatusCode(), CreateGroupActivity.this, 0);
            } catch (GooglePlayServicesNotAvailableException e) {
                Toast.makeText(CreateGroupActivity.this, "Google Play Services is not available.",
                        Toast.LENGTH_LONG)
                        .show();
            }
        }
    };
    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            place = places.get(0);
            try {
                List<Address> matches = geoCoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                bestMatch = (matches.isEmpty() ? null : matches.get(0));

                establishment.setText(bestMatch.getAddressLine(0));
                mAutocompleteView.setText(bestMatch.getLocality());
            } catch (IOException e) {
                e.printStackTrace();
            }


            Log.i(TAG, "Place details received: " + place.getName());
            showPlaceDetails();
            places.release();
        }
    };
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
            final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i(TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_group);

        Bundle bundle = getIntent().getExtras();
        categ = bundle.getString("Category");
        Log.i("Check",categ);

      /*  final ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);*/
        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.selectedLocation);
        mAutocompleteView.setEnabled(false);
        mapIcon = (ImageButton)
                findViewById(R.id.mapicon);
        mapIcon.setEnabled(false);
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mapIcon.setOnClickListener(clickListener);
        geoCoder = new Geocoder(getApplicationContext());
        mRequestingLocationUpdates = false;
        establishment = (EditText) findViewById(R.id.establishment);
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building a GoogleApiClient and requesting the LocationServices
        // API.
        createLocationRequest();


        // Register a listener that receives callbacks when a suggestion has been selected

        // Retrieve the TextViews that will display details and attributions of the selected place.
//        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
//        mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        List<Integer> temp = new ArrayList<>();
        temp.add(new Integer(Place.TYPE_GEOCODE));
        filter = AutocompleteFilter.create(temp);

        mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_SYDNEY, filter);
        mAutocompleteView.setAdapter(mAdapter);

        radioGroup = (RadioGroup) findViewById(R.id.rGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = (RadioButton) CreateGroupActivity.this.findViewById(checkedId);
                if (radioButton.getText().toString().equals("Set my location as Group location")) {
                    startLocationUpdates();
                } else {
                    mAutocompleteView.setText("");
                    mAutocompleteView.setEnabled(true);
                    mapIcon.setEnabled(true);
                }
            }
        });

        groupImage = (ImageButton) findViewById(R.id.group_image);
        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create intent to Open Image applications like Gallery, Google Photos
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
// Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        List<Address> matches = null;
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {


            case (1):
            {
                // yaha apna code likh jo tune attachment wale me likha hai
                try {
                    // When an Image is picked
                    if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                            && null != data) {
                        // Get the Image from data

                        Uri selectedImage = data.getData();
                        String[] filePathColumn = { MediaStore.Images.Media.DATA };

                        // Get the cursor
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                        imgDecodableString = cursor.getString(columnIndex);
                        File f = new File(imgDecodableString);
                        imgName=f.getName();
                        cursor.close();
                        android.util.Log.i("PATH:", filePathColumn[0]);
                        android.util.Log.i("FILE:", imgDecodableString);

                    ImageButton imageButton = (ImageButton) findViewById(R.id.group_image);
                     imageButton.setImageBitmap(BitmapFactory
                        .decodeFile(imgDecodableString));



                    } else {
                        Toast.makeText(this, "You haven't picked Image",
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            }



            case (2):  //yaha piyush ka request code likh instead of 11
            {
                mapIcon.setEnabled(true);

                if (resultCode == Activity.RESULT_OK) {
                   /* User has picked a place, extract data.
                    Data is extracted from the returned intent by retrieving a Place object from
                    the PlacePicker.
                    *//*
                    place = PlacePicker.getPlace(data, this);

                    /* A Place object contains details about that place, such as its name, address
                    and phone number. Extract the name, address, phone number, place ID and place types.
                    */
                    final CharSequence name = place.getName();
                    final String placeId = place.getId();


                    try {
                        matches = geoCoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    bestMatch = (matches.isEmpty() ? null : matches.get(0));

                    establishment.setText(bestMatch.getAddressLine(0)+", "+bestMatch.getAddressLine(1));
                    mAutocompleteView.setText(bestMatch.getLocality());

                    // Print data to debug log
                    Log.d("TAG", "Place selected: " + placeId + " (" + name.toString() + ")");
                }
                else{
                    Toast.makeText(this, "Couldn't pick a place"+ resultCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        /*try {

            if (data!=null && data.getAction().equals(Intent.ACTION_PICK)) {
                if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Log.i("Image","1");
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    Log.i("Image","2");
                    imgDecodableString = cursor.getString(columnIndex);
                    File f = new File(imgDecodableString);
                    imgName=f.getName();
                    Log.i("Image","3");
                    cursor.close();
                    Log.i("PATH:", filePathColumn[0]);
                    Log.i("FILE:", imgDecodableString);
                   // ImageButton imageButton = (ImageButton)findViewById(R.id.group_image);
                    //imageButton.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                }
                else {
                    Toast.makeText(this, "You haven't picked Image",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                mapIcon.setEnabled(true);

                if (resultCode == Activity.RESULT_OK) {
                *//* User has picked a place, extract data.
                   Data is extracted from the returned intent by retrieving a Place object from
                   the PlacePicker.
                 *//*
                    place = PlacePicker.getPlace(data, this);

                *//* A Place object contains details about that place, such as its name, address
                and phone number. Extract the name, address, phone number, place ID and place types.
                 *//*
                    final CharSequence name = place.getName();
                    final String placeId = place.getId();


                    matches = geoCoder.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
                    bestMatch = (matches.isEmpty() ? null : matches.get(0));

                    establishment.setText(bestMatch.getAddressLine(0)+", "+bestMatch.getAddressLine(1));
                    mAutocompleteView.setText(bestMatch.getLocality());

                    // Print data to debug log
                    Log.d("TAG", "Place selected: " + placeId + " (" + name.toString() + ")");
                }
                else{
                    Toast.makeText(this, "Couldn't pick a place"+ resultCode, Toast.LENGTH_LONG)
                            .show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong: "+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }*/
    }

    private void updatemLocation() {
        List<Address> matches;
        try {
            matches = geoCoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1);
            bestMatch = (matches.isEmpty() ? null : matches.get(0));

            establishment.setText(bestMatch.getAddressLine(0)+", "+bestMatch.getAddressLine(1));
            mAutocompleteView.setText(bestMatch.getLocality());
            mAutocompleteView.setEnabled(false);
            mapIcon.setEnabled(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        android.util.Log.i(TAG, "Updating values from bundle");
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                // Since LOCATION_KEY was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {
        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        mRequestingLocationUpdates = true;
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        updatemLocation();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.

        // The final argument to {@code requestLocationUpdates()} is a LocationListener
        // (http://developer.android.com/reference/com/google/android/gms/location/LocationListener.html).
        mRequestingLocationUpdates = false;
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();

        super.onStop();
    }

    /**
     * Runs when a GoogleApiClient object successfully connects.
     */
    @Override
    public void onConnected(Bundle connectionHint) {
        android.util.Log.i(TAG, "Connected to GoogleApiClient");

        // If the initial location was never previously requested, we use
        // FusedLocationApi.getLastLocation() to get it. If it was previously requested, we store
        // its value in the Bundle and check for it in onCreate(). We
        // do not request it again unless the user specifically requests location updates by pressing
        // the Start Updates button.
        //
        // Because we cache the value of the initial location in the Bundle, it means that if the
        // user launches the activity,
        // moves to a new location, and then changes the device orientation, the original location
        // is displayed as the activity is re-created.
        if (mCurrentLocation == null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // If the user presses the Start Updates button before GoogleApiClient connects, we set
        // mRequestingLocationUpdates to true (see startUpdatesButtonHandler()). Here, we check
        // the value of mRequestingLocationUpdates and if it is true, we start location updates.
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    /**
     * Callback that fires when the location changes.
     */
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        android.util.Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    /**
     * Stores activity data in the Bundle.
     */
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_group, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        EditText editText;
        if (id == R.id.action_done) {
            editText = (EditText) findViewById(R.id.group_name);
            String name = editText.getText().toString();

            editText = (EditText) findViewById(R.id.category_name);
            String category = editText.getText().toString();

            editText = (EditText) findViewById(R.id.desc);
            String desc = editText.getText().toString();

            editText = (EditText) findViewById(R.id.establishment);
            String establishment = editText.getText().toString();

            editText = (EditText) findViewById(R.id.selectedLocation);
            String city = editText.getText().toString();

            new MyJsonParser().execute(name, category, desc, establishment, city );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void showPlaceDetails() {
        android.util.Log.d("MyPlace", "Name: " + place.getId());
        android.util.Log.d("MyPlace", "Name: " + place.getName());
        android.util.Log.d("MyPlace", "Name: " + place.getAddress());
        android.util.Log.d("MyPlace", "Name: " + place.getLocale());
        android.util.Log.d("MyPlace", "Name: " + place.getLatLng());
        android.util.Log.d("MyPlace", "Name: " + place.getPlaceTypes());
        android.util.Log.d("MyPlace", "Name: " + place.getPhoneNumber());
        android.util.Log.d("MyPlace", "Name: " + place.getWebsiteUri());
    }




    class MySender extends AsyncTask<Void,Void,Void>
    {
        URL url;
        @Override
        protected Void doInBackground(Void... params) {

            try
            {
                Resources res = getResources();
                //Drawable drawable = res.getDrawable(R.drawable.angelsofallah);
                Bitmap bitmap = BitmapFactory.decodeFile(imgDecodableString);
                // Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();



                url=new URL("https://www.googleapis.com/upload/storage/v1/b/neyber/o??uploadType=media&name="+imgName);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(150000);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-type", "image/jpeg");
                connection.connect();




                android.util.Log.i("common", "Connected");
                OutputStream out =  connection.getOutputStream();
                out.write(bitMapData);

                // osw.write(tester);



                out.flush();
                out.close();
                int responseCode = connection.getResponseCode();
                android.util.Log.i("Code", "" + responseCode);
                if(responseCode == HttpURLConnection.HTTP_OK)
                {
                    InputStream input=connection.getInputStream();
                    Scanner sc=new Scanner(input);
                    System.out.print("Uploading Image");
                    while(sc.hasNext())
                        android.util.Log.d("Common", "Returned: " + sc.nextLine());

                    input.close();
                }
                else
                {
                    android.util.Log.i("Code:", "Error" + responseCode);
                }

            }

            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }









    class MyJsonParser extends AsyncTask<String, Void, Integer> {
        //Group_model group_model = new Group_model();

        EditText editText;

        @Override
        protected Integer doInBackground(String... params) {

//            SharedPreferences sharedPreferences= getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String name=params[0], category=params[1], desc=params[2], establishment=params[3], city=params[4];


            if (name.equals("") || category.equals("") || desc.equals("")||establishment.equals("")||city.equals(""))
                return 0;

            new MySender().execute();

            try {
                MyConnector connector = new MyConnector();
                JSONObject jsonObject = new JSONObject();

                // set other params here
                jsonObject.put("name", name);
                jsonObject.put("category", categ);
                jsonObject.put("group_info", desc);
                jsonObject.put("institution",establishment);
                jsonObject.put("city", city);
                jsonObject.put("admin_id",ActiveProfile.getInstance().getId());
                jsonObject.put("latitude",bestMatch.getLatitude());
                jsonObject.put("longitude",bestMatch.getLongitude());
                jsonObject.put("group_image","https://storage.googleapis.com/neyber/"+imgName);
                Log.i("TAG", "create group JSONObject: " + jsonObject);
                int responsecode = connector.postJson(getString(R.string.parentURI) + "/profiles/"+ActiveProfile.getInstance().getId()+"/groups", jsonObject);
                return responsecode;

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0) {
                Toast.makeText(getApplicationContext(), "Enter valid data", Toast.LENGTH_SHORT).show();
            }
            if (integer == HttpURLConnection.HTTP_OK) {
                Toast.makeText(getApplicationContext(), "Group Created Successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Unable to post data", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}