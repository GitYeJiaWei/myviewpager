package com.example.yjw.Animation.AnimationTest;

import android.animation.TypeEvaluator;

/**
 * 首先在evaluate()方法当中获取到颜色的初始值和结束值，
 * 并通过字符串截取的方式将颜色分为RGB三个部分，并将RGB的值转换成十进制数字，
 * 那么每个颜色的取值范围就是0-255。接下来计算一下初始颜色值到结束颜色值之间的差值，
 * 这个差值很重要，决定着颜色变化的快慢，如果初始颜色值和结束颜色值很相近，
 * 那么颜色变化就会比较缓慢，而如果颜色值相差很大，比如说从黑到白，
 * 那么就要经历255*3这个幅度的颜色过度，变化就会非常快。

 *那么控制颜色变化的速度是通过getCurrentColor()这个方法来实现的，
 *这个方法会根据当前的fraction值来计算目前应该过度到什么颜色，并
 *且这里会根据初始和结束的颜色差值来控制变化速度，最终将计算出的颜色进行返回。

 *最后，由于我们计算出的颜色是十进制数字，
 *这里还需要调用一下getHexString()方法把它们转换成十六进制字符串，
 *再将RGB颜色拼装起来之后作为最终的结果返回
 */

public class ColorEvaluator implements TypeEvaluator {

    private int mCurrentRed = -1;

    private int mCurrentGreen = -1;

    private int mCurrentBlue = -1;

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        String startColor = (String) startValue;
        String endColor = (String) endValue;
        /**
         * Integer.parseInt(str,16) 是把16进制字符串转10进制int型
         * "#0000FF"
         */

        int startRed = Integer.parseInt(startColor.substring(1, 3), 16);
        int startGreen = Integer.parseInt(startColor.substring(3, 5), 16);
        int startBlue = Integer.parseInt(startColor.substring(5, 7), 16);
        int endRed = Integer.parseInt(endColor.substring(1, 3), 16);
        int endGreen = Integer.parseInt(endColor.substring(3, 5), 16);
        int endBlue = Integer.parseInt(endColor.substring(5, 7), 16);
        // 初始化颜色的值
        if (mCurrentRed == -1) {
            mCurrentRed = startRed;
        }
        if (mCurrentGreen == -1) {
            mCurrentGreen = startGreen;
        }
        if (mCurrentBlue == -1) {
            mCurrentBlue = startBlue;
        }
        // 计算初始颜色和结束颜色之间的差值,取绝对值
        int redDiff = Math.abs(startRed - endRed);
        int greenDiff = Math.abs(startGreen - endGreen);
        int blueDiff = Math.abs(startBlue - endBlue);
        int colorDiff = redDiff + greenDiff + blueDiff;
        if (mCurrentRed != endRed) {
            mCurrentRed = getCurrentColor(startRed, endRed, colorDiff, 0,
                    fraction);
        } else if (mCurrentGreen != endGreen) {
            mCurrentGreen = getCurrentColor(startGreen, endGreen, colorDiff,
                    redDiff, fraction);
        } else if (mCurrentBlue != endBlue) {
            mCurrentBlue = getCurrentColor(startBlue, endBlue, colorDiff,
                    redDiff + greenDiff, fraction);
        }
        // 将计算出的当前颜色的值组装返回
        String currentColor = "#" + getHexString(mCurrentRed)
                + getHexString(mCurrentGreen) + getHexString(mCurrentBlue);
        return currentColor;
    }

    /**
     * 根据fraction值来计算当前的颜色。
     */
    private int getCurrentColor(int startColor, int endColor, int colorDiff,
                                int offset, float fraction) {
        int currentColor;
        if (startColor > endColor) {
            currentColor = (int) (startColor - (fraction * colorDiff - offset));
            if (currentColor < endColor) {
                currentColor = endColor;
            }
        } else {
            currentColor = (int) (startColor + (fraction * colorDiff - offset));
            if (currentColor > endColor) {
                currentColor = endColor;
            }
        }
        return currentColor;
    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    private String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

}
