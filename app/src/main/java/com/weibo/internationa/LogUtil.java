package com.weibo.internationa;

/**
 * Author：caokai on 2018/12/3 17:48
 * <p>
 * email：caokai@11td.com
 */
import android.util.Log;
import java.util.Arrays;

public class LogUtil {
    static String className = null;
    static boolean debuggable = false;
    static int lineNum;
    static String methodName;

    private static String createLog(String str) {
        return String.format("[%s:%d] %s", new Object[]{methodName, Integer.valueOf(lineNum), str});
    }

    private static void getMethodNames(StackTraceElement[] stackTraceElementArr) {
        className = stackTraceElementArr[1].getFileName();
        methodName = stackTraceElementArr[1].getMethodName();
        lineNum = stackTraceElementArr[1].getLineNumber();
    }

    public static void callStack() {
        d(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    public static void e(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.e(className, createLog(str));
        }
    }

    public static void i(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.i(className, createLog(str));
        }
    }

    public static void d(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(str));
        }
    }

    public static void array(Object... objArr) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.d(className, createLog(Arrays.toString(objArr)));
        }
    }

    public static void v(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.v(className, createLog(str));
        }
    }

    public static void w(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.w(className, createLog(str));
        }
    }

    public static void wtf(String str) {
        if (debuggable) {
            getMethodNames(new Throwable().getStackTrace());
            Log.wtf(className, createLog(str));
        }
    }

    public static void e(Throwable th) {
        if (debuggable) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("message:");
            stringBuilder.append(th.getMessage());
            e(stringBuilder.toString());
            th.printStackTrace();
        }
    }
}
