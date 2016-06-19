package com.giaquino.chat.model.image;

import android.support.annotation.NonNull;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

/**
 * @author Gian Darren Azriel Aquino.
 */
public class PicassoImageLoader implements ImageLoader {

    private final Picasso picasso;

    public PicassoImageLoader(@NonNull Picasso picasso) {
        this.picasso = picasso;
    }

    @Override public void downloadImageInto(@NonNull String url, @NonNull ImageView imageView) {
        picasso.load(url).fit().centerCrop().into(imageView);
    }
}
