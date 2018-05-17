package com.zdd.myutil.bluetooth;

/**
 * Created by Eric on 2017/9/8.
 */

public class BT {
    private String btName;

    private String address;

    private int state;
    private boolean isPaired;

    private boolean isConnected;

    public BT(String name, String address){
        this.btName = name;
        this.address = address;
    }

    public void setBtName(String name){
        this.btName = name;
    }

    public String getBtName(){
        return this.btName;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getAddress(){
        return this.address;
    }

    public void setPaired(boolean isPaired){
        this.isPaired = isPaired;
        if (isPaired){
            setState(2);
        }else {
            setState(1);
        }
    }

    public boolean isPaired(){
        return isPaired;
    }
    public void setConnected(boolean isConnected){
        this.isConnected = isConnected;
        if (isConnected){
            setState(3);
        }else {
            setState(1);
        }
    }

    public boolean isConnected(){
        return isConnected;
    }
    public void setState(int state){
        this.state = state;
    }

    public int getState(){
        return state;
    }
    @Override
    public boolean equals(Object object){
        if (object instanceof BT){
            BT bt= (BT) object;
            if (bt.address.equals(this.address)){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }
    @Override
    public int hashCode(){
        return 2;
    }
}
