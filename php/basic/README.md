## 16YUN(亿牛云) PHP Demo（private 独享代理, Guzzle）

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录开始，按顺序执行安装和运行步骤；使用总仓库 `demo-sdk` 时，先执行 `cd dedicated-proxy`。

安装：
```
cd php/basic
composer install
```

运行：
```
export PRIVATE_HOST=s1.ip.16yun.cn
export PRIVATE_PORT=39010
export PRIVATE_USERNAME=username
export PRIVATE_PASSWORD=password
php main.php
```

说明：脚本会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
