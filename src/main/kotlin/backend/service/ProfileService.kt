package backend.service

import backend.dto.UpdateProfileRequest
import backend.dto.UserProfileResponse
import backend.model.UserProfile
import backend.repository.UserProfileRepository
import backend.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProfileService(
    private val userRepository: UserRepository,
    private val profileRepository: UserProfileRepository
) {
    fun getProfile(email: String): UserProfileResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Користувача не знайдено") }

        val profile = profileRepository.findByUserId(user.id)
            .orElse(UserProfile(user = user))

        return UserProfileResponse(
            userId = user.id,
            firstName = user.firstName,
            lastName = user.lastName,
            email = user.email,
            role = user.role.name,
            skills = profile.skills,
            experience = profile.experience,
            education = profile.education,
            resumeText = profile.resumeText
        )
    }

    @Transactional
    fun updateProfile(email: String, request: UpdateProfileRequest): UserProfileResponse {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Користувача не знайдено") }

        user.firstName = request.firstName
        user.lastName = request.lastName
        userRepository.save(user)

        val profile = profileRepository.findByUserId(user.id)
            .orElse(UserProfile(user = user))

        profile.skills = request.skills
        profile.experience = request.experience
        profile.education = request.education
        profile.resumeText = request.resumeText
        profileRepository.save(profile)

        return getProfile(email)
    }
}