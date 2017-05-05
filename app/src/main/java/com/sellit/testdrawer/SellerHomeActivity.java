package com.sellit.testdrawer;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SellerHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user;
    TextView sideBarUsername;
    TextView sideBarEmail;
    Switch switch1;
    String TAG = SellerHomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navHeader = navigationView.getHeaderView(0);
        switch1 = (Switch) navHeader.findViewById(R.id.switch1);

        sideBarUsername = (TextView) navHeader.findViewById(R.id.userNameSidebar);
        sideBarEmail = (TextView) navHeader.findViewById(R.id.emailSideBar);
        user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Intent myIntent = new Intent(SellerHomeActivity.this, HomeActivity.class);
                    SellerHomeActivity.this.startActivity(myIntent);

                }
            }
        });


        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserInfo u = dataSnapshot.getValue(UserInfo.class);
                sideBarUsername.setText(u.userName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mRef.child("userInfo").child(user.getUid()).addListenerForSingleValueEvent(listener);
        sideBarEmail.setText(user.getEmail());
        FragmentManager FM = getFragmentManager();
        FragmentTransaction transaction = FM.beginTransaction();
        transaction.replace(R.id.content_frame, new GoalFragment());
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FourthFragment())
                    .commit();

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.nav_seller_addItem) {
            Intent myIntent = new Intent(SellerHomeActivity.this, AddItemActivity.class);
            SellerHomeActivity.this.startActivity(myIntent);
        } else if (id == R.id.nav_sign_out) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FourthFragment())
                    .commit();
        } else if (id == R.id.nav_activity_profile) {
            ProfileFragment pf = new ProfileFragment();
            Bundle b = new Bundle();
            b.putString("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            pf.setArguments(b);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , pf)
                    .commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
