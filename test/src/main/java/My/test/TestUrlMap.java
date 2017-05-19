package My.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import com.google.common.collect.Iterables;

/**
 * Created by fan on 17-5-9.
 */
@Slf4j
public class TestUrlMap {
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .build();

    public static void diffMap(String code) throws IOException {
       String oldUrl = "http://newte.trafficeye.com.cn/TEGateway/123456/EventTraffic.json?version=1601&bizcode=ms86bc62fd64n12bcd&adcode="+ code;
       String newUrl = "http://192.168.0.175:8087/getEventByCity.json?cityCode=" + code;

        String oldData = client.newCall(
                new Request.Builder().url(oldUrl).build())
                .execute().body().string();
        String newData = client.newCall(
                new Request.Builder().url(newUrl).build())
                .execute().body().string();
  //      String oldData = Files.toString(new File("F:/test1"), Charset.forName("utf-8"));
  //      String newData = Files.toString(new File("F:/test2"), Charset.forName("utf-8"));


        if (StringUtils.isAnyBlank(oldData, newData)) {
            log.error("获取数据异常, code={}, oldData={}, newData={}", code, oldData, newData);
        }

        JSONObject oldJson = JSON.parseObject(oldData).getJSONObject("result").getJSONObject("city");
        JSONObject newJson = JSON.parseObject(newData).getJSONObject("result");

        /**
         *   "adcode": "110000",
         " updatetime": "201705081530",
         "version": "1601",
         */

        if (!StringUtils.equals(oldJson.getString("adcode"), newJson.getString("adcode"))) {
            log.error("code={}, adcode 不一致, {}!={}", code, oldJson.getString("adcode"), newJson.getString("adcode"));
            return;
        }
        if (!StringUtils.equals(oldJson.getString("updatetime"), newJson.getString("updatetime"))) {
            log.error("code={}, updatetime 不一致, {}!={}", code, oldJson.getString("updatetime"), newJson.getString("updatetime"));
            return;
        }
        if (!StringUtils.equals(oldJson.getString("version"), newJson.getString("version"))) {
            log.error("code={}, version 不一致, {}!={}", code, oldJson.getString("version"), newJson.getString("version"));
            return;
        }

        int count = newJson.getInteger("count");
        JSONArray oldEvents = oldJson.getJSONArray("events");
        JSONArray newEvents = newJson.getJSONArray("events");
        if (count != oldEvents.size() || count != newEvents.size()) {
            log.error("code={} events长度不一致, {}!={}", code, oldEvents.size(), newEvents.size());
//            return;
        }

 int newI = 0;
int linkIndex = 0;
for (int i = 0; i < oldEvents.size(); i++) {
    String oldEvent = StringUtils.trim(oldEvents.getString(i));
    JSONObject newEvent = newEvents.getJSONObject(newI);

    List<String> oldEventList = Splitter.on(",").splitToList(oldEvent);


    /**
     * {
     "roadName": "团结湖中路",
     "roadDirection": "东向西",
     "roadStartName": "水碓子东街",
     "roadEndName": null,
     "startLength": "0",
     "endLength": "0",
     "eventPublishTime": "",
     "creatTime": "",
     "startTime": "2017-02-06 00:00:00",
     "expectExpireTime": "2019-01-01 00:00:00",
     "expireTime": "2017-04-14 18:00:11",
     "publishTime": "",
     "eventRestrict": "1_1_492",
     "eventReason": "0_0_",
     "roadStatus": "",
     "eventDescription": "自2017年2月6日起，朝阳区团结湖中路（团结湖路至水碓子东街），禁止机动车由东向西方向行驶。",
     "remark": "",
     "lonlat": "116.47210551751,39.927174066216",
     "eventUpdateTime": "1494228540214",
     "niLinkIds": "87758027,87758026,17193851,17193850,565554,565555,49143480,49143481"
     }
     */
    if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("roadName")), Strings.nullToEmpty(oldEventList.get(2)))) {
        log.error("code={}, enventIndex={}, roadName 不一致, {}!={}", code, i, oldEventList.get(2), newEvent.getString("roadName"));
       // return;
    }


    if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("roadDirection")), Strings.nullToEmpty(oldEventList.get(3)))) {
        log.error("code={}, enventIndex={}, roadDirection 不一致, {}!={}", code, i, oldEventList.get(3), newEvent.getString("roadDirection"));
        //return;
    }

    if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("roadStartName")), Strings.nullToEmpty(oldEventList.get(4)))) {
        log.error("code={}, enventIndex={}, roadStartName 不一致, {}!={}", code, i, oldEventList.get(4), newEvent.getString("roadStartName"));
       // return;
    }
	 if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("roadEndName")), Strings.nullToEmpty(oldEventList.get(5)))) {
        log.error("code={}, enventIndex={}, roadEndName 不一致, {}!={}", code, i, oldEventList.get(4), newEvent.getString("roadEndName"));
        //return;
    }
     if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("startLength")), Strings.nullToEmpty(oldEventList.get(6)))) {
        log.error("code={}, enventIndex={}, startLength 不一致, {}!={}", code, i, oldEventList.get(6), newEvent.getString("startLength"));
        //return;
    }
     if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("endLength")), Strings.nullToEmpty(oldEventList.get(7)))) {
        log.error("code={}, enventIndex={}, endLength 不一致, {}!={}", code, i, oldEventList.get(7), newEvent.getString("endLength"));
        //return;
    }   
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("eventPublishTime")), Strings.nullToEmpty(oldEventList.get(8)))) {
        log.error("code={}, enventIndex={}, eventPublishTime 不一致, {}!={}", code, i, oldEventList.get(8), newEvent.getString("eventPublishTime"));
        //return;
    } 
     if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("creatTime")), Strings.nullToEmpty(oldEventList.get(9)))) {
        log.error("code={}, enventIndex={}, creatTime 不一致, {}!={}", code, i, oldEventList.get(9), newEvent.getString("creatTime"));
        //return;
    }    
     if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("startTime")), Strings.nullToEmpty(oldEventList.get(10)))) {
        log.error("code={}, enventIndex={}, startTime 不一致, {}!={}", code, i, oldEventList.get(10), newEvent.getString("startTime"));
        //return;
    }    
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("expectExpireTime")), Strings.nullToEmpty(oldEventList.get(11)))) {
        log.error("code={}, enventIndex={}, expectExpireTime 不一致, {}!={}", code, i, oldEventList.get(11), newEvent.getString("expectExpireTime"));
        //return;
    }    
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("expireTime")), Strings.nullToEmpty(oldEventList.get(12)))) {
        log.error("code={}, enventIndex={}, expireTime 不一致, {}!={}", code, i, oldEventList.get(12), newEvent.getString("expireTime"));
        //return;
    }   
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("publishTime")), Strings.nullToEmpty(oldEventList.get(13)))) {
        log.error("code={}, enventIndex={}, publishTime 不一致, {}!={}", code, i, oldEventList.get(13), newEvent.getString("publishTime"));
        //return;
    }  
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("eventRestrict")), Strings.nullToEmpty(oldEventList.get(14) + "_" + oldEventList.get(15) + "_" + oldEventList.get(19)))) {
        log.error("code={}, enventIndex={}, eventRestrict 不一致, {}!={}", code, i, oldEventList.get(14) + "_" + oldEventList.get(15) + "_" + oldEventList.get(19), newEvent.getString("eventRestrict"));
        //return;
    }  
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("eventReason")), Strings.nullToEmpty(oldEventList.get(16) + "_" + oldEventList.get(17) + "_" + oldEventList.get(20)))) {
        log.error("code={}, enventIndex={}, eventReason 不一致, {}!={}", code, i, oldEventList.get(16) + "_" + oldEventList.get(17) + "_" + oldEventList.get(20), newEvent.getString("eventReason"));
        //return;
    }  
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("roadStatus")), Strings.nullToEmpty(oldEventList.get(18)))) {
        log.error("code={}, enventIndex={}, roadStatus 不一致, {}!={}", code, i, oldEventList.get(18), newEvent.getString("roadStatus"));
        //return;
    }   
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("eventDescription")), Strings.nullToEmpty(oldEventList.get(21)))) {
        log.error("code={}, enventIndex={}, eventDescription 不一致, {}!={}", code, i, oldEventList.get(21), newEvent.getString("eventDescription"));
        //return;
    }   
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("remark")), Strings.nullToEmpty(oldEventList.get(22)))) {
        log.error("code={}, enventIndex={}, remark 不一致, {}!={}", code, i, oldEventList.get(22), newEvent.getString("remark"));
        //return;
    } 
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("lonlat")), Strings.nullToEmpty(oldEventList.get(29) + "," + oldEventList.get(30)))) {
        log.error("code={}, enventIndex={}, lonlat 不一致, {}!={}", code, i, oldEventList.get(29) + "," + oldEventList.get(30), newEvent.getString("lonlat"));
        //return;
    }  
      if (!StringUtils.equals(Strings.nullToEmpty(newEvent.getString("eventUpdateTime")), Strings.nullToEmpty(oldEventList.get(31)))) {
        log.error("code={}, enventIndex={}, eventUpdateTime 不一致, {}!={}", code, i, oldEventList.get(31), newEvent.getString("eventUpdateTime"));
        //return;
    } 
    
    String niLinkIdsStr = newEvent.getString("niLinkIds");
    Iterable<String> split = Splitter.on(",").split(niLinkIdsStr);
    if (!Iterables.get(split, linkIndex, "").equals(Strings.nullToEmpty(oldEventList.get(26)))) {
        log.error("code={}, enventIndex={}, niLinkIds 不一致, {}!={}", code, i, oldEventList.get(26), newEvent.getString("niLinkIds"));
       // return;
    }
    linkIndex++;
    if (linkIndex >= Iterables.size(split)) {
        linkIndex = 0;
        newI++;
    }
    //log.info("code={},i={},对比成功", code, i);
    log.info("[{}]code={},roadName={},对比成功", i, code, newEvent.getString("roadName"));
}
    }

	public static void main(String[] args) throws IOException {
    List<String> codes = Files.readLines(new File("F:/code.txt"), Charset.forName("utf-8"));
    for (String code : codes) {
        diffMap(code);
    }
}


}
