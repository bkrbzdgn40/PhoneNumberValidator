ErişimWeb Arayüzü: http://localhost
Backend API: http://localhost:80804. 
Örnek API İstek ve Yanıtları
POST /api/registration (Kayıt ve Doğrulama)
Başarılı Kayıt
{"name": "...", "email": "...", "phone": "054153"}
{"status": "accepted", 
"message": "Telefon numarası geçerli, kayıt başarıyla oluşturuldu."}201 Created
Geçersiz Numara
{"name": "...", "email": "...", "phone": "123456"}
{"status": "denied", "message": "Geçersiz telefon numarası. Lütfen yeni bir numara deneyin."}400 Bad Request
Numara Tekrarı 
Daha önce kaydedilmiş numara tekrar gönderilir.
"Hata: Bu telefon numarası zaten kayıtlı!"
400 Bad Request
POST /api/phone/validate (Sadece Doğrulama)
Bu endpoint, Frontend'den bağımsız olarak numaranın kurallara uygunluğunu detaylı raporlar.
İstek (Request Body):JSON{
    "number": "054153"
}
Yanıt (Response Body):JSON{
    "number": "054153",
    "rules": {
        "hasNonZeroDigit": true,
        "sumFirstEqualsLast": true,
        "sumOddEqualsEven": true
    },
    "isValid": true
}
GET /api/phone/count (Sayaç) 
Bu endpoint, 000000 ile 999999 arasındaki tüm 6 basamaklı numaraları döngü ile kontrol ederek kurallara uyan toplam geçerli numara sayısını döndürür.İstekYanıt (Response)HTTP KoduGET http://localhost:8080/api/phone/count505200 OK