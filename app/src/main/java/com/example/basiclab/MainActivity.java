package com.example.basiclab;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements FragmentListener {
    private Fragment currentFragment = null;
    private Bundle fragsBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragsBundle = new Bundle();
        fragsBundle.putInt("FIRST_COLOR", Color.TRANSPARENT);
        fragsBundle.putInt("SECOND_COLOR", Color.TRANSPARENT);
        fragsBundle.putInt("THIRD_COLOR", Color.TRANSPARENT);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                if (currentFragment != null) {
                    Log.i("TEST", "Wylosowany kolor " + color);
                    setFragmentBackgroundColor(currentFragment, color);
                }
            }
        });
    }

    @Override
    public void getCurrentFragment(Fragment frag) {
        int color = Color.TRANSPARENT;
        saveColor(currentFragment);
        if (frag instanceof FirstFragment) {
            color = fragsBundle.getInt("FIRST_COLOR");
            Log.i("TEST", "Obecnie wyświetlam FirstFragment");
        } else if (frag instanceof SecondFragment){
            color = fragsBundle.getInt("SECOND_COLOR");
            Log.i("TEST", "Obecnie wyświetlam SecondFragment");
        } else if (frag instanceof ThirdFragment){
            color = fragsBundle.getInt("THIRD_COLOR");
            Log.i("TEST", "Obecnie wyświetlam ThirdFragment");
        }
        currentFragment = frag;
        setFragmentBackgroundColor(currentFragment, color);
    }

    public void saveColor(Fragment frag) {
        int color = Color.TRANSPARENT;
        try {
            Drawable background = currentFragment.getView().getBackground();
            if (background instanceof ColorDrawable) {
                color = ((ColorDrawable) background).getColor();
                Log.i("TEST", "Kolor poprzednio wyswietlanego elementu " + color);
            }
        } catch (NullPointerException e) {
            Log.i("ERROR", "Brak koloru");
        }
        if (frag instanceof FirstFragment) {
            fragsBundle.putInt("FIRST_COLOR", color);
        } else if (frag instanceof SecondFragment){
            fragsBundle.putInt("SECOND_COLOR", color);
        } else if (frag instanceof ThirdFragment){
            fragsBundle.putInt("THIRD_COLOR", color);
        }
    }

    public void setFragmentBackgroundColor(Fragment fragment, int color) {
        fragment.getView().setBackgroundColor(color);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public int getActionId() {
        int actionId = -1;
        if (currentFragment instanceof FirstFragment) {
            actionId = 0;
        } else if (currentFragment instanceof SecondFragment){
            actionId = 1;
        } else if (currentFragment instanceof ThirdFragment){
            actionId = 2;
        }
        return actionId;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Map<Integer, Integer[]> actions = new HashMap<>();
        actions.put(R.id.first_item, new Integer[] {0, R.id.action_SecondFragment_to_FirstFragment,  R.id.action_ThirdFragment_to_FirstFragment});
        actions.put(R.id.second_item, new Integer[] {R.id.action_FirstFragment_to_SecondFragment, 0, R.id.action_ThirdFragment_to_SecondFragment});
        actions.put(R.id.third_item, new Integer[] {R.id.action_FirstFragment_to_ThirdFragment, R.id.action_SecondFragment_to_ThirdFragment, 0});

        int action_id = getActionId();
        if (action_id == -1) {
            return true;
        }
        int action = actions.get(id)[action_id];
        if (action != 0) {
            NavHostFragment.findNavController(currentFragment)
                    .navigate(action);
        }
        return super.onOptionsItemSelected(item);
    }
}