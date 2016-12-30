package joke.recyclerdemo.activity;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import joke.recyclerdemo.R;
import joke.recyclerdemo.widget.TextNumberItemAnimator;

public class NumberTurningActivity extends AppCompatActivity {


    private Handler handler;
    boolean canRun = false;
    private HandlerThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_turning);
        RecyclerView rv_text_number = (RecyclerView) findViewById(R.id.rv_text_number);
        final TextNumberAdapter adapter = new TextNumberAdapter();
        rv_text_number.setAdapter(adapter);
        rv_text_number.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv_text_number.setItemAnimator(new TextNumberItemAnimator());

        final View button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                canRun = !canRun;
                if (canRun) {
                    handler.sendEmptyMessage(1);
                }else{
                    handler.removeMessages(1);
                }
            }
        });

        thread = new HandlerThread("time");
        thread.start();
        handler = new Handler(thread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addNum(1L);
                    }
                });
                if (canRun) {
                    handler.sendEmptyMessageDelayed(1, 1);
                }
                return true;
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        thread.quit();
    }

    class TextNumberAdapter extends RecyclerView.Adapter<NumberHolder> {
        Long num = 0L;
        char[] chars = "0000000000".toCharArray();

        public void initNum(int max) {
            this.num = 0L;
            chars = new char[max];
            for (int i = 0; i < max; i++) {
                chars[i] = '0';
            }
            notifyDataSetChanged();
        }

        public void addNum(Long num) {
            this.num += num;
            String s = String.valueOf(this.num);
            for (int i = s.length(); i > 0; i--) {
                char c = s.charAt(s.length() - i);
                if (chars[chars.length - i] != c) {
                    chars[chars.length - i] = c;
                    notifyItemChanged(chars.length - i);
                }
            }

        }

        @Override
        public NumberHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_text_number, parent, false);
            return new NumberHolder(inflate);
        }

        @Override
        public void onBindViewHolder(NumberHolder holder, int position) {
            holder.bindNumber(chars[position]);

        }

        @Override
        public int getItemCount() {
            return chars.length;
        }
    }

    class NumberHolder extends RecyclerView.ViewHolder {

        private final TextView text;

        public NumberHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void bindNumber(char number) {
            text.setText(number+"");
        }
    }
}
