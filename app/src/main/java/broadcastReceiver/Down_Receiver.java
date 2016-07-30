package broadcastReceiver;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

import com.unite.administrator.test2.Login_Activity;

import unit.ActivityController;
import unit.PollingService;

/**
 * Created by Administrator on 2015/8/23.
 */
public class Down_Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, final Intent intent) {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
        dialogBuilder.setTitle("警告");
        dialogBuilder.setMessage("你已在其他地方登陆");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityController.finishAll();
                Intent intent1 = new Intent(context, Login_Activity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent1);
            }
        });
        AlertDialog alertDialog=dialogBuilder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.show();

        Intent intent1=new Intent(context, PollingService.class);
        context.stopService(intent1);
    }
}
