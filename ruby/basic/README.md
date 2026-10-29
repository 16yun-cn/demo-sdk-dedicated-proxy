## 16YUN(亿牛云) Ruby Demo（private 独享代理, HTTParty）

安装：
```
bundle install
```

运行：
```
export PRIVATE_HOST=s1.ip.16yun.cn
export PRIVATE_PORT=39010
export PRIVATE_USERNAME=username
export PRIVATE_PASSWORD=password
ruby main.rb
```

说明：脚本会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
