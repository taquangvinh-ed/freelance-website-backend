# SOLID Refactoring - Best Practices & Migration Checklist

## Directory Structure (New)

```
src/main/java/com/freelancemarketplace/backend/
├── api/                          # HTTP layer
│   ├── controller/              # REST controllers (thin layer)
│   ├── request/                 # Input DTOs
│   └── response/                # Output DTOs
│
├── application/                  # Application/Business logic layer
│   ├── service/                 # Service interfaces & implementations
│   │   ├── ProjectCRUDService.java
│   │   ├── ProjectSearchService.java
│   │   ├── ProjectStatisticsService.java
│   │   ├── ProjectSkillService.java
│   │   ├── ProjectEmbeddingService.java
│   │   ├── FreelancerProfileService.java
│   │   ├── ClientProfileService.java
│   │   ├── AIProjectSuggestionService.java
│   │   ├── PricingEngineService.java
│   │   ├── AIPromptService.java
│   │   └── AIValidationService.java
│   │
│   └── port/                    # Port interfaces (abstractions)
│       ├── BaseCrudPort.java
│       ├── ProjectCrudPort.java
│       ├── FreelancerCrudPort.java
│       ├── ClientCrudPort.java
│       ├── LLMPort.java
│       ├── CloudStoragePort.java
│       └── EmailPort.java
│
├── domain/                       # Domain layer (entities, value objects)
│   ├── model/                   # Entity models
│   ├── exception/               # Domain exceptions
│   ├── enums/                   # Domain enums
│   └── specification/           # JPA specifications
│
├── infrastructure/              # Infrastructure/Technical layer
│   ├── adapter/                 # Port implementations (adapters)
│   │   ├── ClaudeLLMAdapter.java
│   │   ├── OpenAILLMAdapter.java (future)
│   │   ├── GeminiLLMAdapter.java (future)
│   │   ├── CloudinaryStorageAdapter.java
│   │   ├── S3StorageAdapter.java (future)
│   │   └── AzureBlobStorageAdapter.java (future)
│   │
│   ├── persistence/             # Repository adapters
│   │   ├── ProjectRepositoryAdapter.java
│   │   ├── FreelancerRepositoryAdapter.java
│   │   └── ClientRepositoryAdapter.java
│   │
│   └── config/                  # Spring configuration
│       ├── PortAdapterConfiguration.java
│       └── SecurityConfiguration.java
│
├── service/                     # Service implementations (legacy - being refactored)
│   ├── impl/
│   │   ├── ProjectCRUDServiceImpl.java (NEW)
│   │   ├── ProjectSearchServiceImpl.java (NEW)
│   │   ├── ProjectStatisticsServiceImpl.java (NEW)
│   │   ├── ProjectSkillServiceImpl.java (NEW)
│   │   ├── ProjectEmbeddingServiceImpl.java (NEW)
│   │   ├── FreelancerProfileServiceImpl.java (NEW)
│   │   └── LLMServiceImp.java (being refactored)
│   │
│   └── (old large monolithic services being split)
│
├── mapper/                      # MapStruct mappers
│   └── ProjectMapper.java
│
├── repository/                  # JPA repositories (being abstracted)
│   ├── ProjectsRepository.java
│   ├── FreelancerRepository.java
│   └── ClientsRepository.java
│
└── (other folders)
```

---

## Migration Checklist

### Phase 1: Foundation ✅
- [x] Create port interfaces in `application/port/`
- [x] Create service interfaces in `application/service/`
- [x] Create adapter implementations in `infrastructure/adapter/`
- [x] Create repository adapters in `infrastructure/persistence/`
- [x] Create configuration class in `infrastructure/config/`

### Phase 2: Service Implementation ✅
- [x] ProjectCRUDServiceImpl
- [x] ProjectSearchServiceImpl
- [x] ProjectStatisticsServiceImpl
- [x] ProjectSkillServiceImpl
- [x] ProjectEmbeddingServiceImpl
- [x] FreelancerProfileServiceImpl
- [ ] ClientProfileServiceImpl
- [ ] AIProjectSuggestionServiceImpl
- [ ] PricingEngineServiceImpl
- [ ] AIPromptServiceImpl
- [ ] AIValidationServiceImpl

