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

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.data.Post;


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
        list.setAdapter(new PostAdapter(loadPosts()));
    }

    private List<Post> loadPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        for (int i=0; i<20;i++) {
            posts.add(new Post(i, "Me", "Check it out", "https://camo.githubusercontent.com/0b8ab92abe8962753f1cd0adf291ffb53ba29d59/687474703a2f2f766f61742e636f2f47726170686963732f766f61742d676f61742e706e67",
                    "https://camo.githubusercontent.com/0b8ab92abe8962753f1cd0adf291ffb53ba29d59/687474703a2f2f766f61742e636f2f47726170686963732f766f61742d676f61742e706e67"));
        }
        return posts;
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
            holder.scoreText.setText(post.getScore() + "");
            holder.titleText.setText(post.getTitle());
            holder.authorText.setText(post.getAuthor());
            Glide.with(holder.image.getContext())
                    .load(post.getImageUrl())
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
