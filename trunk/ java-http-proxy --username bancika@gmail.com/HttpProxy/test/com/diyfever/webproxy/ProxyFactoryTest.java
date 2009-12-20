package com.diyfever.webproxy;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Test;

import com.diyfever.httpproxy.ProxyFactory;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

public class ProxyFactoryTest {

	private ProxyFactory factory;

	@Before
	public void setUp() throws Exception {
		BasicConfigurator.configure();
		factory = new ProxyFactory(new MockFlatProxy());
	}

	@Test
	public void testCreateProxyString() {
		ITestProxy proxy = factory.createProxy(ITestProxy.class,
				"doesn't matter");
		String name = "bancika";
		assertEquals(name, proxy.test(name));
	}

	@Test
	public void testCreateProxyStream() throws IOException {
		ITestProxy proxy = factory.createProxy(ITestProxy.class,
				"doesn't matter");
		String name = "bancika";
		InputStream stream = proxy.testStream(name);
		byte[] buff = new byte[256];
		int length = stream.read(buff);
		String result = new String(buff, 0, length);

		XStream xstream = new XStream(new JettisonMappedXmlDriver());
		xstream.setMode(XStream.NO_REFERENCES);
		String expected = xstream.toXML(name);

		assertEquals(expected, result);
	}
}
