/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;
import entity.Course;

import org.json.JSONArray;

/**
 *
 * @author lin
 */
public class JsonReader {
    
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
          sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    public static String[][] dragAllNusMods(String url){
        String[][] arr = new String[4000][5];
        int index = 0;
        try {
            
            JSONObject json = readJsonFromUrl("http://api.nusmods.com/2017-2018/1/moduleList.json");
            //System.out.println(json.toString());
            String moduleCode="";
            String moduleTitle="";
            String moduleInfo = "";
            String workload="";
            String tempUrl;
            String moduleCredit="";
            String prerequisite = "";
            Iterator keys = json.keys();
            while (keys.hasNext()) {
                Object key = keys.next();
                moduleCode = (String) key;
                moduleTitle = json.getString(moduleCode);
                tempUrl = "http://api.nusmods.com/2017-2018/1/modules/" + moduleCode +".json";
                JSONObject newJson = readJsonFromUrl(tempUrl);
                if(newJson.has("ModuleCredit"))
                    moduleCredit = newJson.getString("ModuleCredit");
                if(newJson.has("Prerequisite"))
                    prerequisite = newJson.getString("Prerequisite");
                if(newJson.has("ModuleDescription"))
                    moduleInfo = newJson.getString("ModuleDescription");
                if(newJson.has("Workload"))
                    workload = newJson.getString("Workload");
                //Course course = new Course();
                //System.out.println("course created: " + course.getId());
                //(moduleCode, moduleTitle, moduleInfo ,false,"","", "", "NUS",Integer.parseInt(moduleCredit),workload);
                //System.out.println("course created: " + course.getId() + " and " +course.getSchool());
                //em.flush();
                //em.persist(course);
                arr[index][0]=moduleCode;
                arr[index][1]=moduleTitle;
                arr[index][2]=moduleInfo;
                arr[index][3]=moduleCredit;
                arr[index][4]=workload;
                index++;
            }
            
        } catch (Exception e) {
            System.out.println("Test method in JsonReader: Exception caught!");
            e.printStackTrace();
        }
        //System.out.println(index);
        return arr;
    }
    
    public static String getTimetable(String moduleCode){
        String timetable = "";
        String[] arr;
        int index = 0;
        try {
            JSONObject json = readJsonFromUrl("http://api.nusmods.com/2017-2018/1/modules/" + moduleCode +".json");
            JSONObject time = null;
            if(json.has("Timetable")){
                //json = json.getJSONObject("Timetable");
                //JSONArray dataList = (JSONArray) json.get("Timetable");
                //Object obj = dataList.get(index);
                //json = (JSONObject) obj;
                 JSONArray dataList = json.getJSONArray("Timetable");
                //getJSONObject("Weektext")
                while(true)
                    if(dataList.getJSONObject(index)!=null){
                        System.out.println("dataList"+index+":" + dataList.getJSONObject(index).toString());
                        index++;
                    }
            } 
        } catch (Exception e) {
            System.out.println("Test method in JsonReader: Exception caught!");
            e.printStackTrace();
        }
        
        return timetable;
    }
    
    public void main(String[] args){
    }

}
