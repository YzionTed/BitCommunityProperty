package sdk;



public class NETDEV_AUDIO_SAMPLE_PARAM_S {
	public int dwChannels;                               /* ��������������Ϊ��������Ϊ*/
	public int dwSampleRate;                             /* ������*/
	public int enSampleFormat;                           /* λ��,�ο�NETDEV_AUDIO_SAMPLE_FORMAT_E*/
	
	public class NETDEV_AUDIO_SAMPLE_FORMAT_E
	{
		public static final int NETDEV_AUDIO_SAMPLE_FMT_NONE = -1;
		public static final int NETDEV_AUDIO_SAMPLE_FMT_U8  = 0;            /* �޷���λ����*/
		public static final int NETDEV_AUDIO_SAMPLE_FMT_S16 = 1;            /* �з���λ����*/
		public static final int NETDEV_AUDIO_SAMPLE_FMT_S32 = 2;            /* �з���λ����*/
		public static final int NETDEV_AUDIO_SAMPLE_FMT_FLT = 3;            /* ������*/
		public static final int NETDEV_AUDIO_SAMPLE_FMT_DBL = 4;             /* double��*/
	}
}
