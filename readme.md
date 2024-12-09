
# ImgUr Spring Boot Application

This Spring Boot application integrates with the Imgur API to allow users to upload, view, and delete images. It supports user registration, authentication, and associating images with user profiles.

## Features
- User registration with basic information (username and password)
- Image upload to Imgur API
- View uploaded images associated with a user
- Delete images from Imgur
- All endpoints are protected by user authentication

## Prerequisites
- Java 11 or higher
- Spring Boot 2.x
- Maven
- Imgur API Client ID and Secret
- H2 database (configured in `application.properties`)

## Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/RamyaA-Java/ImgurApp.git
cd ImgurApp
```

### Step 2: Install Dependencies

For Maven:

```bash
mvn clean install
```

For Gradle:

```bash
gradle build
```

### Step 3: Run the Application

Run the application with:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

## API Endpoints

### User Endpoints

#### 1. Register User

**POST** `/users/register`

Registers a new user with basic information.

Request Body:
```json
{
  "username": "john_doe",
  "password": "password123"
}
```

Response:
```json
{
  "id": 1,
  "username": "john_doe"
}
```

#### 2. Authenticate User

**POST** `/users/login`

Authenticates a user with their username and password.

Request Parameters:
- `username`: User's username
- `password`: User's password

Response:
```json
{
  "id": 1,
  "username": "john_doe"
}
```

#### 3. Get User Information

**GET** `/users/{username}`

Retrieves basic information of the user.

Response:
```json
{
  "id": 1,
  "username": "john_doe"
}
```

### Image Endpoints

#### 1. Upload Image

**POST** `/images/upload`

Uploads an image to Imgur and associates it with the logged-in user.

Request Parameters:
- `image`: The image file to upload (multipart form-data)
- `username`: The username for authentication
- `password`: The password for authentication

Response:
```json
{
  "id": "JRBePDz",
  "link": "https://imgur.com/JRBePDz",
  "deleteHash": "EvHVZkhJhdNClgY"
}
```

#### 2. View Image

**GET** `/images/{imageId}`

Retrieves the uploaded image associated with the logged-in user.

Response:
```json
{
  "id": "JRBePDz",
  "link": "https://imgur.com/JRBePDz"
}
```

#### 3. Delete Image

**DELETE** `/images/{deleteHash}`

Deletes the uploaded image from Imgur.

Request Body:
```json
{
  "deleteHash": "EvHVZkhJhdNClgY"
}
```

Response:
```json
{
  "status": 200,
  "success": true
}
```

## Troubleshooting

- If you encounter the `401 Unauthorized` error, ensure your username and password are correct and you are sending the correct `Authorization` headers in the requests.
- Make sure the `client-id` and `client-secret` for Imgur are correctly configured in the `application.properties` file.
