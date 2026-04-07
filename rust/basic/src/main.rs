use anyhow::{anyhow, Context, Result};
use reqwest::Proxy;
use std::env;

fn getenv(key: &str, default: Option<&str>) -> String {
    env::var(key).ok().filter(|s| !s.is_empty()).unwrap_or_else(|| default.unwrap_or("").to_string())
}

#[tokio::main]
async fn main() -> Result<()> {
    let host = getenv("PRIVATE_HOST", Some("s1.ip.16yun.cn"));
    let port = getenv("PRIVATE_PORT", Some("39010"));
    let user = getenv("PRIVATE_USERNAME", None);
    let pass = getenv("PRIVATE_PASSWORD", None);
    let target = getenv("TARGET_URL", Some("https://httpbin.org/ip"));
    let base = getenv("PRIVATE_BASE", Some("http://s1.ip.16yun.cn:887"));
    if user.is_empty() || pass.is_empty() { return Err(anyhow!("Missing PRIVATE_USERNAME/PRIVATE_PASSWORD")); }

    // 管理接口演示（不走代理）
    let plain = reqwest::Client::builder().build()?;
    for p in ["current-ip", "switch-ip", "update"].iter() {
        let url = format!("{}/simple/{}?username={}&password={}", base.trim_end_matches('/'), p, user, pass);
        let r0 = plain.get(&url).send().await?;
        let code = r0.status();
        let body = r0.text().await.unwrap_or_default();
        println!("{} {}\n{}", p, code, body);
    }

    let proxy_url = format!("http://{}:{}@{}:{}", user, pass, host, port);
    let client = reqwest::Client::builder()
        .proxy(Proxy::all(&proxy_url).context("build proxy")?)
        .build()?;
    let resp = client.get(&target).send().await.context("request via private proxy failed")?;
    let status = resp.status();
    let body = resp.text().await.unwrap_or_default();
    println!("status: {}\n{}", status, body);
    Ok(())
}
