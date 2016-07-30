package unit;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static data.ToMd5.stringToMD5;

/**
 * Created by Administrator on 2015/8/23.
 */
public class GetDeviceId{
    private Context context;

    public GetDeviceId(Context context){
        this.context=context;
    }


    public String getId() {
        TelephonyManager TelephonyMgr = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String szImei = TelephonyMgr.getDeviceId();

        WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        String m_szWLANMAC = wm.getConnectionInfo().getMacAddress();

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 +
                Build.BRAND.length() % 10 +
                Build.CPU_ABI.length() % 10 +
                Build.DEVICE.length() % 10 +
                Build.DISPLAY.length() % 10 +
                Build.HOST.length() % 10 +
                Build.ID.length() % 10 +
                Build.MANUFACTURER.length() % 10 +
                Build.MODEL.length() % 10 +
                Build.PRODUCT.length() % 10 +
                Build.TAGS.length() % 10 +
                Build.TYPE.length() % 10 +
                Build.USER.length() % 10;
        Log.i("ID", m_szDevIDShort);

        String ID=szImei+m_szWLANMAC+m_szDevIDShort;
        return stringToMD5(ID);
    }
}
