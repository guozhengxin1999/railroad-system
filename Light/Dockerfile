# 使用官方的 OpenJDK 11 映像作为基础映像
FROM openjdk:17

# 设置工作目录
WORKDIR /app

# 复制编译后的 JAR 文件到容器中
COPY target/Light-0.0.1-SNAPSHOT.jar /app/light-application.jar

# 暴露应用程序运行的端口（如果需要的话）
EXPOSE 8083

# 运行应用程序
CMD ["java", "-jar", "light-application.jar"]

# 指定容器应该监听的端口
ENV PORT=8083