package com.diyfever.webproxy;

import java.io.InputStream;

import com.diyfever.httpproxy.ParamName;

public interface ITestProxy {

	String test(@ParamName("name") String name);

	InputStream testStream(@ParamName("name") String name);
}
