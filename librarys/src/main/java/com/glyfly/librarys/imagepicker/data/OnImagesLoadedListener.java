package com.glyfly.librarys.imagepicker.data;


import com.glyfly.librarys.imagepicker.bean.ImageSet;

import java.util.List;

public interface OnImagesLoadedListener {
    void onImagesLoaded(List<ImageSet> imageSetList);
}
