## 16YUN(亿牛云) 独享代理使用说明

本说明文档对独享代理的管理与接入进行说明，延续本仓库 API_README 风格与结构，提供常见操作的命令与代码示例，便于快速上手与排障。

---

### 一、产品概述

- 独享代理提供固定独占出口与独立端口，HTTP/HTTPS 可用。
- 提供简化管理接口：查询当前出口、切换出口 IP、重启服务实例。
- 典型场景：账号敏感场景、固定 IP 白名单接入、稳定长连业务。

---

### 二、准备与开通

1) 在控制台开通独享代理，记下“管理接口地址”“代理主机与端口”“用户名/密码”。
2) 如需目标站白名单，将独享出口 IP 加入目标站白名单。
3) 建议在本地以环境变量形式保存凭据，避免硬编码。

示例占位：
- 管理接口基址：`http://s1.ip.16yun.cn:887`
- 代理地址：`s1.ip.16yun.cn:39010`

---

### 三、管理接口

| 功能 | 路径 | 频率限制（示例） |
| --- | --- | --- |
| 获取当前 IP | `/simple/current-ip?username=<user>&password=<pass>` | 1200 次/小时 |
| 切换 IP | `/simple/switch-ip?username=<user>&password=<pass>` | 180 次/小时，间隔 > 20s |
| 重启服务 | `/simple/update?username=<user>&password=<pass>` | 10 次/小时，间隔 > 360s |

说明：具体频控以开通信息为准；超过频率会返回失败或 429。

响应示例：
- 获取当前 IP
```json
[{"ip": "180.102.181.64", "ttl": 262.87}]
```
- 切换 IP
```json
[{"last_ip": "180.102.181.64", "result": true}]
```
- 重启服务
```json
[["user1-16yun12312-39507", "", "created"]]
```

---

### 四、快速示例

curl（将 `<user>/<pass>` 替换为真实值）：
```bash
BASE="http://s1.ip.16yun.cn:887"
curl "$BASE/simple/current-ip?username=<user>&password=<pass>"
curl "$BASE/simple/switch-ip?username=<user>&password=<pass>"
curl "$BASE/simple/update?username=<user>&password=<pass>"
```

Python（通过独享代理访问检测地址）：
```python
import requests

proxy = "http://username:password@s1.ip.16yun.cn:39010"
proxies = {"http": proxy, "https": proxy}
resp = requests.get("https://httpbin.org/ip", proxies=proxies, timeout=15)
print(resp.text)
```

Go：
```go
proxyURL, _ := url.Parse("http://username:password@s1.ip.16yun.cn:39010")
client := http.Client{Transport: &http.Transport{Proxy: http.ProxyURL(proxyURL)}}
resp, err := client.Get("https://httpbin.org/ip")
```

---

### 五、使用建议

- 控制调用频率：切换/重启接口受限频，务必加入最小间隔与失败退避。
- 会话与长连：独享代理适合长连接；必要时保持 Keep-Alive，避免频繁重建连接。
- 目标站策略：根据站点要求设置 `User-Agent`、`Referer`、`Cookie` 等，遵守使用规范。
- 凭据安全：不要在代码库中提交真实 `username/password` 与端口，使用环境变量或本地配置。

---

### 六、常见问题与排障

- 401/407：代理认证失败 → 检查用户名/密码；优先使用库内置代理认证接口。
- 429：频率超限 → 降低调用频率或延长间隔，实施指数退避。
- 5xx/超时：网络或目标站异常 → 增加超时、重试与日志；确认本机与目标站网络可达。
- 切换后仍旧出口不变：等待 TTL 到期或连接池释放，再次发起新连接验证。
