package jp.co.testdbconnection.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import jp.co.testdbconnection.manager.ResourceManager;

/**
 * DB に接続するための セッションを管理するクラス<br>
 */
public class DBSessionManager {

	/** 接続対象DB。<br> */
	private static final String DB = ResourceManager.getString("db.name");
	/** DBのホスト。<br> */
	private static final String HOST = ResourceManager.getString("db.host");
	/** パスワード。<br> */
	private static final String PASS = ResourceManager.getString("db.pass");
	/** DBのユーザ。<br> */
	private static final String USER = ResourceManager.getString("db.user");
	/** コネクション。<br> */
	private Connection _con = null;

	/**
	 * コンストラクタ。<br>
	 */
	public DBSessionManager() {
		this._con = null;
	}

	/**
	 * コネクションを閉じます。<br>
	 * @throws SQLException SQl例外
	 */
	public void CloseConnection() throws SQLException {
		if (this._con != null && !this._con.isClosed()) {
			this._con.close();
		}
	}

	/**
	 * コミットします。<br>
	 * @throws SQLException SQl例外
	 */
	public void commit() throws SQLException {
		this._con.commit();
	}

	/**
	 * コネクションを取得します。<br>
	 * @return コネクション
     * @throws SQLException SQL例外
	 */
	public Connection getConnection() throws SQLException {
		if ((this._con != null) && !this._con.isClosed()) {
			return this._con;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
		String url = "jdbc:mysql://" + HOST + "/" + DB;
		this._con = DriverManager.getConnection(url, USER, PASS);
		this._con.setAutoCommit(false);
		return this._con;
	}

	/**
	 * ロールバックします。<br>
	 * @throws SQLException SQL例外
	 */
	public void rollback() throws SQLException {
		this._con.rollback();
	}
}
