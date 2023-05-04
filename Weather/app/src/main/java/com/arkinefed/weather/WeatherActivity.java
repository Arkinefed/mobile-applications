package com.arkinefed.weather;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

public class WeatherActivity extends AppCompatActivity {
    private int itemPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        if (savedInstanceState == null) {
            itemPosition = getIntent().getIntExtra("position", 0);
        } else {
            itemPosition = savedInstanceState.getInt("position");
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewPager2 weatherViewPager = findViewById(R.id.weatherViewPager);

            FragmentStateAdapter pagerAdapter = new FragmentStateAdapter(this) {
                @Override
                public int getItemCount() {
                    return 3;
                }

                @NonNull
                @Override
                public Fragment createFragment(int position) {
                    switch (position) {
                        case 0:
                            WeatherDataPart1Fragment fragment1 = new WeatherDataPart1Fragment();
                            Bundle bundle1 = new Bundle();

                            bundle1.putInt("position", itemPosition);
                            fragment1.setArguments(bundle1);

                            return fragment1;
                        case 1:
                            WeatherDataPart2Fragment fragment2 = new WeatherDataPart2Fragment();
                            Bundle bundle2 = new Bundle();

                            bundle2.putInt("position", itemPosition);
                            fragment2.setArguments(bundle2);

                            return fragment2;
                        case 2:
                            WeatherDataPart3Fragment fragment3 = new WeatherDataPart3Fragment();
                            Bundle bundle3 = new Bundle();

                            bundle3.putInt("position", itemPosition);
                            fragment3.setArguments(bundle3);

                            return fragment3;
                        default:
                            return null;
                    }
                }
            };

            weatherViewPager.setAdapter(pagerAdapter);
            weatherViewPager.setOffscreenPageLimit(2);
        } else {
            WeatherDataPart1Fragment fragment1 = new WeatherDataPart1Fragment();
            Bundle bundle1 = new Bundle();

            bundle1.putInt("position", itemPosition);
            fragment1.setArguments(bundle1);

            WeatherDataPart3Fragment fragment3 = new WeatherDataPart3Fragment();
            Bundle bundle3 = new Bundle();

            bundle3.putInt("position", itemPosition);
            fragment3.setArguments(bundle3);

            getSupportFragmentManager().beginTransaction().add(R.id.left_fragment, fragment1).commit();
            getSupportFragmentManager().beginTransaction().add(R.id.right_fragment, fragment3).commit();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("position", itemPosition);
    }
}