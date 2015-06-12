package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
import co.voat.android.data.Subscription;

/**
 * Nice subscription brah. Be a shame if something happened to it...
 * Created by Jawn on 6/12/2015.
 */
public class SubscriptionViewHolder extends RecyclerView.ViewHolder {

    public static SubscriptionViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subscription, parent, false);
        return new SubscriptionViewHolder(view);
    }

    @InjectView(R.id.subverse_name)
    TextView nameText;

    public SubscriptionViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
    }

    public void bind(Subscription subscription) {
        nameText.setText(subscription.getName());
    }
}
