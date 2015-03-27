This tool allows you to easily wrap PHP or other active web pages into native Java methods and call them from your application. Let's say we have a PHP script named callMe.php on our server http://some-server.com/somePath that takes parameter "name" through http POST. As the result it outputs some text based on the given parameter. To call this PHP script from Java the only thing we would need to do is to write an interface that describes this script:

```
interface MyScript() {
  InputStream callMe(@ParamName("name") String name);
}
```

and call `ProxyFactory.createProxy` method that will generate an instance of `MyScript` that we can use to call our PHP script directly from the code:

```
  ProxyFactory factory = new ProxyFactory(new PhpFlatProxy());
  MyScript script = factory.createProxy(MyScript.class, "http://some-server.com/somePath");
  InputStream stream = script.callMe("bancika");
```

It is also possible to automatically deserialize script output in JSON format. To do that, you just need to specify output type in your proxy interface, as illustrated in the following example:

```
interface MyScript() {
  SomeClass callMe(@ParamName("name") String name);
}
```

All methods that do not have `InputStream` as the result type will be automatically deserialized when executed.

Currently, only PHP implementation is available but it is easy to expand it to work with similar technologies.