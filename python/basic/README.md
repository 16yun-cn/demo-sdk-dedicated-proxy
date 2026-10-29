## 独享代理示例（Python requests）

先按[独享代理接入说明](../../README.md)配置订单信息。以下命令在本示例目录执行（独立仓库根目录下的 `python/basic`；总仓库中先进入 `dedicated-proxy`）。

本示例保留 `/simple/*` 管理接口及查询参数认证，并包含切换 IP、重启服务操作。新版帮助站的管理接口路径和认证方式不同，请勿混用。

环境变量：
- `PRIVATE_HOST`（默认 `s1.ip.16yun.cn`）
- `PRIVATE_PORT`（默认 `39010`）
- `PRIVATE_USERNAME`、`PRIVATE_PASSWORD`（兼容别名 `PRIVATE_USER`、`PRIVATE_PASS`）
- 可选：`TARGET_URL`（默认 `https://httpbin.org/ip`）

运行：
```
pip install -r requirements.txt
python main.py
```

说明：脚本内置管理接口演示，会依次调用 `current-ip`、`switch-ip`、`update`，随后通过独享代理访问 `TARGET_URL`。

可选变量：`PRIVATE_BASE` 为当前管理接口基址（默认 `http://s1.ip.16yun.cn:887`），`TARGET_URL` 为代理访问目标（默认 `https://httpbin.org/ip`）。代理域名和端口通过 `PRIVATE_HOST`、`PRIVATE_PORT` 指定；凭据使用 `PRIVATE_USERNAME`、`PRIVATE_PASSWORD`。

## 官方帮助

[帮助首页](https://www.16yun.cn/help/) · [首次接入](https://www.16yun.cn/help/quickstart/dedicated-access/) · [开发手册](https://www.16yun.cn/help/dev-manual/dedicated-proxy-guide/) · [开发 FAQ](https://www.16yun.cn/help/dev-manual/dedicated-proxy-faq/) · [代码示例与 SDK](https://www.16yun.cn/help/code-sdk/dedicated-proxy/) · [错误码说明](https://www.16yun.cn/help/dev-manual/errcode/)
