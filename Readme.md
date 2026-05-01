# 💸 Expense Splitter Backend

A Spring Boot backend application to split expenses among group members (like Splitwise).

---

## 🚀 Features

* User authentication (JWT)
* Create & manage groups
* Add expenses (Equal / Exact split)
* Balance calculation
* Debt settlement (optimized algorithm)
* Global exception handling
* Clean API response structure

---

## 🛠 Tech Stack

* Java
* Spring Boot
* Spring Security (JWT)
* Spring Data JPA
* MySQL

---

## ⚙️ How to Run

1. Clone repo
2. Configure DB in `application.properties`
3. Run the project

---

## 🔐 Authentication APIs

* POST `/auth/signup`
* POST `/auth/login`

---

## 📊 Sample Response

```json
{
  "status": "success",
  "message": "Login successful",
  "data": "JWT_TOKEN",
  "timestamp": "..."
}
```

---

## 👨‍💻 Author

Dev Bhatt
