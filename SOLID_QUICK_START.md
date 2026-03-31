# SOLID Refactoring - Developer Quick Start Guide

## Quick Overview

The project has been refactored to follow SOLID principles. Here's what changed:

### Old Way
```
Large Services → Repositories → Database
```

### New Way
```
Controllers → Application Services → Ports → Adapters → Infrastructure
```

---

## For Frontend/Backend Developers

### Using New Services in Controllers

**Step 1: Inject Services**
```java
@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    // Inject the services you need
    private final ProjectCRUDService projectCRUDService;
    private final ProjectSearchService projectSearchService;
    private final ProjectStatisticsService projectStatsService;
    
    public ProjectController(ProjectCRUDService crud,
                            ProjectSearchService search,
                            ProjectStatisticsService stats) {
        this.projectCRUDService = crud;
        this.projectSearchService = search;
        this.projectStatsService = stats;
    }
}
```

**Step 2: Use Services in Endpoints**
```java
@PostMapping("/")
public ResponseEntity<ResponseDTO> createProject(
        @AuthenticationPrincipal AppUser appUser,
        @RequestBody CreateProjectRequest request) {
    try {
        ProjectDTO project = projectCRUDService.createProject(appUser.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(
                        ResponseStatusCode.SUCCESS,
                        ResponseMessage.SUCCESS,
                        project
                ));
    } catch (BaseApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ResponseDTO(
                        String.valueOf(e.getErrorCode().getCode()),
                        e.getMessage()
                ));
    }
}

@GetMapping("/search")
public ResponseEntity<ResponseDTO> searchProjects(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) List<String> skills,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
    try {
        Page<ProjectDTO> results = projectSearchService.searchProjects(
                keyword, skills, null, null, null, null, null, null,
                PageRequest.of(page, size)
        );
        return ResponseEntity.ok(new ResponseDTO(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                results
        ));
    } catch (BaseApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ResponseDTO(
                        String.valueOf(e.getErrorCode().getCode()),
                        e.getMessage()
                ));
    }
}

@GetMapping("/stats")
public ResponseEntity<ResponseDTO> getProjectStats() {
    try {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalProjects", projectStatsService.countAllProjects());
        stats.put("activeProjects", projectStatsService.getActiveProjectCount());
        stats.put("completedProjects", projectStatsService.getCompletedProjectCount());
        
        return ResponseEntity.ok(new ResponseDTO(
                ResponseStatusCode.SUCCESS,
                ResponseMessage.SUCCESS,
                stats
        ));
    } catch (BaseApplicationException e) {
        return ResponseEntity.status(e.getHttpStatus())
                .body(new ResponseDTO(
                        String.valueOf(e.getErrorCode().getCode()),
                        e.getMessage()
                ));
    }
}
```

---

## For Service Developers

### Creating a New Service (Step-by-Step)

**Step 1: Define Service Interface**
```java
// In application/service/
public interface MyNewService {
    void doSomething(String input);
}
```

**Step 2: Create Implementation**
```java
// In service/impl/
@Service
@Slf4j
public class MyNewServiceImpl implements MyNewService {
    
    private final SomeDependency dependency;
    
    // Constructor injection
    public MyNewServiceImpl(SomeDependency dependency) {
        this.dependency = dependency;
    }
    
    @Override
    public void doSomething(String input) {
        log.info("Doing something with: {}", input);
        // Business logic
    }
}
```

**Step 3: If you need external dependencies, create a Port**
```java
// In application/port/
public interface MyExternalPort {
    String callExternalService(String data);
}

// In infrastructure/adapter/
@Component
public class MyExternalAdapter implements MyExternalPort {
    @Override
    public String callExternalService(String data) {
        // Implementation
        return null;
    }
}

// In infrastructure/config/
@Configuration
public class PortAdapterConfiguration {
    @Bean
    public MyExternalPort myExternalPort(MyExternalAdapter adapter) {
        return adapter;
    }
}
```

**Step 4: Use the Port in your Service**
```java
@Service
public class MyNewServiceImpl implements MyNewService {
    
    private final MyExternalPort externalPort;
    
    public MyNewServiceImpl(MyExternalPort externalPort) {
        this.externalPort = externalPort;
    }
    
    @Override
    public void doSomething(String input) {
        String result = externalPort.callExternalService(input);
        // Use result
    }
}
```

---

## Common Tasks

### Task 1: Adding a New Endpoint

```java
// 1. Make sure you have the service
@RestController
public class MyController {
    private final MyService myService;
    
    public MyController(MyService myService) {
        this.myService = myService;
    }
    
    // 2. Create endpoint
    @GetMapping("/my-endpoint")
    public ResponseEntity<ResponseDTO> myEndpoint(@RequestParam String param) {
        try {
            Object result = myService.doSomething(param);
            return ResponseEntity.ok(new ResponseDTO(
                    ResponseStatusCode.SUCCESS,
                    ResponseMessage.SUCCESS,
                    result
            ));
        } catch (BaseApplicationException e) {
            return ResponseEntity.status(e.getHttpStatus())
                    .body(new ResponseDTO(
                            String.valueOf(e.getErrorCode().getCode()),
                            e.getMessage()
                    ));
        }
    }
}
```

