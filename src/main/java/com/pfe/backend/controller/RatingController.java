package com.pfe.backend.controller;

import com.pfe.backend.domain.Rating;
import com.pfe.backend.service.impl.RatingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    @Autowired
    private RatingServiceImpl ratingService;

    @PostMapping("/add")
    public ResponseEntity<Rating> addRating(@RequestBody Rating request) {
        Rating rating = ratingService.addRating(request.getUsername(), request.getHandymanUsername(), request.getRate());
        return ResponseEntity.ok(rating);
    }

    @PutMapping("/update")
    public ResponseEntity<Rating> updateRating(@RequestBody Rating request) {
        Rating rating = ratingService.updateRating(request.getUsername(), request.getHandymanUsername(), request.getRate());
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteRating(@RequestBody Rating request) {
        ratingService.deleteRating(request.getUsername(), request.getHandymanUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<Integer> getRating(@RequestParam String username, @RequestParam String handymanUsername) {
        int rate = ratingService.getRating(username, handymanUsername);
        return ResponseEntity.ok(rate);
    }

    @GetMapping("/overall")
    public ResponseEntity<Double> getOverallRating(@RequestParam String handymanUsername) {
        double overallRating = ratingService.getOverallRating(handymanUsername);
        return ResponseEntity.ok(overallRating);
    }
}

