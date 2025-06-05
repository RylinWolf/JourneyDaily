# JourneyDaily

JourneyDaily 是一个基于 Spring Boot 的日记管理系统，允许用户创建、管理和搜索他们的日常日记。

## 项目简介

JourneyDaily 提供了一个完整的日记管理解决方案，支持日记的创建、编辑、删除、恢复和搜索功能。系统使用 MySQL 进行数据存储，Elasticsearch 进行全文搜索，Redis 进行缓存，确保高效的数据访问和查询性能。

## 功能特点

- 用户管理：支持用户注册、登录和权限控制
- 日记管理：
  - 创建和发布日记
  - 查看日记列表和详情
  - 编辑和更新日记
  - 删除和恢复日记
  - 日记草稿功能
- 标签管理：为日记添加标签，方便分类和查找
- 全文搜索：基于 Elasticsearch 的强大搜索功能
- API 文档：集成 Knife4j 提供 API 文档

## 技术栈

- **后端框架**：Spring Boot 2.7.3
- **数据库**：MySQL 8.0
- **持久层框架**：MyBatis Plus 3.5.7
- **缓存**：Redis
- **搜索引擎**：Elasticsearch 7.12.1
- **连接池**：Druid
- **认证**：JWT (JSON Web Token)
- **API 文档**：Knife4j 3.0.2
- **工具库**：Lombok, Hutool
- **Java 版本**：Java 17

## 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis
- Elasticsearch 7.12.1
- Docker (可选，用于容器化部署)

## 部署方式

### 1. 准备环境

确保已安装以下软件：
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis
- Elasticsearch 7.12.1

### 2. 数据库配置

1. 创建 MySQL 数据库：
```sql
CREATE DATABASE journeyDaily CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件：
   - 根据您的环境修改 `src/main/resources/application-dev.yml` 中的数据库配置

### 3. Elasticsearch 配置

1. 确保 Elasticsearch 服务已启动
2. 修改配置文件中的 Elasticsearch 连接信息

### 4. 编译打包

```bash
mvn clean package -DskipTests
```

### 5. 运行应用

```bash
java -jar target/journeydaily-1.0-SNAPSHOT.jar
```

### 6. Docker 部署 (可选)

如果您希望使用 Docker 进行部署，可以按照以下步骤操作：

1. 构建 Docker 镜像：
```bash
docker build -t journeydaily:1.0 .
```

2. 使用 Docker Compose 启动服务：
```bash
docker-compose up -d
```

## 访问应用

应用启动后，可以通过以下方式访问：

- Web 应用：http://localhost:8999
- API 文档：http://localhost:8999/doc.html

## 开发指南

1. 克隆项目：
```bash
git clone [项目仓库URL]
```

2. 导入 IDE (如 IntelliJ IDEA)

3. 安装依赖：
```bash
mvn install
```

4. 运行应用：
```bash
mvn spring-boot:run
```
