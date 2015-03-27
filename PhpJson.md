# Introduction #

Suppose we have a PHP script on the server that does something and outputs the result in JSON format. Our goal is to write a native Java wrapper around that script that allows us to call it from Java application and get the result as Java object.

# Details #

In the example below we have a script named `createPerson.php` that takes two parameters - first name and last name and outputs Person object. PHP code looks something like this:

```
<?php
  echo "{\"Person\":{\"first\":\"".$_POST["first"]."\",\"last\":\"".$_POST["last"]."\"}}"
?>
```

On Java side we have created `Person` class with the following content (entity bean methods omitted for clarity):

```
class Person {
  String first;
  String last;
}
```

Note that PHP output structure matches our class structure. This will allow us to deserialize server output into objects of type Person.

Now lets create an interface to our script.

```
interface MyScript() {
  Person createPerson(@ParamName("first") String first, @ParamName("last") String last);
}
```

Note that method name matches with the script name and parameter annotations match script parameters read through POST. This is necessary because we are generating HTTP requests based on this interface. Now we are ready to create a proxy.

```
  ProxyFactory factory = new ProxyFactory(new PhpFlatProxy());
  MyScript script = factory.createProxy(MyScript.class, "http://some-server.com/somePath");
```

This will create an instance of `MyScript` interface and use it to invoke PHP scripts located under `http://some-server.com/somePath`. For our example, calls to `MyScript.createPerson` will be redirected to `http://some-server.com/somePath/createPerson.php` and method parameter values will be passed through POST.
Lets try to call our server through proxy:

```
  Person me = script.createPerson("Bane", "Stojkovic");
```

This call will post the two parameters to our `createPerson.php` script, read the result and deserialize it to `Person` object.