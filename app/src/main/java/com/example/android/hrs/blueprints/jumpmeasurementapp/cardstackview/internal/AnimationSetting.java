package com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.internal;

import android.view.animation.Interpolator;

import com.example.android.hrs.blueprints.jumpmeasurementapp.cardstackview.Direction;

public interface AnimationSetting {
    Direction getDirection();
    int getDuration();
    Interpolator getInterpolator();
}
