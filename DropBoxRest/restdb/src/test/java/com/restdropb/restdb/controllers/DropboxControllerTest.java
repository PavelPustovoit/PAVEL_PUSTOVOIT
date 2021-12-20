package com.restdropb.restdb.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.jayway.jsonpath.internal.function.text.Length;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.restdropb.restdb.dropboxent.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.web.multipart.MultipartFile;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ComponentScan(basePackages = "com.restdropb.restdb")
public class DropboxControllerTest {
    @Autowired
    private  DropboxClient dropboxClient;
    private String token="sl.A-iiVigqflpKYV-_d62lHGL_EEBxw1Vta93AJiYJOaApH8vfzkngqTyxXMePVrjsji4JmwgdOq3m3ZGuvtCA05fewVTAP9bBakS1edRT3WPVC3jpzgO5eUB5nwCanPRI3zXRKk8";
    private String nameApp="pqm_app_test";
    private String dbxFolder="";
    @Autowired
    private DropboxController dbxController;
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    public DropboxControllerTest(){

    }

    @Test
    void testCheckFile() {
        IDropBoxService dropboxClient1=new  DropboxClient();
        dropboxClient1.setAppDbxClient(nameApp,token);
        var client= dropboxClient1.getDbxClient();
        assertNotNull(client);
        String err="";
        List<String> listFiles= dropboxClient1.GetFiles(dbxFolder,err);
        
        assertTrue(listFiles.size()>0);
    }

    @Test
    void testGetListFiles() {
        var dbxController=new DropboxController();
        ResponseEntity<String> str= dbxController.getListFiles(nameApp,token,dbxFolder);
        assertNotNull(str,"list Files is null");
        assertTrue( str!=null);
    }

 
    //@Test
    void testPutFile() throws Exception {
        MockMultipartFile file  = 
        new MockMultipartFile("file", "hello9.txt", MediaType.TEXT_PLAIN_VALUE, 
        "Hello, Testing DropBox!".getBytes()
      );
      var dbxController=new DropboxController();
      String ret= dbxController.putFile(file,nameApp, token,dbxFolder);
       assertTrue(ret.contains("saved"));
        //MockMvc mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        //mockMvc.perform(multipart("/uploads").file(file)).andExpect(status().isOk());
    }
    @Test
    void testDelFile(){
        var dbxController=new DropboxController();
        String ret=dbxController.delFile(nameApp, token, "", "hello.txt");
        assertTrue(ret.contains("success"));
    }
}
