package com.ziroom.ferrari.test;

import com.ziroom.ferrari.dao.DataChangeMessageDao;
import com.ziroom.ferrari.produce.MqProduceClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class FerrariApplicationTests {
	private MqProduceClient mqProduceClient;
	@Test
	public void contextLoads() {
		if (mqProduceClient == null){
			mqProduceClient = new MqProduceClient();
		}

		System.out.print("1");
	}

}
