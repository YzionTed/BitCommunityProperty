package sdk;

import android.view.Surface;

import java.util.ArrayList;


public class NetDEVSDK {
	static {
        System.loadLibrary("Curl");
        System.loadLibrary("MP4");
        System.loadLibrary("mXML");
        System.loadLibrary("RM_Module");
        System.loadLibrary("NDRender");
        System.loadLibrary("dspvideomjpeg");
        System.loadLibrary("NDPlayer");
        System.loadLibrary("Discovery");
        System.loadLibrary("NetDEVSDK");
        System.loadLibrary("NetDEVSDK_JNI");
    }
   
	//private OnNotifyListener mNotifyListener;
 	public static int lpUserID;       	/* User ID*/
 	public static int glpcloudID;      	/* Cloud ID*/
	public static String strDevName;
 	public static NETDEV_FINDDATA_S []m_astVodFile = new NETDEV_FINDDATA_S[10];
 	
 	/**
 	 * @enum tagNETDEVPlayControl
 	 * @brief Playback control commands Enumeration definition
 	 * @attention  None
 	 */
 	public class NETDEV_VOD_PLAY_CTRL_E{
 		public static final int NETDEV_PLAY_CTRL_PLAY           = 0;           	/* Play */
 		public static final int NETDEV_PLAY_CTRL_PAUSE          = 1;           	/* Pause */
 		public static final int NETDEV_PLAY_CTRL_RESUME         = 2;          	/* Resume */
 		public static final int NETDEV_PLAY_CTRL_GETPLAYTIME    = 3;           	/* Obtain playing time */
 		public static final int NETDEV_PLAY_CTRL_SETPLAYTIME    = 4;           	/* Configure playing time */
 		public static final int NETDEV_PLAY_CTRL_GETPLAYSPEED   = 5;          	/* Obtain playing speed */
 		public static final int NETDEV_PLAY_CTRL_SETPLAYSPEED   = 6;        	/* Configure playing speed */
 	}
 	
 	/**
 	 * @enum tagNETDEVVodPlayStatus
 	 * @brief Playback and download status Enumeration definition
 	 * @attention None
 	 */
 	public class NETDEV_VOD_PLAY_STATUS_E
 	{
 	/** ����״̬  Play status */
 		public static final int NETDEV_PLAY_STATUS_16_BACKWARD        = 0;        	/* Backward at 16x speed */
 		public static final int NETDEV_PLAY_STATUS_8_BACKWARD         = 1;       	/* Backward at 8x speed */
 		public static final int NETDEV_PLAY_STATUS_4_BACKWARD         = 2;       	/* Backward at 4x speed */
 	    public static final int NETDEV_PLAY_STATUS_2_BACKWARD         = 3;        	/* Backward at 2x speed */
 		public static final int NETDEV_PLAY_STATUS_1_BACKWARD         = 4;        	/* Backward at normal speed */
 	    public static final int NETDEV_PLAY_STATUS_HALF_BACKWARD      = 5;        	/* Backward at 1/2 speed */
 	    public static final int NETDEV_PLAY_STATUS_QUARTER_BACKWARD   = 6;        	/* Backward at 1/4 speed */
 	    public static final int NETDEV_PLAY_STATUS_QUARTER_FORWARD    = 7;       	/* Play at 1/4 speed */
 	    public static final int NETDEV_PLAY_STATUS_HALF_FORWARD       = 8;        	/* Play at 1/2 speed */
 	    public static final int NETDEV_PLAY_STATUS_1_FORWARD          = 9;        	/* Forward at normal speed */
 	    public static final int NETDEV_PLAY_STATUS_2_FORWARD          = 10;       	/* Forward at 2x speed */
 	    public static final int NETDEV_PLAY_STATUS_4_FORWARD          = 11;       	/* Forward at 4x speed */
 	    public static final int NETDEV_PLAY_STATUS_8_FORWARD          = 12;       	/* Forward at 8x speed */
 	    public static final int NETDEV_PLAY_STATUS_16_FORWARD         = 13;      	/* Forward at 16x speed */
        public static final int NETDEV_PLAY_STATUS_INVALID            = 14;
 	}
 	
