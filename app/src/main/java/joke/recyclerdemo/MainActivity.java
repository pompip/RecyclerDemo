package joke.recyclerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import joke.recyclerdemo.widget.FlowLayoutManager;

public class MainActivity extends AppCompatActivity {
  final static int[] colors = {0xff00ff00, 0xff0000ff, 0xffff0000};
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(MyApplication.themeID);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        rv = (RecyclerView)findViewById(R.id.rv);
        rv.setAdapter(new RecyclerAdapter());
        rv.setLayoutManager(new FlowLayoutManager());


    }

    class RecyclerAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            ViewGroup inflate = (ViewGroup) LayoutInflater.from(MainActivity.this).inflate(R.layout.item_rv, parent, false);
            return new Holder(inflate);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewGroup itemView = (ViewGroup) (holder.itemView);


            itemView.setBackgroundColor(colors[position % 3]);
            TextView childAt = (TextView) (itemView.getChildAt(0));
            int n = new Random().nextInt(20);
            StringBuilder text = new StringBuilder(position+"--") ;
            for (int i = 0;i<n;i++) {
                text.append(i);
            }
            childAt.setText(text);
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
