package backend.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String = "",

    @Column(nullable = false)
    val password: String = "",

    @Column(nullable = false)
    val firstName: String = "",

    @Column(nullable = false)
    val lastName: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val role: UserRole = UserRole.JOB_SEEKER
)

enum class UserRole {
    JOB_SEEKER,
    EMPLOYER,
    ADMIN
}