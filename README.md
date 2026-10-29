# 亿牛云独享代理使用说明

独享代理为单个业务提供独立出口 IP，适合需要稳定出口和自主控制切换时机的任务。代理连接使用域名、端口、用户名和密码，无需配置代理服务的 IP 白名单；目标站自己的白名单要求仍需按业务配置。

## 官方帮助

[帮助首页](https://www.16yun.cn/help/) · [首次接入](https://www.16yun.cn/help/quickstart/dedicated-access/) · [开发手册](https://www.16yun.cn/help/dev-manual/dedicated-proxy-guide/) · [开发 FAQ](https://www.16yun.cn/help/dev-manual/dedicated-proxy-faq/) · [代码示例与 SDK](https://www.16yun.cn/help/code-sdk/dedicated-proxy/) · [错误码说明](https://www.16yun.cn/help/dev-manual/errcode/)

接口概念另见[新版接口说明](https://www.16yun.cn/help/product-intro/dedicated-interface-manual/)。**本仓库暂时保留现有 `/simple/*` 管理接口及查询参数认证。新版帮助站介绍的 `/ip`、`/switch-ip`、`/ip-status` 和 Basic 认证是另一套调用方式，不要混用。以下命令与本仓库当前实现一致。**

## 首次接入

1. 开通独享代理订单，取得代理域名、端口、用户名、密码以及现有管理接口基址。
2. 先验证代理连接，再按需要执行管理操作。代理端口和管理接口端口用途不同，均以订单接入信息为准。
3. 选择语言示例，设置对应环境变量。

首次连接信息见[独享代理接入](https://www.16yun.cn/help/quickstart/dedicated-access/)。

## 先验证代理出口

下面的占位值需替换为订单信息。访问 HTTPS 目标时，HTTP 代理 URL 仍以 `http://` 开头。

```bash
curl --proxy 'http://<代理域名>:<代理端口>' \
  --proxy-user '<用户名>:<密码>' \
  'https://httpbin.org/ip'
```

检查响应出口 IP 是否为独享出口，确认请求经过代理。

## 当前管理接口

下面沿用现有实现，不迁移接口路径、认证或返回格式。`PRIVATE_BASE` 是管理接口基址，示例默认值为 `http://s1.ip.16yun.cn:887`，实际值以接入信息为准。

| 操作 | 路径 | 作用 |
| --- | --- | --- |
| 查询当前出口 | `/simple/current-ip` | 获取当前 IP 与剩余有效时间 |
| 切换出口 | `/simple/switch-ip` | 主动切换 IP |
| 重启服务 | `/simple/update` | 重启服务实例，可能影响当前连接 |

三个接口均通过查询参数 `username`、`password` 传入凭据。下面先展示查询命令；执行切换或重启前，请确认当前任务可以中断。

```bash
export PRIVATE_BASE='http://s1.ip.16yun.cn:887'
export PRIVATE_USERNAME='<订单用户名>'
export PRIVATE_PASSWORD='<订单密码>'
curl --get "$PRIVATE_BASE/simple/current-ip" \
  --data-urlencode "username=$PRIVATE_USERNAME" \
  --data-urlencode "password=$PRIVATE_PASSWORD"
```

需要切换或重启时，在上述命令中分别将 `current-ip` 替换为 `switch-ip` 或 `update`。调用频率、最小间隔及实际返回以现有接口和订单约定为准。

## 运行语言示例

**现有语言示例包含管理操作，会查询出口、切换 IP、重启服务，然后通过代理访问目标站。运行前请确认这些操作符合当前订单的使用安排。**

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录执行；如果使用总仓库 `demo-sdk`，先执行 `cd dedicated-proxy`。需要 Python、pip 和可调用当前管理接口的订单，依赖见[Python 示例](python/basic/README.md)。

```bash
cd python/basic
pip install -r requirements.txt
export PRIVATE_HOST='<订单代理域名>'
export PRIVATE_PORT='<订单代理端口>'
export PRIVATE_BASE='http://s1.ip.16yun.cn:887'
export PRIVATE_USERNAME='<订单用户名>'
export PRIVATE_PASSWORD='<订单密码>'
export TARGET_URL='https://httpbin.org/ip'
python main.py
```

各语言支持的变量和默认值见对应示例 README。凭据只在本地配置，不提交真实账号或密码。

## 语言与示例

| 语言 | 示例 |
| --- | --- |
| [C++](cpp/README.md) | [basic](cpp/basic/README.md) |
| [C#](csharp/README.md) | [basic](csharp/basic/README.md) |
| [Go](golang/README.md) | [basic](golang/basic/README.md) |
| [Java](java/README.md) | [basic](java/basic/README.md) |
| [Kotlin](kotlin/README.md) | [basic](kotlin/basic/README.md)（暂无源码） |
| [Node.js](nodejs/README.md) | [basic](nodejs/basic/README.md) |
| [PHP](php/README.md) | [basic](php/basic/README.md) |
| [Python](python/README.md) | [basic](python/basic/README.md) |
| [Ruby](ruby/README.md) | [basic](ruby/basic/README.md) |
| [Rust](rust/README.md) | [basic](rust/basic/README.md) |

## 常见问题

| 现象 | 排查动作 |
| --- | --- |
| 代理返回 407 | 检查代理连接信息和请求库的代理认证配置 |
| 管理接口认证失败 | 检查当前 `/simple/*` 接口的基址及查询参数，不要混用新版 Basic 认证示例 |
| 管理接口限频 | 按接口约定降低频率，避免连续切换或重启 |
| 代理已配置但出口未变化 | 使用 IP 查询地址验证，检查请求是否实际经过代理 |
| 切换后仍使用原出口 | 检查接口响应和连接复用情况，使用新连接重新验证 |
| HTTPS 或网络异常 | 先检查目标能否直连，再检查代理连接、认证及超时 |

代理接入排障见[独享开发手册](https://www.16yun.cn/help/dev-manual/dedicated-proxy-guide/)、[开发 FAQ](https://www.16yun.cn/help/dev-manual/dedicated-proxy-faq/)和[错误码说明](https://www.16yun.cn/help/dev-manual/errcode/)。其中管理接口调用方式与本仓库的差异以上文说明为准。