 	public class NETDEV_VOD_PTZ_CMD_E
 	{
 		public static final int NETDEV_PTZ_FOCUSNEAR_STOP       = 0x0201;       	/* Focus near stop */
 		public static final int NETDEV_PTZ_FOCUSNEAR            = 0x0202;       	/* Focus near */
 		public static final int NETDEV_PTZ_FOCUSFAR_STOP        = 0x0203;       	/* Focus far stop */
 		public static final int NETDEV_PTZ_FOCUSFAR             = 0x0204;       	/* Focus far */
 		public static final int NETDEV_PTZ_ZOOMTELE             = 0x0302;       	/* Zoom in */
 		public static final int NETDEV_PTZ_ZOOMWIDE             = 0x0304;       	/* Zoom out */
 		public static final int NETDEV_PTZ_TILTUP               = 0x0402;       	/* Tilt up */
 		public static final int NETDEV_PTZ_TILTDOWN             = 0x0404;       	/* ilt down */
 		public static final int NETDEV_PTZ_PANRIGHT             = 0x0502;       	/* Pan right */
 		public static final int NETDEV_PTZ_PANLEFT              = 0x0504;      		/* Pan left */
 		public static final int NETDEV_PTZ_LEFTUP               = 0x0702;       	/* Move up left */
 		public static final int NETDEV_PTZ_LEFTDOWN             = 0x0704;       	/* Move down left */
 		public static final int NETDEV_PTZ_RIGHTUP              = 0x0802;       	/* Move up right */
 		public static final int NETDEV_PTZ_RIGHTDOWN            = 0x0804;       	/* Move down right */
 		public static final int NETDEV_PTZ_ALLSTOP              = 0x0901;       	/* All-stop command word */
 	}
 	
 	
 	static AlarmCallBack_PF pfAlarmCallBack;
 	
	/**
	* Callback function to receive alarm information
	* @param [IN] iUserID              	User login ID
	* @param [IN] iChannelID           	Channel number
	* @param [IN] iAlarmType          	Alarm type
	* @param [IN] tAlarmTime           	Alarm time
	* @param [IN] strName              	Alarm source name
	* @param [IN] iBufLen             	Length of structure for alarm information
	* @note*/
	public void alarmCallBack(int iUserID, int iChannelID, int iAlarmType, int tAlarmTime, String strName, int iBufLen) {
		pfAlarmCallBack.alarmCallBack(iUserID, iChannelID, iAlarmType, tAlarmTime, strName, iBufLen) ;
    }
 	
	public native int  NETDEV_SetAlarmCallBack(int iUserID, String strAlarmMessCallBack, int iUserData);
	public int  NETDEV_Android_SetAlarmCallBack(int iUserID, AlarmCallBack_PF strAlarmMessCallBack, int iUserData){
		pfAlarmCallBack = strAlarmMessCallBack;
		return NETDEV_SetAlarmCallBack(iUserID,"alarmCallBack",0);
	}
	
 	/**
 	* SDK initialization
 	* @return 1 means success, and any other value means failure.
 	* @note Thread not safe
 	*/
 	public  native int NETDEV_Init();
 	
 	/**
 	* DK uninitialization
 	* @return  1 means success, and any other value means failure.
 	* @note Thread not safe
 	*/
 	public static native int NETDEV_Cleanup();
 	
 	/**
 	*  User login
 	* @param [IN]  DevIP         IP Device IP
 	* @param [IN]  DevPort       Device server port
 	* @param [IN]  UserName      Username
 	* @param [IN]  Password      Password
 	* @param [OUT] oDeviceInfo   device information 
 	* @return  Returned user login ID. 0 indicates failure, and other values indicate the user ID.
 	* @note
 	*/
	public static native int NETDEV_Login(String DevIP, int DevPort, String Username, String Password, NETDEV_DEVICE_INFO_S oDeviceInfo);
	
