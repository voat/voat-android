package co.voat.android.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import co.voat.android.R;
import co.voat.android.data.Subverse;

/**
 * Nice subverse brah. Be a shame if something happened to it...
 * Created by Jawn on 6/12/2015.
 */
public class SubverseViewHolder extends RecyclerView.ViewHolder {

    public static SubverseViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new SubverseViewHolder(view);
    }

    @InjectView(R.id.subverse_name)
    TextView nameText;

    public SubverseViewHolder(View view) {
        super(view);
        ButterKnife.inject(this, view);
    }

    public void bind(Subverse subverse) {
        nameText.setText(subverse.getName());
    }
}
