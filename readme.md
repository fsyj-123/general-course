# 课表爬虫项目

![GitHub repo size](https://img.shields.io/github/repo-size/yourusername/your-repo)
![GitHub stars](https://img.shields.io/github/stars/yourusername/your-repo?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/your-repo?style=social)

这个项目利用爬虫技术登录教务处网站，使用API获取课表信息，并通过JavaScript进行解析。项目的目标是帮助学生轻松获取他们的课表信息。

## 安装与使用

以下是项目的安装和使用步骤：

1. 配置 `application.yaml` 文件中的 `wx.app-id` 和 `secret`，这是用于身份验证的微信小程序的应用ID和密钥。
```yaml
wx:
  app-id: YOUR_APP_ID
  secret: YOUR_SECRET_KEY
```

2. 配置砚湖易办账号密码

配置 `site.fsyj.course.utils.JWConsts` 类中的 `username` 和 `password`，这是砚湖易办网站的登录凭证。

```java
public static final String username = "YOUR_USERNAME";
public static final String password = "YOUR_PASSWORD";
```

3. 添加开发环境配置文件，例如 `dev.yaml`，并配置数据库连接信息。然后在项目的 `sql` 目录下执行 `init.sql` 脚本，以初始化数据库。



4. 配置并启动 Redis，它将用于存储缓存数据。确保 Redis 服务器已经启动。

5. 启动 `course-api` 服务，它将提供课表信息的API接口。

6. 启动项目，确保所有配置和服务都已经正确启动。
