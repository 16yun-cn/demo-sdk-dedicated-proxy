## 16YUN(亿牛云) Node.js Demo（private 独享代理）

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录开始，按顺序执行安装和运行步骤；使用总仓库 `demo-sdk` 时，先执行 `cd dedicated-proxy`。

环境变量：
- `PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`
- 可选：`TARGET_URL`（默认 `http://httpbin.org/ip`）

运行：
```
cd nodejs/basic
npm i
npm start
```

说明：脚本会先调用管理接口 `current-ip`、`switch-ip`、`update`，再通过独享代理访问目标。