### Phase 3: Controller Updates
- [ ] ProjectController - Use new services
- [ ] FreelancerController - Use new services
- [ ] ClientController - Use new services
- [ ] AIProjectSuggestionController - Use new services
- [ ] All other controllers

### Phase 4: Testing
- [ ] Unit tests for all services
- [ ] Integration tests
- [ ] End-to-end tests
- [ ] Performance tests

### Phase 5: Documentation
- [x] Architecture documentation
- [x] Implementation guide
- [ ] API documentation update
- [ ] Deployment guide

### Phase 6: Cleanup
- [ ] Remove duplicate exception handlers
- [ ] Clean up old service implementations
- [ ] Archive legacy code
- [ ] Final code review

---

## Best Practices

### 1. Constructor Injection (DI)

**✅ DO:**
```java
@Service
public class ProjectCRUDServiceImpl implements ProjectCRUDService {
    private final ProjectCrudPort projectCrudPort;
    private final ProjectMapper projectMapper;
    
    public ProjectCRUDServiceImpl(ProjectCrudPort projectCrudPort,
                                 ProjectMapper projectMapper) {
        this.projectCrudPort = projectCrudPort;
        this.projectMapper = projectMapper;
    }
}
```

**❌ DON'T:**
```java
@Service
public class ProjectCRUDServiceImpl implements ProjectCRUDService {
    @Autowired
    private ProjectCrudPort projectCrudPort;
    
    @Autowired
    private ProjectMapper projectMapper;
    // Field injection - allows null pointer exceptions
}
```

**❌ DON'T:**
```java
@Service
@AllArgsConstructor  // Too implicit, hard to refactor
public class ProjectCRUDServiceImpl implements ProjectCRUDService {
    private final ProjectCrudPort projectCrudPort;
    private final ProjectMapper projectMapper;
}
```

---

### 2. Interface Segregation

**✅ DO:**
```java
// Small, focused interface
public interface ProjectCRUDService {
    ProjectDTO createProject(Long clientId, CreateProjectRequest request);
    ProjectDTO getProjectById(Long projectId);
    ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO);
    void deleteProject(Long projectId);
}

// Clients depend only on CRUD methods
// If they need search, they inject ProjectSearchService separately
```

**❌ DON'T:**
```java
// Large interface forcing clients to implement/depend on everything
public interface ProjectService {
    ProjectDTO createProject(...);
    ProjectDTO updateProject(...);
    void deleteProject(...);
    List<ProjectDTO> getAllProject();
    Page<ProjectDTO> filter(...);
    long countAllProjects();
    long getNewProjectCountToday();
    // ... 10+ more methods
}
```

---

### 3. Error Handling

**✅ DO:**
```java
@Transactional
public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
    // Validate input
    if (request == null) {
        throw new BaseApplicationException(
                ErrorCode.INVALID_REQUEST,
                HttpStatus.BAD_REQUEST,
                "Project request cannot be null"
        );
    }
    
    // Find related entity with proper error handling
    ClientModel client = clientsRepository.findById(clientId)
            .orElseThrow(() -> new BaseApplicationException(
                    ErrorCode.USER_NOT_FOUND,
                    HttpStatus.NOT_FOUND,
                    "Client not found with id: " + clientId
            ));
    
    // Business logic
    ProjectModel project = projectMapper.toEntity(request);
    ProjectModel saved = projectCrudPort.save(project);
    
    return projectMapper.toDto(saved);
}
```

**❌ DON'T:**
```java
public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
    // No validation
    ClientModel client = clientsRepository.findById(clientId).get(); // NPE risk
    
    ProjectModel project = projectMapper.toEntity(request);
    ProjectModel saved = projectsRepository.save(project);
    
    return projectMapper.toDto(saved);
}
```

---

### 4. Logging

