package org.godotengine.godot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import javax.microedition.khronos.opengles.GL10;

public class AdMob extends Godot.SingletonBase {

    protected Activity appActivity;
    protected Context appContext;
    private int instanceId = 0;
    private RewardedVideoAd mRewardedVideoAd;


    public void getInstanceId(int pInstanceId) {
        // You will need to call this method from Godot and pass in the get_instance_id().
        instanceId = pInstanceId;
    }

    static public Godot.SingletonBase initialize(Activity p_activity) {
        return new AdMob(p_activity);
    }

    public AdMob(Activity p_activity) {
        // Register class name and functions to bind.
        registerClass("AdMob", new String[]
                {
                        "getInstanceId",
                        "show_rewarded_video",
                        "request_rewarded_video",
                        "is_rewarded_video_loaded"

                });
        this.appActivity = p_activity;
        this.appContext = appActivity.getApplicationContext();
        // You might want to try initializing your singleton here, but android
        // threads are weird and this runs in another thread, so to interact with Godot you usually have to do.
        p_activity.runOnUiThread(new Runnable() {
            public void run() {

                MobileAds.initialize(appActivity, "ca-app-pub-3940256099942544~3347511713");
                mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(appActivity);
                mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
                    @Override
                    public void onRewarded(RewardItem reward) {
                        GodotLib.calldeferred(instanceId, "_on_rewarded", new Object[] {reward.getAmount()});
                    }

                    @Override
                    public void onRewardedVideoAdLeftApplication() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_leftApplication", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoAdClosed() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_closed", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoAdFailedToLoad(int errorCode) {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_failed", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoAdLoaded() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_loaded", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoAdOpened() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_opened", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoStarted() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_started", new Object[] {null});
                    }

                    @Override
                    public void onRewardedVideoCompleted() {
                        GodotLib.calldeferred(instanceId, "_on_rewarded_video_completed", new Object[] {null});
                    }
                });


            }
        });

    }

    // Forwarded callbacks you can reimplement, as SDKs often need them.

    protected void onMainActivityResult(int requestCode, int resultCode, Intent data) {
    }

    protected void onMainRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    protected void onMainPause() {
        mRewardedVideoAd.pause(appActivity);
    }

    protected void onMainResume() {
        mRewardedVideoAd.resume(appActivity);
    }

    protected void onMainDestroy() {
        mRewardedVideoAd.destroy(appActivity);
    }

    public boolean is_rewarded_video_loaded() {
        return mRewardedVideoAd.isLoaded();
    }

    public void request_rewarded_video(final String unit_id) {
        appActivity.runOnUiThread(new Runnable() {
            public void run() {
                mRewardedVideoAd.loadAd(unit_id,
                        new AdRequest.Builder().build());
            }
        });
    }

    public void show_rewarded_video() {
        appActivity.runOnUiThread(new Runnable() {
            public void run() {
                mRewardedVideoAd.show();
            }
        });
    }


    protected void onGLDrawFrame(GL10 gl) {
    }

    protected void onGLSurfaceChanged(GL10 gl, int width, int height) {
    } // Singletons will always miss first 'onGLSurfaceChanged' call.

}