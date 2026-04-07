using System;
using System.Net;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;

class Program {
  static async Task Main() {
    string host = Environment.GetEnvironmentVariable("PRIVATE_HOST") ?? "s1.ip.16yun.cn";
    string port = Environment.GetEnvironmentVariable("PRIVATE_PORT") ?? "39010";
    string? user = Environment.GetEnvironmentVariable("PRIVATE_USERNAME") ?? Environment.GetEnvironmentVariable("PRIVATE_USER");
    string? pass = Environment.GetEnvironmentVariable("PRIVATE_PASSWORD") ?? Environment.GetEnvironmentVariable("PRIVATE_PASS");
    string target = Environment.GetEnvironmentVariable("TARGET_URL") ?? "https://httpbin.org/ip";
    string baseUrl = (Environment.GetEnvironmentVariable("PRIVATE_BASE") ?? "http://s1.ip.16yun.cn:887").TrimEnd('/');
    if (string.IsNullOrWhiteSpace(user) || string.IsNullOrWhiteSpace(pass)) throw new ArgumentException("缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD");

    // 管理接口演示（不走代理）
    using (var plain = new HttpClient() { Timeout = TimeSpan.FromSeconds(15) }) {
      foreach (var p in new []{"current-ip","switch-ip","update"}) {
        var url = $"{baseUrl}/simple/{p}?username={user}&password={pass}";
        try {
          var resp0 = await plain.GetAsync(url);
          Console.WriteLine($"{p} {(int)resp0.StatusCode}");
          Console.WriteLine(await resp0.Content.ReadAsStringAsync());
        } catch (Exception ex) {
          Console.Error.WriteLine($"{p} error: {ex.Message}");
        }
      }
    }

    var encodedUser = Uri.EscapeDataString(user);
    var encodedPass = Uri.EscapeDataString(pass);
    var credentials = new NetworkCredential(user, pass);
    var proxyUri = new Uri($"http://{host}:{port}");
    var credentialCache = new CredentialCache();
    credentialCache.Add(proxyUri, "Basic", credentials);
    var proxy = new WebProxy(new Uri($"http://{encodedUser}:{encodedPass}@{host}:{port}")) {
      Credentials = credentialCache
    };
    var handler = new HttpClientHandler {
      Proxy = proxy,
      UseProxy = true,
      PreAuthenticate = true,
      DefaultProxyCredentials = credentialCache
    };
    using var client = new HttpClient(handler) { Timeout = TimeSpan.FromSeconds(15) };
    var request = new HttpRequestMessage(HttpMethod.Get, target);
    if (target.StartsWith("http://", StringComparison.OrdinalIgnoreCase)) {
      var token = Convert.ToBase64String(Encoding.ASCII.GetBytes($"{user}:{pass}"));
      request.Headers.TryAddWithoutValidation("Proxy-Authorization", $"Basic {token}");
    }
    var resp = await client.SendAsync(request);
    Console.WriteLine((int)resp.StatusCode);
    Console.WriteLine(await resp.Content.ReadAsStringAsync());
  }
}
