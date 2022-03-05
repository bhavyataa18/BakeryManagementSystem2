package com.example.bakerymanagementsystem2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DeliveryFoodPanel_BottomNavigation extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_food_panel_bottom_navigation);

            BottomNavigationView navigationView = findViewById(R.id.delivery_bottom_navigation);
            navigationView.setOnNavigationItemSelectedListener(this);

        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment fragment = null;
            switch (item.getItemId()){
                case R.id.ShipOrder:
                    fragment=new DeliveryShipOrder();
                    break;
            }
            switch (item.getItemId()){
                case R.id.Pending:
                    fragment=new DeliveryPendingOrders();
                    break;
            }
            return loaddeliveryfragment(fragment);
        }

        private boolean loaddeliveryfragment(Fragment fragment) {
            if(fragment != null){
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_del_Person,fragment).commit();
                return true;
            }
            return false;
        }
}