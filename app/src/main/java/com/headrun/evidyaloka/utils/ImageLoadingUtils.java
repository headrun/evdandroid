package com.headrun.evidyaloka.utils;

import android.net.Uri;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by HP-HP on 30-03-2016.
 */
public class ImageLoadingUtils {

    public static void load(final SimpleDraweeView imageView, final String url) {

        if (url != null && imageView != null) {
            ImageRequest imageRequest = ImageRequest.fromUri(url);
            CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                    .getEncodedCacheKey(imageRequest);
            BinaryResource resource = ImagePipelineFactory.getInstance()
                    .getMainDiskStorageCache().getResource(cacheKey);

            if (resource != null) {
                Uri uri = Uri.parse(url);
                DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                        .setUri(uri)
                        .build();

                imageView.setController(draweeController);
            } else {
                networkLoad(imageView, url);
            }
        }
    }

    private static void networkLoad(final SimpleDraweeView imageView, final String url) {

        Uri uri = Uri.parse(url);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setImageRequest(request)
                .setTapToRetryEnabled(true)
                .build();

        imageView.setController(draweeController);
    }
}
