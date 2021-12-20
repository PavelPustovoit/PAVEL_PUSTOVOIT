package com.restdropb.restdb.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;

import com.restdropb.restdb.dropboxent.utils;

import java.util.Optional;

import com.restdropb.restdb.dropboxent.*;


@ComponentScan(basePackages = { "com.restdropb.restdb.dropboxent" })
@RestController("/")
public class DropboxController {
    
    @Autowired
    private  IDropBoxService dropboxClient;
    public DropboxController(){
        dropboxClient=new DropboxClient();
    }


    @GetMapping("/")
    public String hello(){
        String h="Runing....";
        return h;
    }
    @GetMapping("/getfiles")
    public ResponseEntity<String> getListFiles(@RequestParam String nameapp
    ,@RequestParam String token,@RequestParam String folder) {
        var dropboxClient=new DropboxClient();
        dropboxClient.setAppDbxClient(nameapp,token);
        String out;
        String err="";
        var listFiles= dropboxClient.GetFiles(folder,err);
        if(err!=""){
            return ResponseEntity.ok(err);
        }
        if(listFiles.isEmpty()){
            out="No files";
        }else{
            out=listFiles.stream().reduce((f, out1)->out1+","+f).get();
        }
        
        return ResponseEntity.ok(out);
    }

    @PostMapping("/upload")
    public String putFile(@RequestParam("file") MultipartFile file,@RequestParam String nameapp
    , @RequestParam String token
    , @RequestParam("tofolder") String folder){
        try{
            String rootFolder=System.getProperty("user.dir")+"/uploads/";
            var fileName= utils.saveFile(rootFolder, file);
            dropboxClient.setAppDbxClient(nameapp,token);
            String pf=folder+"/"+file.getOriginalFilename();
            dropboxClient.sendFileToDropBox(fileName, pf);
        }catch(RuntimeException ex){
            return JSONObject.escape("{error:'Error at save'}");
        }
        
        return JSONObject.escape("{'success':'saved'}");
    }

    @DeleteMapping("/")
    public String delFile(@RequestParam String nameapp,@RequestParam String token
    , @RequestParam("fromfolder") String folder,@RequestParam("file") String file){
        dropboxClient.setAppDbxClient(nameapp,token);
        var err= dropboxClient.deleteFile( folder,file);
        if(err!="")
            return JSONObject.escape("{\"error\":\"deleted\"}");

        return JSONObject.escape("{\"success\":\"deleted\"}");
    }

}
