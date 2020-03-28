package com.rcklos.lentme.clientOld;

import com.rcklos.lentme.dataOld.constPool;

public abstract class ClientUtils {
    protected static String TAG = constPool.TAG + "ClientUtils";
    //public static final String CLINET_DATA_MATCH = "MATCH";//占用5个字节
    //public static final String CLINET_DATA_MATCH_CANCEL = "MATCHCANCEL";//占用11个字节

    /**
     * 从data 中提取 user信息
     * @param data
     * @return
     */
    public static String getUserFromData(String data) {
        if ( data == null ) {
            System.out.println("data:"+data+"获取User失败-->非法数据包");
            return null;
        }
        if ( data.length() < constPool.LENGTH_USER ) {
            System.out.println("data:"+data+"获取User失败-->非法数据包");
            return null;
        }
        return data.substring(0, constPool.LENGTH_USER);
    }

    /**
     * 获取数据包类型
     * @param data
     * @return
     */
    public static int getDataType(String data) {
        String stringType = data.substring(data.length()-constPool.LENGTH_TYPE);
        int type = new Integer(stringType);
        return type;
    }

    /**
     * 处理心跳包函数
     * @return
     */
    public abstract boolean aliveAction(String user,String data);

    /**
     * 处理匹配包函数
     * @param user
     * @return
     */
    public abstract boolean matchAction(String user);

    /**
     * 处理信息包函数
     * @param object
     * @return
     */
    public abstract boolean msgAction(Object object);
    /**
     * 检查用户是否违法
     * @param data
     * @return
     */
    public abstract String getUserInLogin( String data );

    /**
     * 解包函数
     * @param user
     * @param data
     * @return
     * @throws Exception
     */
    public abstract boolean unpack( String user , String data ) throws Exception;
}
