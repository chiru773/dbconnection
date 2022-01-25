package jp.co.testdbconnection.manager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * リソース管理<br>
 * @author Chiru
 */
public class ResourceManager {

	/** ログ出力<br> */
	private static final Log LOG = LogFactory.getLog( ResourceManager.class );
	 /** システムリソース<br> */
	private static final ResourceBundle SYS_RESOURCE;
	 /** SQLリソース<br> */
	private static final ResourceBundle SQL_RESOURCE;

	static {
		ResourceBundle tmpResource;
		 // システムリソース
		tmpResource = null;
		try {
			tmpResource = ResourceBundle.getBundle( "resource" );
		} catch( Exception e ) {
			e.printStackTrace();
			if( LOG.isErrorEnabled() ) {
				LOG.error( e.getMessage(), e );
			}
		}
		SYS_RESOURCE = tmpResource;

		 // SQLリソース
		tmpResource = null;
		try {
			tmpResource = ResourceBundle.getBundle( "resource/sqlResource" );
		} catch( Exception e ) {
			e.printStackTrace();
			if( LOG.isErrorEnabled() ) {
				LOG.error( e.getMessage(), e );
			}
		}
		SQL_RESOURCE = tmpResource;
	}

	/**
	 * システム設定値を数値で取得します。<br>
	 * @param id 登録ID
	 * @return システム設定値
	 */
	public static int getInt(String id) {
		return getInt( id, Integer.MIN_VALUE );
	}

	/**
	 * システム設定値を数値で取得します。<br>
	 * @param id 登録ID
	 * @param defaultValue 初期値
	 * @return システム設定値
	 */
	public static int getInt(String id, int defaultValue) {
		try {
			return Integer.parseInt( getString( id ) );
		} catch( Exception e ) {
			//
		}
		return defaultValue;
	}

	/**
	 * システム設定値を数値で取得します。<br>
	 * @param id 登録ID
	 * @return システム設定値
	 */
	public static long getLong(String id) {
		return getLong( id, Long.MIN_VALUE );
	}

	/**
	 * システム設定値を数値で取得します。<br>
	 * @param id 登録ID
	 * @param defaultValue 初期値
	 * @return システム設定値
	 */
	public static long getLong(String id, long defaultValue) {
		try {
			return Long.parseLong( getString( id ) );
		} catch( Exception e ) {
			//
		}
		return defaultValue;
	}

	/**
	 * システム設定値を取得します。<br>
	 * @param id 登録ID
	 * @return システム設定値
	 */
	public static String getString(String id) {
		try {
			return SYS_RESOURCE.getString( id );
		} catch( MissingResourceException mre ) {
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "getString(String) id=" + id, mre );
			}
		} catch( Exception e ) {
			if( LOG.isErrorEnabled() ) {
				LOG.error( "getString(String) id=" + id, e );
			}
		}
		return null;
	}

	/**
	 * システム設定値を取得します。<br>
	 * @param id 登録ID
	 * @param defaultValue 初期値
	 * @return システム設定値
	 */
	public static final String getString(String id, String defaultValue) {
		String result = getString( id );
		return ( result == null ) ? defaultValue : result;
	}

	/**
	 * システム設定値をリストで取得します。<br>
	 * @param id 登録ID
	 * @return システム設定値
	 */
	public static String[] getStringArray(String id) {
		try {
			String val = SYS_RESOURCE.getString( id );
			return val.split(",");
		} catch( Exception e ) {
			if( LOG.isErrorEnabled() ) {
				LOG.error( "getStringArray(String) id=" + id, e );
			}
		}
		return null;
	}

	/**
	 * システム設定値を理論値で取得します。<br>
	 * @param id 登録ID
	 * @return 理論値 false:未登録、又は"true"以外(大文字/小文字無視)
	 */
	public static final boolean getBoolean(String id) {
		return Boolean.parseBoolean( getString( id ) );
	}

	/**
	 * システム設定値をSQL日付で取得します。<br>
	 * @param id 登録ID
	 * @param defaultValue 初期値
	 * @return SQL日付
	 */
	public static final Date getSqlDate(String id, Date defaultValue) {
		try {
			return new Date(new SimpleDateFormat( "yyyy-MM-dd" )
											.parse( getString( id ) ).getTime() );
		} catch( Exception e ) {
			if( LOG.isTraceEnabled() ) {
				LOG.trace( "getString(String) id=" + id, e );
			}
		}
		return defaultValue;
	}

	/**
	 * SQLを取得します。<br>
	 * @param id SQL登録ID
	 * @return SQL
	 */
	public static String getSql(String id) {
		try {
			return SQL_RESOURCE.getString( id );
		} catch( Exception e) {
			if( LOG.isErrorEnabled() ) {
				LOG.error( "getSql(String) id=" + id, e );
			}
		}
		return null;
	}

}