	/**
	*  User logout
	* @param [IN] lpUserID    �û���¼User login ID
	* @return 1 means success, and any other value means failure.
	* @note
	*/
    public static native int NETDEV_Logout(int lpUerID);
    
    /**
    * Query channel capabilities
    * @param [IN]    lpUserID           User login ID
    * @param [INOUT] pdwChlCount        Number of channels
    * @return ArrayList    List of channel capabilities.
    * @note
    */
    public static native ArrayList<NETDEV_VIDEO_CHL_DETAIL_INFO_S> NETDEV_QueryVideoChlDetailList(int lpUserID, int dwChlCount);
    
    /**
    *   Get error code
    * @return Error codes
    */
    public static native int NETDEV_GetLastError();
    
    /**
    *  Modify image display ratio
    */
    public static native void NETDEV_SetRenderSurface(Surface view);
    
    /**
    * Start Live view
    * @param [IN]  lpUserID             User login ID
    * @param [IN]  oPreviewInfo       	see enumeration
    * @return  Returned user login ID. 0 indicates failure, and other values indicate the user ID.
    * @note
    */
    public  static native int NETDEV_RealPlay(int lpUerID,NETDEV_PREVIEWINFO_S oPreviewInfo);
    
    /**
    *  Stop Live view
    * @param [IN]  lpPlayID     ID Preview ID
    * @return 1 means success, and any other value means failure.
    * @note  Stop the live view started by NETDEV_RealPlay
    */
    public  static native int NETDEV_StopRealPlay(int lpPlayID);
    
    /**
    * Live view snapshot
    * @param [IN]  lpPlayID         Preview\playback handle
    * @param [IN]  pszFileName      File path to save images (including file name)
    * @param [IN]  dwCaptureMode    Image saving format, see #NETDEV_PICTURE_FORMAT_E
    * @return means success, and any other value means failure.
    * @note  File format suffix is not required in the file name
    */
    public static native int NETDEV_CapturePicture(int lpPlayID, String pszFileName, int dwCaptureMode);
    
    /**
     * PTZ control operation (preview required)
     * @param [IN]  lpPlayHandle         Live preview handle
     * @param [IN]  dwPTZCommand         PTZ control commands, see #NETDEV_PTZ_E
     * @param [IN]  dwSpeed              Speed of PTZ control, which is configured according to the speed control value of different decoders. Value ranges from 1 to 9.
     * @return TRUE means success, and any other value means failure.
     * @note
     */
     public static native int NETDEV_PTZControl(int lpPlayID, int dwPTZCommand, int dwSpeed);
     
    /**
    * Get PTZ preset List 
    * @param [IN]  lpUserID         	User login ID
    * @param [IN]  dwChannelID       	Channel ID
    * @param [OUT] dwPresetIDList       Preset ID list
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_GetPTZPresetList(int lpUserID, int dwChannelID, int[] dwPresetIDList);
      
    /**
    * PTZ preset operation (preview required)  
    * @param [IN]  lpPlayHandle         Live preview handle
    * @param [IN]  dwPTZPresetCmd 		Preset Control commond
    * @param [IN]  pszPresetName     	Preset name
    * @param [IN]  dwPresetID           Preset number (starting from 0). Up to 255 presets are supported.
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_PTZPreset(int lpPlayID, int  dwPTZPresetCmd, String szPresetName, int dwPresetID);

    /**
    * PTZ preset operation (preview not required)
    * @param [IN]  lpUserID             User login ID
    * @param [IN]  dwChannelID          Channel number
    * @param [IN]  dwPTZPresetCmd       PTZ preset operation commands, see NETDEV_PTZ_PRESETCMD_E
    * @param [IN]  pszPresetName        Preset name
    * @param [IN]  dwPresetID           Preset number (starting from 0). Up to 255 presets are supported.
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_PTZPreset_Other(int lpUserID, int dwChannelID, int  dwPTZPresetCmd, String szPresetName, int dwPresetID);

    /**
     * User login to cloud accounts
     * @param [IN]  CloudUrl       	Cloud server URL 
     * @param [IN]  CloudUser       Cloud account name
     * @param [IN]  Cloudpassword   Cloud account password 
     * @return  Returned user ID. 0 means failure, any other value is a user ID.
     * @note 
     */
     public static native int NETDEV_LoginCloud(String CloudUrl, String CloudUser, String Cloudpassword);
     
    
    /**
    *  Cloud device login with dynamic password
    * @param [IN]  lpUserID             Cloud account login ID
    * @param [IN]  CloudDevInfo         Cloud device login info
    * @param [OUT] cloudDeviceInfo      device info
    * @return  Returned user ID. 0 means failure, any other value is a user ID.
    * @note 
        1��pCloudInfo ��szDevicePassword�ֶβ�����д��The szDevicePassword field in pCloudInfo needs not to be filled in.
    */
    public static native int NETDEV_LoginByDynamic(int lpCloudID,NETDEV_CLOUD_DEV_LOGIN_S CloudDevInfo,NETDEV_DEVICE_INFO_S cloudDeviceInfo);
    
