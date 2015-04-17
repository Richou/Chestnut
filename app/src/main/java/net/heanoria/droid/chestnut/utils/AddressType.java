package net.heanoria.droid.chestnut.utils;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

public class AddressType {

    public static Drawable getAddressTypeDrawable(Resources resources, int resourceId) {
        Drawable drawable = resources.getDrawable(resourceId);
        int width = drawable.getMinimumWidth();
        int height = drawable.getMinimumHeight();
        drawable.setBounds(0,0, width, height);

        return drawable;
    }
}
