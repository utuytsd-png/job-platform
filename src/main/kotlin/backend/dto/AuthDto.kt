package backend.dto

data class RegisterRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val role: String = "JOB_SEEKER"
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: String
)