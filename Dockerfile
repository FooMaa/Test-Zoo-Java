# Используем официальный образ OpenJDK
FROM eclipse-temurin:17-jdk-jammy as builder

# Рабочая директория
WORKDIR /app

# Копируем только файлы, необходимые для сборки
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Собираем приложение
RUN ./mvnw clean package -DskipTests

# Финальный образ
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Копируем собранный JAR из builder-стадии
COPY --from=builder /app/target/*.jar app.jar

# Открываем порт, на котором работает приложение
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]