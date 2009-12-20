package com.diyfever.webproxy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.diyfever.httpproxy.PhpFlatProxy;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class PhpProxyTest {

	static final String URL = "http://diyfever.x10hosting.com";

	PhpFlatProxy proxy;

	@Before
	public void setUp() throws Exception {
		proxy = new PhpFlatProxy();
	}

	@Test
	public void testInvoke() throws IOException {
		String name = "bancika";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		InputStream stream = proxy.invoke(URL, "test", params);
		byte[] buff = new byte[256];
		int length = stream.read(buff);
		String result = new String(buff, 0, length);

		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		String expected = xstream.toXML(name);

		assertEquals(expected, result);
	}

	@Test
	public void testInvokeAndDeserialize() {
		String name = "bancika";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		Object result = proxy.invokeAndDeserialize(URL, "test", params);

		assertEquals(name, result);
	}
}
