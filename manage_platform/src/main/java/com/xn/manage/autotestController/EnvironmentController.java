package com.xn.manage.autotestController;

import com.xn.manage.Enum.DatabaseTypeEnum;
import com.xn.manage.bean.Service;
import com.xn.manage.bean.System;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/autotest/environment")
public class EnvironmentController {
	
	@RequestMapping(value="/{path}", method = RequestMethod.GET)
	public String getEnvironmentmPage(@PathVariable String  path, ModelMap model) {
		List<System> systemList = new ArrayList<System>();
		systemList.add(new System(1,"风控规则"));
		systemList.add(new System(2,"支付中心"));
		systemList.add(new System(3,"征信公司"));
		systemList.add(new System(4,"商户平台"));

		List<Service> serviceList = new ArrayList<Service>();
		serviceList.add(new Service(1,"ruleengineService"));
		serviceList.add(new Service(2,"riskWeb"));
		serviceList.add(new Service(3,"huaZhengCreditService"));

		List<DatabaseTypeEnum> databaseTypeEnumList=new ArrayList<DatabaseTypeEnum>();
		for(DatabaseTypeEnum item:DatabaseTypeEnum.values()){
			databaseTypeEnumList.add(item);
		}

		model.put("serviceList", serviceList);
		model.put("systemList", systemList);
		model.put("databaseTypeEnumList",databaseTypeEnumList);

		return "environment/" + path;
	}

}