**✅ DO:**
```java
@Slf4j
public class ProjectCRUDServiceImpl {
    @Transactional
    public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
        log.info("Creating new project for client: {}", clientId);
        
        try {
            ProjectModel saved = projectCrudPort.save(project);
            log.info("Project created successfully with id: {}", saved.getId());
            return projectMapper.toDto(saved);
        } catch (Exception e) {
            log.error("Error creating project for client: {}", clientId, e);
            throw e;
        }
    }
}
```

**❌ DON'T:**
```java
public class ProjectCRUDServiceImpl {
    public ProjectDTO createProject(Long clientId, CreateProjectRequest request) {
        ProjectModel saved = projectCrudPort.save(project);
        return projectMapper.toDto(saved);
    }
}
```

---

### 5. Configuration Management

**✅ DO:**
```java
@Configuration
public class PortAdapterConfiguration {
    @Bean
    public LLMPort llmPort(@Value("${llm.provider}") String provider,
                           ClaudeLLMAdapter claudeAdapter,
                           OpenAILLMAdapter openaiAdapter) {
        return "claude".equals(provider) ? claudeAdapter : openaiAdapter;
    }
}
```

**❌ DON'T:**
```java
@Service
public class ProjectService {
    private final LLMPort llmPort = new ClaudeLLMAdapter(); // Hard-coded
}
```

---

### 6. Testing

**✅ DO:**
```java
@Test
public void testCreateProject() {
    // Mock the dependencies
    ProjectCrudPort mockPort = mock(ProjectCrudPort.class);
    ProjectMapper mockMapper = mock(ProjectMapper.class);
    
    // Create service with mocks
    ProjectCRUDService service = new ProjectCRUDServiceImpl(mockPort, mockMapper);
    
    // Setup expectations
    when(mockMapper.toEntity(any())).thenReturn(new ProjectModel());
    when(mockPort.save(any())).thenReturn(new ProjectModel(1L, ...));
    when(mockMapper.toDto(any())).thenReturn(new ProjectDTO(1L, ...));
    
    // Execute
    ProjectDTO result = service.createProject(1L, request);
    
    // Verify
    assertNotNull(result);
    assertEquals(1L, result.getId());
    verify(mockPort).save(any());
}
```

**❌ DON'T:**
```java
@Test
public void testCreateProject() {
    // Tests using real database
    ProjectDTO result = projectService.createProject(1L, request);
    // Slow, flaky, hard to isolate
}
```

---

## Code Review Checklist

When reviewing refactored code:

- [ ] Does it implement single responsibility?
- [ ] Are dependencies injected (not hard-coded)?
- [ ] Are exceptions properly caught and handled?
- [ ] Is logging present for debugging?
- [ ] Are tests written for business logic?
- [ ] Does it follow naming conventions?
- [ ] Are there code smells (long methods, deep nesting)?
- [ ] Is documentation updated?
- [ ] Are performance implications considered?
- [ ] Is error handling consistent?

---

## Common Pitfalls to Avoid

### 1. Circular Dependencies
```java
// ❌ BAD - Circular dependency
public interface ServiceA {
    void doA();
}

public interface ServiceB {
    void doB();
}

@Service
public class ServiceAImpl implements ServiceA {
    @Autowired private ServiceB serviceB; // ServiceB depends on ServiceA
}

@Service
public class ServiceBImpl implements ServiceB {
    @Autowired private ServiceA serviceA;
}
```

**Solution:** Inject common dependency:
```java
@Service
public class ServiceAImpl implements ServiceA {
    @Autowired private CommonRepository repo;
}

@Service
public class ServiceBImpl implements ServiceB {
    @Autowired private CommonRepository repo;
}
```

### 2. God Services
```java
// ❌ BAD - Service doing too much
@Service
public class ProjectService {
    public ProjectDTO create(...) { }
    public ProjectDTO update(...) { }
    public Page<ProjectDTO> search(...) { }
    public long countTotal() { }
    public void assignSkill(...) { }
    public ProjectDTO uploadAttachment(...) { }
    // etc...
}
```

**Solution:** Split into focused services:
```java
@Service
public class ProjectCRUDService { create, update, delete }

@Service
public class ProjectSearchService { search, filter }

@Service
public class ProjectStatisticsService { count, stats }

@Service
public class ProjectSkillService { assignSkill, removeSkill }

@Service
public class ProjectAttachmentService { upload, download, delete }
```

