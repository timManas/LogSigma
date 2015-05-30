package com.illustrostudios.spirit.logsigma;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Spirit on 5/20/2015.
 */
public class BackgroundService extends Service
{
    static String workSSIDBackgrdService;
    static String currentWIFISSIDBackgrdService;
    static String startTime, endTime  = "000";
    static String Time, Date, WeekDay , Month, dateNumerical = "000";
    static String previousStartTime = "000";
    static String previousEndTime = "000";
    static String bgServiceContent;

    static long diff, diffSeconds, diffMinutes, diffHours, diffDays;
    static long prevTime;
    static boolean DailyBool, WeeklyBool, MonthlyBool;

    static Calendar c;
    static SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    static java.util.Date date1, date2;

    static final String TAG = "My Service Demo";
    static Boolean isAtWork = false;
    static Boolean check;
    static Boolean isStartTimeCounter = false;
    Thread testThread;

    public BackgroundService()
    {
        // DO NOT DELETE THIS ...IM WATCHIN YOU
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        Toast.makeText(this, "My Service Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");
        try
        {
            check = true;
            TrackUserIsInWorkWifi();
            initialize();
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy()
    {
        Toast.makeText(this, "My Service Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");
        check = false;
        testThread.interrupt();
        testThread = null;

        System.out.println("Thread has been Stopped");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Toast.makeText(this, "service starting", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onStart");
        return START_STICKY;
    }

    //==================================================================================================================================================

    private void TrackUserIsInWorkWifi() throws ParseException
    {
        final WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        System.out.println("Check if user is in Work Wifi !!");
        final Handler mHandler = new Handler();

        testThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (check)
                {
                    try
                    {
                        testThread.sleep(1000);  // YOU CAN CHANGE THIS HOWEVER YOU LIKE
                        mHandler.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                final NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

                                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                                currentWIFISSIDBackgrdService = wifiInfo.getSSID().replace("\"", "");

                                System.out.println("WORK WIFI: " + workSSIDBackgrdService);
                                System.out.println("CURRENT WIFI HOTSPOT: " + currentWIFISSIDBackgrdService);
                                c = Calendar.getInstance();

                                SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
                                Time = time.format(c.getTime());

                                SimpleDateFormat date = new SimpleDateFormat("dd/MMMM/yyyy");
                                Date = date.format(c.getTime());

                                SimpleDateFormat weekday = new SimpleDateFormat("EEEE");
                                WeekDay = weekday.format(c.getTime());

                                SimpleDateFormat dateNumber = new SimpleDateFormat("dd");
                                dateNumerical = dateNumber.format(c.getTime());


                                CheckTimeToNotifyUser(Time, Date, WeekDay, dateNumerical);

                                if (networkInfo.isConnected())
                                {
                                    if (currentWIFISSIDBackgrdService.equals(workSSIDBackgrdService) && isAtWork == false)
                                    {
                                        isAtWork = true;
                                        System.out.println("User just entered work ! ");
                                        startTime = time.format(c.getTime());
                                        System.out.println("Start time: " + startTime);
                                        isStartTimeCounter = true;


                                    } else if (currentWIFISSIDBackgrdService.equals(workSSIDBackgrdService) && isAtWork == true)
                                    {
                                        System.out.println("User at work ! ");

                                    } else if ((!currentWIFISSIDBackgrdService.equals(workSSIDBackgrdService) || !networkInfo.isAvailable()) && isAtWork == true)
                                    {
                                        System.out.println("User just left work ! ");
                                        isAtWork = false;
                                    } else
                                    {
                                        System.out.println("User not at work ! ");
                                        isAtWork = false;
                                        endTime = time.format(c.getTime());

                                        if (isStartTimeCounter)
                                        {
                                            CalculateDiffStartEndTimes();
                                        }
                                    }
                                } else
                                {
                                    System.out.println("NO WIFI DETECTED !!!");
                                    isAtWork = false;

                                    endTime = time.format(c.getTime());

                                    if (isStartTimeCounter)
                                    {
                                        CalculateDiffStartEndTimes();
                                    }
                                }

                            }

                        });
                    }
                    catch (Exception e)
                    {
                        // TODO: handle exception
                    }
                }
            }
        });

        testThread.start();

    }


    // ===================================================================================================================================================================================


    private void CalculateDiffStartEndTimes()
    {
        System.out.println("Logged Start time: " + startTime);
        System.out.println("Logged End time: " + endTime);

        if ((!previousStartTime.equals(startTime)) && (!previousEndTime.equals(endTime)))
        {
            System.out.println("Recorded ==========================================================");

            try
            {
                date1 = time.parse(startTime);
                date2 = time.parse(endTime);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            diff = date2.getTime() - date1.getTime();

            diffSeconds = diff / 1000 % 60;
            diffMinutes = diff / (60 * 1000) % 60;
            diffHours = diff / (60 * 60 * 1000) % 24;
            diffDays = diff / (24 * 60 * 60 * 1000);

            System.out.println(diffDays + " days ");
            System.out.println(diffHours + " hours ");
            System.out.println(diffMinutes + " minutes ");
            System.out.println(diffSeconds + " seconds");

            previousStartTime = startTime;
            previousEndTime = endTime;

            bgServiceContent = "[" + Date + " " + "Start: " + startTime + " " + "End: " + endTime + " " + "Shift Duration: " + diffHours + " hours " + diffMinutes + " minutes " + " ]";

            GetTotalTimeToPreferences();
            SaveTotalTimeToPreferences();
            LogSendStartEndDiff();
        }
    }

    // ===================================================================================================================================================================================


    private void LogSendStartEndDiff()
    {
        System.out.println("Send Files to be Put in TEXT FILES ============================= ");
        String fileNameTimeSheet = "TimeSheetReport.txt";
        FileOperations fopMonthly = new FileOperations();
        try
        {
            fopMonthly.write(fileNameTimeSheet, bgServiceContent);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("WRITING FAILED =(");
        }
    }


    // ===================================================================================================================================================================================
    // NEW

    private void initialize()
    {
        String SharedPreferenceName = "SharedPref";
        String wifiKey = "WifiKey";
        String DVMKeyDaily = "DVMKeyDaily";
        String DVMKeyWeekly = "DVMKeyWeekly";
        String DVMKeyMonthly = "DVMKeyMonthly";

        SharedPreferences settings = getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE); //1
        workSSIDBackgrdService = settings.getString(wifiKey, "");

        DailyBool = settings.getBoolean(DVMKeyDaily, false);
        WeeklyBool = settings.getBoolean(DVMKeyWeekly, false);
        MonthlyBool = settings.getBoolean(DVMKeyMonthly, false);

        System.out.println("============================================== SHARED PREFERENCE WIFIKEY: " + workSSIDBackgrdService);
        System.out.println("============================================== SHARED PREFERENCE DAILY: " + DailyBool);
        System.out.println("============================================== SHARED PREFERENCE WEEKLY: " + WeeklyBool);
        System.out.println("============================================== SHARED PREFERENCE MONTHLY: " + MonthlyBool);

    }

    // ===================================================================================================================================================================================

    private void CheckTimeToNotifyUser(String timeFormat, String dateFormat, String Week, String dayNumber )
    {
        if (DailyBool)
        {
            if (timeFormat.equals("23:30:01"))
            {
                System.out.println("DAILY NOTIFICATION HAS BEEN SENT ------------------------------------------------------------------------->");
                createNotification();
            }

        } else if (WeeklyBool)
        {
            if (timeFormat.equals("23:30:01") && Week.equals("Friday"))  // && Week is end of the week
            {
                System.out.println("WEEKLY NOTIFICATION HAS BEEN SENT ------------------------------------------------------------------------->");
                createNotification();
            }

        } else if (MonthlyBool)
        {
            if (timeFormat.equals("23:30:01") && dayNumber.equals("01")) // // && Month is end of the month
            {
                System.out.println("MONTHLY NOTIFICATION HAS BEEN SENT ------------------------------------------------------------------------->");
                createNotification();
            }
        }
    }

    // ===================================================================================================================================================================================

    public void createNotification()
    {
        int NOTIFICATION_ID = 12345;
        Intent resultIntent = new Intent(this, Main.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logsigmaicon);
        mBuilder.setContentTitle("Reminder to Send your TimeSheet");
        mBuilder.setContentText("Click here to send your Time Sheet");

        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        sendBroadcast(resultIntent);
    }




    private void GetTotalTimeToPreferences()
    {
        String timeKey = "timeKey";
        prevTime =  getSharedPreferences("SharedPref", Context.MODE_PRIVATE).getLong(timeKey, 0);
    }


    private void SaveTotalTimeToPreferences()
    {
        String timeKey = "timeKey";
        String SharedPreferenceName = "SharedPref";
        Long newTime = diff + prevTime;

        SharedPreferences WifiSharedPref = getApplicationContext().getSharedPreferences(SharedPreferenceName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = WifiSharedPref.edit();
        editor.putLong(timeKey, newTime);
        editor.commit();
    }





} // End of class
