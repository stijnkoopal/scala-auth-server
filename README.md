# Scala Authentication Server

## Stack
* Akka
* Spray
* Slick
* Maven

## Modules
| Command | Description |
| --- | --- |
| `auth-password` | REST implementation for authentication through email/password |
| `token-api` | An API module that can be incorporated in projects that want to communicate with the `token-manager` |
| `token-manager` | REST implementation to generate and verity JWT tokens  |
| `identity-management` | REST implementation for identity management |
