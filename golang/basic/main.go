// 16YUN(亿牛云) Go 私有独享代理 Demo
package main

import (
    "fmt"
    "io"
    "net/http"
    "net/url"
    "os"
    "strings"
    "time"
)

func getenv(k, def string) string {
    v := os.Getenv(k)
    if v == "" {
        return def
    }
    return v
}

func main() {
    host := getenv("PRIVATE_HOST", "s1.ip.16yun.cn")
    port := getenv("PRIVATE_PORT", "39010")
    user := os.Getenv("PRIVATE_USERNAME")
    pass := os.Getenv("PRIVATE_PASSWORD")
    target := getenv("TARGET_URL", "https://httpbin.org/ip")
    base := strings.TrimRight(getenv("PRIVATE_BASE", "http://s1.ip.16yun.cn:887"), "/")
    if user == "" || pass == "" { panic("缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD") }

    // 管理接口演示
    http.DefaultClient.Timeout = 15 * time.Second
    for _, path := range []string{"current-ip", "switch-ip", "update"} {
        url := fmt.Sprintf("%s/simple/%s?username=%s&password=%s", base, path, user, pass)
        resp, err := http.Get(url)
        if err != nil { fmt.Println(path, "error:", err); continue }
        body, _ := io.ReadAll(resp.Body); resp.Body.Close()
        fmt.Println(path, resp.Status, string(body))
    }

    proxyURL, _ := url.Parse(fmt.Sprintf("http://%s:%s@%s:%s", user, pass, host, port))
    tr := &http.Transport{ Proxy: http.ProxyURL(proxyURL) }
    client := &http.Client{ Transport: tr, Timeout: 15 * time.Second }

    resp, err := client.Get(target)
    if err != nil { panic(err) }
    defer resp.Body.Close()
    fmt.Println("STATUS:", resp.Status)
}
