package im.ene.toro.sample.feature.facebook.timeline;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.squareup.picasso.Picasso;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.sample.R;
import im.ene.toro.sample.feature.facebook.AlbadiyaTimelineFragment;
import im.ene.toro.sample.feature.facebook.PostsTimlineFragment;

/**
 * Created by T on 17-03-2017.
 */

public class PostsPhotoViewHolder extends PostsTimelineViewHolder {

    static final int LAYOUT_RES = R.layout.post_photo;

    private TimelineItem.PhotoItem photoItem;
    TimelineAdapter adapter;
    private ImageView mThumbnail;
    private TextView mInfo;
    private CircleImageView mThumbnailUser;
    private TextView mInfoUser;
    private TextView user_desc;
    private TextView time;
    private ImageView user_like;
    private ImageView download;
    private ImageView share_it;
    private TextView  no_of_likes;
    private TextView  no_of_views;
    private ImageView delete_btn;
    AlbadiyaTimelineFragment albadiyaTimelineFragment;
    String post_id,get_user_like;
    String member_id;
    PostsTimlineFragment postsTimlineFragment;
    private ImageView  heartAnim;




    public PostsPhotoViewHolder(View itemView) {
        super(itemView);
        mThumbnail = (ImageView) itemView.findViewById(R.id.photoviewimage);
        mInfo = (TextView) itemView.findViewById(R.id.info);
        mThumbnailUser = (CircleImageView) itemView.findViewById(R.id.tumbnailuser);
        mInfoUser = (TextView) itemView.findViewById(R.id.infouser);
        user_desc = (TextView) itemView.findViewById(R.id.user_desc);
        time  = (TextView) itemView.findViewById(R.id.time);
        user_like = (ImageView) itemView.findViewById(R.id.user_like);
        download = (ImageView) itemView.findViewById(R.id.download);
        //share_it = (ImageView) itemView.findViewById(R.id.share_it);
        no_of_likes = (TextView) itemView.findViewById(R.id.no_of_likes);
        no_of_views = (TextView) itemView.findViewById(R.id.no_of_views);
        delete_btn = (ImageView) itemView.findViewById(R.id.delete_btn);
        heartAnim = (ImageView) itemView.findViewById(R.id.heart_anim);
    }
    public PostsPhotoViewHolder(View itemView,AlbadiyaTimelineFragment fragment,PostsTimlineFragment timeline) {
        super(itemView);
        mThumbnail = (ImageView) itemView.findViewById(R.id.photoviewimage);
        mInfo = (TextView) itemView.findViewById(R.id.info);
        mThumbnailUser = (CircleImageView) itemView.findViewById(R.id.tumbnailuser);
        mInfoUser = (TextView) itemView.findViewById(R.id.infouser);
        user_desc = (TextView) itemView.findViewById(R.id.user_desc);
        time  = (TextView) itemView.findViewById(R.id.time);
        user_like = (ImageView) itemView.findViewById(R.id.user_like);
        download = (ImageView) itemView.findViewById(R.id.download);
        // share_it = (ImageView) itemView.findViewById(R.id.share_it);
        no_of_likes = (TextView) itemView.findViewById(R.id.no_of_likes);
        no_of_views = (TextView) itemView.findViewById(R.id.no_of_views);
        delete_btn = (ImageView) itemView.findViewById(R.id.delete_btn);
        heartAnim = (ImageView) itemView.findViewById(R.id.heart_anim);
        albadiyaTimelineFragment = fragment;
        postsTimlineFragment = timeline;


    }

