# Module20 — API Automation (RestAssured + TestNG + Allure)

## Menjalankan test
```bash
./gradlew test
```
Report Allure otomatis dibuat setelah test selesai (baik lulus maupun gagal).

- Raw result: `build/allure-results`
- Report HTML: `build/reports/allure-report/allureReport/index.html`

## Membuka report
```bash
./gradlew allureServe          # buka report di browser
# atau buka langsung file index.html di atas
```

Report saja tanpa menjalankan ulang test:
```bash
./gradlew allureReport
```
