## 16YUN(亿牛云) Rust Demo（private 独享代理, reqwest）

运行：
```
cd rust-demo/private
cargo run --release
```

环境变量：`PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`（默认 host/port 如下）。

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
