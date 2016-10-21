## Overview

<p align="justify">This tool allows you to easily wrap PHP or other active web pages into native Java methods and call them from your application. Let's say we have a PHP script named callMe.php on our server http://some-server.com/somePath that takes parameter "name" through http POST. As the result it outputs some text based on the given parameter. To call this PHP script from Java the only thing we would need to do is to write an interface that describes this script:</p>

```javascript
interface MyScript() {
  InputStream callMe(@ParamName("name") String name);
}
```

and call ProxyFactory.createProxy method that will generate an instance of MyScript that we can use to call our PHP script directly from the code:

```javascript
ProxyFactory factory = new ProxyFactory(new PhpFlatProxy());
MyScript script = factory.createProxy(MyScript.class, "http://some-server.com/somePath");
InputStream stream = script.callMe("bancika");
```

<p align="justify">It is also possible to automatically deserialize script output in JSON format. To do that, you just need to specify output type in your proxy interface, as illustrated in the following example:</p>

```javascript
interface MyScript() {
  SomeClass callMe(@ParamName("name") String name);
}
```

<p align="justify">All methods that do not have InputStream as the result type will be automatically deserialized when executed.</p>

## Details

<p align="justify">In the example below we have a script named createPerson.php that takes two parameters - first name and last name and outputs Person object. PHP code looks something like this:</p>

```php
<?php echo "{\"Person\":{\"first\":\"".$_POST["first"]."\",\"last\":\"".$_POST["last"]."\"}}" ?>
```

<p align="justify">On Java side we have created Person class with the following content (entity bean methods omitted for clarity):</p>

```javascript
class Person { String first; String last; }
```

<p align="justify">Note that PHP output structure matches our class structure. This will allow us to deserialize server output into objects of type Person.</p>

<p align="justify">Now lets create an interface to our script.</p>

```javascript
interface MyScript() { Person createPerson(@ParamName("first") String first, @ParamName("last") String last); }
```

<p align="justify">Note that method name matches with the script name and parameter annotations match script parameters read through POST. This is necessary because we are generating HTTP requests based on this interface. Now we are ready to create a proxy.</p>

```javascript
ProxyFactory factory = new ProxyFactory(new PhpFlatProxy()); 
MyScript script = factory.createProxy(MyScript.class, "http://some-server.com/somePath");
```

<p align="justify">This will create an instance of MyScript interface and use it to invoke PHP scripts located under http://some-server.com/somePath. For our example, calls to MyScript.createPerson will be redirected to http://some-server.com/somePath/createPerson.php and method parameter values will be passed through POST. Lets try to call our server through proxy:</p>

```javascript
Person me = script.createPerson("Bane", "Stojkovic");
```

This call will post the two parameters to our createPerson.php script, read the result and deserialize it to Person object.
