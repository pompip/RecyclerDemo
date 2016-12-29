package joke.recyclerdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import joke.recyclerdemo.activity.FlowLayoutActivity;
import joke.recyclerdemo.activity.NumberTurningActivity;
import joke.recyclerdemo.widget.FlowLayoutManager;
import joke.recyclerdemo.widget.TextNumberItemAnimator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {

            case R.id.button1:
                intent = new Intent(this, FlowLayoutActivity.class);
                break;
            case R.id.button2:
                intent = new Intent(this, NumberTurningActivity.class);
                break;
        }
        startActivity(intent);
    }
}
