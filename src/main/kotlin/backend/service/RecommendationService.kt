package backend.service

import backend.dto.VacancyResponse
import backend.repository.JobVacancyRepository
import backend.repository.UserProfileRepository
import backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class RecommendationService(
    private val userRepository: UserRepository,
    private val profileRepository: UserProfileRepository,
    private val vacancyRepository: JobVacancyRepository
) {
    fun getRecommendations(email: String): List<RecommendedVacancy> {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Користувача не знайдено") }

        val profile = profileRepository.findByUserId(user.id)
            .orElse(null)

        // Якщо профіль порожній — повертаємо всі вакансії з 0%
        val skillsRaw = profile?.skills ?: ""
        val skills = skillsRaw
            .split(",")
            .map { it.trim().lowercase() }
            .filter { it.isNotEmpty() }

        val vacancies = vacancyRepository.findAllByIsActiveTrue()

        return vacancies
            .map { vacancy ->
                val text = "${vacancy.title} ${vacancy.description}".lowercase()
                val matchedSkills = skills.filter { skill -> text.contains(skill) }
                val score = if (skills.isEmpty()) 0
                else (matchedSkills.size * 100) / skills.size

                RecommendedVacancy(
                    id = vacancy.id,
                    title = vacancy.title,
                    company = vacancy.company,
                    location = vacancy.location,
                    salary = vacancy.salary,
                    employmentType = vacancy.employmentType,
                    description = vacancy.description,
                    employerFirstName = vacancy.employer.firstName,
                    employerLastName = vacancy.employer.lastName,
                    employerEmail = vacancy.employer.email,
                    createdAt = vacancy.createdAt.toString(),
                    matchScore = score,
                    matchedSkills = matchedSkills
                )
            }
            .filter { it.matchScore > 0 }   // тільки де є збіг
            .sortedByDescending { it.matchScore }
    }
}

data class RecommendedVacancy(
    val id: Long,
    val title: String,
    val company: String,
    val location: String,
    val salary: String,
    val employmentType: String,
    val description: String,
    val employerFirstName: String,
    val employerLastName: String,
    val employerEmail: String,
    val createdAt: String,
    val matchScore: Int,
    val matchedSkills: List<String>
)