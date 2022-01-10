package com.pgmate.app.util;

import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcClient;
import org.apache.xmlrpc.XmlRpcException;


public class WhoisSMS
{
  private String mId;
  private String mPass;
  private String mTo;
  private String mFrom;
  private String mMessage;
  private String mDate;
  private String mYear;
  private String mMonth;
  private String mSmsType;
  private int mRetCode;
  private String mRetMessage;
  private Hashtable mStatistic;
  private int mLastPoint;
  private static String server_url = "http://www.whoisweb.net/emma/API/EmmaSend_JSP.php";
  
  public void setId(String arg)
  {
    this.mId = arg;
  }
  
  public void setPass(String arg)
  {
    this.mPass = arg;
  }
  
  public void setTo(String arg)
  {
    this.mTo = arg;
  }
  
  public void setFrom(String arg)
  {
    this.mFrom = arg;
  }
  
  public void setMessage(String arg)
  {
    this.mMessage = arg;
  }
  
  public void setDate(String arg)
  {
    this.mDate = arg;
  }
  
  public void setYear(Integer arg)
  {
    this.mYear = arg.toString();
  }
  
  public void setMonth(Integer arg)
  {
    this.mMonth = arg.toString();
  }
  
  public void setSmsType(String arg)
  {
    this.mSmsType = arg.toString();
  }
  
  public String getDate()
  {
    return this.mYear + "-" + this.mMonth;
  }
  
  public void login(String pId, String pPass)
  {
    setId(pId);
    setPass(pPass);
  }
  
  public void setParams(String pId, String pPass, String pTo, String pFrom, String pMessage, String pDate)
  {
    setId(pId);
    setPass(pPass);
    setTo(pTo);
    setFrom(pFrom);
    setMessage(pMessage);
    setDate(pDate);
  }
  
  public void setParams(String pTo, String pFrom, String pMessage, String pDate)
  {
    setTo(pTo);
    setFrom(pFrom);
    setMessage(pMessage);
    setDate(pDate);
  }
  
  public void setParams(String pTo, String pFrom, String pMessage, String pDate, String pSmsType)
  {
    setTo(pTo);
    setFrom(pFrom);
    setMessage(pMessage);
    setDate(pDate);
    setSmsType(pSmsType);
  }
  
  public void setParams(String pId, String pPass, Integer pYear, Integer pMonth)
  {
    setId(pId);
    setPass(pPass);
    setYear(pYear);
    setMonth(pMonth);
  }
  
  public void setParams(Integer pYear, Integer pMonth)
  {
    setYear(pYear);
    setMonth(pMonth);
  }
  
  public void setUtf8()
  {
    server_url = "http://www.whoisweb.net/emma/API/EmmaSend_JSP_All.php";
  }
  
  public int getRetCode()
  {
    return this.mRetCode;
  }
  
  public String getRetMessage()
  {
    return this.mRetMessage;
  }
  
  public int getLastPoint()
  {
    return this.mLastPoint;
  }
  
  public Hashtable getStatistic()
  {
    return this.mStatistic;
  }
  
  public void emmaSend()
  {
    try
    {
      XmlRpcClient server = new XmlRpcClient(server_url);
      
      Vector params = new Vector();
      
      params.addElement(this.mId.toString());
      params.addElement(this.mPass.toString());
      params.addElement(this.mTo.toString());
      params.addElement(this.mFrom.toString());
      params.addElement(this.mMessage.toString());
      params.addElement(this.mDate.toString());
      if (this.mSmsType == null)
      {
        this.mSmsType = new String();
        this.mSmsType = "";
      }
      params.addElement(this.mSmsType.toString());
      
      Hashtable result = (Hashtable)server.execute("EmmaSend", params);
      if ((result.get("Code") instanceof String))
      {
        String retCode = (String)result.get("Code");
        this.mRetCode = new Integer(retCode).intValue();
      }
      else
      {
        this.mRetCode = ((Integer)result.get("Code")).intValue();
      }
      if (this.mRetCode == 0) {
        this.mLastPoint = ((Integer)result.get("LastPoint")).intValue();
      }
      this.mRetMessage = result.get("CodeMsg").toString();
    }
    catch (XmlRpcException exception)
    {
      System.err.println("JavaClient: XML-RPC Fault #" + 
        Integer.toString(exception.code) + ": " + 
        exception.toString());
    }
    catch (Exception exception)
    {
      exception.printStackTrace();
    }
  }
  
  public void emmaPoint()
  {
    try
    {
      XmlRpcClient server = new XmlRpcClient(server_url);
      
      Vector params = new Vector();
      
      params.addElement(this.mId.toString());
      params.addElement(this.mPass.toString());
      
      Hashtable result = (Hashtable)server.execute("EmmaPoint", params);
      if ((result.get("Code") instanceof String))
      {
        String retCode = (String)result.get("Code");
        this.mRetCode = new Integer(retCode).intValue();
      }
      else
      {
        this.mRetCode = ((Integer)result.get("Code")).intValue();
      }
      if (this.mRetCode == 0) {
        this.mLastPoint = ((Integer)result.get("Point")).intValue();
      }
      this.mRetMessage = result.get("CodeMsg").toString();
    }
    catch (XmlRpcException exception)
    {
      System.err.println("JavaClient: XML-RPC Fault #" + 
        Integer.toString(exception.code) + ": " + 
        exception.toString());
    }
    catch (Exception exception)
    {
      System.err.println("JavaClient: " + exception.toString());
      exception.printStackTrace();
    }
  }
  
  public void emmaStatistic()
  {
    try
    {
      XmlRpcClient server = new XmlRpcClient(server_url);
      
      Vector params = new Vector();
      
      params.addElement(this.mId.toString());
      params.addElement(this.mPass.toString());
      params.addElement(getDate());
      
      Hashtable result = (Hashtable)server.execute("EmmaStatistic", params);
      if ((result.get("Code") instanceof String))
      {
        String retCode = (String)result.get("Code");
        this.mRetCode = new Integer(retCode).intValue();
      }
      else
      {
        this.mRetCode = ((Integer)result.get("Code")).intValue();
      }
      if (this.mRetCode == 0)
      {
        this.mLastPoint = ((Integer)result.get("Point")).intValue();
        this.mStatistic = ((Hashtable)result.get("Statistics"));
      }
      this.mRetMessage = result.get("CodeMsg").toString();
    }
    catch (XmlRpcException exception)
    {
      System.err.println("JavaClient: XML-RPC Fault #" + 
        Integer.toString(exception.code) + ": " + 
        exception.toString());
    }
    catch (Exception exception)
    {
      System.err.println("JavaClient: " + exception.toString());
    }
  }
  
  public static void main(String[] args)
  {
    WhoisSMS whois_sms = new WhoisSMS();
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);
    Date cNow = new Date();
    String toDayTime = sdf.format(cNow);
    System.out.println(toDayTime);
    
    whois_sms.login("smstest", "smstest");
    whois_sms.setUtf8();
    whois_sms.emmaPoint();
    System.out.println("getRetCode : " + whois_sms.getRetCode());
    System.out.println("getRetMessage : " + whois_sms.getRetMessage());
    System.out.println("getLastPoint : " + whois_sms.getLastPoint());
  }
}