### Task 2: Switching Cloud Storage Provider

Current: Cloudinary
Want to switch to: S3

**Step 1: Create S3 Adapter**
```java
@Component("s3StorageAdapter")
public class S3StorageAdapter implements CloudStoragePort {
    // S3-specific implementation
}
```

**Step 2: Update Configuration**
```java
@Configuration
public class PortAdapterConfiguration {
    @Bean
    public CloudStoragePort cloudStoragePort(S3StorageAdapter s3Adapter) {
        return s3Adapter;
    }
}
```

**Step 3: Done!** No changes needed in services using CloudStoragePort

### Task 3: Switching LLM Provider

Current: Claude
Want to switch to: OpenAI

**Step 1: Create OpenAI Adapter**
```java
@Component("openaiLLMAdapter")
public class OpenAILLMAdapter implements LLMPort {
    // OpenAI-specific implementation
}
```

**Step 2: Update Configuration**
```java
@Configuration
public class PortAdapterConfiguration {
    @Bean
    public LLMPort llmPort(OpenAILLMAdapter openaiAdapter) {
        return openaiAdapter;
    }
}
```

**Step 3: Done!** Services using LLMPort continue to work

---

## Testing Guide

### Unit Test Template

```java
public class MyServiceTest {
    
    // Mock your dependencies
    @Mock
    private MyDependency mockDependency;
    
    // Create service under test with mocks
    private MyService myService;
    
    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        myService = new MyServiceImpl(mockDependency);
    }
    
    @Test
    public void testHappyPath() {
        // Arrange
        when(mockDependency.getSomething()).thenReturn("value");
        
        // Act
        String result = myService.doSomething();
        
        // Assert
        assertEquals("expected", result);
        verify(mockDependency).getSomething();
    }
    
    @Test
    public void testErrorHandling() {
        // Arrange
        when(mockDependency.getSomething())
            .thenThrow(new RuntimeException("error"));
        
        // Act & Assert
        assertThrows(BaseApplicationException.class, 
            () -> myService.doSomething());
    }
}
```

### Integration Test Template

```java
@SpringBootTest
public class MyServiceIntegrationTest {
    
    @Autowired
    private MyService myService;
    
    @Autowired
    private MyRepository repository;
    
    @Test
    @Transactional
    public void testEndToEnd() {
        // Test with real database
        MyEntity entity = new MyEntity("test");
        MyDTO result = myService.createEntity(entity);
        
        assertNotNull(result.getId());
        assertTrue(repository.existsById(result.getId()));
    }
}
```

---

## Debugging Tips

### Enable Debug Logging
```properties
# application.properties
logging.level.com.freelancemarketplace=DEBUG
```

### Check Service Wiring
```bash
# Add this to startup to see all beans
# application.properties
logging.level.org.springframework.beans=DEBUG
```

### Test Locally
```bash
# Run single test
mvn test -Dtest=MyServiceTest#testMethod

# Run all tests
mvn test

# Run with coverage
mvn test jacoco:report
```

---

## File Structure Reference

When you need to find something:

| Need | Location |
|------|----------|
| Service interface | `application/service/` |
| Service implementation | `service/impl/` |
| Port interface | `application/port/` |
| Adapter implementation | `infrastructure/adapter/` |
| Repository adapter | `infrastructure/persistence/` |
| Configuration | `infrastructure/config/` |
| DTO | `dto/` |
| Request object | `request/` |
| Response object | `response/` |
| Controller | `controller/` |
| Entity model | `model/` |
| Exception | `exception/` |
| Mapper | `mapper/` |

---

## Troubleshooting

### Problem: "Could not autowire service"
**Solution:** Make sure service is annotated with `@Service` and is in component scan path.

### Problem: "No qualifying bean found"
**Solution:** Check `PortAdapterConfiguration` - make sure port is bound to implementation.

### Problem: Circular dependency
**Solution:** Extract common dependency or use interfaces to break cycle.

### Problem: NPE on null object
**Solution:** Always use `.orElseThrow()` instead of `.get()` for Optional.

### Problem: Lazy loading error
**Solution:** Use `@Transactional` annotation on service method.

### Problem: Transaction not rolling back
**Solution:** Make sure exception extends `RuntimeException` or is configured with `@Transactional(rollbackFor=...)`

---

## Quick Commands

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Run with test profile
mvn spring-boot:run -Dspring-boot.run.profiles=test

# Build Docker image
mvn clean package -DskipTests
docker build -t freelancer-backend .

# Check code quality
mvn sonar:sonar
```

---

## Documentation Files

- **SOLID_REFACTORING_PLAN.md** - Overall plan and strategy
- **SOLID_REFACTORING_IMPLEMENTATION.md** - Detailed implementation guide
- **SOLID_BEST_PRACTICES.md** - Best practices and patterns
- **This file** - Quick start for developers

---

## Getting Help

1. Check the documentation files
2. Look at existing implementations in same folder
3. Ask in team Slack/Chat
4. Create an issue with detailed description

---

Happy coding! 🚀

