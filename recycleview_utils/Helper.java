package com.heysound.superstar.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.callback.SaveCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.heysound.framework.utils.NetworkUtils;
import com.heysound.superstar.R;
import com.heysound.superstar.module.common.ChorseImageActivity;
import com.heysound.superstar.module.common.PayActivity;
import com.heysound.superstar.module.common.bus.BusProvider;
import com.heysound.superstar.module.common.bus.CacheInfoChangeEvent;
import com.heysound.superstar.module.common.bus.EventReciver;
import com.heysound.superstar.module.common.bus.PlayNetworkWarningEvent;
import com.heysound.superstar.module.common.content.item.MediaItem;
import com.heysound.superstar.module.my.BuyVipFragment;
import com.heysound.superstar.module.my.MineInfoCache;
import com.heysound.superstar.module.my.ShowRemindFragment;
import com.heysound.superstar.module.my.bus.ChorseImageChangeEvent;
import com.heysound.superstar.module.my.content.CurrentUserInfo;
import com.heysound.superstar.module.my.item.OrderInfo;
import com.heysound.superstar.module.my.item.VipInfo;
import com.heysound.superstar.module.net.oss.FileRW;
import com.readystatesoftware.viewbadger.BadgeView;
import com.squareup.otto.Subscribe;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 1 on 2015/11/11.
 */
public class Helper {

    public final static SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public final static SimpleDateFormat DATEFORMAT_ = new SimpleDateFormat("yyyy-MM-dd");
    public static final HashMap<String, String> mFileTypes = new HashMap<String, String>();
    public static ExecutorService service = Executors.newCachedThreadPool();
    static ShowViewHAtTimeHelper showViewHAtTimeHelper;

    static {
        //images
        mFileTypes.put("FFD8FF", "jpg");
        mFileTypes.put("89504E47", "png");
        mFileTypes.put("47494638", "gif");
        mFileTypes.put("424D", "bmp");
        mFileTypes.put(null, "");
        mFileTypes.put("", "");
    }

