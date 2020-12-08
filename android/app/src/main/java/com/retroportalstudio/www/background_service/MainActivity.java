package com.retroportalstudio.www.background_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;


public class MainActivity extends FlutterActivity {

  private Intent forService;
  BroadcastReceiver receiver;
  EventChannel.EventSink flutter_event_channel;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    GeneratedPluginRegistrant.registerWith(this);

    forService = new Intent(MainActivity.this,MyService.class);

    new MethodChannel(getFlutterView(),"com.retroportalstudio.messages")
            .setMethodCallHandler(new MethodChannel.MethodCallHandler() {
      @Override
      public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
        if(methodCall.method.equals("startService")){
          startService();
//          resultData = result;
          result.success("Service Started");
        }
      }
    });

    registerBroadcast();
    streamDataToFlutter();


  }

  private void registerBroadcast() {
    receiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        Log.e("Broadcast from Activity","saras lyo");
        if(flutter_event_channel!=null)
          flutter_event_channel.success("this is string data");
      }
    };

    IntentFilter filter = new IntentFilter();
    filter.addAction("SOME_ACTION");
    registerReceiver(receiver, filter);
  }

  private void streamDataToFlutter() {
    String STREAM_CHANNEL = "wingquest.stablekernel.io/speech";
    new EventChannel(getFlutterView(), STREAM_CHANNEL).setStreamHandler(
            new EventChannel.StreamHandler() {
              @Override
              public void onListen(Object args, EventChannel.EventSink events) {
                Log.w("TAG", "adding listener");
                flutter_event_channel = events;
              }

              @Override
              public void onCancel(Object args) {
                Log.w("TAG", "cancelling listener");
              }
            }
    );
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    unregisterReceiver(receiver);
      flutter_event_channel.endOfStream();
  }

  private void startService(){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
      startForegroundService(forService);
    } else {
      startService(forService);
    }
  }



}