### 3. Leaky Abstractions
```java
// ❌ BAD - Port exposes implementation details
public interface ProjectCrudPort {
    ProjectModel save(ProjectModel entity);
    List<ProjectModel> findAllByCustomQuery(String jpql); // JPA specific
}
```

**Solution:** Hide implementation:
```java
// ✅ GOOD - Port is implementation-agnostic
public interface ProjectCrudPort {
    ProjectModel save(ProjectModel entity);
    List<ProjectModel> findActiveProjects();
    List<ProjectModel> findByClientId(Long clientId);
}
```

### 4. Service Layer Anemia
```java
// ❌ BAD - Service doesn't do anything
@Service
public class ProjectService {
    public ProjectDTO createProject(CreateProjectRequest req) {
        ProjectModel model = mapper.toEntity(req);
        ProjectModel saved = repository.save(model);
        return mapper.toDto(saved);
    }
}
```

**Solution:** Add business logic:
```java
// ✅ GOOD - Service has business logic
@Service
public class ProjectCRUDService {
    public ProjectDTO createProject(Long clientId, CreateProjectRequest req) {
        // Validate client exists
        ClientModel client = clientPort.findById(clientId)
            .orElseThrow(...);
        
        // Create project
        ProjectModel project = mapper.toEntity(req);
        project.setClient(client);
        project.setStatus(ProjectStatus.OPEN);
        
        // Generate embeddings
        embeddingService.generateEmbeddings(project);
        
        // Save
        ProjectModel saved = projectCrudPort.save(project);
        
        return mapper.toDto(saved);
    }
}
```

---

## Performance Considerations

### 1. N+1 Query Problem
```java
// ❌ BAD - N+1 queries
List<ProjectDTO> projects = projectSearchService.getAllProjects();
for (ProjectDTO project : projects) {
    System.out.println(project.getClient().getName()); // Query per project
}

// ✅ GOOD - Eager loading
@Query("SELECT p FROM ProjectModel p JOIN FETCH p.client WHERE p.status = 'OPEN'")
List<ProjectModel> findActiveProjectsWithClient();
```

### 2. Lazy Loading Issues
```java
// ❌ BAD - Lazy loading outside transaction
ProjectDTO project = projectCRUDService.getProjectById(1L);
// Later, after transaction closed...
project.getClient().getName(); // LazyInitializationException

// ✅ GOOD - Access within transaction
@Transactional
public ProjectDTO getProjectWithClient(Long projectId) {
    ProjectModel project = projectCrudPort.findById(projectId).orElseThrow();
    project.getClient().getName(); // Safe, within transaction
    return mapper.toDto(project);
}
```

### 3. Caching
```java
// ✅ GOOD - Cache frequently accessed data
@Service
@CacheConfig(cacheNames = "projects")
public class ProjectSearchServiceImpl {
    @Cacheable(key = "#projectId")
    public ProjectDTO getProjectById(Long projectId) {
        return projectCrudPort.findById(projectId)
            .map(mapper::toDto)
            .orElseThrow(...);
    }
    
    @CacheEvict(key = "#projectId")
    public ProjectDTO updateProject(Long projectId, ProjectDTO dto) {
        // Update logic
    }
}
```

---

## Deployment Considerations

### 1. Breaking Changes
When rolling out refactored services:
- Use feature flags for gradual rollout
- Keep old services temporarily for fallback
- Version APIs if possible
- Maintain backward compatibility

### 2. Data Migration
If changing data structures:
- Create migration scripts
- Test migrations on staging
- Have rollback plan
- Monitor data integrity

### 3. Monitoring
Set up monitoring for:
- Service response times
- Error rates
- Database query performance
- Cache hit rates
- Memory usage

---

## Resources

- Clean Architecture: A Craftsman's Guide to Software Structure and Design
- The SOLID Principles Explained
- Ports and Adapters Architecture (Hexagonal Architecture)
- Domain-Driven Design by Eric Evans

