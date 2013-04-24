package com.joyplus.installer;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Observable;

public class PackageInstaller extends Observable
{
  private static final String TAG = "PackInstaller";
  public static final String KEY_PACKAGE_NAME = "packageName";
  public static final String KEY_RESULT_CODE = "resultCode";
  public static final String KEY_RESULT_DESC = "resultDescription";
  private static final String ACTION_INSTALL = "INSTALL_";
  private static final String ACTION_DELETE = "DELETE_";
  private PackageManager mPackageManager = null;

  public PackageInstaller(Context context) {
    this.mPackageManager = context.getPackageManager();
  }

  public static Object invoke(Object c, String m, Class<?>[] cls, Object[] args) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException
  {
    Method method = c.getClass().getMethod(m, cls);

    return method.invoke(c, args);
  }

  private boolean installPackage(Uri packageURI, IPackageInstallObserver observer, int flags, String installerPackageName) {
    try {
      Method method = this.mPackageManager.getClass().getMethod("installPackage", new Class[] { Uri.class, IPackageInstallObserver.class, Integer.TYPE, String.class });
      method.invoke(this.mPackageManager, new Object[] { packageURI, observer, Integer.valueOf(flags), installerPackageName });
      return true;
    }
    catch (SecurityException e) {
      Log.e("PackInstaller", "No permission to invoke PackageManager.installPackage", e);
    } catch (NoSuchMethodException e) {
      Log.e("PackInstaller", "No such method: PackageManager.installPackage", e);
    } catch (IllegalArgumentException e) {
      Log.e("PackInstaller", "Illegal argument to invoke PackageManager.installPackage", e);
    } catch (IllegalAccessException e) {
      Log.e("PackInstaller", "Illegal access to invoke PackageManager.installPackage", e);
    } catch (InvocationTargetException e) {
      Log.e("PackInstaller", "Failed to invoke PackageManager.installPackage", e);
    }

    return false;
  }

  private String getResultDescription(String action, int resultCode) {
    try {
      Field[] fields = this.mPackageManager.getClass().getFields();
      for (Field f : fields) {
        if ((f.getName().startsWith(action)) && (f.getInt(this.mPackageManager) == resultCode)) {
          return f.getName();
        }
      }
      return null;
    }
    catch (SecurityException e) {
      Log.e("PackInstaller", "No permission to invoke PackageManager.installPackage", e);
    }
    catch (IllegalArgumentException e) {
      e.printStackTrace();
    }
    catch (IllegalAccessException e) {
      e.printStackTrace();
    }

    return null;
  }

  private boolean deletePackage(String packageName, IPackageDeleteObserver observer, int flags) {
    try {
      Method method = this.mPackageManager.getClass().getMethod("deletePackage", new Class[] { String.class, IPackageDeleteObserver.class, Integer.TYPE });
      method.invoke(this.mPackageManager, new Object[] { packageName, observer, Integer.valueOf(flags) });
      return true;
    }
    catch (SecurityException e) {
      Log.e("PackInstaller", "No permission to invoke PackageManager.deletePackage", e);
    } catch (NoSuchMethodException e) {
      Log.e("PackInstaller", "No such method: PackageManager.deletePackage", e);
    } catch (IllegalArgumentException e) {
      Log.e("PackInstaller", "Illegal argument to invoke PackageManager.deletePackage", e);
    } catch (IllegalAccessException e) {
      Log.e("PackInstaller", "Illegal access to invoke PackageManager.deletePackage", e);
    } catch (InvocationTargetException e) {
      Log.e("PackInstaller", "Failed to invoke PackageManager.deletePackage", e);
    }

    return false;
  }

  public boolean instatll(String pathFileApk, String packageName) {
    int installFlags = 0;

    if (packageName != null)
      try {
        PackageInfo pi = this.mPackageManager.getPackageInfo(packageName, 
          8192);
        if (pi != null) {
          installFlags |= 2;
          Log.i("PackInstaller", "Package already exsists, replace existing!!");
        }
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
      }
    Uri mPackageURI = Uri.parse(pathFileApk);
    PackageInstallObserver observer = new PackageInstallObserver();
    return installPackage(mPackageURI, observer, installFlags, packageName);
  }

  public void uninstall(String packageName)
  {
    PackageDeleteObserver observer = new PackageDeleteObserver();
    deletePackage(packageName, observer, 0);
  }

  private class PackageDeleteObserver extends IPackageDeleteObserver.Stub
  {
    public void packageDeleted(String packageName, int returnCode)
      throws RemoteException
    {
      Bundle options = new Bundle();
      options.putString("packageName", packageName);
      options.putInt("resultCode", returnCode);
      options.putString("resultDescription", 
        PackageInstaller.this.getResultDescription("DELETE_", returnCode));
      Log.i("PackInstaller", "====UNINSTALL_COMPLETE:" + packageName + " returnCode:" + returnCode);
      PackageInstaller.this.setChanged();
      PackageInstaller.this.notifyObservers(options);
    }
  }

  private class PackageInstallObserver extends IPackageInstallObserver.Stub
  {
    public void packageInstalled(String packageName, int returnCode)
    {
      Bundle options = new Bundle();
      options.putString("packageName", packageName);
      options.putInt("resultCode", returnCode);
      options.putString("resultDescription", 
        (returnCode == 1) ? "INSTALL_SUCCEEDED" : PackageInstaller.this.getResultDescription("INSTALL_", returnCode));

      PackageInstaller.this.setChanged();
      PackageInstaller.this.notifyObservers(options);
      Log.i("PackInstaller", "====INSTALL_COMPLETE:" + packageName + " returnCode:" + returnCode);
    }
  }
}