package backend.controller

import backend.model.UserRole
import backend.repository.JobVacancyRepository
import backend.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = ["http://localhost:5173"])
class AdminController(
    private val userRepository: UserRepository,
    private val vacancyRepository: JobVacancyRepository
) {
    // Перевірка що юзер — адмін
    private fun checkAdmin(auth: Authentication) {
        if (!auth.authorities.any { it.authority == "ROLE_ADMIN" }) {
            throw RuntimeException("Доступ заборонено")
        }
    }

    // Всі користувачі
    @GetMapping("/users")
    fun getAllUsers(auth: Authentication): ResponseEntity<Any> {
        checkAdmin(auth)
        val users = userRepository.findAll().map {
            mapOf(
                "id" to it.id,
                "email" to it.email,
                "firstName" to it.firstName,
                "lastName" to it.lastName,
                "role" to it.role.name
            )
        }
        return ResponseEntity.ok(users)
    }

    // Видалити користувача
    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Long, auth: Authentication): ResponseEntity<Any> {
        checkAdmin(auth)
        if (!userRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Користувача не знайдено"))
        }
        userRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Видалено"))
    }

    // Всі вакансії
    @GetMapping("/vacancies")
    fun getAllVacancies(auth: Authentication): ResponseEntity<Any> {
        checkAdmin(auth)
        val vacancies = vacancyRepository.findAll().map {
            mapOf(
                "id" to it.id,
                "title" to it.title,
                "company" to it.company,
                "location" to it.location,
                "employerEmail" to it.employer.email,
                "createdAt" to it.createdAt.toString()
            )
        }
        return ResponseEntity.ok(vacancies)
    }

    // Видалити вакансію
    @DeleteMapping("/vacancies/{id}")
    fun deleteVacancy(@PathVariable id: Long, auth: Authentication): ResponseEntity<Any> {
        checkAdmin(auth)
        if (!vacancyRepository.existsById(id)) {
            return ResponseEntity.badRequest().body(mapOf("error" to "Вакансію не знайдено"))
        }
        vacancyRepository.deleteById(id)
        return ResponseEntity.ok(mapOf("message" to "Видалено"))
    }

    // Статистика
    @GetMapping("/stats")
    fun getStats(auth: Authentication): ResponseEntity<Any> {
        checkAdmin(auth)
        return ResponseEntity.ok(mapOf(
            "totalUsers" to userRepository.count(),
            "totalVacancies" to vacancyRepository.count(),
            "jobSeekers" to userRepository.findAllByRole(UserRole.JOB_SEEKER).size,
            "employers" to userRepository.findAllByRole(UserRole.EMPLOYER).size
        ))
    }
}