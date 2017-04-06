package com.xn.manage.autotestController;

import com.xn.manage.bean.CommonResult;
import com.xn.manage.utils.FileUpload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/*")
public class WebController {
	
	@RequestMapping(value="/decorators/{path}", method = RequestMethod.GET)
	public String getDecoratorsPage(@PathVariable String  path ) {
		return "decorators/" + path;
	}

	/**
	 * 上传图片
	 *
	 * @param files
	 * @param request
	 * @return
	 */
	@RequestMapping("/autotest/{folderName}/uploadJar")
	@ResponseBody
	public CommonResult filesUpload(@RequestParam("file") MultipartFile[] files, @PathVariable String  folderName,
									HttpServletRequest request) {
		return FileUpload.upload(files,folderName,request);
	}
}
