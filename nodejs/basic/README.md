## 16YUN(亿牛云) Node.js Demo（private 独享代理）

环境变量：
- `PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`
- 可选：`TARGET_URL`（默认 `https://httpbin.org/ip`）

运行：
```
npm i
npm start
```

说明：脚本会先调用管理接口 `current-ip`、`switch-ip`、`update`，再通过独享代理访问目标。
