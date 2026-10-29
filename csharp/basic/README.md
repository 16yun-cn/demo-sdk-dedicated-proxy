## 16YUN(亿牛云) C# Demo（private 独享代理, .NET 6）

以下命令从独立仓库 `demo-sdk-dedicated-proxy` 根目录开始，按顺序执行安装和运行步骤；使用总仓库 `demo-sdk` 时，先执行 `cd dedicated-proxy`。

运行：
```
cd csharp/basic
export PRIVATE_HOST=s1.ip.16yun.cn
export PRIVATE_PORT=39010
export PRIVATE_USERNAME=username
export PRIVATE_PASSWORD=password
dotnet run
```

说明：程序会先调用管理接口（`current-ip`/`switch-ip`/`update`），再通过独享代理访问 `TARGET_URL`。
