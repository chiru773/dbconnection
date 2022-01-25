package jp.co.testdbconnection.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import jp.co.testdbconnection.manager.ResourceManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SQLを実行・管理する機能を提供するクラス<br>
 */
public class SQLExecutor {

	/** ロガー。<br> */
	private static final Log LOG = LogFactory.getLog(SQLExecutor.class);
	/** クエリタイムアウト(sec)。<br> */
	private static final int QUERY_TIMEOUT_TIME = ResourceManager.getInt("query.timeout.time");
	/** セッション管理オブジェクト。<br> */
	private DBSessionManager _session;

	/**
	 * コンストラクタ。<br>
	 */
	public SQLExecutor() {
		this._session = new DBSessionManager();
	}

	/**
	 * リソースを解放します。<br>
	 */
	public void close() {
		try {
			this._session.CloseConnection();
		} catch (Exception e) {
			if (LOG.isErrorEnabled()) {
                LOG.error("予期せぬクローズ失敗", e);
            }
		}
	}

	/**
	 * コミットします。<br>
	 * @throws SQLException SQL例外
	 */
	public void commit() throws SQLException {
		this._session.commit();
	}

	/**
     * ロールバックします。<br>
     * @return 処理結果 true:成功 false:失敗
     */
	public boolean rollback() {
		try {
			this._session.rollback();
		} catch (SQLException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error("roll back false", e);
            }
            return false;
        }
        return true;
    }

    /**
     * コネクションを取得します。<br>
     * @return コネクション
     * @throws SQLException SQL例外
     */
	private Connection getConnection() throws SQLException {
		return this._session.getConnection();
	}

	/**
	 * 可変バインド変数用の置換文字列を取得します。<br>
     * @param paramLength 可変バインド数
     * @return 可変バインド変数用の置換文字列
	 */
	private String getBindParam(int paramLength) {
		StringBuilder result = new StringBuilder();
		result.append('?'); // 必ず1つ以上
		for (int i = 1; i < paramLength; i++) {
			result.append(",?");
		}
		return result.toString();
	}

    /**
     * プリペア済ステートメントを取得します。<br>
     * SQLはリソースより取得します。
     * @param sqlId SQL取得ID
     * @return プリペア済ステートメント
     * @throws SQLException SQL例外
     */
	private PreparedStatement getPreparedStatement(String sqlId) throws SQLException {
		final String logMethod = "getPreparedStatement(" + sqlId + ')';
        if (LOG.isTraceEnabled()) {
            LOG.trace(logMethod + "start");
        }

        String sql = ResourceManager.getSql(sqlId);
        if (LOG.isDebugEnabled()) {
            LOG.debug("sql=" + sql);
        }

        PreparedStatement result = getConnection().prepareStatement(sql);
        result.setQueryTimeout(QUERY_TIMEOUT_TIME);

        if (LOG.isTraceEnabled()) {
            LOG.trace(logMethod + "end");
        }
        return result;
    }

	/**
     * プリペア済ステートメントを取得します。<br>
     * SQLはリソースより取得します。<br>
     * SQLの{0}を可変バインド数1に指定した値で展開します。<br>
     * @param sqlId SQL取得ID
     * @param paramLength1 可変バインド数1
     * @return プリペア済ステートメント
     * @throws SQLException SQL例外
     */
	private PreparedStatement getPreparedStatement(String sqlId, int paramLength1) throws SQLException {
		final String logMethod = "getPreparedStatement(" + sqlId + ", " + paramLength1 + ')';
		if (LOG.isTraceEnabled()) {
			LOG.trace(logMethod + "start");
		}

		String template = ResourceManager.getSql(sqlId);
		if (LOG.isDebugEnabled()) {
            LOG.debug("template=" + template);
        }

		String sql = MessageFormat.format(template, getBindParam(paramLength1));
		if (LOG.isDebugEnabled()) {
            LOG.debug("sql=" + sql);
        }

		PreparedStatement result = getConnection().prepareStatement(sql);
		result.setQueryTimeout(QUERY_TIMEOUT_TIME);

        if (LOG.isTraceEnabled()) {
            LOG.trace(logMethod + "end");
        }
        return result;
	}

	/**
	 * 情報を取得します。<br>
    * @throws SQLException SQL例外
	 */
	public int getCount() throws SQLException {
		final String logMethod = "getCount()" + ' ';
		if (LOG.isInfoEnabled()) {
			LOG.info(logMethod + "start");
		}

		int result = 0;
		PreparedStatement ps = this.getPreparedStatement("TEST_001");
		try {
			ResultSet rs = ps.executeQuery();
			try {
				if (rs.first()) {
					result = rs.getInt("COUNT");
				}
			} finally {
				rs.close();
			}
		} finally {
			ps.close();
		}

		if (LOG.isInfoEnabled()) {
           LOG.info(logMethod + "end");
       }
		return result;
	}


//	/**
//	 * 情報を取得します。<br>
//     * @param code
//     * @throws SQLException SQL例外
//	 */
//	public Book getModel(String code) throws SQLException {
//		final String logMethod = "";
//		if (LOG.isInfoEnabled()) {
//            LOG.info(logMethod + "start");
//        }
//
//		Book result = null;
//		PreparedStatement ps = this.getPreparedStatement("sql1");
//		try {
//				ps.setString(1, "bookId");
//
//			ResultSet rs = ps.executeQuery();
//			try {
//				if (rs.next()) {
//					result = new Book(rs.getString("bookName"));
//				}
//			} finally {
//				rs.close();
//			}
//		} finally {
//			ps.close();
//		}
//
//		if (LOG.isInfoEnabled()) {
//            LOG.info(logMethod + "end");
//        }
//		return result;
//	}
//
//	/**
//	 * 更新します。<br>
//	 * @param Id 追加キー
//	 * @throws SQLException SQL例外
//	 */
//	public void updateTblBookStatus(Book target)throws SQLException {
//		final String logMethod = "";
//		if (LOG.isInfoEnabled()) {
//			LOG.info(logMethod + "start");
//		}
//		PreparedStatement ps = getPreparedStatement("sql2", target.geList().size());
//		try {
//			int paramPos = 1;
//			for (Long Id : target.getList()) {
//                ps.setLong(paramPos++, id);
//            }
//
//			int updateCnt = ps.executeUpdate();
//			if (updateCnt == target.getList().size()) {
//				if (LOG.isDebugEnabled()) {
//					LOG.debug("updateCnt=" + updateCnt);
//				}
//			} else {
//				if (LOG.isWarnEnabled()) {
//					LOG.warn("updateCnt=" + updateCnt);
//				}
//			}
//		} finally {
//			ps.close();
//		}
//
//		if (LOG.isInfoEnabled()) {
//            LOG.info(logMethod + "end");
//        }
//	}
}
