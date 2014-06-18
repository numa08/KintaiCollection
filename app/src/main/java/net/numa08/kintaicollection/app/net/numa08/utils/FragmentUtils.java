package net.numa08.kintaicollection.app.net.numa08.utils;

import android.app.Fragment;

public class FragmentUtils {

    public static boolean isActive(Fragment fragment) {
        return fragment.isAdded() && !fragment.isDetached() && !fragment.isRemoving() && fragment.isResumed();
    }
}
