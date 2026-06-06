package backend.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "job_vacancies")
data class JobVacancy(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val title: String = "",

    @Column(nullable = false, length = 5000)
    val description: String = "",

    @Column(nullable = false)
    val company: String = "",

    @Column(nullable = false)
    val location: String = "",

    val salary: String = "",

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    val employer: User = User(),

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val isActive: Boolean = true
)