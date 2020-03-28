package com.rcklos.lentme.utilsOld;

/**
 * 学习，不是为了学习而学习
 * 而是为了解决存在的问题而学习，
 * 如果你感到迷茫，那就去摸索问题，
 * 再来学习当中摸索答案
 */

/**
 * LogUtil 调试输出工具
 * @author RCKLOS
 * 2020年2月20日 15点02分
 */

public class LogUtil {
	
//	public static void main(String args) {
//		//配置printer
//		myPrinter printer = new myPrinter() {
//			public void print(String text) {
//				// TODO Auto-generated method stub
//				System.out.println(text);
//			}
//		};
//		//LogUtil加载printer
//		LogUtil.loadPrinter(printer);
//		//LogUtil设置输出等级(默认为ERROR)
//		//LogUtil.setLoglevel(LogUtil.ERROR);
//		LogUtil.v("LogUtil","这是LogUtil工具类的使用demo");
//	}

	private LogUtil() {}
	
	private static int Loglevel = 5;
	
	private static final int VERBOSE = 1;
	private static final int DEBUG = 2;
	private static final int INFO = 3;
	private static final int WARN = 4;
	private static final int ERROR = 5;
	
	private static myPrinter printer;
	
	/**
	 * LogUtil 使用时必须要用LogUtil
	 * 加载pirnter
	 * @param print
	 */
	public static void loadPrinter( myPrinter Printer ) {
		printer = Printer;
	}
	
	/**
	 * 设置Log输出等级
	 * @param level
	 */
	public static void setLoglevel( int level ) {
		if ( level < 0 ) Loglevel = 0;
		else if ( level > ERROR ) Loglevel = ERROR;
		else Loglevel = level;
	}
	
	private static void print( String text ) {
		printer.print(text);
	}
	
	public static void v( String TAG , String text ){
		if ( Loglevel >= VERBOSE )
			print("VERBOSE：[" +TAG+ "]：" + text  );
	}
	
	public static void e( String TAG , String text ){
		if ( Loglevel >= ERROR )
			print("ERROR：[" +TAG+ "]：" + text  );
	}
	
	public static void i( String TAG , String text) {
		if ( Loglevel >= INFO )
			print("INFO：[" +TAG+ "]：" + text  );
	}
	
	public static void d( String TAG , String text) {
		if ( Loglevel >= DEBUG )
			print("DEBUG：[" +TAG+ "]：" + text  );
	}
	
	public static void w( String TAG , String text) {
		if ( Loglevel >= WARN )
			print("WARN：[" +TAG+ "]：" + text  );
	}
	
	public interface myPrinter {
		
		public void print(String text);

	}

	
}
