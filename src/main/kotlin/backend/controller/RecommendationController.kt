package backend.controller

import backend.service.RecommendationService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = ["http://localhost:5173"])
class RecommendationController(
    private val recommendationService: RecommendationService
) {
    @GetMapping
    fun getRecommendations(authentication: Authentication): ResponseEntity<Any> {
        return try {
            ResponseEntity.ok(recommendationService.getRecommendations(authentication.name))
        } catch (e: RuntimeException) {
            ResponseEntity.badRequest().body(mapOf("error" to e.message))
        }
    }
}