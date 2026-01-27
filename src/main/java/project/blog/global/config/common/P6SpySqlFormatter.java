package project.blog.global.config.common;

import com.p6spy.engine.logging.Category;
import com.p6spy.engine.spy.P6SpyOptions;
import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class P6SpySqlFormatter implements MessageFormattingStrategy {

    // P6Spy의 공식 포맷터로 해당 클래스 등록
    @PostConstruct
    public void setLogMessageFormat() {
        P6SpyOptions.getActiveInstance().setLogMessageFormat(P6SpySqlFormatter.class.getName());
    }

    /**
     * SQL 실행이 끝난 직후 호출
     * @param elapsed: 쿼리 실행에 걸린 시간(ms)
     * @param category: 로그 종류 (statement, commit 등)
     * @param sql: 파라미터가 채워진 SQL
     */
    @Override
    public String formatMessage(int connectionId, String now, long elapsed, String category, String prepared, String sql, String url) {
        sql = formatSql(category, sql);
        return String.format("[%s] | %dms | %s", category, elapsed, sql);
    }

    private String formatSql(String category, String sql) {
        if (sql == null || sql.trim().isEmpty()) return sql;

        if (Category.STATEMENT.getName().equals(category)) {
            String singleLineSql = sql.replaceAll("\\s+", " ").trim();
            return singleLineSql;
        }

        return sql;
    }

}
