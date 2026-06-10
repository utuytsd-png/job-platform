package backend.controller

import backend.dto.UpdateProfileRequest
import backend.service.ProfileService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = ["http://localhost:5173"])
class ProfileController(private val profileService: ProfileService) {

    @GetMapping
    fun getProfile(authentication: Authentication): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(profileService.getProfile(authentication.name))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }

    @PutMapping
    fun updateProfile(
        @RequestBody request: UpdateProfileRequest,
        authentication: Authentication
    ): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(profileService.updateProfile(authentication.name, request))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}