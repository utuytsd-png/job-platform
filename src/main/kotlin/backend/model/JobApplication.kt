package backend.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "job_applications")
data class JobApplication(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vacancy_id", nullable = false)
    val vacancy: JobVacancy = JobVacancy(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "applicant_id", nullable = false)
    val applicant: User = User(),

    @Column(nullable = false, length = 2000)
    val coverLetter: String = "",

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ApplicationStatus = ApplicationStatus.PENDING,

    @Column(nullable = false)
    val appliedAt: LocalDateTime = LocalDateTime.now()
)

enum class ApplicationStatus {
    PENDING,    // очікує розгляду
    REVIEWED,   // переглянуто
    ACCEPTED,   // прийнято
    REJECTED    // відхилено
}