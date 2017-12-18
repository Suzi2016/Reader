package com.suzi.reader.horizontal_readmodel;

/**
 * Created by Suzi on 2016/12/23.
 */

public interface OnReadStateChangeListener
{
    void onChapterChanged();

    void onPageChanged(int index);

    void onLoadChapterFailure();

    void onCenterClick();

    void onFlip();
}