    /**
    * �ƶ��豸��¼ Cloud device login
    * @param [IN]  lpUserID             Cloud account login ID 
    * @param [IN]  CloudDevInfo         Cloud device login info 
    * @param [OUT] cloudDeviceInfo      device info 
    * @return Returned user ID. 0 means failure, any other value is a user ID.
    * @note 
        1��The szDevicePassword field in pCloudInfo must be filled in.
    */
    public static native int NETDEV_LoginCloudDev(int lpCloudID,NETDEV_CLOUD_DEV_LOGIN_S CloudDevInfo,NETDEV_DEVICE_INFO_S cloudDeviceInfo);
    
    /**
    * Query device list under a cloud account
    * @param [IN]  lpUserID            User login ID
    * @return  0 means failure, any other value will be used as parameter of functions including NETDEV_FindNextCloudDevInfo and NETDEV_FindCloseDevList.
    * @note  
    */
    public static native int NETDEV_FindCloudDevList(int lpCloudID);
    
    /**
    *   Obtain info about detected devices one by one
    * @param [IN]  lpCloudID         Search handle
    * @param [OUT] pstDevInfo        Pointer to saved device info
    * @return  1 means success, and any other value means failure.
    * @note returned failure indicates the end of search.
    */
    public static native int NETDEV_FindNextCloudDevInfo(int lpFindID,NETDEV_CLOUD_DEV_INFO_S clouddeviceinfo);
    
    /**
    *  Stop search and release resource
    * @param [IN] lpCloudID  File search handle
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int  NETDEV_FindCloseCloudDevList(int lpCloudID);
    
    /**
    * Query recording files according to file type and time
    * @param [IN]  lpUserID      	User login ID
    * @param [IN]  pstFindCond  	Search condition
    * @return Recording search service number. 0 means failure. Other values are used as the handle parameters of functions like NETDEV_FindClose.
    * @note 
    */   
    public static native int NETDEV_FindFile(int lpUserID, NETDEV_FILECOND_S pstFindCond);
    
    /**
    * Obtain the information of found files one by one.
    * @param [IN]  lpFindHandle     File search handle
    * @param [OUT] pstFindData      Pointer to save file information
    * @return TRUE means success, and any other value means failure.
    * @note  A returned failure indicates the end of search.
    */
    public static native int NETDEV_FindNextFile(int lpFindHandle, NETDEV_FINDDATA_S pstFindData);
    
