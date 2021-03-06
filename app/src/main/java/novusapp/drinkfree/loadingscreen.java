/*
    Loadingscreen.java: Acts like a buffer screen while the application is determining whether the user is already logged in or not.
    Author: Ivan Zhang
    Company: Novusapp.com

 */

package novusapp.drinkfree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;


public class loadingscreen extends Activity {

    private static final String DID_LOGIN = "didlogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingscreen);
        Firebase.setAndroidContext(getApplicationContext());

        TextView text = (TextView) findViewById(R.id.textView2);

        // Checks to see if the network is available, shows toast if it is not
        if(isNetworkAvailable() == false) {
            text.setVisibility(View.VISIBLE);
            Toast.makeText(getApplicationContext(), "Please enable internet", Toast.LENGTH_LONG).show();
        }else {

            // Setup Firebase
            final Firebase myFirebaseRef = new Firebase("https://drinkfreeapp.firebaseio.com/");

            // Get android_id
            final String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            myFirebaseRef.child(DID_LOGIN).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(android_id).exists()) {
                        Intent mainIntent = new Intent(getApplicationContext(), main.class);
                        startActivity(mainIntent);
                        finish();
                    } else {
                        Intent regIntent = new Intent(getApplicationContext(), register.class);
                        startActivity(regIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });

        }
    }

    // Network method to check if there is connetion to internet or not
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if(activeNetworkInfo != null) return true;
        else return false;
    }

}
