package joke.recyclerdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

import joke.recyclerdemo.MainActivity;
import joke.recyclerdemo.MyApplication;
import joke.recyclerdemo.R;
import joke.recyclerdemo.widget.FlowLayoutManager;

public class FlowLayoutActivity extends AppCompatActivity {

    final static int[] colors = {0x5500ff00, 0x550000ff, 0x55ff0000};
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApplication.themeID);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setAdapter(new RecyclerAdapter());
        rv.setLayoutManager(new FlowLayoutManager());
//        rv.setLayoutManager(new LinearLayoutManager(this));


    }

    class RecyclerAdapter extends RecyclerView.Adapter {
        ArrayList<String> list = new ArrayList<>();

        {
            Random random = new Random();
            for (int j = 0; j < 200; j++) {
                int n = random.nextInt(20);
                StringBuilder text = new StringBuilder(j + "--");
                for (int i = 0; i < n; i++) {
                    text.append(i);
                }
                list.add(text.toString());
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ViewGroup inflate = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false);
            return new Holder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewGroup itemView = (ViewGroup) (holder.itemView);

            itemView.setBackgroundColor(colors[position % 3]);
            TextView childAt = (TextView) (itemView.getChildAt(0));

            childAt.setText(list.get(position));
            childAt.setTextColor(0xffffffff);
        }

        @Override
        public int getItemCount() {
            return 200;
        }

        class Holder extends RecyclerView.ViewHolder {

            public Holder(View itemView) {
                super(itemView);
            }
        }
    }
}
