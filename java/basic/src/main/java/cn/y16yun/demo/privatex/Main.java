package cn.y16yun.demo.privatex;

import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main {
    private static String getenv(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }

    public static void main(String[] args) throws IOException {
        String host = getenv("PRIVATE_HOST", "s1.ip.16yun.cn");
        int port = Integer.parseInt(getenv("PRIVATE_PORT", "39010"));
        String user = getenv("PRIVATE_USERNAME", getenv("PRIVATE_USER", null));
        String pass = getenv("PRIVATE_PASSWORD", getenv("PRIVATE_PASS", null));
        if (user == null || pass == null) {
            throw new IllegalArgumentException("Missing PRIVATE_USERNAME/PRIVATE_PASSWORD");
        }
        String target = getenv("TARGET_URL", "https://httpbin.org/ip");
        String base = getenv("PRIVATE_BASE", "http://s1.ip.16yun.cn:887").replaceAll("/+$", "");

        // 管理接口演示（不走代理）
        OkHttpClient http = new OkHttpClient();
        for (String p : new String[]{"current-ip", "switch-ip", "update"}) {
            String url = String.format("%s/simple/%s?username=%s&password=%s", base, p, user, pass);
            Request rq = new Request.Builder().url(url).build();
            try (Response r = http.newCall(rq).execute()) {
                System.out.println(p + " " + r.code());
                System.out.println(r.body() != null ? r.body().string() : "");
            }
        }

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
        OkHttpClient client = new OkHttpClient.Builder()
                .proxy(proxy)
                .proxyAuthenticator((route, response) ->
                        response.request().newBuilder()
                                .header("Proxy-Authorization", Credentials.basic(user, pass))
                                .build())
                .build();

        Request req = new Request.Builder().url(target).build();
        try (Response resp = client.newCall(req).execute()) {
            System.out.println("status: " + resp.code());
            System.out.println(resp.body() != null ? resp.body().string() : "");
        }
    }
}