    public static String getVersionName(Context context) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void getCacheInfo(final Context context) {
        if (context==null)
            return;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (context==null||context.getCacheDir()==null||
                        TextUtils.isEmpty(context.getCacheDir().getAbsolutePath())){
                    return;
                }
                long size = 0;
                List<File> dirs = new ArrayList<>();
                File root = context.getCacheDir();
                getCacheDirs(root, dirs);


                if(null!= context.getExternalCacheDir()&&
                        !TextUtils.isEmpty( context.getExternalCacheDir().getAbsolutePath())){
                    root = context.getExternalCacheDir();

                    getCacheDirs(root, dirs);
                }

                for (int i = 0; i < dirs.size(); i++) {
                    size += dirs.get(i).length();
                }

                final Object[] result = new Object[]{size, dirs};
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        BusProvider.getInstance().post(new CacheInfoChangeEvent(result));
                    }
                });
            }
        };
        service.execute(runnable);
    }

    public static void getCacheDirs(File path, List<File> dirs) {
        if (path.isDirectory()) {
            File[] fileList = path.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                getCacheDirs(fileList[i], dirs);
            }
        } else {
            dirs.add(path);
        }
    }

    public static String retain2(double f) {
        return calcuteTwoBigDecimal(BigDecimal.valueOf(f));
    }

    public static String calcuteTwoBigDecimal(BigDecimal price){
        return price.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String retain1(double f) {
        return calcuteTwoBigDecimal1(BigDecimal.valueOf(f));
    }

    public static String calcuteTwoBigDecimal1(BigDecimal price){
        return price.setScale(1, BigDecimal.ROUND_HALF_UP).toString();
    }

    public static String getClientIp(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (netWorkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
            WifiManager wifiManager = (WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int i = wifiInfo.getIpAddress();
            StringBuilder sb = new StringBuilder();
            sb.append(i & 0xFF).append(".");
            sb.append((i >> 8) & 0xFF).append(".");
            sb.append((i >> 16) & 0xFF).append(".");
            sb.append((i >> 24) & 0xFF);
            return sb.toString();
        }
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e(Helper.class.getName(), ex.toString());
        }
        return null;
    }

    public static void showZhanWeiDialog(final Activity activity,int num, DialogUtils.TDialogListener listener){
        DialogUtils.Builder builder=new DialogUtils.Builder(activity);
        builder.setCancel("取消");
        builder.setOk("抢占");
        builder.setHeight(120);
        builder.setMsg("抢本次占位需消耗"+num+"个绿头牌");
        builder.setListener(listener);
        builder.builder().show();
    }

    public static void showTiGuanDialog(final Activity activity,int num, DialogUtils.TDialogListener listener){
        DialogUtils.Builder builder=new DialogUtils.Builder(activity);
        builder.setCancel("我认怂");
        builder.setHeight(120);
        builder.setOk("篡位");
        builder.setMsg("本次篡位需消耗" + num + "个绿头牌");
        builder.setListener(listener);
        builder.builder().show();
    }

    public static void showBeiJiXiaWeiDialog(final Activity activity,int num, DialogUtils.TDialogListener listener){
        DialogUtils.Builder builder=new DialogUtils.Builder(activity);
        builder.setCancel("我认怂");
        builder.setHeight(120);
        builder.setOk("继续抢");
        builder.setMsg("您已被篡位,\n抢" +
                "回需消耗" + num + "个绿头牌");
        builder.setListener(listener);
        builder.builder().show();
    }

    public static void showBuyGiftDialog(final Activity activity, final View v){
        DialogUtils.Builder builder=new DialogUtils.Builder(activity);
        builder.setCancel("取消");
        builder.setOk("购买");
        builder.setHeight(140);
        builder.setMsg("您拥有的绿头牌不足，请先购买");
        builder.setListener(new DialogUtils.TDialogListener() {
            @Override
            public void ok() {

                Intent intent = new Intent(activity, PayActivity.class);
                intent.putExtra(PayActivity.PAYTYPE, 0);
                VipInfo vInf = new VipInfo();
                OrderInfo orInf = new OrderInfo();
                orInf.buy_count = 0;
                orInf.order_type = 2;
                orInf.pay_type = 0;
                orInf.user_id = CurrentUserInfo.userInfo.user_id;
                orInf.client_ip = null;
                intent.putExtra(PayActivity.PAYKEY, orInf);
                activity.startActivity(intent);
                v.setClickable(true);
            }

            @Override
            public void cancel() {
               v.setClickable(true);
            }
        });
        builder.builder().show();
    }

    public static void showBuyGiftDialog(final Activity activity, final int num){
        DialogUtils.Builder builder=new DialogUtils.Builder(activity);
        builder.setCancel("取消");
        builder.setOk("购买");
        builder.setHeight(140);
        builder.setMsg("您拥有的绿头牌不足，请先购买");
        builder.setListener(new DialogUtils.TDialogListener() {
            @Override
            public void ok() {

                Intent intent = new Intent(activity, PayActivity.class);
                intent.putExtra(PayActivity.PAYTYPE, 0);
                VipInfo vInf = new VipInfo();
                OrderInfo orInf = new OrderInfo();
                orInf.buy_count = num;
                orInf.order_type = 2;
                orInf.pay_type = 0;
                orInf.user_id = CurrentUserInfo.userInfo.user_id;
                orInf.client_ip = null;
                intent.putExtra(PayActivity.PAYKEY, orInf);
                activity.startActivity(intent);
            }

            @Override
            public void cancel() {

            }
        });
        builder.builder().show();
    }

    /**
     * 显示购买直播券弹框
     *
     * @param activity
     */
    public synchronized static void showBuyTicketsDialog(final Activity activity, int hasNum, int tatolNum, final int needNum) {
        final View root = LinearLayout.inflate(activity, R.layout.buy_tickets_dialog, null);
        root.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
            }
        });
        root.findViewById(R.id.tv_buy_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
                Intent intent = new Intent(activity, PayActivity.class);
                intent.putExtra(PayActivity.PAYTYPE, 0);
                VipInfo vInf = new VipInfo();
                OrderInfo orInf = new OrderInfo();
                orInf.buy_count = needNum;
                orInf.order_type = 0;
                orInf.pay_type = 0;
                orInf.user_id = CurrentUserInfo.userInfo.user_id;
                orInf.client_ip = null;
                intent.putExtra(PayActivity.PAYKEY, orInf);
                activity.startActivity(intent);
            }
        });
        TextView textView = (TextView) root.findViewById(R.id.tv_need_tickets_num);
        textView.setText(String.valueOf(tatolNum));
        textView = (TextView) root.findViewById(R.id.tv_has_tickets_num);
        textView.setText(String.valueOf(hasNum));
        textView = (TextView) root.findViewById(R.id.tv_need_buy_tickets_num);
        textView.setText(String.valueOf(needNum));
        T.showLongWaitUI(activity, root, "", (int) calcuteDp(245, activity), (int) calcuteDp(160, activity));
    }

    /**
     * 显示购买会员弹框
     *
     * @param activity
     */
    public static void showBuyVipDialog(final FragmentManager fragmentManager, Activity activity) {
        final View root = LinearLayout.inflate(activity, R.layout.buy_vip_dialog, null);
        root.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
            }
        });
        root.findViewById(R.id.tv_buy_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                BuyVipFragment vip_fragment = new BuyVipFragment();
                ft.replace(R.id.ly_content, vip_fragment);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        });
        TextView textView = (TextView) root.findViewById(R.id.tv_dialog_title);
        textView.setVisibility(View.GONE);
        T.showLongWaitUI(activity, root, "", (int) calcuteDp(245, activity), (int) calcuteDp(120, activity));
    }

    /**
     * 显示设置提醒弹框
     *
     * @param activity
     */
    public static void showSetRemindDialog2(final Activity activity, final FragmentManager fragmentManager, final MediaItem showInfo) {
        if (MineInfoCache.getInstance().hasSetRemind(showInfo,activity)){
            DialogUtils.Builder builder=new DialogUtils.Builder(activity);
            builder.setOk("确定");
            builder.setMsg("提醒已设置");
            builder.setHeight(140);
            builder.setWidth(245);
            builder.setTitle("预告提醒");
            builder.setTitleBg(R.mipmap.ic_remind_head);
            builder.setHeadIcon(R.mipmap.ic_remind_broad);
            builder.builder().show();
            return;
        }
        final View root = LinearLayout.inflate(activity, R.layout.set_remind, null);
        root.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
            }
        });
        String timeStr = Helper.DATEFORMAT.format(new Date(showInfo.start_time));
        try {
            showInfo.start_time = Helper.DATEFORMAT.parse(timeStr).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        root.findViewById(R.id.tv_buy_vip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.dismissWaitUI();
                MineInfoCache.getInstance().saveRemind(activity, showInfo);
                T.showLong(activity, "设置提醒成功");
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                ShowRemindFragment fragment = new ShowRemindFragment();
                ft.replace(R.id.ly_content, fragment);
                ft.addToBackStack(null);
                ft.commitAllowingStateLoss();
            }
        });
        TextView textView = (TextView) root.findViewById(R.id.tv_dialog_msg);
        textView.setText(showInfo.title);

        T.showLongWaitUI(activity, root, "", (int) calcuteDp(245, activity), (int) calcuteDp(140, activity));
    }

    /**
     * 将dp值计算为实际像素点值
     * @param dp
     * @param context
     * @return
     */
    public static float calcuteDp(float dp, Context context) {
        if (context==null)
            return 0;
        DisplayMetrics displaymetrics = context.getResources().getDisplayMetrics();

        return dp * displaymetrics.density+0.5f;
    }

    public static float calcuteDp(float dp, Resources res) {
        if (res==null)
            return 0;
        DisplayMetrics displaymetrics = res.getDisplayMetrics();

        return dp * displaymetrics.density+0.5f;
    }

    public static void chorseImage(final Activity activity, final SaveCallback callback) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            T.showLong(activity, "系统版本过低，不支持换头像");
            return;
        }
        EventReciver reciver = new EventReciver() {

            @Subscribe
            public void reciver(ChorseImageChangeEvent event) {
                callback.onSuccess(event.path);
                destory();
            }
        };
        Bitmap bc = createBitmapAsWindow(activity);
        ChorseImageActivity.start(activity, bc);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void uploadHeadImg(final Activity activity, final String path, final SaveCallback callback) {
        final Handler uiHandler = new Handler(activity.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        //执行上传
                        break;
                    case 2:
                        //上传进度
                        break;
                    case 3:
                        //上传成功
                        String key = (String) ((Object[]) msg.obj)[0];
                        String filePath = (String) ((Object[]) msg.obj)[1];
                        try {
                            key = activity.getPackageManager().getApplicationInfo(activity.getPackageName(), PackageManager.GET_META_DATA).metaData.getString("app_oss_host") + key;
                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                        CurrentUserInfo.userInfo.pic_url = key;
                        callback.onSuccess(filePath);
                        break;
                    case 4:
                        //上传失败
                        callback.onFailure("", null);
                        break;
                }
            }
        };
        String uname = "";
        String end = "";
        if (CurrentUserInfo.userInfo.pic_url==null)
            CurrentUserInfo.userInfo.pic_url="";

        if (CurrentUserInfo.userInfo.pic_url.lastIndexOf(".") > 1)
            end = CurrentUserInfo.userInfo.pic_url.substring(CurrentUserInfo.userInfo.pic_url.lastIndexOf(".") + 1, CurrentUserInfo.userInfo.pic_url.length());

        if (end.equals("1")) {
            uname = CurrentUserInfo.userInfo.user_id + ".2";
        } else {
            uname = CurrentUserInfo.userInfo.user_id + ".1";
        }
        FileRW.FileService service = FileRW.getInstance(activity).getService();
        String url = "";

        url += FileRW.AVATAR_PATH + uname;
        service.uploadFile(path, url, new SaveCallback() {
            @Override
            public void onSuccess(String s) {
                uiHandler.sendMessage(uiHandler.obtainMessage(3, new Object[]{s, path}));
            }

            @Override
            public void onProgress(String s, int i, int i1) {

            }

            @Override
            public void onFailure(String s, OSSException e) {
                uiHandler.sendMessage(uiHandler.obtainMessage(4, s));
            }
        });
    }

    /**
     * 判断是否是图片文件
     *
     * @param path
     * @return
     */
    public static boolean isImageFile(String path) {
        String key = getFileType(path);
        if (null == key)
            return false;
        switch (key) {
            case "png":
            case "jpg":
            case "gif":
            case "bmp":
                return true;
        }
        return false;
    }

    /**
     * 获取文件类型
     *
     * @param filePath
     * @return
     */
    public static String getFileType(String filePath) {
        return mFileTypes.get(getFileHeader(filePath));
    }

    //获取文件头信息
    public static String getFileHeader(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return "";
        byte[] b = null;
        FileInputStream is = null;
        String value = null;
        try {
            String exitName = filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
            if (exitName==null)
                return null;
            if (exitName.equalsIgnoreCase("png")) {
                b = new byte[4];
            } else if (exitName.equalsIgnoreCase("bmp")) {
                b = new byte[2];
            } else if (exitName.equalsIgnoreCase("gif")) {
                b = new byte[4];
            } else {
                b = new byte[3];
            }
            is = new FileInputStream(filePath);
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }

    public static Bitmap creatBitmap(String srcPath, float w, float h) {
        if (TextUtils.isEmpty(srcPath))
            return null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 设置为ture只获取图片大小
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        // 返回为空
        BitmapFactory.decodeFile(srcPath, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scale = 0.f;

        if (width>height){
            if (width>w){
                scale = width/w;
            }else{
                scale = 1f;
            }
        }else{
            if (height>h){
                scale =  height/h;
            }else{
                scale = 1f;
            }
        }

        int tSCale = (int) scale;
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = tSCale;
        Log.d("创建了文件的原始图片资源",""+tSCale);
        WeakReference<Bitmap> weak = new WeakReference<Bitmap>(BitmapFactory.decodeFile(srcPath, opts));
        w = width / scale;
        h = height / scale;

        return Bitmap.createScaledBitmap(weak.get(), (int) w, (int) h, true);// 压缩好比例大小后再进行质量压缩
    }

    // 图片质量压缩
    private static Bitmap compressImage(Bitmap image) {
        if (image==null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
            if (options < 0)
                options = 1;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static boolean saveBitmap(Bitmap bitmap, String path) {
        if (bitmap==null||TextUtils.isEmpty(path))
            return false;
        OutputStream os = null;
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            os = new FileOutputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean ret=false;
        try {
             ret= bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
        }finally {
            bitmap.recycle();
            bitmap=null;
            if (null!=os) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 清除fresco内存缓存
     *
     * @param path
     */
    public static void clearCacheWithImageMemery(final String path) {
        if (!StringUtils.isEmpty(path))
            Fresco.getImagePipeline().evictFromCache(Uri.parse(path));
    }

    /**
     * @param view
     * @param s    秒
     */
    public static void showViewAtTime(final View view, int s) {

        if (null != showViewHAtTimeHelper && showViewHAtTimeHelper.isRun) {
            showViewHAtTimeHelper.isReVisiable = true;
            synchronized (showViewHAtTimeHelper) {
                showViewHAtTimeHelper.notify();
                showViewHAtTimeHelper = new ShowViewHAtTimeHelper();
                showViewHAtTimeHelper.time = s * 1000;
                showViewHAtTimeHelper.view = view;
                service.execute(showViewHAtTimeHelper);
            }
        } else {
            showViewHAtTimeHelper = new ShowViewHAtTimeHelper();
            showViewHAtTimeHelper.time = s * 1000;
            showViewHAtTimeHelper.view = view;
            service.execute(showViewHAtTimeHelper);
        }
        view.setVisibility(View.VISIBLE);
    }

    public static void hideInputSoft(View view, Context context) {
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showInputSoft(View view, Context context) {
        view.requestFocus();
        InputMethodManager manager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        manager.showSoftInputFromInputMethod(view.getWindowToken(), InputMethodManager.SHOW_FORCED);
    }

    public static Bitmap createBitmapAsWindow(Activity ac) {
        if (ac==null)
            return null;
        View v = ac.getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache(true);
        v.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        Bitmap bitmap = v.getDrawingCache(true);
        bitmap=bitmap.copy(Bitmap.Config.ARGB_4444,false);
        v.destroyDrawingCache();
        System.gc();
        return bitmap;
    }

    public static class ShowViewHAtTimeHelper implements Runnable {
        boolean isRun = false;
        int time;
        View view;
        boolean isReVisiable = false;
        public ShowViewHAtTimeHelper() {
            super();
        }

        @Override
        public void run() {
            isRun = true;
            while (isRun) {
                try {
                    synchronized (this) {
                        this.wait(time);
                        if (!isReVisiable)
                            view.post(new Runnable() {
                                @Override
                                public void run() {

                                    view.setVisibility(View.INVISIBLE);

                                }
                            });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isRun = false;
                isReVisiable = false;
            }
        }
    }

    public static boolean showRecommentStar(Context context){
            long time=24*60*60*1000;
        Settings settings=new Settings(context);
        long lastTime=settings.getLastDspRecommentTime();
        settings.setLastdspRemmentTime(System.currentTimeMillis());
        if (System.currentTimeMillis()-lastTime<=time){
            int count=settings.getLastReadRemmentCount();
            if (count>=3){
                settings.setLastReadRemmentCount(1);
                return true;
            }else{
                count++;
                settings.setLastReadRemmentCount(count);
            }
        }
        return false;
    }

    public final static void addNumerMsgToView(int msg,View target){
        BadgeView badge = new BadgeView(target.getContext(), target);
        if (msg>99){
            badge.setText("99+");
        }else{
            badge.setText(String.valueOf(msg));
        }
        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badge.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        badge.show();
        android.widget.FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) badge.getLayoutParams();
        Paint paint=new Paint();
        paint.setTextSize(Helper.calcuteDp(8,target.getContext()));
        lp.height=(int)  Helper.calcuteDp(paint.measureText("1"),target.getContext());
        lp.gravity= Gravity.TOP|Gravity.RIGHT;
        badge.setGravity(Gravity.CENTER);
        badge.setLayoutParams(lp);
    }

    public static boolean showNetworkWarning(Activity activity) {
        if (NetworkUtils.getNetWorkStatus(activity)) {
            return false;
        }
        int resId = R.mipmap.bg_yellow_head_dialog;
        T.showThemeDialog(resId, activity, "提醒", "您处于计费移动网络下，\n继续播放吗？", "退出", "继续", new T.TDialogListener() {

            @Override
            public void ok() {
                BusProvider.getInstance().post(new PlayNetworkWarningEvent(true));
            }

            @Override
            public void cancel() {
                BusProvider.getInstance().post(new PlayNetworkWarningEvent(false));
            }
        });
        return true;
    }
}
