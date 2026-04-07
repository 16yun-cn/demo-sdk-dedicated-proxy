#!/usr/bin/env python3
import os
import requests

host = os.getenv("PRIVATE_HOST", "s1.ip.16yun.cn")
port = os.getenv("PRIVATE_PORT", "39010")
# Canonical with alias fallback
user = os.getenv("PRIVATE_USERNAME") or os.getenv("PRIVATE_USER")
pwd = os.getenv("PRIVATE_PASSWORD") or os.getenv("PRIVATE_PASS")
target = os.getenv("TARGET_URL", "https://httpbin.org/ip")
base = os.getenv("PRIVATE_BASE", "http://s1.ip.16yun.cn:887").rstrip("/")

if not user or not pwd:
    raise SystemExit("缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD")

# 管理接口演示
for path in ("current-ip", "switch-ip", "update"):
    url = f"{base}/simple/{path}?username={user}&password={pwd}"
    try:
        resp = requests.get(url, timeout=15)
        print(path, resp.status_code, resp.text)
    except Exception as e:
        print(path, "error:", e)

# 通过独享代理访问目标
proxy = f"http://{user}:{pwd}@{host}:{port}"
r = requests.get(target, proxies={"http": proxy, "https": proxy}, timeout=15)
print(r.status_code)
print(r.text)
