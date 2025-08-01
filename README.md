ABC BERKUT Test Task: Telegram Bot Integration API
📚 Project Overview
REST API для интеграции с Telegram ботом, позволяющее:

Регистрировать пользователей

Генерировать токены для привязки Telegram чатов

Отправлять сообщения через API с дублированием в Telegram

Просматривать историю сообщений

⚙️ Конфигурация
Java 17

Spring Boot 3.x

H2 Database (in-memory для быстрого старта)

Ngrok для тестирования Telegram webhook

🚀 Запуск проекта
1. Настройка Telegram Webhook
bash
curl -X POST "https://api.telegram.org/<your-bot-token>/setWebhook" \
     -H "Content-Type: application/json" \
     -d '{"url": "https://your-ngrok-url.ngrok.io/api/telegram/webhook"}'
2. Регистрация пользователя
http
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "name": "Иван Иванов",
  "login": "ivan",
  "password": "securePassword123"
}
3. Аутентификация
http
POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "login": "ivan",
  "password": "securePassword123"
}
В ответе получите JWT токен для авторизации.

4. Генерация Telegram токена
http
GET http://localhost:8080/telegram/generate-token
Authorization: Bearer <your-jwt-token>
5. Привязка Telegram чата
Скопируйте полученный токен

Отправьте его вашему боту в Telegram

6. Отправка сообщений
http
POST http://localhost:8080/messages
Authorization: Bearer <your-jwt-token>
Content-Type: application/json

{
  "content": "Привет, бот!"
}
7. Просмотр истории сообщений
http
GET http://localhost:8080/messages
Authorization: Bearer <your-jwt-token>

⚠️ Ограничения реализации
Нет валидации входных данных

Отсутствуют глобальные обработчики исключений

Нет миграций БД (используется H2 in-memory)

Нет Docker-контейнеризации

📝 Примечания
Для тестирования webhook используется ngrok:

bash
ngrok http 8080
Проект разработан как тестовое задание с акцентом на базовую функциональность.
