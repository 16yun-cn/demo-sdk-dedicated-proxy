## 16YUN(亿牛云) Kotlin Demo（private 独享代理, OkHttp）

环境要求：JDK 17、Maven 3。

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录执行；使用总仓库 `demo-sdk` 时，先执行 `cd dedicated-proxy`。

运行：
```bash
cd kotlin/basic
export PRIVATE_HOST='<订单代理域名>'
export PRIVATE_PORT='<订单代理端口>'
export PRIVATE_USERNAME='<订单用户名>'
export PRIVATE_PASSWORD='<订单密码>'
mvn -q -DskipTests package
java -jar target/kotlin-demo-private-0.1.0-jar-with-dependencies.jar
```

必需环境变量：
- `PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`

凭据兼容别名 `PRIVATE_USER`、`PRIVATE_PASS`。可选变量：`PRIVATE_BASE` 为现有管理接口基址（默认 `http://s1.ip.16yun.cn:887`），`TARGET_URL` 为通过代理访问的目标（默认 `https://httpbin.org/ip`）。

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。

本示例沿用 `/simple/current-ip`、`/simple/switch-ip`、`/simple/update`，通过查询参数 `username`、`password` 认证；不使用新版管理接口的路径或 Basic 认证方式。运行会切换 IP 并重启服务，请确认当前任务允许这些操作。
