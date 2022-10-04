package com.fisolution.multiDatabase.hyun;

import com.fisolution.multiDatabase.hyun.app.repository.UseConnection;
import com.fisolution.multiDatabase.hyun.app.repository.UseEntityManger;
import com.fisolution.multiDatabase.hyun.app.repository.UseTransactionAnnotation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLException;

@SpringBootTest
class TestApplicationTests {

	@Autowired
	private UseConnection useConnection;
	@Autowired
	private UseEntityManger useEntityManger;
	@Autowired
	private UseTransactionAnnotation useTransactionAnnotation;

	@Test
	void contextLoads() throws SQLException {
		Member test = new Member(5L, "test");
		useConnection.mainSave(test);
		useConnection.secondSave(test);
		useConnection.mainSave(test);
		useConnection.secondSave(test);
	}

	@Test
	void useEm() throws SQLException {
		Member test = new Member(5L, "test");
		useEntityManger.mainSave(test);
		useEntityManger.secondSave(test);
	}

	@Test
	void useAnnotation(){
		Member test = new Member("test");
		Member test1 = new Member("test");
		useTransactionAnnotation.secondSave(test);
//		useTransactionAnnotation.mainSave(test);
	}

}
