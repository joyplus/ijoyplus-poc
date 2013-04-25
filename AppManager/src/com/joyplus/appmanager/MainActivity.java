package com.joyplus.appmanager;

import java.util.Observable;
import java.util.Observer;

import com.lenovo.lsf.installer.PackageInstaller;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MainActivity extends Activity  implements Observer{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 PackageInstaller pi = new PackageInstaller(MainActivity.this);
	     pi.addObserver(this);
//	        pi.instatll("/mnt/sdcard/test.apk","com.example.test");
	     pi.instatll("/sdcard/joyplus.apk", "com.joyplus");
//	        pi.uninstall("test.com.androidTest8");
//	     pi.uninstall("com.joyplus");
	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		
	}

}
