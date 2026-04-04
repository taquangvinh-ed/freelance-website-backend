# SOLID Project Structure Reorganization Guide

## Current State vs Target State

### Current Structure (Messy)
```
backend/
├── application/              (NEW - partially filled)
│   ├── port/                (NEW)
│   └── service/             (NEW)
├── audit/                   (OLD - scattered)
├── auth/                    (OLD - scattered)
├── config/                  (OLD - mixed concerns)
├── controller/              (REST endpoints)
├── dto/                     (Data transfer objects)
├── enums/                   (Domain enums)
├── exception/               (Exception handling)
├── exceptionHandling/       (Duplicate of exception)
├── filter/                  (Security filters)
├── handler/                 (Handler classes)
├── infrastructure/          (NEW - partially filled)
│   ├── adapter/            (NEW)
│   ├── persistence/        (NEW)
│   └── config/             (NEW)
├── jwt/                     (JWT utilities)
├── mapper/                  (MapStruct mappers)
├── model/                   (Domain entities)
├── recommandation/          (Recommendation engine - typo!)
│   └── imp/                (Implementation)
├── repository/              (JPA repositories)
├── request/                 (Request DTOs)
├── response/                (Response DTOs)
├── service/                 (Business logic)
│   ├── impl/               (Implementations)
│   └── imp/                (DUPLICATE - typo!)
└── specification/           (JPA specifications)
```

### Target Structure (Clean & SOLID)
```
backend/
├── api/                                    # REST API layer
│   ├── controller/                        # HTTP endpoints
│   ├── request/                           # Input DTOs
│   └── response/                          # Response DTOs
│
├── application/                           # Business logic layer
│   ├── service/                          # Service interfaces
│   └── port/                             # Port/adapter interfaces
│
├── domain/                                # Domain layer (business entities)
│   ├── model/                            # Domain entities
│   ├── enums/                            # Domain enums
│   ├── exception/                        # Domain exceptions
│   └── specification/                    # JPA specifications
│
├── infrastructure/                        # Technical layer
│   ├── adapter/                          # Adapter implementations
│   ├── persistence/                      # Repository adapters
│   ├── config/                           # Spring configuration
│   ├── security/                         # Security configs (JWT, Auth)
│   └── external/                         # External service integrations
│
├── mapper/                                # DTO/Entity mappers
├── support/                               # Support utilities
│   ├── audit/                            # Audit utilities
│   ├── handler/                          # Event handlers
│   ├── filter/                           # HTTP filters
│   └── util/                             # Common utilities
│
└── FreelancerBackendApplication.java
```

---

## Migration Plan

### Phase 1: Create New Clean Structure (Safe)
1. Create new directories in `/domain` folder
2. Create new directories in `/infrastructure/security`
3. Create new directories in `/infrastructure/external`
4. Create new directories in `/support` folder
5. Create new directories in `/api` folder
6. **NO deletions yet** - just creating new structure

### Phase 2: Move/Copy Files (Non-Breaking)
1. Move exception files to `/domain/exception`
2. Move enums to `/domain/enums`
3. Move specifications to `/domain/specification`
4. Move models to `/domain/model`
5. Copy controllers to `/api/controller` (keep original as backup)
6. Copy DTOs to `/api/request` and `/api/response`
7. Move JWT files to `/infrastructure/security/jwt`
8. Move Auth files to `/infrastructure/security/auth`
9. Move Audit to `/support/audit`
10. Move Handlers to `/support/handler`
11. Move Filters to `/support/filter`

### Phase 3: Update Package Names (Test Thoroughly)
1. Update all imports in moved files
2. Update configuration classes to reference new packages
3. Run tests to ensure everything works
4. Update Spring component scanning if needed

### Phase 4: Archive Old Structure (Optional)
1. Keep old files as backup
2. Tag in git before deletion
3. Plan deletion for future sprint

---

## Detailed Migration Steps

### Step 1: Create Domain Layer
```bash
mkdir -p src/main/java/com/freelancemarketplace/backend/domain/{model,enums,exception,specification}
```

### Step 2: Create API Layer
```bash
mkdir -p src/main/java/com/freelancemarketplace/backend/api/{controller,request,response}
```

### Step 3: Create Support Layer
```bash
mkdir -p src/main/java/com/freelancemarketplace/backend/support/{audit,handler,filter,util}
```

