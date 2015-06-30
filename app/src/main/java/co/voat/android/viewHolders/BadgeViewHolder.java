package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.voat.android.R;
import co.voat.android.data.Badge;

/**
 * Your badges are weak
 * Created by Jawn on 6/13/2015.
 */
public class BadgeViewHolder extends RecyclerView.ViewHolder {

    public static BadgeViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_badge, parent, false);
        return new BadgeViewHolder(view);
    }

    @Bind(R.id.image)
    ImageView image;

    public BadgeViewHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void bind(Badge badge) {
        Glide.with(itemView.getContext())
                .load(badge.getBadgeGraphic())
                .into(image);
    }
}
