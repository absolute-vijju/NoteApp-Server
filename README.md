# NoteApp-Server

This is a simple Rest API for Note App. Where users can create & login his/her account and add, update and delete their notes. This server created by Ktor(https://ktor.io/).


## API Reference

#### Register User
```http
  POST /v1/users/register
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/register_user.png">

---

#### Login User
```http
  POST /v1/users/login
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/login_user.png">

---

#### ⚠ Warning: When you login, you get jwt in message. Pass this token as Bearer Token for below APIs to know the current user.

---

#### Get All Notes Of  User
```http
  POST /v1/notes
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/user_notes.png">

---

#### Create Note

```http
  POST /v1/notes/create
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/create_note_token.png">
<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/delete_note_params.png">

---

#### Update User
```http
  POST /v1/users/update
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/update_note_token.png">
<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/update_note_body.png">

---

#### Delete User

```http
  POST /v1/users/delete
```
- Request & Response

<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/delete_note_token.png">
<img src="https://github.com/absolute-vijju/NoteApp-Server/blob/main/images/delete_note_params.png">
