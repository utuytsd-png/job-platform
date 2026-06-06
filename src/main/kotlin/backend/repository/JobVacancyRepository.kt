package backend.repository

import backend.model.JobVacancy
import org.springframework.data.jpa.repository.JpaRepository

interface JobVacancyRepository : JpaRepository<JobVacancy, Long> {
    fun findAllByIsActiveTrue(): List<JobVacancy>
    fun findAllByEmployerId(employerId: Long): List<JobVacancy>
    fun findAllByTitleContainingIgnoreCase(title: String): List<JobVacancy>
}