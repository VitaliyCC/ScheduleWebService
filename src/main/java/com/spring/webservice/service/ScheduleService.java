package com.spring.webservice.service;

import com.spring.webservice.jsonReader.JsonReader;
import com.spring.webservice.models.Department;
import com.spring.webservice.models.Group;
import com.spring.webservice.models.Schedule;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Service
public class ScheduleService {
    @Value("${URLDEPT}")
    String urlDEPT;
    @Value("${URLGR}")
    String urlGR;
    @Value("${URLS}")
    String urlS;

    private final JsonReader reader;
    private final Logger LOGGER = Logger.getLogger(ScheduleService.class);

    public ScheduleService(JsonReader reader) {
        this.reader = reader;
    }

    @Cacheable("getAllDepartments")
    public List<Department> getAllDepartments() {

        List<Department> resultList = new LinkedList<>();

        JSONObject jsonObject = null;
        try {
            jsonObject = reader.readJson(urlDEPT);
        } catch (IOException e) {
            LOGGER.error("Error in service reading department from JSON - ", e);
        }
        JSONArray departments = (JSONArray) jsonObject.get("departments");

        for (int i = 0; i < departments.length(); i++) {
            JSONObject dep = (JSONObject) departments.get(i);

            Department department = new Department(
                    dep.getInt("CODE"),
                    dep.getString("SHORT"),
                    dep.getString("NAME"),
                    dep.get("CHIEF").toString() == "null" ? " " : (String) dep.get("CHIEF"));
            resultList.add(department);
        }
        return resultList;
    }

    @Cacheable("getAllGroupsByDepartment")
    public List<Group> getAllGroupsByDepartment(Integer depId) {
        List<Group> resultList = new LinkedList<>();

        JSONObject jsonObject = null;
        try {
            jsonObject = reader.readJson(urlGR.concat(String.valueOf(depId)));
        } catch (IOException e) {
            LOGGER.error("Error in service reading groups by department from JSON - ", e);
        }
        JSONArray groups = (JSONArray) jsonObject.get("groups");

        for (int i = 0; i < groups.length(); i++) {
            JSONObject temp = (JSONObject) groups.get(i);

            Group group = new Group(
                    (Integer) temp.get("DEP"),
                    (Integer) temp.get("COURSE"),
                    (Integer) temp.get("STRM"),
                    (Integer) temp.get("GRP"),
                    (String) temp.get("DEPSHORT"),
                    (String) temp.get("NAME"),
                    (String) temp.get("Name_p"));
            resultList.add(group);
        }
        return resultList;
    }

    @Cacheable("findScheduleByGroup")
    public List<Schedule> findScheduleByGroup(Integer depId, Integer course,
                                              Integer stream, Integer groupCode,
                                              Integer subGroupCode) {
        List<Schedule> resultList = new LinkedList<>();
        String time;
        String subGroupCodeNew = subGroupCode == 0 ? "" : String.valueOf(subGroupCode);
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) reader.readJson(urlS + depId +
                    "/" + course +
                    "/" + stream +
                    "/" + groupCode +
                    "/" + subGroupCodeNew).get("schedule");
        } catch (IOException e) {
            LOGGER.error("Error in service reading schedule by group from JSON - ", e);
        }

        JSONArray schedules = jsonObject.names();

        for (int i = 0; i < schedules.length(); i++) {
            time = String.valueOf(schedules.get(i));
            JSONObject temp = jsonObject.getJSONObject(time);

            Schedule schedule = new Schedule(
                    time,
                    temp.getString("teacher"),
                    temp.getString("discipline"),
                    temp.getString("classroom"),
                    temp.getString("group"),
                    (Boolean) temp.get("isLecture")
            );
            resultList.add(schedule);
        }
        return resultList;
    }
}
