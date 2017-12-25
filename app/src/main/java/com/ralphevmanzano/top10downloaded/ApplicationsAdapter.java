package com.ralphevmanzano.top10downloaded;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;

/**
 * Created by ralphemerson on 12/6/2017.
 */

public class ApplicationsAdapter extends RecyclerView.Adapter<ApplicationsAdapter.ApplicationsViewHolder> {

    private ArrayList<FeedEntry> applications;
    private Context context;

    public ApplicationsAdapter(ArrayList<FeedEntry> applications) {
        this.applications = applications;
    }

    @Override
    public ApplicationsAdapter.ApplicationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item, parent, false);

        return new ApplicationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicationsAdapter.ApplicationsViewHolder holder, int position) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(applications.get(position).getReleaseDate());
        } catch (ParseException e) {
            Log.i(TAG, "onBindViewHolder: Parse Error");
            e.printStackTrace();
        }
        sdf = new SimpleDateFormat("MMM dd, yyyy");

        String formattedDate = sdf.format(date);

        holder.tvTitle.setText(applications.get(position).getName());
        holder.tvArtist.setText(applications.get(position).getArtist());
        holder.tvReleaseDate.setText(Html.fromHtml("<b>"+ "Release Date: " + "</b>" + formattedDate));
        holder.tvSummary.setText(applications.get(position).getSummary());
        Picasso.with(context).load(applications.get(position).getImageURL()).into(holder.ivIcon);

    }

    @Override
    public int getItemCount() {
        return applications.size();
    }

    class ApplicationsViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitle;
        private TextView tvArtist;
        private TextView tvReleaseDate;
        private TextView tvSummary;
        private ImageView ivIcon;
        private boolean isExpanded = false;
        private RecyclerView recyclerView;
        private CardView cardView;

        public ApplicationsViewHolder(View itemView) {
            super(itemView);

            Log.i(TAG, "ApplicationsViewHolder: VIEW VIEW");
            
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvArtist = itemView.findViewById(R.id.tv_artist);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvSummary = itemView.findViewById(R.id.tv_summary);
            recyclerView = itemView.findViewById(R.id.recyclerView);
            cardView = itemView.findViewById(R.id.card_view);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onClick(View v) {
                    if (!isExpanded) {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        tvSummary.setVisibility(View.VISIBLE);
                        isExpanded = true;
                    } else {
                        TransitionManager.beginDelayedTransition(cardView, new AutoTransition());
                        tvSummary.setVisibility(View.GONE);
                        isExpanded = false;
                    }
                }
            });
        }
    }
}
