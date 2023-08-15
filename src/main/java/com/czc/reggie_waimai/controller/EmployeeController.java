package com.czc.reggie_waimai.controller;

/*
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Employee;
import com.czc.reggie_waimai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
*/

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czc.reggie_waimai.common.R;
import com.czc.reggie_waimai.entity.Employee;
import com.czc.reggie_waimai.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
/*`@RestController` 是 Spring Framework 中的一个注解，它可以用于标记一个类
表明该类是一个 RESTful 风格的控制器。RESTful 风格的控制器是用于接收 HTTP 请求并返回 HTTP 响应的组件，通常用于构建 Web 应用程序的后端服务。
使用 `@RestController` 注解可以让 Spring 自动将控制器中的方法返回值序列化为 JSON 或 XML 格式的数据，并放入 HTTP 响应中返回给客户端。
这样，我们就可以方便地构建 RESTful 风格的 Web 服务，为客户端提供数据接口。
下面是一个使用 `@RestController` 注解的示例：
@RestController
@RequestMapping("/api")
public class MyController {
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello, world!";
    }
}
这个示例中，我们使用 `@RestController` 注解标记了一个名为 `MyController` 的类，
它包含了一个 `@GetMapping` 注解的方法 `sayHello()`，用于处理 HTTP GET 请求。
`@GetMapping` 注解中的参数 `/hello` 表示该方法处理的请求路径是 `/api/hello`。当客户端访问 `/api/hello` 路径时，
该方法会返回一个字符串 `"Hello, world!"`，Spring 会自动将该字符串序列化为 JSON 格式的数据，并放入 HTTP 响应中返回给客户端。*/
@RestController
/*`@RequestMapping`和`@PostMapping`是Spring MVC框架中的注解，用于处理HTTP请求。它们之间的区别在于：
- `@RequestMapping`是一个通用的注解，可以用于处理多种HTTP请求（GET、POST、PUT、DELETE等），而`@PostMapping`仅用于处理HTTP POST请求。
- `@RequestMapping`中可以指定请求的URL路径、请求方法、请求头等信息，而`@PostMapping`仅用于指定请求的URL路径。
- `@RequestMapping`可以用于类级别和方法级别，而`@PostMapping`只能用于方法级别。
举个例子，假设我们要处理一个HTTP POST请求，请求路径为`/user/create`，那么可以使用`@RequestMapping`或`@PostMapping`来处理该请求：
// 使用 @RequestMapping 处理 POST 请求
@RequestMapping(value = "/user/create", method = RequestMethod.POST)
public String createUser(@RequestBody User user) {
    // 处理请求
}
// 使用 @PostMapping 处理 POST 请求
@PostMapping("/user/create")
public String createUser(@RequestBody User user) {
    // 处理请求
}
在这个例子中，两个方法的功能是相同的，但是使用了不同的注解来处理HTTP POST请求。*/
@RequestMapping("/employee")
public class EmployeeController {


    @Autowired
    private EmployeeService employeeService;

/*具体来说，@RequestBody 注解可以用于将 HTTP 请求体中的 JSON、XML 或其他格式的数据绑定到 Java 对象上。
例如，如果 HTTP 请求体是一个 JSON 对象，@RequestBody 注解可以将该 JSON 对象转换为 Java 对象。*/

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
/*HttpServletRequest是Java Servlet API提供的一个接口，用于封装HTTP请求的内容。
它提供了许多方法来获取HTTP请求的各种信息，如请求参数、请求头、请求体等。通过HttpServletRequest对象，Servlet可以获得客户端发送的请求信息，并作出相应的响应。
HttpServletRequest接口提供了以下常用方法：
1. getParameter(String name)：获取请求参数的值，name为参数名。
2. getParameterNames()：获取所有请求参数的名称。
3. getHeader(String name)：获取指定请求头的值，name为请求头名称。
4. getHeaderNames()：获取所有请求头的名称。
5. getMethod()：获取请求方法，如GET、POST等。
6. getInputStream()：获取请求体的输入流。
7. getRequestURL()：获取请求的URL。
8. getSession()：获取与请求关联的会话对象。
除了以上方法，HttpServletRequest还提供了许多其他方法，如获取请求的协议、主机名、端口号、字符编码等。通过这些方法，Servlet可以获取到请求的各种信息，从而作出相应的处理和响应。*/
        //对页面的密码进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //根据用户名查数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
/*LambdaQueryWrapper是MyBatis-Plus提供的一个查询构造器，它基于Lambda表达式来构建查询条件。
使用LambdaQueryWrapper可以方便地构建复杂的查询条件，同时也可以避免SQL注入等安全问题。
LambdaQueryWrapper可以通过Lambda表达式来构建查询条件，Lambda表达式可以使用Java 8中引入的函数式接口来实现。
例如，可以使用Lambda表达式来实现查询某个字段等于某个值的条件：
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getName, "张三");
List<User> userList = userMapper.selectList(wrapper);

上面的代码中，LambdaQueryWrapper的eq方法接受一个Lambda表达式作为参数，
该Lambda表达式表示查询User实体中的name字段等于"张三"的记录。最后，调用selectList方法执行查询并返回结果。*/
        //判断查询结果
        if (emp == null) {
            return R.error("登录失败");

        }

