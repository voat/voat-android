package co.voat.android;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.api.SubverseResponse;
import co.voat.android.api.VoatClient;
import co.voat.android.data.Post;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;


public class MainActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @InjectView(R.id.list)
    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
        list.setLayoutManager(new LinearLayoutManager(this));

        VoatClient.instance().getSubverse("all", new Callback<SubverseResponse>() {
            @Override
            public void success(SubverseResponse subverseResponse, Response response) {
                if (subverseResponse.success) {
                    list.setAdapter(new PostAdapter(subverseResponse.data));
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.e(error.toString());
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

        private List<Post> mValues;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            @InjectView(R.id.post_score)
            TextView scoreText;
            @InjectView(R.id.post_title)
            TextView titleText;
            @InjectView(R.id.post_author)
            TextView authorText;
            @InjectView(R.id.post_image)
            ImageView image;

            public ViewHolder(View view) {
                super(view);
                ButterKnife.inject(this, view);
            }
        }

        public Post getValueAt(int position) {
            return mValues.get(position);
        }

        public PostAdapter(List<Post> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            Post post = getValueAt(position);
            holder.scoreText.setText(post.getUpVotes() + "");
            holder.titleText.setText(post.getTitle());
            holder.authorText.setText(post.getUserName());
            Glide.with(holder.image.getContext())
                    .load(post.getThumbnail())
                    .into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(v, "hello", Snackbar.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }
}
