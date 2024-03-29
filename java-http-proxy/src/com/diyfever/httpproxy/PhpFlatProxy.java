package com.diyfever.httpproxy;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.security.AnyTypePermission;

/**
 * {@link IFlatProxy} implementation for PHP. Method names are interpreted as PHP script names,
 * excluding the ".php" extension. Example below shows how native Java call is translated into PHP
 * call:<br>
 * <br>
 * <code>invoke("yahoo.com", "search", params)</code> <br>
 * <br>
 * This call will query the server using PHP script <code>yahoo.com/search.php</code> and will pass
 * the parameters using POST method.
 * 
 * @author Branislav Stojkovic
 */
public class PhpFlatProxy implements IFlatProxy {

  private static final Logger LOG = Logger.getLogger(PhpFlatProxy.class);

  @Override
  public InputStream invoke(String url, String methodName, Map<String, Object> params) {
    InputStream serverInput;
    try {
      // Flatten params map into an array
      List<Object> paramList = new ArrayList<Object>();

      for (Map.Entry<String, Object> entry : params.entrySet()) {
        if (entry.getValue() != null) {
          paramList.add(entry.getKey());
          paramList.add(entry.getValue());
        }
      }

      // Call the server.
      URL phpUrl = new java.net.URL(createPhpFileName(url, methodName));
      LOG.debug("Connecting to: " + phpUrl);
      serverInput = ClientHttpRequest.post(phpUrl, paramList.toArray());
      return serverInput;
    } catch (MalformedURLException e) {
      LOG.error(e);
    } catch (IOException e) {
      LOG.error(e);
    }
    return null;
  }

  @Override
  public Object invokeAndDeserialize(String url, String methodName, Map<String, Object> params) {
    InputStream stream = invoke(url, methodName, params);
    if (stream == null) {
      return null;
    }
    // Deserialize the stream
    XStream xstream = new XStream(new JettisonMappedXmlDriver());
    xstream.addPermission(AnyTypePermission.ANY);
    xstream.setMode(XStream.NO_REFERENCES);
    return xstream.fromXML(stream);
  }

  private String createPhpFileName(String url, String methodName) {
    if (url.endsWith("/")) {
      return url + methodName + ".php";
    } else {
      return url + "/" + methodName + ".php";
    }
  }
}
