//
// Created by 蓝华建 on 2020/3/19.
//

#include "giflib/gif_lib.h"
#include <android/bitmap.h>

#ifndef GIFLIBRARY_GIF_H
#define GIFLIBRARY_GIF_H

#endif //GIFLIBRARY_GIF_H

int drawFrame(GifFileType *gifFileType, AndroidBitmapInfo *info, int *pixels, int frame_no,
              bool force_dispose_1);
