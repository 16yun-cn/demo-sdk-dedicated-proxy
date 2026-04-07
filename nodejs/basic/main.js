// 16YUN(亿牛云) Node.js 私有独享代理 Demo
const axios = require('axios');
const { HttpProxyAgent } = require('http-proxy-agent');
const { HttpsProxyAgent } = require('https-proxy-agent');

const host = process.env.PRIVATE_HOST || 's1.ip.16yun.cn';
const port = process.env.PRIVATE_PORT || '39010';
// Canonical with alias fallback
const user = process.env.PRIVATE_USERNAME || process.env.PRIVATE_USER;
const pass = process.env.PRIVATE_PASSWORD || process.env.PRIVATE_PASS;
const target = process.env.TARGET_URL || 'http://httpbin.org/ip';
const base = (process.env.PRIVATE_BASE || 'http://s1.ip.16yun.cn:887').replace(/\/$/, '');

if (!user || !pass) {
  console.error('缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD');
  process.exit(2);
}

const proxyUrl = `http://${user}:${pass}@${host}:${port}`;
const httpAgent = new HttpProxyAgent(proxyUrl);
const httpsAgent = new HttpsProxyAgent(proxyUrl);

// 管理接口演示
(async () => {
  for (const p of ['current-ip', 'switch-ip', 'update']) {
    const url = `${base}/simple/${p}?username=${encodeURIComponent(user)}&password=${encodeURIComponent(pass)}`;
    try {
      const r = await axios.get(url, { timeout: 15000 });
      console.log(p, r.status, typeof r.data === 'string' ? r.data : JSON.stringify(r.data));
    } catch (e) {
      console.error(p, e.message);
    }
  }
  // 通过独享代理访问目标
  try {
    const proxyConfig = {
      protocol: 'http',
      host: host,
      port: parseInt(port),
      auth: {
        username: user,
        password: pass
      }
    };

    // Axios proxy config
    const r = await axios.get(target, {
      proxy: proxyConfig,
      timeout: 15000
    });
    console.log(r.status, r.data);
  } catch (e) {
    console.error('proxy', e.message);
    process.exit(1);
  }
})();
