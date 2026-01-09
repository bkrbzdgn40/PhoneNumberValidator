package com.odev.telefon_servisi.controller;


import com.odev.telefon_servisi.model.Registration;
import com.odev.telefon_servisi.repository.RegistrationRepository;
import com.odev.telefon_servisi.utils.PhoneValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class PhoneController {
    @Autowired
    RegistrationRepository registrationRepository;

    @PostMapping("/phone/validate")
    public ResponseEntity<?> validatePhone(@RequestBody Map<String, String> payload) {
        String phone = payload.get("number");
        PhoneValidator.ValidationResult result = PhoneValidator.validate(payload.get("phone"));
        Map<String, Object> response = new HashMap<>();
        response.put("number", phone);

        Map<String, Boolean> rules = new HashMap<>();
        rules.put("hasNonZeroDigits",result.hasNonZeroDigit);
        rules.put("sumFirstEqualsLast",result.sumFirstEqualsLast);
        rules.put("sumOddEqualEven", result.sumOddEqualsEven);

        response.put("rules", rules);
        response.put("isValid", result.isValid);

        if(phone==null || !phone.matches("\\d{6}")){
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);

    }
    @PostMapping("/registration")
    public ResponseEntity<?> registerPhone(@RequestBody Registration registration) {
        PhoneValidator.ValidationResult result = PhoneValidator.validate(registration.getPhone());
        if(!result.isValid){
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "denied");
            errorResponse.put("message", "Numara formatı gecersiz!");
            errorResponse.put("isValid", false);
            return ResponseEntity.status(400).body(errorResponse);
        }
        try {
            Registration savedData = registrationRepository.save(registration);
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("status", "accepted");
            successResponse.put("message", "Telefon numarası geçerli, kayıt başarılı!");
            successResponse.put("data", savedData);
            return ResponseEntity.status(201).body(successResponse);
        }catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(400).body("Hata: Bu telefon numarası zaten kayıtlı!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Sunucu tarafında beklenmedik bir hata oluştu.");
        }
    }
    @GetMapping("/phone/count")
    public ResponseEntity<Integer> getCount() {
        int validCount = 0;

        for (int i = 0; i <= 999999; i++) {
            String phone = String.format("%06d", i);
            if (PhoneValidator.validate(phone).isValid) {
                validCount++;
            }
        }
        return ResponseEntity.ok(validCount);
    }


}
