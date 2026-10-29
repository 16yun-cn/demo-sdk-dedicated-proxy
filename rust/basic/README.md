## 16YUN(亿牛云) Rust Demo（private 独享代理, reqwest）

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录开始，按顺序执行安装和运行步骤；使用总仓库 `demo-sdk` 时，先执行 `cd dedicated-proxy`。

运行：
```
cd rust/basic
cargo run --release
```

环境变量：`PRIVATE_HOST`、`PRIVATE_PORT`、`PRIVATE_USERNAME`、`PRIVATE_PASSWORD`（主机和端口按订单填写；代码默认主机为 `s1.ip.16yun.cn`、端口为 `39010`）。

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
