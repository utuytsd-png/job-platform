package backend.service

import backend.dto.CreateVacancyRequest
import backend.dto.VacancyResponse
import backend.model.JobVacancy
import backend.repository.JobVacancyRepository
import backend.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class VacancyService(
    private val vacancyRepository: JobVacancyRepository,
    private val userRepository: UserRepository
) {
    fun getAllVacancies(): List<VacancyResponse> {
        return vacancyRepository.findAllByIsActiveTrue().map { it.toResponse() }
    }

    fun getVacancyById(id: Long): VacancyResponse {
        val vacancy = vacancyRepository.findById(id)
            .orElseThrow { RuntimeException("Вакансію не знайдено") }
        return vacancy.toResponse()
    }

    fun createVacancy(request: CreateVacancyRequest, employerEmail: String): VacancyResponse {
        val employer = userRepository.findByEmail(employerEmail)
            .orElseThrow { RuntimeException("Користувача не знайдено") }

        val vacancy = JobVacancy(
            title = request.title,
            description = request.description,
            company = request.company,
            location = request.location,
            salary = request.salary,
            employer = employer
        )

        return vacancyRepository.save(vacancy).toResponse()
    }

    fun deleteVacancy(id: Long, employerEmail: String) {
        val vacancy = vacancyRepository.findById(id)
            .orElseThrow { RuntimeException("Вакансію не знайдено") }

        if (vacancy.employer.email != employerEmail) {
            throw RuntimeException("Немає доступу")
        }

        vacancyRepository.delete(vacancy)
    }

    fun searchVacancies(query: String): List<VacancyResponse> {
        return vacancyRepository.findAllByTitleContainingIgnoreCase(query).map { it.toResponse() }
    }

    private fun JobVacancy.toResponse() = VacancyResponse(
        id = id,
        title = title,
        description = description,
        company = company,
        location = location,
        salary = salary,
        employerFirstName = employer.firstName,
        employerLastName = employer.lastName,
        employerEmail = employer.email,
        createdAt = createdAt,
        isActive = isActive
    )
}