package com.zdd.myutil.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.util.Log;

import com.edong.myechoshow.ui.settings.bluetooth.BlueConnectTask;
import com.edong.myechoshow.ui.settings.db.BT;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by yd on 2018/5/5.
 */

public class BluetoothUtil {

    private static BluetoothUtil bluetoothUtil;
    private static BluetoothAdapter mBluetoothAdapter;
    private static Context context;
    private static List<BT> bts = new ArrayList<>();
    private static boolean isScanning = false;
    private static BT selectBT;
    private static BT connectBT;
    private static int pairCount;
    private static ScanLitener scanLitener;

    public static BluetoothUtil openBt(Context context){
        BluetoothUtil.context = context;
        BluetoothUtil.bluetoothUtil = new BluetoothUtil();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter!=null) {
            mBluetoothAdapter.enable();
        }
        register();
        return bluetoothUtil;
    }

    public static List<BT> getBts(){
        return bts;
    }
    public static int getPairCount(){
        return pairCount;
    }
    public static void scanBt(ScanLitener litener){
        BluetoothUtil.scanLitener = litener;
        scanBt();
    }
    private static void scanBt(){
        if (mBluetoothAdapter!=null&&!isScanning) {
            isScanning = true;
            // 获取所有已经绑定的蓝牙设备
            bts.clear();
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            if (devices.size() > 0) {
                pairCount = 0;
                for (BluetoothDevice bluetoothDevice : devices) {
//                    Log.i("device.getName()", bluetoothDevice.getName());
                    BT bt = new BT(bluetoothDevice.getName(), bluetoothDevice.getAddress());
                    bt.setPaired(true);
                    bts.add(bt);
                    if (scanLitener!=null){
                        scanLitener.discovered(bts,bt,connectBT!=null,pairCount);
                    }
                    pairCount++;
                }
            }
            mBluetoothAdapter.startDiscovery();
        }
    }
    private static void register(){
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
//        registerReceiver(mReceiver, mFilter);
        // 注册搜索完时的receiver
        mFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        mFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        context.registerReceiver(mReceiver, mFilter);
    }
    public static boolean removeBond(Class btClass, BluetoothDevice btDevice){
        Method removeBondMethod = null;
        Boolean returnValue = false;
        try {
            removeBondMethod = btClass.getMethod("removeBond");
            returnValue = (Boolean) removeBondMethod.invoke(btDevice);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return returnValue.booleanValue();
    }
    private static void checkState(){
        if(mBluetoothAdapter.isEnabled()){

            int a2dp = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.A2DP); // 可操控蓝牙设备，如带播放暂停功能的蓝牙耳机
            int headset = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEADSET); // 蓝牙头戴式耳机，支持语音输入输出
            int health = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.HEALTH); // 蓝牙穿戴式设备
            int GATT = mBluetoothAdapter.getProfileConnectionState(BluetoothProfile.GATT);
            Log.e("edong","a2dp="+a2dp+",headset="+headset+",health="+health);
            // 查看是否蓝牙是否连接到三种设备的一种，以此来判断是否处于连接状态还是打开并没有连接的状态
            int flag = -1;
            if (a2dp == BluetoothProfile.STATE_CONNECTED) {
                flag = a2dp;
            } else if (headset == BluetoothProfile.STATE_CONNECTED) {
                flag = headset;
            } else if (health == BluetoothProfile.STATE_CONNECTED) {
                flag = health;
            }
            if (flag!=-1)
                mBluetoothAdapter.getProfileProxy(context, new BluetoothProfile.ServiceListener() {

                    @Override
                    public void onServiceDisconnected(int profile) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onServiceConnected(int profile, BluetoothProfile proxy) {
                        // TODO Auto-generated method stub
                        List<BluetoothDevice> mDevices = proxy.getConnectedDevices();
                        if (mDevices != null && mDevices.size() > 0) {
                            for (BluetoothDevice device : mDevices) {
                                BT bt = getBTForAddress(device.getAddress());
                                if (bt!=null&&!bt.isConnected()){
                                    bt.setConnected(true);
                                    connectBT = bt;
                                    if (scanLitener!=null){
                                        scanLitener.discovered(bts,bt,connectBT!=null,pairCount);
                                    }
                                }
                            }
                        } else {
                            Log.i("edong", "mDevices is null");
                        }
                    }
                }, flag);

        } else {
//            setBtState(BluetoothAdapter.STATE_OFF);
        }
    }
    public static void connect(String address){
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            if (device.getBondState() == BluetoothDevice.BOND_NONE) {
                Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
                Log.d("edong", "开始配对");
                Boolean result = (Boolean) createBondMethod.invoke(device);
            } else if (device.getBondState() == BluetoothDevice.BOND_BONDED) {
//                removeBond(device.getClass(),device);
                Log.d("edong", "开始连接");
                BlueConnectTask connectTask = new BlueConnectTask(address);
                connectTask.setBlueConnectListener(new BlueConnectTask.BlueConnectListener() {
                    @Override
                    public void onBlueConnect(String address, BluetoothSocket socket) {

                    }
                });
                connectTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, device);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("edong",action);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    checkState();
                }
            },3000);

            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.i("edong", "device.getName()"+device.getName()+device.getAddress());
                if (device.getName()!=null) {
//                    Log.i("device.getName()", device.getName());
//                    bts.add(device.getName());
                }
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 显示在TextView上
                    if (device.getName()!=null) {
//                        Log.i("device.getName()", device.getName());
                        BT bt = new BT(device.getName(),device.getAddress());
                        if (!bts.contains(bt)) {
                            bts.add(bt);
                        }
                        if (scanLitener!=null){
                            scanLitener.discovered(bts,bt,connectBT!=null,pairCount);
                        }
                    }
                }
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.d("edong", "正在配对......"+device.getName());
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        BT bt = getBTForAddress(device.getAddress());
                        if (bt!=null){
                            bt.setPaired(true);
                            if (scanLitener!=null){
                                scanLitener.discovered(bts,bt,connectBT!=null,pairCount);
                            }
                        }
                        Log.d("edong", "完成配对"+device.getName());
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        bt = getBTForAddress(device.getAddress());
                        if (bt!=null){
                            bt.setPaired(false);
                            if (bt.equals(connectBT)){
                                connectBT = null;
                            }
                            if (scanLitener!=null){
                                scanLitener.discovered(bts,bt,connectBT!=null,pairCount);
                            }
                        }
                        Log.d("edong", "取消配对"+device.getName());
                    default:
                        break;
                }
            }else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                scanBt();
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                Log.i("edong", "ACTION_DISCOVERY_FINISHED");
                isScanning = false;
                if (scanLitener!=null){
                    scanLitener.scanEnd(bts);
                }
            }
        }
    };

    public static void setScanLitener(ScanLitener litener){
        BluetoothUtil.scanLitener = litener;
    }
    private static BT getBTForAddress(String address){
        for (BT bt:bts){
            if (bt.getAddress().equals(address)){
                return bt;
            }
        }
        return null;
    }

    public interface ScanLitener{
        void discovered(List bts, BT bt, boolean isConnected, int pairCount);
        void scanEnd(List bts);
    }
}
