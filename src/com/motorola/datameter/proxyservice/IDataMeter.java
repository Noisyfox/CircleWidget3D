package com.motorola.datameter.proxyservice;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public abstract interface IDataMeter
  extends IInterface
{
  public abstract long getCurrentUsageBytes()
    throws RemoteException;
  
  public abstract int getPolicyCycleDay()
    throws RemoteException;
  
  public abstract long getPolicyLimitBytes()
    throws RemoteException;
  
  public abstract boolean setPolicyCycleDay(int paramInt)
    throws RemoteException;
  
  public abstract boolean setPolicyLimitBytes(long paramLong)
    throws RemoteException;
  
  public static abstract class Stub
    extends Binder
    implements IDataMeter
  {
    private static final String DESCRIPTOR = "com.motorola.datameter.proxyservice.IDataMeter";
    static final int TRANSACTION_getCurrentUsageBytes = 5;
    static final int TRANSACTION_getPolicyCycleDay = 1;
    static final int TRANSACTION_getPolicyLimitBytes = 3;
    static final int TRANSACTION_setPolicyCycleDay = 2;
    static final int TRANSACTION_setPolicyLimitBytes = 4;
    
    public Stub()
    {
      attachInterface(this, "com.motorola.datameter.proxyservice.IDataMeter");
    } 
    
    public static IDataMeter asInterface(IBinder paramIBinder)
    {
      if (paramIBinder == null) {
        return null;
      } 
      IInterface localIInterface = paramIBinder.queryLocalInterface("com.motorola.datameter.proxyservice.IDataMeter");
      if ((localIInterface != null) && ((localIInterface instanceof IDataMeter))) {
        return (IDataMeter)localIInterface;
      } 
      return new Proxy(paramIBinder);
    } 
    
    public IBinder asBinder()
    {
      return this;
    } 
    
    public boolean onTransact(int paramInt1, Parcel paramParcel1, Parcel paramParcel2, int paramInt2)
      throws RemoteException
    {
      switch (paramInt1)
      {
      default: 
        return super.onTransact(paramInt1, paramParcel1, paramParcel2, paramInt2);
      case 1598968902: 
        paramParcel2.writeString("com.motorola.datameter.proxyservice.IDataMeter");
        return true;
      case 1: 
        paramParcel1.enforceInterface("com.motorola.datameter.proxyservice.IDataMeter");
        int k = getPolicyCycleDay();
        paramParcel2.writeNoException();
        paramParcel2.writeInt(k);
        return true;
      case 2: 
        paramParcel1.enforceInterface("com.motorola.datameter.proxyservice.IDataMeter");
        boolean bool2 = setPolicyCycleDay(paramParcel1.readInt());
        paramParcel2.writeNoException();
        int j = 0;
        if (bool2) {
          j = 1;
        } 
        paramParcel2.writeInt(j);
        return true;
      case 3: 
        paramParcel1.enforceInterface("com.motorola.datameter.proxyservice.IDataMeter");
        long l2 = getPolicyLimitBytes();
        paramParcel2.writeNoException();
        paramParcel2.writeLong(l2);
        return true;
      case 4: 
        paramParcel1.enforceInterface("com.motorola.datameter.proxyservice.IDataMeter");
        boolean bool1 = setPolicyLimitBytes(paramParcel1.readLong());
        paramParcel2.writeNoException();
        int i = 0;
        if (bool1) {
          i = 1;
        } 
        paramParcel2.writeInt(i);
        return true;
      } 
      paramParcel1.enforceInterface("com.motorola.datameter.proxyservice.IDataMeter");
      long l1 = getCurrentUsageBytes();
      paramParcel2.writeNoException();
      paramParcel2.writeLong(l1);
      return true;
    } 
    
    private static class Proxy
      implements IDataMeter
    {
      private IBinder mRemote;
      
      Proxy(IBinder paramIBinder)
      {
        mRemote = paramIBinder;
      } 
      
      public IBinder asBinder()
      {
        return mRemote;
      } 
      
      public long getCurrentUsageBytes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.motorola.datameter.proxyservice.IDataMeter");
          mRemote.transact(5, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        } 
      } 
      
      public String getInterfaceDescriptor()
      {
        return "com.motorola.datameter.proxyservice.IDataMeter";
      } 
      
      public int getPolicyCycleDay()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.motorola.datameter.proxyservice.IDataMeter");
          mRemote.transact(1, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          return i;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        } 
      } 
      
      public long getPolicyLimitBytes()
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.motorola.datameter.proxyservice.IDataMeter");
          mRemote.transact(3, localParcel1, localParcel2, 0);
          localParcel2.readException();
          long l = localParcel2.readLong();
          return l;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        } 
      } 
      
      public boolean setPolicyCycleDay(int paramInt)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.motorola.datameter.proxyservice.IDataMeter");
          localParcel1.writeInt(paramInt);
          mRemote.transact(2, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          boolean bool = false;
          if (i != 0) {
            bool = true;
          } 
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        } 
      } 
      
      public boolean setPolicyLimitBytes(long paramLong)
        throws RemoteException
      {
        Parcel localParcel1 = Parcel.obtain();
        Parcel localParcel2 = Parcel.obtain();
        try
        {
          localParcel1.writeInterfaceToken("com.motorola.datameter.proxyservice.IDataMeter");
          localParcel1.writeLong(paramLong);
          mRemote.transact(4, localParcel1, localParcel2, 0);
          localParcel2.readException();
          int i = localParcel2.readInt();
          boolean bool = false;
          if (i != 0) {
            bool = true;
          } 
          return bool;
        }
        finally
        {
          localParcel2.recycle();
          localParcel1.recycle();
        } 
      } 
    } 
  } 
} 

/* Location:           J:\技术文档\安卓固件相关\moto\classes_dex2jar.jar
 * Qualified Name:     com.motorola.datameter.proxyservice.IDataMeter
 * JD-Core Version:    0.6.2
 */