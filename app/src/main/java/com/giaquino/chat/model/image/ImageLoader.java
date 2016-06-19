package com.giaquino.chat.model.image;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * @author Gian Darren Azriel Aquino.
 */
public interface ImageLoader {

    void downloadImageInto(@NonNull String url, @NonNull ImageView imageView);
}
