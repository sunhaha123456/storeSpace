/*
package com.store.controler;

import com.store.common.data.response.ResponseString;
import com.store.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MarkerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/hello/")
public class DemoControler {


	@ResponseBody
	@RequestMapping(value = "a")
	public Boolean a() {
		log.error("你好啊");
		log.error(MarkerFactory.getMarker("USER_MARKER"), "我很好啊");
		return true;
	}

	@ResponseBody
	@RequestMapping(value = "b")
	public int b() {
		return 1;
	}

	@ResponseBody
	@RequestMapping(value = "c")
	public Integer c() throws BusinessException {
		throw new BusinessException("未登录，请先登录！");
//		return 1;
	}

	@ResponseBody
	@RequestMapping(value = "d")
	public void d() {

	}

	@ResponseBody
//	@RequestMapping(value = "e")
    @GetMapping(value = "e")
//	@PostMapping(value = "e")
	public List<String> e() {
		List<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		list.add("3");
		return list;
	}

	@ResponseBody
	@RequestMapping(value = "f")
	public ResponseString f() {
		return new ResponseString("成功了");
	}

	@RequestMapping(value = "g")
	public String g() {
		return "index";
	}
}
*/