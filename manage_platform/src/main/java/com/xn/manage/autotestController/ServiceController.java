package com.xn.manage.autotestController;

<<<<<<< HEAD
import com.xn.company.dto.CompanyDto;
import com.xn.company.dto.DepartmentDto;
import com.xn.company.service.CompanyService;
import com.xn.company.service.DepartmentService;
import com.xn.interfacetest.dto.TestServiceDto;
import com.xn.interfacetest.dto.TestSystemDto;
import com.xn.interfacetest.service.TestServiceService;
import com.xn.interfacetest.service.TestSystemService;
=======
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
>>>>>>> hezhouxiyiyangde
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xn.common.api.CompanyService;
import com.xn.common.api.DepartmentService;
import com.xn.common.base.CommonResult;
import com.xn.common.dto.CompanyDto;
import com.xn.common.dto.DepartmentDto;
import com.xn.interfacetest.Enum.CommonResultEnum;
import com.xn.interfacetest.api.TestServiceService;
import com.xn.interfacetest.api.TestSystemService;
import com.xn.interfacetest.dto.TestServiceDto;
import com.xn.interfacetest.dto.TestSystemDto;

@Controller
@RequestMapping("/autotest/service")
public class ServiceController {
<<<<<<< HEAD
=======
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

>>>>>>> hezhouxiyiyangde
	@Autowired
	private CompanyService companyService;

	@Autowired
	private DepartmentService departmentService;

	@Autowired
<<<<<<< HEAD
	private TestServiceService serviceService;

	@Autowired
	private TestSystemService systemService;


	@RequestMapping(value="/{path}", method = RequestMethod.GET)
	public String getSystemPage(@PathVariable String  path, ModelMap model) {
=======
	private TestServiceService testServiceService;

	@Autowired
	private TestSystemService testSystemService;


	@RequestMapping(value="/service_list", method = RequestMethod.GET)
	public String getSystemPage(HttpServletRequest request, ModelMap model) {
		//查询公司
>>>>>>> hezhouxiyiyangde
		List<CompanyDto> companyList = new ArrayList<CompanyDto>();
		CompanyDto dto = new CompanyDto();
		companyList = companyService.list(dto);

<<<<<<< HEAD

		List<TestSystemDto> systemList = new ArrayList<TestSystemDto>();
		TestSystemDto systemDto = new TestSystemDto();
		systemList = systemService.list(systemDto);


=======
		//查询系统
		List<TestSystemDto> systemList = new ArrayList<TestSystemDto>();
		TestSystemDto systemDto = new TestSystemDto();
		systemList = testSystemService.list(systemDto);

		//查询部门
>>>>>>> hezhouxiyiyangde
		List<DepartmentDto> departmentList = new ArrayList<DepartmentDto>();
		DepartmentDto departmentDto = new DepartmentDto();
		departmentList = departmentService.list(departmentDto);

<<<<<<< HEAD
		List<TestServiceDto> serviceList = new ArrayList<TestServiceDto>();
		TestServiceDto serviceDto = new TestServiceDto();
		serviceList = serviceService.list(serviceDto);
=======
		//查询服务-获取参数
		String companyId = request.getParameter("selectCompanyId");
		String departmentId = request.getParameter("selectDepartmentId");
		String systemId = request.getParameter("selectSystemId");
		String serviceName = request.getParameter("serviceName");

		Map<String,Object> params = new HashMap<String,Object>();
		if(StringUtils.isNotBlank(companyId) && !"null".equals(companyId)){
			params.put("companyId",companyId);
			model.put("companyId",companyId);
		}

		if(StringUtils.isNotBlank(departmentId) && !"null".equals(departmentId)){
			params.put("departmentId",departmentId);
			model.put("departmentId",departmentId);
		}

		if(StringUtils.isNotBlank(systemId) && !"null".equals(systemId)){
			params.put("systemId",systemId);
			model.put("systemId",systemId);
		}

		if(StringUtils.isNotBlank(serviceName) && !"null".equals(serviceName)){
			params.put("name",serviceName);
			model.put("name",serviceName);
		}


		List<TestServiceDto> serviceList = new ArrayList<TestServiceDto>();
		serviceList = testServiceService.listByParams(params);
>>>>>>> hezhouxiyiyangde

		model.put("serviceList", serviceList);
		model.put("systemList", systemList);
		model.put("departmentList", departmentList);
		model.put("companyList", companyList);
		return "autotest/service/service_list";
	}


	@RequestMapping(value="/saveService", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult saveService(TestServiceDto testServiceDto) {
		CommonResult result = new CommonResult();
		try{
			if(StringUtils.isNotBlank(testServiceDto.getName()) && !"null".equals(testServiceDto.getName())){
				testServiceService.save(testServiceDto);
			} else {
				int code = CommonResultEnum.ERROR.getReturnCode();
				String message ="name不能为空！";
				result.setCode(code);
				result.setMessage(message);
			}
		}catch (Exception e){
			int code = CommonResultEnum.ERROR.getReturnCode();
			String message =e.getMessage();
			result.setCode(code);
			result.setMessage(message);
			logger.error("保存公司异常｛｝",e);
		}
		return  result;
	}

	@RequestMapping(value="/deleteService", method = RequestMethod.POST)
	@ResponseBody
	public CommonResult deleteService(HttpServletRequest request) {
		CommonResult result = new CommonResult();
		String id = request.getParameter("id");
		try{
			if(StringUtils.isNotBlank(id)){
				testServiceService.deleteByPK(Long.parseLong(id));
			}
		}catch (Exception e){
			int code = CommonResultEnum.ERROR.getReturnCode();
			String message = e.getMessage();
			result.setCode(code);
			result.setMessage(message);
			logger.error("删除操作异常｛｝",e);
		}
		return  result;
	}


}
