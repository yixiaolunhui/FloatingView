package com.dalong.floatingview;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dalong.floatview.FloatingView;


public class MainActivity extends Activity {

    private FloatingView floatingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingView=(FloatingView)findViewById(R.id.floatingView);
        View view= LayoutInflater.from(this).inflate(R.layout.item_view,null);
        ImageView image= (ImageView) view.findViewById(R.id.test_img);

        floatingView.addFloatingView(view)
                .setFloatingDuration(200L)
                .setFloatingPercent(0.5f)
                .setFloatingScale(1f);

        Glide.with(this)
                .load("http://img2.imgtn.bdimg.com/it/u=2679297709,1421413045&fm=21&gp=0.jpg")
                .placeholder(R.mipmap.ic_launcher)
                .into(image);


        floatingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "别打我", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
