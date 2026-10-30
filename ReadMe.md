# PhoneNumberValidator

Spring Boot (Java 17), MySQL ve Nginx ile çalışan, **özel kurallara göre altı basamaklı sayı doğrulayan** örnek kayıt uygulaması. Gerçek telefon numaralarını veya ülke kodlarını doğrulamaz.

## Çalıştırma

Gereksinimler: Java 17, Maven ve Docker Compose.

```bash
cd api
./mvnw test
./mvnw package -DskipTests
cd ..
docker compose up --build
```

Windows'ta `./mvnw` yerine `mvnw.cmd` kullanılabilir. Dockerfile hazır derlenmiş JAR dosyasını kopyaladığı için **Compose öncesinde paketleme adımı gereklidir.**

- Web arayüzü: http://localhost/
- Backend: http://localhost:8080/
- MySQL: host üzerinde port 3307, konteynerler arasında 3306

Üretim ortamında örnek `docker-compose.yml` içindeki varsayılan veritabanı parolalarını değiştirmeden kullanmayın.

## Doğrulama kuralları

Altı ASCII rakamı (başında sıfır olabilir) için şu üç koşul birlikte sağlanmalıdır:

1. Rakamların en az biri sıfırdan farklıdır.
2. İlk üç rakamın toplamı son üç rakamın toplamına eşittir.
3. 1., 3. ve 5. basamakların toplamı 2., 4. ve 6. basamakların toplamına eşittir.

Örneğin `123321` geçerlidir, `000000` ve `12345` geçersizdir.

## API

### POST /api/phone/validate

```json
{"number":"123321"}
```

Başarılı yanıt (`200 OK`):

```json
{
  "number": "123321",
  "rules": {
    "hasNonZeroDigit": true,
    "sumFirstEqualsLast": true,
    "sumOddEqualsEven": true
  },
  "isValid": true
}
```

Altı rakam biçimine uymayan veya eksik `number` alanı içeren isteklere `400 Bad Request` döner.

### POST /api/registration

```json
{"name":"Örnek Kullanıcı","email":"ornek@example.com","phone":"123321"}
```

- **201 Created:** Kayıt başarılı, `status: "accepted"`.
- **400 Bad Request:** Sayı doğrulama kurallarına uymuyor, `status: "denied"`.
- **409 Conflict:** Aynı numara önceden kayıtlı, `status: "denied"` ve açıklayıcı `message`.
- **500 Internal Server Error:** Beklenmeyen sunucu hatası, `status: "error"`.

Kayıt API'si verileri MySQL'de saklar. Web formu bu yanıtları kullanıcıya gösterir.

### GET /api/phone/count

`000000` ile `999999` arasındaki tüm altı basamaklı değerleri tarar ve kurallara uyanların sayısını döndürür: `6699`. 

## Testler

```bash
cd api
./mvnw test
```

Testler H2 bellek içi veritabanı kullanır; yerel MySQL kurulumu gerektirmez. GitHub Actions, PR açıldığında ve `main` güncellendiğinde API testlerini çalıştırır.

## Notlar

- Bu proje eğitim amaçlıdır ve telefon/SMS sahipliği doğrulaması yapmaz.
- Web formu API'yi aynı hostname üzerindeki `8080` portundan çağırır. Farklı domain/ters proxy ortamları için API adresi yapılandırılmalıdır.
- Form girdisinin altı rakam olması tek başına yeterli değildir; asıl doğrulama sunucuda yapılır.
