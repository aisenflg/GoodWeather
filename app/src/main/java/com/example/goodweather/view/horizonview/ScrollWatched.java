package com.example.goodweather.view.horizonview;

/**
 * 定义滑动监听接口
 */
public interface ScrollWatched {
    void addWatcher(ScrollWatcher watcher);
    void removeWatcher(ScrollWatcher watcher);
    void notifyWatcher(int x);
}
