## 独享代理示例（Python requests）

环境变量：
- `PRIVATE_HOST`（默认 `s1.ip.16yun.cn`）
- `PRIVATE_PORT`（默认 `39010`）
- `PRIVATE_USER`、`PRIVATE_PASS`
- 可选：`TARGET_URL`（默认 `https://httpbin.org/ip`）

运行：
```
pip install -r requirements.txt
python main.py
```

说明：脚本内置管理接口演示，会依次调用 `current-ip`、`switch-ip`、`update`，随后通过独享代理访问 `TARGET_URL`。
