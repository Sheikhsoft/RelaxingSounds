package com.msevgi.relaxingsounds.utils;

import android.support.annotation.IntDef;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class ToolbarOptions {
    public final static int TOGGLE_NONE = 1;
    public final static int TOGGLE_BACK = 2;
    private String mTitle;
    private int mToggleType;

    public static ToolbarOptions getDefaultToolbarOptions() {
        ToolbarOptions options = new ToolbarOptions();
        options.setTitle("");
        options.setToggleType(TOGGLE_NONE);
        return options;
    }

    public String getTitle() {
        return mTitle;
    }

    public ToolbarOptions setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public int getToggleType() {
        return mToggleType;
    }

    public ToolbarOptions setToggleType(@toggleType int mToggleType) {
        this.mToggleType = mToggleType;
        return this;
    }

    @IntDef({TOGGLE_NONE, TOGGLE_BACK})
    public @interface toggleType {
    }
}
