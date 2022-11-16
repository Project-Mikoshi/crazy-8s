package Controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

	@GetMapping("/api/game")
	public String index() {
		return "Greetings from Spring Boot!";
	}
}