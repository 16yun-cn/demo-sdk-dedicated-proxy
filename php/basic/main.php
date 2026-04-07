<?php
// 16YUN(亿牛云) PHP 独享代理 Demo
require __DIR__ . '/vendor/autoload.php';

use GuzzleHttp\Client;

$host = getenv('PRIVATE_HOST') ?: 's1.ip.16yun.cn';
$port = getenv('PRIVATE_PORT') ?: '39010';
$user = getenv('PRIVATE_USERNAME') ?: getenv('PRIVATE_USER');
$pass = getenv('PRIVATE_PASSWORD') ?: getenv('PRIVATE_PASS');
$target = getenv('TARGET_URL') ?: 'https://httpbin.org/ip';
if (!$user || !$pass) { fwrite(STDERR, "缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD\n"); exit(2); }

$base = rtrim(getenv('PRIVATE_BASE') ?: 'http://s1.ip.16yun.cn:887', '/');

// 管理接口演示（不走代理）
$plain = new Client(['timeout' => 15.0, 'verify' => true]);
foreach (['current-ip','switch-ip','update'] as $p) {
  $url = sprintf('%s/simple/%s?username=%s&password=%s', $base, $p, $user, $pass);
  try {
    $r0 = $plain->get($url);
    echo $p, ' ', $r0->getStatusCode(), "\n", (string)$r0->getBody(), "\n";
  } catch (Exception $e) {
    fwrite(STDERR, $p.' error: '.$e->getMessage()."\n");
  }
}

// 通过独享代理访问目标
$client = new Client(['timeout' => 15.0, 'verify' => true, 'proxy' => "http://{$user}:{$pass}@{$host}:{$port}"]);
$resp = $client->get($target);
echo $resp->getStatusCode(), "\n";
echo $resp->getBody();
