## 16YUN(亿牛云) Kotlin Demo（private 独享代理, OkHttp）

运行：
```
cd kotlin-demo/private
mvn -q -DskipTests package
java -jar target/kotlin-demo-private-0.1.0-shaded.jar
```

必需环境变量：
- `PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
