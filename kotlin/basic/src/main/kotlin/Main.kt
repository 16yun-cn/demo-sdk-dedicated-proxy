import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.InetSocketAddress
import java.net.Proxy

fun getenv(name: String, def: String? = null): String? {
    val v = System.getenv(name)
    return if (v != null && v.isNotBlank()) v else def
}

fun main() {
    val host = getenv("PRIVATE_HOST", "s1.ip.16yun.cn")!!
    val port = getenv("PRIVATE_PORT", "39010")!!.toInt()
    val user = getenv("PRIVATE_USERNAME") ?: getenv("PRIVATE_USER") ?: error("Missing PRIVATE_USERNAME")
    val pass = getenv("PRIVATE_PASSWORD") ?: getenv("PRIVATE_PASS") ?: error("Missing PRIVATE_PASSWORD")
    val target = getenv("TARGET_URL", "https://httpbin.org/ip")!!
    val base = getenv("PRIVATE_BASE", "http://s1.ip.16yun.cn:887")!!.trimEnd('/')

    // 管理接口演示（不走代理）
    val http = OkHttpClient()
    for (p in arrayOf("current-ip", "switch-ip", "update")) {
        val url = "$base/simple/$p?username=$user&password=$pass"
        val rq = Request.Builder().url(url).build()
        http.newCall(rq).execute().use { r ->
            println("$p ${'$'}{r.code}")
            println(r.body?.string() ?: "")
        }
    }

    val proxy = Proxy(Proxy.Type.HTTP, InetSocketAddress(host, port))
    val client = OkHttpClient.Builder()
        .proxy(proxy)
        .proxyAuthenticator { _, response ->
            val credential = Credentials.basic(user, pass)
            response.request.newBuilder().header("Proxy-Authorization", credential).build()
        }
        .build()

    val req = Request.Builder().url(target).build()
    client.newCall(req).execute().use { r ->
        println("status: ${'$'}{r.code}")
        println(r.body?.string() ?: "")
    }
}
