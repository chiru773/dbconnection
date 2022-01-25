package jp.co.testdbconnection;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import jp.co.testdbconnection.util.SQLExecutor;

public class TestDBConnection {

	/** ロガー。<br> */
	private static final Log LOG = LogFactory.getLog(TestDBConnection.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final String logMethod = "main()" + ' ';
		SQLExecutor sqlExe = new SQLExecutor();
		int cnt = 0;
		try {
			cnt = sqlExe.getCount();
		} catch (SQLException e) {
			if (LOG.isErrorEnabled()) {
				LOG.error(logMethod + e);
			}
		}

		if (LOG.isInfoEnabled()) {
			LOG.info(logMethod + "件数:" + cnt);
		}
	}

}
