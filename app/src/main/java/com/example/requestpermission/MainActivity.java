package com.example.requestpermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView text_permission;
    private Button btn_requestPermission;
    public static final int REQUEST_CODE_BLUETOOTH =100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_permission=findViewById(R.id.text_permission);
        btn_requestPermission=findViewById(R.id.btn_requestPermission);

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT)== PackageManager.PERMISSION_GRANTED){
            text_permission.setText("Permission Granted.............");
        }else {
            text_permission.setText("Permission Not Granted.............");
        }

        btn_requestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED){

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_CONNECT)){
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("We Need The Permission for ...")
                                    .setCancelable(true)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_CONNECT},REQUEST_CODE_BLUETOOTH);
                                        }
                                    }).show();

                    }else {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.BLUETOOTH_CONNECT},REQUEST_CODE_BLUETOOTH);
                    }
                }else {
                    text_permission.setText("Permission Granted.............");
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_BLUETOOTH){
            if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                text_permission.setText("Permission Granted.............");
            }else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.BLUETOOTH_CONNECT)){
//                DENIED PERMISSION....
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("You have permanently denied this permission,go to settings to enable it")
                            .setPositiveButton("go to settings", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    gotoApplicationSettings();
                                }
                            })
                            .setNegativeButton("Cancle",null)
                            .setCancelable(false)
                            .show();
                }else {
                    text_permission.setText("Permission Not Granted.............");
                }
            }
        }
    }

    private void gotoApplicationSettings(){
        Intent intent=new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri=Uri.fromParts("package",this.getPackageName(),null);
        intent.setData(uri);
        startActivity(intent);
    }
}