### Step 4: Create Infrastructure/Security Layer
```bash
mkdir -p src/main/java/com/freelancemarketplace/backend/infrastructure/{security,external}
mkdir -p src/main/java/com/freelancemarketplace/backend/infrastructure/security/{jwt,auth}
```

---

## Files to Move/Reorganize

### To `/domain/model/` (from `/model/`)
- All `*Model.java` files
- Change: None (just location)

### To `/domain/enums/` (from `/enums/`)
- All enum files
- Change: Update package import

### To `/domain/exception/` (from `/exception/`)
- All exception files
- Merge with `/exceptionHandling/` if needed
- Remove duplicates

### To `/domain/specification/` (from `/specification/`)
- All `*Specification.java` files
- Change: Update package imports

### To `/api/controller/` (from `/controller/`)
- All `*Controller.java` files
- Change: Update package imports

### To `/api/request/` (from `/request/`)
- All request DTOs
- Change: Update package imports

### To `/api/response/` (from `/response/`)
- All response DTOs
- Change: Update package imports

### To `/infrastructure/security/jwt/` (from `/jwt/`)
- All JWT-related files
- Change: Update package imports

### To `/infrastructure/security/auth/` (from `/auth/`)
- All authentication-related files
- Change: Update package imports

### To `/support/audit/` (from `/audit/`)
- All audit-related files
- Change: Update package imports

### To `/support/handler/` (from `/handler/`)
- All handler classes
- Change: Update package imports

### To `/support/filter/` (from `/filter/`)
- All filter classes
- Change: Update package imports

### Duplicate/Remove
- `/exceptionHandling/` - Merge into `/domain/exception/`
- `/service/imp/` - Merge into `/service/impl/`
- `/recommandation/` - Rename to `/recommendation/` and organize

---

## Updated Package Structure

```
com.freelancemarketplace.backend
├── api
│   ├── controller
│   ├── request
│   └── response
├── application
│   ├── service        # Interfaces
│   └── port          # Port interfaces
├── domain
│   ├── model
│   ├── enums
│   ├── exception
│   └── specification
├── infrastructure
│   ├── adapter
│   ├── persistence
│   ├── config
│   ├── security
│   │   ├── jwt
│   │   └── auth
│   └── external
├── mapper
├── service           # Legacy - being refactored
│   └── impl
├── repository        # JPA repositories
└── support
    ├── audit
    ├── handler
    ├── filter
    └── util
```

---

## Spring Configuration Updates

### Update Component Scanning
Before:
```java
@SpringBootApplication
public class FreelancerBackendApplication { }
```

After (if needed):
```java
@SpringBootApplication(scanBasePackages = {
    "com.freelancemarketplace.backend.api",
    "com.freelancemarketplace.backend.application",
    "com.freelancemarketplace.backend.infrastructure",
    "com.freelancemarketplace.backend.support",
    "com.freelancemarketplace.backend.service"  // Legacy until refactored
})
public class FreelancerBackendApplication { }
```

### Update Configuration Classes
Move and update:
- `config/` → `infrastructure/config/`
- Update all package references

---

## Expected Benefits

✅ **Clarity** - Know where to find any file
✅ **Modularity** - Each layer has clear responsibility  
✅ **Testability** - Can test layers independently
✅ **Maintainability** - Easy to navigate and modify
✅ **SOLID Compliance** - Structure enforces principles
✅ **Team Onboarding** - New devs understand structure quickly
✅ **Scalability** - Easy to split into services later

---

## Implementation Timeline

- **Day 1**: Create new directories
- **Day 2**: Copy and update files
- **Day 3**: Update all imports and configuration
- **Day 4**: Run tests and validation
- **Day 5**: Documentation and training
- **Later**: Archive old structure

---

## Rollback Plan

If issues arise:
1. Git revert to restore original structure
2. Keep backup branch of refactored version
3. Identify problematic imports
4. Fix and re-test
5. Re-commit

---

## Quality Checks

Before committing:
- [ ] All files copied to new locations
- [ ] All imports updated
- [ ] No duplicate packages
- [ ] Spring context loads without errors
- [ ] All tests pass
- [ ] IDE recognizes all packages
- [ ] No circular dependencies
- [ ] Documentation updated