    @Override public void bind(final RecyclerView.Adapter adapter, @Nullable final Object object) {
        if (!(object instanceof TimelineItem)
                || !(((TimelineItem) object).getEmbedItem() instanceof TimelineItem.PhotoItem)) {
            throw new IllegalArgumentException("Only PhotoItem is accepted");
        }

        this.photoItem = (TimelineItem.PhotoItem) ((TimelineItem) object).getEmbedItem();
        this.mInfoUser.setText((((TimelineItem) object).getAuthor().getUserName()));
        Picasso.with(itemView.getContext()).load((((TimelineItem) object).getAuthor().getUserUrl())).into(mThumbnailUser);
        this.user_desc.setText(((TimelineItem) object).getAuthor().getUserDescription());
        Picasso.with(itemView.getContext()).load(this.photoItem.getPhotoUrlstr()).placeholder(R.drawable.placeholder).into(mThumbnail);
        this.time.setText(((TimelineItem) object).getAuthor().getUserTime());
        this.no_of_likes.setText(((TimelineItem) object).getAuthor().getUserLikes());
        this.no_of_views.setText(((TimelineItem) object).getAuthor().getUserViews());



        mThumbnailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsTimlineFragment.go_to_user_profile(((TimelineItem) object).getAuthor().getPersonId());
            }
        });

        mInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postsTimlineFragment.go_to_user_profile(((TimelineItem) object).getAuthor().getPersonId());
            }
        });

        Ion.with(itemView.getContext())
                .load(Settings.SERVER_URL+"view.php")
                .setBodyParameter("post_id",((TimelineItem) object).getAuthor().getUserId())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.e("views_response",result.get("status").getAsString());
                            if (result.get("status").getAsString().equals("Success")){
                                //Toast.makeText(itemView.getContext(),result.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                            }else {
                                //Toast.makeText(itemView.getContext(),result.get("message").getAsString(),Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception ex){
                            e.printStackTrace();
                        }

                    }
                });