        //密码比对
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }
        //查看员工状态
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }
        //登录成功，把id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    //员工退出
    @PostMapping("/logout")
    public R<String> loginout(HttpServletRequest request) {
        //清楚session中保存的员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");


    }

    //新增员工
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {

        log.info("新增员工，员工信息：{}", employee.toString());

        //设置密码，进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
/*`DigestUtils.md5DigestAsHex()` 是一个Java方法，它可以将一个字节数组的MD5摘要转换为一个十六进制字符串。
MD5摘要是一种常用的哈希算法，它可以将任意长度的数据转换为一个固定长度的摘要，通常用于数据完整性校验和密码存储等场景。

以下是一个示例代码，展示如何使用`DigestUtils.md5DigestAsHex()`方法将一个字符串转换为MD5摘要的十六进制字符串：
import org.apache.commons.codec.digest.DigestUtils;
public class Example {
    public static void main(String[] args) {
        String str = "Hello, world!";
        byte[] md5Digest = DigestUtils.md5(str);
        String md5Hex = DigestUtils.md5DigestAsHex(md5Digest);
        System.out.println(md5Hex); // 输出：65a8e27d8879283831b664bd8b7f0ad4
    }
}
在这个示例中，我们首先将字符串`"Hello, world!"`转换为一个MD5摘要，
然后使用`DigestUtils.md5DigestAsHex()`方法将摘要转换为一个十六进制字符串。
最终输出的字符串为`65a8e27d8879283831b664bd8b7f0ad4`，这就是`"Hello, world!"`的MD5摘要的十六进制表示。*/


//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());



        //获取当前登录的用户id
//        Long empId = (Long) request.getSession().getAttribute("employee");
 /*`request.getSession().getAttribute()` 是 Java Servlet API 中 `HttpServletRequest` 接口的一个方法，用于从会话中检索对象。
在 Web 应用程序中，会话是一种跨多个客户端请求维护状态信息的方式。当客户端向服务器发出请求时，服务器创建一个会话对象，可用于存储客户端与应用程序的交互信息。
`getAttribute()` 方法用于检索先前存储在会话中的对象。该方法接受一个参数，即要检索的属性名称。如果会话中未找到属性，则方法返回 `null`。
以下是使用 `request.getSession().getAttribute()` 从会话中检索对象的示例：
```java
// 从请求中获取会话对象
HttpSession session = request.getSession();
// 使用属性名称 "myAttribute" 从会话中检索对象
Object myObject = session.getAttribute("myAttribute");
```*/


//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);


        employeeService.save(employee);

        return R.success("新增员工成功");
    }
    /*好的，举个例子来说明一下@PostMapping和@GetMapping的区别。
假设我们正在开发一个在线商城应用程序，其中有一个订单管理功能。我们需要提供以下两个API：
1. 获取所有订单的列表
2. 创建一个新订单
对于第一个API，我们可以使用@GetMapping注解来处理HTTP GET请求。代码示例如下：
@GetMapping("/orders")
public List<Order> getAllOrders() {
    // 从数据库中获取所有订单的列表
    List<Order> orders = orderService.getAllOrders();
    return orders;
}
对于第二个API，我们可以使用@PostMapping注解来处理HTTP POST请求。代码示例如下：
@PostMapping("/orders")
public ResponseEntity<?> createOrder(@RequestBody Order order) {
    // 将新订单保存到数据库中
    orderService.createOrder(order);

    // 返回HTTP 201 Created状态码
    return ResponseEntity.status(HttpStatus.CREATED).build();
}
在这个例子中，我们使用@GetMapping注解来处理HTTP GET请求，以获取所有订单的列表。而对于创建新订单的API，我们使用@PostMapping注解来处理HTTP POST请求，以向服务器提交新订单数据。*/

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {

        log.info("page={},pageSize={},name={}", page, pageSize, name);
        // //构造分页构造器
        Page pageInfo=new Page(page,pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
    /*StringUtils.isNotEmpty(name)是一个Apache Commons Lang库中的方法调用，用于判断字符串name是否不为空或null。
具体来说，该方法会检查传入的字符串是否为null，如果为null则返回false，否则会去掉字符串两端的空格，然后再判断去掉空格后的字符串是否为空。如果去掉空格后的字符串不为空，则返回true，否则返回false。
例如，如果name为null或空字符串或只包含空格的字符串，则StringUtils.isNotEmpty(name)会返回false，否则返回true。
这个方法在Java编程中经常用于判断字符串是否为空，避免在对空字符串进行操作时出现NullPointerException异常。*/
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
//执行查询
    employeeService.page(pageInfo,queryWrapper);

    return  R.success(pageInfo);

    }
    //根据id修改员工信息
    @PutMapping
    /*@PutMapping是Spring MVC框架中的一个注解，它用于将HTTP PUT请求映射到特定的处理程序方法上。
    PUT请求通常用于更新资源，因此@PutMapping注解通常用于处理更新操作。
在使用@PutMapping注解时，需要指定请求的URL路径。例如，如果我们要将PUT请求映射到路径“/employees/{id}”，可以这样写：
@PutMapping("/employees/{id}")
public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
    // 处理更新操作
}
在这个例子中，我们将PUT请求映射到路径“/employees/{id}”，其中{id}是一个占位符，表示要更新的员工的ID。
@PathVariable注解用于将占位符{id}映射到方法参数id上，@RequestBody注解用于将请求体中的JSON或XML数据映射到Employee对象上。
当Spring MVC接收到一个PUT请求时，它会根据请求的URL路径和请求体中的数据，调用updateEmployee方法来处理请求，并返回一个包含更新后的Employee对象的ResponseEntity对象。*/
    public R<String> update(HttpServletRequest request ,@RequestBody Employee employee){
        log.info(employee.toString());

//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(empId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }

    //编辑员工信息

    @GetMapping("/{id}")
    public R<Employee>geteById(@PathVariable Long id){
        log.info("根据id查询寻员工信息");

        Employee employee=employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }

       return R.error("没有查询到员工信息");

    }
    /*在 Spring MVC 中，`@DeleteMapping` 是一个注解，用于处理 HTTP DELETE 请求。当发送 DELETE 请求到指定的 URL 时，被 `@DeleteMapping` 注解的方法会被执行。方法的响应将返回给客户端。

以下是在 Spring MVC 控制器中使用 `@DeleteMapping` 的示例：

```java
@RestController
@RequestMapping("/users")
public class UserController {

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // code to delete user with the specified id
        return ResponseEntity.noContent().build();
    }
}
```

在这个例子中，`@DeleteMapping` 注解被用来处理 DELETE 请求到 `/users/{id}` 端点。方法 `deleteUser` 通过 `@PathVariable` 注解的 `Long` 参数获取需要删除的用户的 `id`。方法返回一个带有 `noContent` 状态码的 `ResponseEntity`，表示用户已经被成功删除。*/
   @DeleteMapping
    public R<String>delete(Long id){

        employeeService.removeById(id);
        return R.success("删除用户成功");
   }

}




















