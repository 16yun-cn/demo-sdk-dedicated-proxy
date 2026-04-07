## 16YUN(亿牛云) Java Demo（private 独享代理, OkHttp）

运行：
```
export PRIVATE_HOST=s1.ip.16yun.cn
export PRIVATE_PORT=39010
export PRIVATE_USERNAME=username
export PRIVATE_PASSWORD=password
mvn -q -DskipTests package
java -jar target/java-demo-private-0.1.0-jar-with-dependencies.jar
```

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
