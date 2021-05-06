package com.example.a50540.lastorder.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Common {
//  public static final String SERVER_URL = "http://121.196.145.100:8082/api";
  public static final String SERVER_URL = "http://192.168.31.68:8082/api";
  public static final String IMAGE_BASE_PATH = "http://121.196.145.100:8080/assets/shop/image/";

  public static Map<String,Object> JsonToMap(JSONObject j){
    Map<String,Object> map = new HashMap<>();
    Iterator<String> iterator = j.keys();
    while(iterator.hasNext())
    {
      String key = (String)iterator.next();
      Object value = null;
      try {
        value = j.get(key);
      } catch (JSONException e) {
        e.printStackTrace();
      }
      map.put(key, value);
    }
    return map;
  }
}

