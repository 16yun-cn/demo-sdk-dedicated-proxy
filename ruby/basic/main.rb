#!/usr/bin/env ruby
require 'httparty'

host = ENV['PRIVATE_HOST'] || 's1.ip.16yun.cn'
port = (ENV['PRIVATE_PORT'] || '39010').to_i
user = ENV['PRIVATE_USERNAME'] || ENV['PRIVATE_USER']
pass = ENV['PRIVATE_PASSWORD'] || ENV['PRIVATE_PASS']
target = ENV['TARGET_URL'] || 'https://httpbin.org/ip'
base = (ENV['PRIVATE_BASE'] || 'http://s1.ip.16yun.cn:887').sub(%r{/*$}, '')
abort('缺少 PRIVATE_USERNAME/PRIVATE_PASSWORD') if !user || !pass

# 管理接口演示（不走代理）
%w[current-ip switch-ip update].each do |p|
  url = "#{base}/simple/#{p}?username=#{user}&password=#{pass}"
  begin
    r0 = HTTParty.get(url, timeout: 15)
    puts "#{p} #{r0.code}"
    puts r0.body
  rescue => e
    warn "#{p} error: #{e.message}"
  end
end

# 通过独享代理访问目标
resp = HTTParty.get(target,
  http_proxyaddr: host,
  http_proxyport: port,
  http_proxyuser: user,
  http_proxypass: pass,
  timeout: 15
)
puts resp.code
puts resp.body
