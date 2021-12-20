package com.restdropb.restdb;

import com.restdropb.restdb.dropboxent.IDropBoxService;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.google.appengine.repackaged.com.google.api.client.util.Value;
import com.restdropb.restdb.dropboxent.DropboxClient;

@SpringBootTest
class RestdbApplicationTests {
	@Autowired(required = true)
	private  IDropBoxService dropboxClient;

	@Value("${app.token}") String token;

	@Test
	void contextLoads() {
		//IDropBoxService dropboxClient1=new DropboxClient();
		dropboxClient.setAppDbxClient("app_test_pqm","_p1KlXmbNMjQAAAAAAAAAAaYEpIwohK0Oi85baKzTXYxKYGlbcAFCX1Fz5jcMwTYV");
		var client= dropboxClient.getDbxClient();
        Assertions.assertNotNull(client);
        String folder="";//app_test_pqm";
        String err="";
        var res= dropboxClient.GetFiles(folder,err);
        if(res.isEmpty() && err!=""){
            res.add(err);
        }

	}
	@Test
	void tok(){
		String t=token;
	}

}
