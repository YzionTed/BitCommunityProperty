package sdk;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//import java.util.Vector;

public class AlarmCallBack_PF {
	public static int mAlarmcnt = 0;
	//public static NETDEV_ALARMINFO_S[] mAlarmlist = new NETDEV_ALARMINFO_S[8];
	public static List<String> mAlarmlist = new ArrayList<String>();
	
	public static List<String> getData(int offset, int maxnumber)
    {
		//List<String> temp = new ArrayList<String>();
		return mAlarmlist;
    }
	
	/*
	* Callback function to receive alarm information
	* @param [IN] iUserID              	ID User login ID
	* @param [IN] iChannelID           	Channel number
	* @param [IN] iAlarmType          	Alarm type
	* @param [IN] tAlarmTime           	Alarm time
	* @param [IN] strName              	Alarm source name
	* @param [IN] iBufLen             	Length of structure for alarm information
	* @note*/
	@SuppressLint("SimpleDateFormat")
	public void alarmCallBack(int iUserID, int iChannelID, int iAlarmType, int tAlarmTime, String strName, int iBufLen) {
		/*
		NETDEV_ALARMINFO_S alarminfo = new NETDEV_ALARMINFO_S();
		alarminfo.mUserID = iUserID;
		alarminfo.mChannelID = iChannelID;
		alarminfo.mAlarmType = iAlarmType;
		alarminfo.mAlarmTime = tAlarmTime;
		alarminfo.mStrName = strName;
		//mAlarmlist.add(alarminfo);
		mAlarmlist[mAlarmcnt++] = alarminfo;
		*/
		DateFormat formatter = new SimpleDateFormat("hh:mm:ss yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(tAlarmTime*1000);
		//Date curDate = new Date(System.currentTimeMillis());
		//����
		mAlarmlist.add(0, formatter.format(calendar.getTime()) + " \r\n ChannelID : " + iChannelID + ",AlarmType : " + iAlarmType);
		mAlarmcnt++;
		System.out.println("Android Report alarm info , Time :" +  formatter.format(calendar.getTime()) + " UserID : " + iUserID + " , ChannelID : " + iChannelID + ",AlarmType : " + iAlarmType);
	}

}