    /**
    * Close file search and release resources
    * @param [IN] lpFindHandle  File search handle
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_FindClose(int lpFindHandle);
    
    /**
    * Play back recording by time. 
    * @param [IN] lpUserID          User login ID
    * @param [IN] pstPlayBackCond   Pointer to playback-by-time structure, see LPNETDEV_PLAYBACKCOND_S
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_PlayBackByTime(int lpUserID, NETDEV_PLAYBACKCOND_S pstPlayBackInfo);
    
    /**
    * Stop playback service
    * @param [IN] lpPlayHandle   Playback handle
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_StopPlayBack(int lpPlayHandle);
    
    /**
    * Control recording playback status.
    * @param [IN]  lpPlayHandle     Playback or download handle
    * @param [IN]  dwControlCode    Command for controlling recording playback status, see NETDEV_VOD_PLAY_CTRL_E
    * @param [INOUT]  lpBuffer     Pointer to input/output parameters. For playing speed, see NETDEV_VOD_PLAY_STATUS_E. The type of playing time: INT64.
    * @return TRUE means success, and any other value means failure.
    * @note When playing, pause or resume videos, set IpBuffer as NULL.
    */
    public static native int NETDEV_PlayBackControl(int lpPlayHandle, int dwControlCode, NETDEV_PLAYBACKCONTROL_S lpBuffer);
    
    /**
    * Download recordings by time
    * @param [IN]  lpUserID          	User login ID
    * @param [IN]  pstPlayBackCond   	Pointer to playback-by-time structure, see LPNETDEV_PLAYBACKCOND_S
    * @param [IN]  *pszSaveFileName    	Downloaded file save path on PC, must be an absolute path (including file name)
    * @param [IN]  dwFormat         	Recording file saving format
    * @return Download handle. 0 means failure. Other values are used as the handle parameters of functions like NETDEV_StopGetFile.
    * @note
    */
    public static native int NETDEV_DownloadFile(int lpUserID, NETDEV_PLAYBACKCOND_S lpBuffer, String szSaveFileName, int dwFormat);
    
    /**
    * Stop downloading recording files
    * @param [IN]  lpPlayHandle  Playback handle
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_StopDownloadFile(int lpPlayHandle);
    
    /**
    * Get device basic info
    * @param [IN]  lpUserID          	User login ID
    * @param [IN]  dwChannelID          Channel ID
    * @param [OUT]  stDevinfo           Device Info
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    public static native int NETDEV_GetDeviceInfo(int lpUserID, int dwChannelID, NETDEV_DEVICE_BASICINFO_S stDevinfo);
        
    public static native int initialize();
    public static native int rendererRender();
    public static native int setRendererViewport(int w, int h);
    public static native int initializeRenderer();    
    
    
    
    /**
    * Start input voice server
    * @param [IN]  lpUserID                 User login ID
    * @param [IN]  dwChannelID              Channel ID
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    //public static native int NETDEV_StartInputVoiceSrv(int lpUserID,int dwChannelID);
    
    /**
    * Stop input voice server
    * @param [IN]  lpVoiceComHandle         Two-way audio handle
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    //public static native int NETDEV_StopInputVoiceSrv(int lpVoiceComHandle);
    
    /**
    * input voice Data
    * @param [IN]  lpVoiceComHandle         Two-way audio handle
    * @param [IN]  lpDataBuf         		Data buffer
    * @param [IN]  dwDataLen         		Data length
    * @param [IN]  pstVoiceParam         	Voice param
    * @return TRUE means success, and any other value means failure.
    * @note
    */
    //public static native int NETDEV_InputVoiceData(int lpVoiceComHandle,byte[] lpDataBuf,int dwDataLen,NETDEV_AUDIO_SAMPLE_PARAM_S pstVoiceParam);
   
    
    /* public interface OnNotifyListener 
    { 
    	public void nativeNotifyDecodeAudioData(byte[] voiceData, int u32WaveFormat, int length, int type);
    }
    
    public void setNotifyListener(OnNotifyListener notifyListener) 
    {
        mNotifyListener = notifyListener;
    }

    public void nativeNotifyDecodeAudioData(byte[] voiceData,int u32WaveFormat, int length, int type) 
    {
    	if (mNotifyListener != null) 
    	{
    		mNotifyListener.nativeNotifyDecodeAudioData(voiceData, u32WaveFormat, length, type);
    	}
    }
    */
}

