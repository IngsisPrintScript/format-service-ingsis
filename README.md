# Format Service

Servicio Spring Boot (Java 22, Gradle) con Postgres para procesamiento de documentos. Incluye formatter (Spotless), linter (Checkstyle), cobertura (JaCoCo) y CI (GitHub Actions).

## Requisitos

- Java 22 (Temurin recomendado)
- Docker y Docker Compose
- Git

## Servicios locales

- **Postgres 16** en docker-compose.yml (usuario `app`, password `app`, DB `formats`)
- **Adminer** en http://localhost:8080

## Configuración de aplicación

Archivo `src/main/resources/application.yml` apunta a Postgres local. Variables override opcionales:

- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`

## Ejecutar para levantar

```bash
# Docker
docker compose up -d

# Spring (puerto 8083)
./gradlew bootRun
```

## Builds

```bash
# Compilar y testear
./gradlew build

# Sólo formatear código
./gradlew spotlessApply
```

## Formatter / Linter / Coverage

```bash
# Verificar formato
./gradlew spotlessCheck

# Linter (Checkstyle)
./gradlew checkstyleMain

# Cobertura (JaCoCo)
# Reporte HTML: build/reports/jacoco/test/html/index.html
./gradlew jacocoTestReport

# Verificación (umbral actual 30% para desarrollo inicial)
./gradlew jacocoTestCoverageVerification
```

## Hooks de Git

```bash
# Instalar hooks locales
git config core.hooksPath .githooks
```

Hooks incluidos:
- **pre-commit**: aplica Spotless y re-stagea cambios
- **pre-push**: ejecuta tests

## CI (GitHub Actions)

- Workflow en `.github/workflows/ci.yml`
- Dispara en push/PR a `dev` y `main`
- Ejecuta `./gradlew clean build`

## Base de datos (Docker)

```bash
# Levantar Postgres
docker compose up -d postgres

# Adminer
docker compose up -d adminer
# → http://localhost:8080
```

## Configuración específica

- **Puerto de aplicación**: 8083
- **Base de datos**: `formats`
- **Package**: `com.ingsis.format`
- **Descripción**: "Format Service for document processing"

## Troubleshooting

- Si falla `spotless*`: ejecutar `./gradlew spotlessApply`
- Si falla cobertura en check: `./gradlew test jacocoTestReport` y abrir el reporte HTML para identificar clases sin cubrir
- Si Docker no levanta: verificar que Docker esté corriendo y puertos disponibles
- Warning de conexión DB: normal si PostgreSQL no está ejecutándose

## Próximos pasos

1. Implementar lógica de negocio específica para format service
2. Agregar dependencias específicas (ej: Apache POI para documentos)
3. Configurar endpoints REST según requerimientos
4. Implementar tests unitarios e integración
5. Configurar logging apropiado
6. Documentar API con OpenAPI/Swagger