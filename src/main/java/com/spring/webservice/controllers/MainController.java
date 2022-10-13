package com.spring.webservice.controllers;

import com.spring.webservice.models.Department;
import com.spring.webservice.models.Group;
import com.spring.webservice.models.Schedule;
import com.spring.webservice.service.ScheduleService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
public class MainController {
    private final Logger LOGGER = Logger.getLogger(MainController.class);
    private final ScheduleService scheduleService;

    public MainController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/departments")
    public List<Department> findAllDepartment() {
        LOGGER.debug("Sending all department info");

        return scheduleService.getAllDepartments();
    }

    @GetMapping("/groups/{deptId}")
    public List<Group> findGroupByDepartment(@PathVariable int deptId) {
        LOGGER.debug("Sending all group by department with id - " + deptId);

        return scheduleService.getAllGroupsByDepartment(deptId);
    }

    @GetMapping("/schedule/{deptId}/{course}/{stream}/{groupCode}/{subGroupCode}")
    public List<Schedule> findScheduleByGroupInfo(
            @PathVariable int deptId,
            @PathVariable int course,
            @PathVariable int stream,
            @PathVariable int groupCode,
            @PathVariable int subGroupCode) {
        LOGGER.debug("Sending schedule for group with deptId - " + deptId +
                " course - " + course + " stream - " + stream +
                " groupCode - " + groupCode + " subGroupCode - " + subGroupCode);

        return scheduleService.findScheduleByGroup(deptId, course, stream, groupCode, subGroupCode);
    }
}
