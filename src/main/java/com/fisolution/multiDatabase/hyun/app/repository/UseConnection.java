package com.fisolution.multiDatabase.hyun.app.repository;

import com.fisolution.multiDatabase.hyun.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class UseConnection {

    private final DataSource mainDataSource;
    private final DataSource secondDataSource;

    /**
     * 데이터 소스를 이용하여 다중 DB 설정 완료
     * @param mainDataSource
     * @param secondDataSource
     */
    public UseConnection(DataSource mainDataSource, @Qualifier("secondDataSource") DataSource secondDataSource) {
        this.mainDataSource = mainDataSource;
        this.secondDataSource = secondDataSource;
    }

    public Member mainSave(Member member) throws SQLException {
        String sql = "insert into member(id, name) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = mainDataSource.getConnection();
            log.info("{} {}", con.getMetaData().getURL(), con);
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, LocalDateTime.now().getNano());
            pstmt.setString(2, "d");
            pstmt.executeUpdate();
            con.commit();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            con.rollback();
            return null;
        } finally {
            close(con, pstmt, null);
        }
    }

    public Member secondSave(Member member) throws SQLException {
        String sql = "insert into member(id, name) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = secondDataSource.getConnection();
            log.info("{} {}", con.getMetaData().getURL(), con);
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, LocalDateTime.now().getNano());
            pstmt.setString(2, "member.getName()");
            pstmt.executeUpdate();
            con.commit();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            con.rollback();
            return null;
        } finally {
            close(con, pstmt, null);
        }
    }

    private void close(Connection con, Statement stmt, ResultSet rs) {

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

    }

}
