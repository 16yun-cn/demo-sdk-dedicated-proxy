#include <curl/curl.h>
#include <cstdlib>
#include <iostream>
#include <string>

static size_t write_cb(char* ptr, size_t size, size_t nmemb, void*) {
  std::cout.write(ptr, size * nmemb);
  return size * nmemb;
}

static const char* getEnv(const char* k, const char* defv) {
  const char* v = std::getenv(k);
  return v ? v : defv;
}

int main() {
  const char* host = getEnv("PRIVATE_HOST", "s1.ip.16yun.cn");
  const char* port = getEnv("PRIVATE_PORT", "39010");
  const char* user = getEnv("PRIVATE_USERNAME", getEnv("PRIVATE_USER", ""));
  const char* pass = getEnv("PRIVATE_PASSWORD", getEnv("PRIVATE_PASS", ""));
  const char* target = getEnv("TARGET_URL", "https://httpbin.org/ip");
  const char* base = getEnv("PRIVATE_BASE", "http://s1.ip.16yun.cn:887");
  if (std::string(user).empty() || std::string(pass).empty()) {
    std::cerr << "缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD" << std::endl;
    return 2;
  }

  // 管理接口演示（不走代理）
  for (auto path : {"current-ip", "switch-ip", "update"}) {
    CURL* c = curl_easy_init();
    if (!c) return 1;
    std::string url = std::string(base) + "/simple/" + path + "?username=" + user + "&password=" + pass;
    curl_easy_setopt(c, CURLOPT_URL, url.c_str());
    curl_easy_setopt(c, CURLOPT_TIMEOUT, 15L);
    curl_easy_setopt(c, CURLOPT_WRITEFUNCTION, write_cb);
    CURLcode rc = curl_easy_perform(c);
    if (rc != CURLE_OK) std::cerr << path << " error: " << curl_easy_strerror(rc) << std::endl;
    curl_easy_cleanup(c);
  }

  CURL* curl = curl_easy_init();
  if (!curl) return 1;

  curl_easy_setopt(curl, CURLOPT_URL, target);
  curl_easy_setopt(curl, CURLOPT_PROXY, (std::string("http://") + host + ":" + port).c_str());
  curl_easy_setopt(curl, CURLOPT_PROXYUSERPWD, (std::string(user) + ":" + pass).c_str());
  curl_easy_setopt(curl, CURLOPT_TIMEOUT, 15L);
  curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, write_cb);

  CURLcode res = curl_easy_perform(curl);
  if (res != CURLE_OK) std::cerr << "curl: " << curl_easy_strerror(res) << std::endl;
  curl_easy_cleanup(curl);
  return (res == CURLE_OK) ? 0 : 1;
}
