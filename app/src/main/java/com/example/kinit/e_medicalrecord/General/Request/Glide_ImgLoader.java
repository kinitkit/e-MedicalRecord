package com.example.kinit.e_medicalrecord.General.Request;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.kinit.e_medicalrecord.R;

public class Glide_ImgLoader {

    public static void with(Context context, String loadString, int placeHolderResourceId, int errorResourceId, final ImageView intoImageView) {
        Glide.with(context)
                .load(UrlString.getImageUrl(loadString))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        e.printStackTrace();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                /*.placeholder((placeHolderResourceId != 0) ? placeHolderResourceId : R.mipmap.icon_user_default)*/
                .error((errorResourceId != 0) ? errorResourceId : R.mipmap.icon_user_default)
                .into(intoImageView);
    }
}
