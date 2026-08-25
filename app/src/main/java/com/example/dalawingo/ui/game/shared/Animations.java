package com.example.dalawingo.ui.game.shared;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.airbnb.lottie.LottieAnimationView;
import com.example.dalawingo.R;

import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Animations {

    /**
     * Play a Lottie celebration animation and hide it after finishing.
     *
     * @param animView LottieAnimationView already bound in your layout (with @raw/celebrate)
     */
    public static void playFireworks(LottieAnimationView animView) {
        if (animView == null) return;

        animView.setScaleX(1.7f); // widen
        animView.setScaleY(1.7f); // heighten

        animView.setVisibility(View.VISIBLE);
        animView.playAnimation();

        animView.addAnimatorListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animView.setVisibility(View.GONE);
                animView.removeAnimatorListener(this); // cleanup
            }
        });
    }

    /** 🎉 Konfetti celebration for Unit completion */
    public static void playKonfetti(KonfettiView konfettiView, Context context) {
        if (konfettiView == null) return;

        konfettiView.setVisibility(View.VISIBLE);

        Party party = new PartyFactory(new Emitter(2, TimeUnit.SECONDS).perSecond(100))
                .spread(360)
                .setSpeedBetween(5f, 15f)
                .timeToLive(2000L)
                .fadeOutEnabled(true)
                .position(new Position.Relative(0.5, 0.3)) // drop from top center
                .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE))
                .sizes(Arrays.asList(new Size(12, 5f, 0.2f)))
                .colors(Arrays.asList(
                        ContextCompat.getColor(context, R.color.pastel_red),
                        ContextCompat.getColor(context, R.color.pastel_yellow),
                        ContextCompat.getColor(context, R.color.pastel_blue),
                        ContextCompat.getColor(context, R.color.pastel_green)
                ))
                .build();

        konfettiView.start(party);
    }


}
