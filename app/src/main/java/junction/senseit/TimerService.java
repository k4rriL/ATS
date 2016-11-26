package junction.senseit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;


public class TimerService extends Service {

    // variables
    MyCounter timer;


    @Override
    public void onCreate() {
        timer = new MyCounter(30 * 60 * 1000, 1000);//counter of 30minutes and tick interval is //1 second(i.e.1000) you can increase its limit whatever you want as per your requirement
        super.onCreate();
        timer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyCounter extends CountDownTimer {

        public MyCounter(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @SuppressWarnings("static-access")
        @Override
        public void onFinish() {
            //timer finished 30 minutes
            stopSelf();//to stop service after counter stop
        }


        @Override
        public void onTick(long millisUntilFinished) {
            //timer clock tick event after each 1 second
        }
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        timer.cancel();
        super.onDestroy();
        // call start servce here for lifetime running of service
        // startService(new Intent(this, TimerService.class));

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

}