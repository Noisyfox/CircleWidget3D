package com.motorola.homescreen.common.util;

/*import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;*/

public class Perf {
	/*public static final String DELIM = ":";
	protected static final byte END_CHAR = 10;
	public static final String END_DELIM = "end";
	public static final PerfEvent EVT_FPS = PerfEvent.FPS;
	public static final PerfEvent EVT_LAUNCH = PerfEvent.LAUNCH;
	public static final PerfEvent EVT_TOUCH = PerfEvent.TOUCH;
	public static final PerfEvent EVT_ONDRAW = PerfEvent.ONDRAW;
	public static final PerfEvent EVT_DISPATCH_DRAW = PerfEvent.DISPATCH_DRAW;
	public static final LogModuleId LOG_ID_WIDGET = LogModuleId.WIDGET;
	public static final LogModuleId LOG_ID_WIDGET_VIEW = LogModuleId.WIDGET_VIEW;
	public static final LogModuleId LOG_ID_WIDGET_ACTIVITY = LogModuleId.WIDGET_ACTIVITY;
	public static final LogModuleId LOG_ID_LAUNCH_PERF_APP = LogModuleId.LAUNCH_PERF_APP;
	public static final LogModuleId LOG_ID_HOMESCREEN = LogModuleId.HOMESCREEN;
	static final int LOG_ID_MAX = LogModuleId.HOMESCREEN.ordinal();
	static LogState[] sFPSLogStates = new LogState[1 + LOG_ID_MAX];
	public static final boolean PERF_ENABLED = false;
	protected static final String PERF_LAUNCH_PERF_APP_PACKAGE = "com.motorola.launchperfapp";
	protected static final String PROP_PERF_LOG_ENABLED = "homescreen.perf.log.enabled";
	protected static final String PROP_PERF_SERVER_ADDRESS = "homescreen.perf.log.server.address";
	protected static final String PROP_PERF_SERVER_PORT = "homescreen.perf.log.server.port";
	public static final String START_DELIM = "start";
	private static Class<?> SystemPropertiesClass = null;
	protected static final String TAG = "PERF";
	private static Method getBooleanMethod = null;
	private static Method getStringMethod = null;
	static boolean sPERF_LOG_WRITE_ENABLED = false;
	static String sPERF_SERVER_ADDRESS = null;
	static int sPERF_SERVER_PORT = 0;

	static StringBuilder buildLogMessage(StringBuilder paramStringBuilder,
			Object[] paramArrayOfObject) {
		int i = paramArrayOfObject.length;
		for (int j = 0; j < i; j++) {
			paramStringBuilder.append(paramArrayOfObject[j]);
			paramStringBuilder.append(":");
		}
		return paramStringBuilder;
	}

	public static boolean enabled() {
		return sPERF_LOG_WRITE_ENABLED;
	}

	public static void end(PerfEvent paramPerfEvent,
			LogModuleId paramLogModuleId) {
		if (!sPERF_LOG_WRITE_ENABLED) {
			return;
		}
		switch (paramPerfEvent) {
		default:
			logu(paramPerfEvent, paramLogModuleId, new Object[] { "end" });
			return;
		case LAUNCH:
		}
		switch (getFPSLogState(paramLogModuleId)) {
		default:
			return;
		case STARTED:
			setFPSLogState(paramLogModuleId, LogState.ENDED);
			return;
		case ENDING:
		}
		setFPSLogState(paramLogModuleId, LogState.ENDING);
	}

	public static void end(String paramString) {
		end(paramString, "");
	}

	public static void end(String paramString, LogModuleId paramLogModuleId) {
		logu(paramString, getLogModuleIdString(paramLogModuleId),
				new Object[] { "end" });
	}

	public static void end(String paramString1, String paramString2) {
		logu(paramString1, paramString2, new Object[] { "end" });
	}

	static LogState getFPSLogState(LogModuleId paramLogModuleId) {
		if (sFPSLogStates[paramLogModuleId.ordinal()] == null) {
			sFPSLogStates[paramLogModuleId.ordinal()] = LogState.ENDED;
		}
		return sFPSLogStates[paramLogModuleId.ordinal()];
	}

	static String getLogModuleIdString(LogModuleId paramLogModuleId) {
		switch (paramLogModuleId) {
		default:
			return "unknown";
		case WIDGET:
			return "widget";
		case WIDGET_VIEW:
			return "widget_view";
		case WIDGET_ACTIVITY:
			return "widget_activity";
		case LAUNCH_PERF_APP:
			return "launch_perf_app";
		case HOMESCREEN:
			return "homescreen";
		}
	}

	public static Boolean getSystemBoolean(String paramString,
			boolean paramBoolean) throws IllegalArgumentException {
		Boolean.valueOf(paramBoolean);
		try {
			if (getBooleanMethod == null) {
				Class<?>[] arrayOfClass = new Class[2];
				arrayOfClass[0] = String.class;
				arrayOfClass[1] = Boolean.TYPE;
				getBooleanMethod = getSystemPropertiesClass().getMethod(
						"getBoolean", arrayOfClass);
			}
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = new String(paramString);
			arrayOfObject[1] = new Boolean(paramBoolean);
			Boolean localBoolean = (Boolean) getBooleanMethod.invoke(
					SystemPropertiesClass, arrayOfObject);
			return localBoolean;
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw localIllegalArgumentException;
		} catch (Exception localException) {
			Log.e("PERF", "failed to get system boolean", localException);
		}
		return Boolean.valueOf(paramBoolean);
	}

	public static Class<?> getSystemPropertiesClass()
			throws ClassNotFoundException {
		if (SystemPropertiesClass == null) {
			SystemPropertiesClass = Class
					.forName("android.os.SystemProperties");
		}
		return SystemPropertiesClass;
	}

	public static String getSystemString(String paramString1,
			String paramString2) throws IllegalArgumentException {
		try {
			if (getStringMethod == null) {
				Class<?>[] arrayOfClass = { String.class, String.class };
				getStringMethod = getSystemPropertiesClass().getMethod(
						"getString", arrayOfClass);
			}
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = new String(paramString1);
			arrayOfObject[1] = new String(paramString2);
			String str = (String) getStringMethod.invoke(SystemPropertiesClass,
					arrayOfObject);
			return str;
		} catch (IllegalArgumentException localIllegalArgumentException) {
			throw localIllegalArgumentException;
		} catch (Exception localException) {
		}
		return paramString2;
	}

	public static void launch(Intent paramIntent, String paramString) {
		if ((sPERF_LOG_WRITE_ENABLED)
				&& (paramIntent != null)
				&& (paramIntent.getComponent() != null)
				&& ("com.motorola.launchperfapp".equals(paramIntent
						.getComponent().getPackageName()))) {
			logu(EVT_LAUNCH, LOG_ID_HOMESCREEN, new Object[] { "start",
					paramString });
		}
	}

	public static void log(String paramString, Object[] paramArrayOfObject) {
		logu(paramString, "", paramArrayOfObject);
	}

	public static void logu(PerfEvent paramPerfEvent, LogModuleId paramLogModuleId, Object[] paramArrayOfObject)
  {
    if (sPERF_LOG_WRITE_ENABLED) {
      switch (paramPerfEvent)
      {
      } 
    } 
    for (;;)
    {
      logu(paramPerfEvent.toString(), getLogModuleIdString(paramLogModuleId), paramArrayOfObject);
      return;
      switch (1.$SwitchMap$com$motorola$homescreen$common$util$Perf$LogState[getFPSLogState(paramLogModuleId).ordinal()])
      {
      } 
    } 
    PerfEvent localPerfEvent2 = PerfEvent.FPS;
    setFPSLogState(paramLogModuleId, LogState.STARTED);
    logu(localPerfEvent2.toString(), getLogModuleIdString(paramLogModuleId), new Object[] { "start" });
    return;
    PerfEvent localPerfEvent1 = PerfEvent.FPS;
    setFPSLogState(paramLogModuleId, LogState.ENDED);
    logu(localPerfEvent1.toString(), getLogModuleIdString(paramLogModuleId), new Object[] { "end" });
  }	private static void logu(String paramString1, String paramString2,
			long paramLong, Object[] paramArrayOfObject) {
		StringBuilder localStringBuilder = new StringBuilder(64);
		Object[] arrayOfObject = new Object[2];
		arrayOfObject[0] = Long.valueOf(paramLong);
		arrayOfObject[1] = paramString1;
		buildLogMessage(localStringBuilder, arrayOfObject);
		if (!paramString2.isEmpty()) {
			buildLogMessage(localStringBuilder, new Object[] { paramString2 });
		}
		LogClient.log(buildLogMessage(localStringBuilder, paramArrayOfObject)
				.toString());
	}

	public static void logu(String paramString1, String paramString2,
			Object[] paramArrayOfObject) {
		if (sPERF_LOG_WRITE_ENABLED) {
			logu(paramString1, paramString2, System.nanoTime(),
					paramArrayOfObject);
		}
	}

	static void setFPSLogState(LogModuleId paramLogModuleId,
			LogState paramLogState) {
		sFPSLogStates[paramLogModuleId.ordinal()] = paramLogState;
	}

	public static void start(PerfEvent paramPerfEvent, LogModuleId paramLogModuleId)
  {
    if (!sPERF_LOG_WRITE_ENABLED) {
      return;
    } 
    switch (paramPerfEvent.ordinal())
    {
    default: 
      logu(paramPerfEvent, paramLogModuleId, new Object[] { "start" });
      return;
    } 
    switch (1.$SwitchMap$com$motorola$homescreen$common$util$Perf$LogState[getFPSLogState(paramLogModuleId).ordinal()])
    {
    case 1: 
    case 2: 
    default: 
      return;
    case 3: 
      logu(paramPerfEvent.toString(), getLogModuleIdString(paramLogModuleId), new Object[] { "end" });
    } 
    setFPSLogState(paramLogModuleId, LogState.STARTING);
  }	public static void start(String paramString) {
		start(paramString, "");
	}

	public static void start(String paramString, LogModuleId paramLogModuleId) {
		logu(paramString, getLogModuleIdString(paramLogModuleId),
				new Object[] { "start" });
	}

	public static void start(String paramString1, String paramString2) {
		logu(paramString1, paramString2, new Object[] { "start" });
	}

	public boolean fpsEnded(LogModuleId paramLogModuleId) {
		return getFPSLogState(paramLogModuleId) == LogState.ENDED;
	}

	static class LogClient extends Thread {
		private static LogClient sInstance = null;
		private DataOutputStream mConnection = null;
		private ArrayList<String> mLogs = new ArrayList<String>(256);
		private Socket mSocket = null;

		public LogClient(Socket paramSocket,
				DataOutputStream paramDataOutputStream) {
			mSocket = paramSocket;
			mConnection = paramDataOutputStream;
			start();
		}

		public static void log(String paramString) {
			if (Perf.sPERF_SERVER_PORT != 0) {
				if (sInstance == null) {
				}
				try {
					Socket localSocket = new Socket(Perf.sPERF_SERVER_ADDRESS,
							Perf.sPERF_SERVER_PORT);
					sInstance = new LogClient(localSocket,
							new DataOutputStream(new BufferedOutputStream(
									localSocket.getOutputStream())));
					sInstance.writeLog(paramString);
					return;
				} catch (Exception localException) {
					for (;;) {
						Log.e("PERF", "failed to connect to server",
								localException);
					}
				}
			}
			Log.d("PERF", paramString);
		}

		public void run() {
			while (mLogs.size() == 0) {
				try {
					mLogs.wait();
				} catch (InterruptedException localInterruptedException) {
					Log.e("PERF", "interrupted while waiting for new logs");
				}
			}
			ArrayList localArrayList = mLogs;
			for (;;) {
				if (localArrayList.size() > 0) {
				}
				try {
					int i = localArrayList.size();
					String str = null;
					if (i > 0) {
						str = (String) localArrayList.get(0);
						localArrayList.remove(0);
					}
					if (str == null) {
						continue;
					}
					DataOutputStream localDataOutputStream = mConnection;
					try {
						localDataOutputStream.writeBytes(str);
						localDataOutputStream.writeByte(10);
						localDataOutputStream.flush();
					} catch (IOException localIOException) {
					}
					if ((!mSocket.isOutputShutdown()) && (!mSocket.isClosed())) {
						continue;
					}
					Log.e("PERF", "conection failed", localIOException);
					return;
				} finally {
				}
			}
		}

		public void writeLog(String paramString) {
			synchronized (mLogs) {
				mLogs.add(paramString);
				mLogs.notify();
				return;
			}
		}

		static class LogConnections extends Thread {
			private ArrayList<SocketChannel> mConnections = new ArrayList();
			private Perf.LogClient.LogServer mLogServer = null;
			private Selector mSelector = null;
			private boolean mStarted = false;

			public LogConnections(Perf.LogClient.LogServer paramLogServer) {
				mLogServer = paramLogServer;
			}

			 Error 
			public boolean add(SocketChannel paramSocketChannel)
					throws IOException {
				// Byte code:
				// 0: aload_0
				// 1: getfield 27
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mSelector
				// Ljava/nio/channels/Selector;
				// 4: ifnonnull +10 -> 14
				// 7: aload_0
				// 8: invokestatic 41 java/nio/channels/Selector:open
				// ()Ljava/nio/channels/Selector;
				// 11: putfield 27
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mSelector
				// Ljava/nio/channels/Selector;
				// 14: aload_1
				// 15: aload_0
				// 16: getfield 27
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mSelector
				// Ljava/nio/channels/Selector;
				// 19: iconst_1
				// 20: sipush 256
				// 23: invokestatic 47 java/nio/ByteBuffer:allocate
				// (I)Ljava/nio/ByteBuffer;
				// 26: invokevirtual 53 java/nio/channels/SocketChannel:register
				// (Ljava/nio/channels/Selector;ILjava/lang/Object;)Ljava/nio/channels/SelectionKey;
				// 29: pop
				// 30: aload_0
				// 31: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 34: astore 5
				// 36: aload 5
				// 38: monitorenter
				// 39: aload_0
				// 40: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 43: aload_1
				// 44: invokevirtual 56 java/util/ArrayList:add
				// (Ljava/lang/Object;)Z
				// 47: pop
				// 48: aload 5
				// 50: monitorexit
				// 51: aload_0
				// 52: monitorenter
				// 53: aload_0
				// 54: getfield 29
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mStarted
				// Z
				// 57: ifne +12 -> 69
				// 60: aload_0
				// 61: invokevirtual 59
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:start
				// ()V
				// 64: aload_0
				// 65: iconst_1
				// 66: putfield 29
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mStarted
				// Z
				// 69: aload_0
				// 70: monitorexit
				// 71: iconst_1
				// 72: ireturn
				// 73: astore_3
				// 74: aload_3
				// 75: invokevirtual 62
				// java/nio/channels/ClosedChannelException:printStackTrace ()V
				// 78: iconst_0
				// 79: ireturn
				// 80: astore_2
				// 81: aload_2
				// 82: invokevirtual 63 java/io/IOException:printStackTrace ()V
				// 85: aload_2
				// 86: athrow
				// 87: astore 6
				// 89: aload 5
				// 91: monitorexit
				// 92: aload 6
				// 94: athrow
				// 95: astore 8
				// 97: aload_0
				// 98: monitorexit
				// 99: aload 8
				// 101: athrow
				// Local variable table:
				// start length slot name signature
				// 0 102 0 this LogConnections
				// 0 102 1 paramSocketChannel SocketChannel
				// 80 6 2 localIOException IOException
				// 73 2 3 localClosedChannelException
				// java.nio.channels.ClosedChannelException
				// 87 6 6 localObject1 Object
				// 95 5 8 localObject2 Object
				// Exception table:
				// from to target type
				// 0 14 73 java/nio/channels/ClosedChannelException
				// 14 30 73 java/nio/channels/ClosedChannelException
				// 0 14 80 java/io/IOException
				// 14 30 80 java/io/IOException
				// 39 51 87 finally
				// 89 92 87 finally
				// 53 69 95 finally
				// 69 71 95 finally
				// 97 99 95 finally
			}

			 Error 
			public void run() {
				// Byte code:
				// 0: aload_0
				// 1: getfield 27
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mSelector
				// Ljava/nio/channels/Selector;
				// 4: invokevirtual 72 java/nio/channels/Selector:select ()I
				// 7: istore 19
				// 9: iload 19
				// 11: istore_2
				// 12: iload_2
				// 13: ifeq -13 -> 0
				// 16: iconst_0
				// 17: istore_3
				// 18: new 22 java/util/ArrayList
				// 21: dup
				// 22: invokespecial 23 java/util/ArrayList: ()V
				// 25: astore 4
				// 27: iload_2
				// 28: ifle +172 -> 200
				// 31: iload_3
				// 32: aload_0
				// 33: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 36: invokevirtual 75 java/util/ArrayList:size ()I
				// 39: if_icmpge +161 -> 200
				// 42: aload_0
				// 43: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 46: astore 8
				// 48: aload 8
				// 50: monitorenter
				// 51: aload_0
				// 52: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 55: iload_3
				// 56: invokevirtual 79 java/util/ArrayList:get
				// (I)Ljava/lang/Object;
				// 59: checkcast 49 java/nio/channels/SocketChannel
				// 62: astore 10
				// 64: aload 8
				// 66: monitorexit
				// 67: aload 10
				// 69: aload_0
				// 70: getfield 27
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mSelector
				// Ljava/nio/channels/Selector;
				// 73: invokevirtual 83 java/nio/channels/SocketChannel:keyFor
				// (Ljava/nio/channels/Selector;)Ljava/nio/channels/SelectionKey;
				// 76: astore 11
				// 78: aload 11
				// 80: invokevirtual 89
				// java/nio/channels/SelectionKey:attachment
				// ()Ljava/lang/Object;
				// 83: checkcast 43 java/nio/ByteBuffer
				// 86: astore 12
				// 88: iconst_1
				// 89: aload 11
				// 91: invokevirtual 92 java/nio/channels/SelectionKey:readyOps
				// ()I
				// 94: ior
				// 95: ifeq +62 -> 157
				// 98: iinc 2 -1
				// 101: aload 10
				// 103: aload 12
				// 105: invokevirtual 96 java/nio/channels/SocketChannel:read
				// (Ljava/nio/ByteBuffer;)I
				// 108: pop
				// 109: aload 12
				// 111: invokevirtual 99 java/nio/ByteBuffer:remaining ()I
				// 114: ifle +43 -> 157
				// 117: aload 12
				// 119: iconst_m1
				// 120: aload 12
				// 122: invokevirtual 102 java/nio/ByteBuffer:position ()I
				// 125: aload 12
				// 127: invokevirtual 99 java/nio/ByteBuffer:remaining ()I
				// 130: iadd
				// 131: iadd
				// 132: invokevirtual 105 java/nio/ByteBuffer:get (I)B
				// 135: bipush 10
				// 137: if_icmpne +20 -> 157
				// 140: ldc 107
				// 142: aload 12
				// 144: invokevirtual 111 java/nio/ByteBuffer:toString
				// ()Ljava/lang/String;
				// 147: invokestatic 117 android/util/Log:d
				// (Ljava/lang/String;Ljava/lang/String;)I
				// 150: pop
				// 151: aload 12
				// 153: invokevirtual 121 java/nio/ByteBuffer:clear
				// ()Ljava/nio/Buffer;
				// 156: pop
				// 157: iinc 3 1
				// 160: goto -133 -> 27
				// 163: astore 18
				// 165: return
				// 166: astore 9
				// 168: aload 8
				// 170: monitorexit
				// 171: aload 9
				// 173: athrow
				// 174: astore 13
				// 176: aload 10
				// 178: invokevirtual 125 java/nio/channels/SocketChannel:isOpen
				// ()Z
				// 181: ifne -24 -> 157
				// 184: aload 11
				// 186: invokevirtual 128 java/nio/channels/SelectionKey:cancel
				// ()V
				// 189: aload 4
				// 191: aload 10
				// 193: invokevirtual 56 java/util/ArrayList:add
				// (Ljava/lang/Object;)Z
				// 196: pop
				// 197: goto -40 -> 157
				// 200: aload_0
				// 201: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 204: astore 5
				// 206: aload 5
				// 208: monitorenter
				// 209: aload_0
				// 210: getfield 25
				// com/motorola/homescreen/common/util/Perf$LogClient$LogConnections:mConnections
				// Ljava/util/ArrayList;
				// 213: aload 4
				// 215: invokevirtual 132 java/util/ArrayList:removeAll
				// (Ljava/util/Collection;)Z
				// 218: pop
				// 219: aload 5
				// 221: monitorexit
				// 222: goto -222 -> 0
				// 225: astore 6
				// 227: aload 5
				// 229: monitorexit
				// 230: aload 6
				// 232: athrow
				// 233: astore_1
				// 234: iconst_0
				// 235: istore_2
				// 236: goto -224 -> 12
				// Local variable table:
				// start length slot name signature
				// 0 239 0 this LogConnections
				// 233 1 1 localIOException IOException
				// 11 225 2 i int
				// 17 141 3 j int
				// 25 189 4 localArrayList1 ArrayList
				// 225 6 6 localObject1 Object
				// 166 6 9 localObject2 Object
				// 62 130 10 localSocketChannel SocketChannel
				// 76 109 11 localSelectionKey java.nio.channels.SelectionKey
				// 86 66 12 localByteBuffer java.nio.ByteBuffer
				// 174 1 13 localException Exception
				// 163 1 18 localClosedSelectorException
				// java.nio.channels.ClosedSelectorException
				// 7 3 19 k int
				// Exception table:
				// from to target type
				// 0 9 163 java/nio/channels/ClosedSelectorException
				// 51 67 166 finally
				// 168 171 166 finally
				// 101 157 174 java/lang/Exception
				// 209 222 225 finally
				// 227 230 225 finally
				// 0 9 233 java/io/IOException
			}
		}

		public static class LogServer extends Thread {
			private static LogServer sInstance = null;
			private Perf.LogClient.LogConnections mConnections = null;
			private int mPort = 0;

			public LogServer(int paramInt) {
				mPort = paramInt;
				mConnections = new Perf.LogClient.LogConnections(this);
				setDaemon(true);
				start();
			}

			public void run() {
				try {
					ServerSocket localServerSocket = new ServerSocket(mPort);
					while (true) {
						System.err.println("Performance counter ready.....");
						SocketChannel localSocketChannel = localServerSocket
								.getChannel().accept();
						mConnections.add(localSocketChannel);
					}
				} catch (IOException localIOException) {
					localIOException.printStackTrace();
					Log.e("PERF", "Failed to create socket for PerfLogServer");
				}
			}
		}
	}

	public static enum LogModuleId {
		WIDGET, WIDGET_VIEW, WIDGET_ACTIVITY, LAUNCH_PERF_APP, HOMESCREEN
	}

	protected static enum LogState {
		STARTING, STARTED, ENDING, ENDED
	}

	public static enum PerfEvent {
		FPS, LAUNCH, TOUCH, ONDRAW, DISPATCH_DRAW

	}*/
}

/*
 * Location: J:\鎶�湳鏂囨。\瀹夊崜鍥轰欢鐩稿叧\moto\classes_dex2jar.jar Qualified Name:
 * com.motorola.homescreen.common.util.Perf JD-Core Version: 0.6.2
 */