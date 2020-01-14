package com.ctb_open_car.view;

import android.app.Activity;
import android.view.animation.Animation;

public class DisplayNextView
    implements Animation.AnimationListener {

        Object obj;

        // 动画监听器的构造函数
        Activity ac;
        int order;

    public DisplayNextView(Activity ac, int order) {
            this.ac = ac;
            this.order = order;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            doSomethingOnEnd(order);
        }

        public void onAnimationRepeat(Animation animation) {
        }

        private final class SwapViews implements Runnable {
            public void run() {
                switch (order) {
                    case Constants.KEY_FIRST_INVERSE:
//                        ((TranslateLayout) ac).jumpToSecond();
                        break;
                    case Constants.KEY_SECOND_CLOCKWISE:
//                        ((Second) ac).jumpToFirst();
                        break;
                }
            }
        }

        //动画完成的监听
        public void doSomethingOnEnd(int _order) {
            switch (_order) {
                case Constants.KEY_FIRST_INVERSE:
//                    ((TranslateLayout) ac).layout1.post(new SwapViews());
                    break;

                case Constants.KEY_SECOND_CLOCKWISE:
//                    ((Second) ac).layout2.post(new SwapViews());
                    break;
            }
        }

    public class Constants {//相关常量

        public final static int KEY_FIRST_INVERSE = 1;

        public final static int KEY_FIRST_CLOCKWISE = 2;

        public final static int KEY_SECOND_INVERSE = 3;

        public final static int KEY_SECOND_CLOCKWISE = 4;
    }
}