//    share_it.setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View view) {
//        Intent i=new Intent(Intent.ACTION_SEND);
//        i.setType("text/plain");
//        i.putExtra(Intent.EXTRA_SUBJECT,((TimelineItem) object).getAuthor().getUserName());
//        i.putExtra(Intent.EXTRA_TEXT, ((TimelineItem) object).getAuthor().getUserUrl());
//        i.putExtra(Intent.EXTRA_TEXT,((TimelineItem) object).getAuthor().getUserDescription());
//        itemView.getContext().startActivity(Intent.createChooser(i,"Share via"));
//      }
//    });

        mThumbnail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return gd.onTouchEvent(event);
            }
        });





        if (((TimelineItem) object).getAuthor().getPersonId().equals(Settings.GetUserId(itemView.getContext()))){
            delete_btn.setVisibility(View.VISIBLE);
        }else {
            delete_btn.setVisibility(View.GONE);
        }

        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_popup();

            }
        });




        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressBar progressBar = new ProgressBar(itemView.getContext());
                final ProgressDialog progressDialog = new ProgressDialog(itemView.getContext());
                progressDialog.setMessage("Please wait image is downloading..");
                progressDialog.setIndeterminate(false);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
                Ion.with(itemView.getContext())
                        .load(photoItem.getPhotoUrlstr())
                        .progressBar(progressBar)
                        .progressDialog(progressDialog)
                        // .uploadProgressBar(progressBar)
                        .progress(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                progressDialog.setMax((int) total);
                                progressDialog.setProgress((int) downloaded);
                                System.out.println("" + (int) downloaded + " / " + (int) total);
                            }
                        })
                        .write(new File("/sdcard/Albadiya" + (((TimelineItem)object).getAuthor().getUserId())  + ".jpg"))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File file) {
                                if (progressDialog != null)
                                    progressDialog.dismiss();
                                Toast.makeText(itemView.getContext(), "image saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });



        post_id = ((TimelineItem) object).getAuthor().getUserId();
        get_user_like = ((TimelineItem) object).getAuthor().getUserLikes();


        if (postsTimlineFragment.get_like_id(((TimelineItem)object).getAuthor().getUserId())) {
            user_like.setBackgroundResource(R.drawable.with);
        }
        else {
            user_like.setBackgroundResource(R.drawable.without);
        }

        user_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ion.with(itemView.getContext())
                        .load(Settings.SERVER_URL+"like.php")
                        .setBodyParameter("member_id",Settings.GetUserId(itemView.getContext()))
                        .setBodyParameter("post_id",((TimelineItem) object).getAuthor().getUserId())
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
//                           if (result.get("status").getAsString().equals("Success")){
//                             Toast.makeText(itemView.getContext(),result.get("message").getAsString(),Toast.LENGTH_SHORT).show();
//                             user_like.setBackgroundResource(R.drawable.heart);
//                           }else{
//                               user_like.setBackgroundResource(R.drawable.ic_likes_vi);
//                               Toast.makeText(itemView.getContext(),result.get("message").getAsString(),Toast.LENGTH_SHORT).show();
//                             }
                                postsTimlineFragment.set_like_id(((TimelineItem)object).getAuthor().getUserId());
                                postsTimlineFragment.set_like_count(((TimelineItem)object).getAuthor().getUserId());

                                if (postsTimlineFragment.get_like_id(((TimelineItem)object).getAuthor().getUserId())) {
                                    user_like.setBackgroundResource(R.drawable.with);
                                    no_of_likes.setText( postsTimlineFragment.get_like_count(((TimelineItem)object).getAuthor().getUserId()));

                                }
                                else {
                                    user_like.setBackgroundResource(R.drawable.without);
                                    no_of_likes.setText(((TimelineItem) object).getAuthor().getUserLikes());
                                    no_of_likes.setText( postsTimlineFragment.get_like_count(((TimelineItem)object).getAuthor().getUserId()));

                                }

                            }
                        });
            }
        });



    }


    public void delete_popup() {
        AlertDialog.Builder builder2 = new AlertDialog.Builder(itemView.getContext());
        builder2.setMessage("Delete post?");
        builder2.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Ion.with(itemView.getContext())
                        .load(Settings.SERVER_URL+"post-delete.php")
                        .setBodyParameter("member_id", Settings.GetUserId(itemView.getContext()))
                        .setBodyParameter("post_id", post_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try {
                                    if (result.get("status").getAsString().equals("Success")) {
                                        Toast.makeText(itemView.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                        postsTimlineFragment.delete_post(post_id);
                                    } else {
                                        Toast.makeText(itemView.getContext(), result.get("message").getAsString(), Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception ex) {
                                    e.printStackTrace();
                                }
                            }


                        });
            }
        });
        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//        onViewHolderBound();
                Toast.makeText(itemView.getContext(), "U Clicked Cancel ", Toast.LENGTH_LONG).show();
            }

        });

        builder2.show();
    }

    final GestureDetector gd = new GestureDetector(itemView.getContext(), new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {

            Animation pulse_fade = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.pulse_fade_in);
            pulse_fade.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    heartAnim.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    heartAnim.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            heartAnim.startAnimation(pulse_fade);

            if (postsTimlineFragment.get_like_id(post_id)) {
                user_like.setBackgroundResource(R.drawable.with);
            }


            Ion.with(itemView.getContext())
                    .load(Settings.SERVER_URL+"like.php")
                    .setBodyParameter("member_id",Settings.GetUserId(itemView.getContext()))
                    .setBodyParameter("post_id",post_id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            postsTimlineFragment.set_like_id(post_id);
                            postsTimlineFragment.set_like_count(post_id);

                            if (postsTimlineFragment.get_like_id(post_id)) {
                                user_like.setBackgroundResource(R.drawable.with);
//                                user_like.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.with));
                                no_of_likes.setText( postsTimlineFragment.get_like_count(post_id));

                            }


                        }
                    });





//      user_like.setImageDrawable(itemView.getContext().getResources().getDrawable(R.drawable.with));
//      no_of_likes.setText( albadiyaTimelineFragment.get_like_count(post_id));

            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }
    });




}
