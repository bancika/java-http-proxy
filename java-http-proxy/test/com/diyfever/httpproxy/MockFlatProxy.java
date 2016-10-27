package com.diyfever.httpproxy;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

import com.diyfever.httpproxy.IFlatProxy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class MockFlatProxy implements IFlatProxy {

	@Override
	public InputStream invoke(String url, String methodName, Map<String, Object> params) {
		// InputStream stream = new ByteArrayInputStream(("{\"string\":\""
		// + params.get("name").toString() + "\"}").getBytes());
		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		InputStream stream = new ByteArrayInputStream(xstream.toXML(params.get("name")).getBytes());
		return stream;
	}

	@Override
	public Object invokeAndDeserialize(String url, String methodName, Map<String, Object> params) {
		return params.get("name");
	}
}
