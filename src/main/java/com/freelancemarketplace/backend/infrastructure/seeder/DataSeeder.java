package com.freelancemarketplace.backend.infrastructure.seeder;

import com.freelancemarketplace.backend.category.domain.model.CategoryModel;
import com.freelancemarketplace.backend.category.infrastructure.repository.CategoriesRepository;
import com.freelancemarketplace.backend.client.domain.model.ClientModel;
import com.freelancemarketplace.backend.client.infrastructure.repository.ClientsRepository;
import com.freelancemarketplace.backend.freelancer.domain.model.Bio;
import com.freelancemarketplace.backend.freelancer.domain.model.FreelancerModel;
import com.freelancemarketplace.backend.freelancer.infrastructure.repository.FreelancersRepository;
import com.freelancemarketplace.backend.language.domain.model.LanguageModel;
import com.freelancemarketplace.backend.language.infrastructure.repository.LanguagesRepository;
import com.freelancemarketplace.backend.location.domain.model.LocationModel;
import com.freelancemarketplace.backend.location.infrastructure.repository.LocationsRepository;
import com.freelancemarketplace.backend.project.domain.enums.BudgetTypes;
import com.freelancemarketplace.backend.project.domain.enums.ProjectStatus;
import com.freelancemarketplace.backend.project.domain.model.BudgetModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectModel;
import com.freelancemarketplace.backend.project.domain.model.ProjectScope;
import com.freelancemarketplace.backend.project.infrastructure.repository.BudgetsRepository;
import com.freelancemarketplace.backend.project.infrastructure.repository.ProjectsRepository;
import com.freelancemarketplace.backend.skill.domain.model.SkillModel;
import com.freelancemarketplace.backend.skill.infrastructure.repository.SkillsRepository;
import com.freelancemarketplace.backend.user.domain.enums.AccountStatus;
import com.freelancemarketplace.backend.user.domain.enums.UserRoles;
import com.freelancemarketplace.backend.user.domain.model.UserModel;
import com.freelancemarketplace.backend.user.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserRepository userRepository;
    private final FreelancersRepository freelancersRepository;
    private final ClientsRepository clientsRepository;
    private final SkillsRepository skillsRepository;
    private final CategoriesRepository categoriesRepository;
    private final ProjectsRepository projectsRepository;
    private final BudgetsRepository budgetsRepository;
    private final LanguagesRepository languagesRepository;
    private final LocationsRepository locationsRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    @Profile("dev")
    public CommandLineRunner seedData() {
        return args -> {
            if (userRepository.count() > 0) {
                log.info("Database already has data, skipping seed");
                return;
            }
            log.info("Starting database seeding...");
            seedAll();
            log.info("Database seeding completed!");
        };
    }

    private void seedAll() {
        seedLocations();
        seedLanguages();
        seedSkills();
        seedCategories();
        seedFreelancers();
        seedClients();
        seedProjects();
    }

    private void seedLocations() {
        LocationModel loc1 = new LocationModel();
        loc1.setDetails("San Francisco, USA");
        locationsRepository.save(loc1);

        LocationModel loc2 = new LocationModel();
        loc2.setDetails("Hanoi, Vietnam");
        locationsRepository.save(loc2);

        LocationModel loc3 = new LocationModel();
        loc3.setDetails("London, UK");
        locationsRepository.save(loc3);
    }

    private void seedLanguages() {
        List<LanguageModel> languages = List.of(
            createLanguage("English", "en"),
            createLanguage("Vietnamese", "vi"),
            createLanguage("French", "fr"),
            createLanguage("Spanish", "es"),
            createLanguage("German", "de"),
            createLanguage("Japanese", "ja"),
            createLanguage("Chinese", "zh"),
            createLanguage("Korean", "ko")
        );
        languagesRepository.saveAll(languages);
    }

    private LanguageModel createLanguage(String name, String code) {
        LanguageModel lang = new LanguageModel();
        lang.setLanguageName(name);
        lang.setIsoCode(code);
        lang.setIsActived(true);
        return lang;
    }

    private void seedSkills() {
        List<SkillModel> skills = List.of(
            createSkill("Java", "Java programming language"),
            createSkill("Python", "Python programming language"),
            createSkill("JavaScript", "JavaScript programming language"),
            createSkill("TypeScript", "TypeScript programming language"),
            createSkill("React", "React.js frontend framework"),
            createSkill("Angular", "Angular frontend framework"),
            createSkill("Vue.js", "Vue.js frontend framework"),
            createSkill("Node.js", "Node.js backend runtime"),
            createSkill("Spring Boot", "Spring Boot Java framework"),
            createSkill("Django", "Django Python framework"),
            createSkill("Flutter", "Flutter cross-platform framework"),
            createSkill("React Native", "React Native mobile framework"),
            createSkill("AWS", "Amazon Web Services"),
            createSkill("Docker", "Docker containerization"),
            createSkill("Kubernetes", "Kubernetes orchestration"),
            createSkill("PostgreSQL", "PostgreSQL database"),
            createSkill("MongoDB", "MongoDB NoSQL database"),
            createSkill("GraphQL", "GraphQL API"),
            createSkill("REST API", "REST API development"),
            createSkill("Machine Learning", "Machine learning and AI"),
            createSkill("UI/UX Design", "User interface and experience design"),
            createSkill("Figma", "Figma design tool"),
            createSkill("Adobe XD", "Adobe XD design tool"),
            createSkill("Content Writing", "Content creation and writing"),
            createSkill("SEO", "Search engine optimization")
        );
        skillsRepository.saveAll(skills);
    }

    private SkillModel createSkill(String name, String description) {
        SkillModel skill = new SkillModel();
        skill.setName(name);
        skill.setDescription(description);
        return skill;
    }

    private void seedCategories() {
        List<SkillModel> allSkills = skillsRepository.findAll();
        
        List<CategoryModel> categories = List.of(
            createCategory("Web Development", "https://example.com/web.png", getSkillsByNames(allSkills, "Java", "Python", "JavaScript", "TypeScript", "React", "Angular", "Vue.js", "Node.js", "Spring Boot", "Django")),
            createCategory("Mobile Development", "https://example.com/mobile.png", getSkillsByNames(allSkills, "Flutter", "React Native", "Java", "Swift")),
            createCategory("Cloud & DevOps", "https://example.com/cloud.png", getSkillsByNames(allSkills, "AWS", "Docker", "Kubernetes", "Python", "JavaScript")),
            createCategory("Data Science & ML", "https://example.com/data.png", getSkillsByNames(allSkills, "Python", "Machine Learning", "AWS")),
            createCategory("UI/UX Design", "https://example.com/design.png", getSkillsByNames(allSkills, "UI/UX Design", "Figma", "Adobe XD", "React")),
            createCategory("Content Writing", "https://example.com/content.png", getSkillsByNames(allSkills, "Content Writing", "SEO"))
        );
        categoriesRepository.saveAll(categories);
    }

    private CategoryModel createCategory(String name, String image, Set<SkillModel> skills) {
        CategoryModel category = new CategoryModel();
        category.setName(name);
        category.setImage(image);
        category.setSkills(skills);
        return category;
    }

    private Set<SkillModel> getSkillsByNames(List<SkillModel> allSkills, String... names) {
        Set<SkillModel> result = new HashSet<>();
        for (String name : names) {
            allSkills.stream()
                .filter(s -> s.getName().equals(name))
                .findFirst()
                .ifPresent(result::add);
        }
        return result;
    }

    private void seedFreelancers() {
        List<SkillModel> allSkills = skillsRepository.findAll();
        List<LocationModel> locations = locationsRepository.findAll();
        LanguageModel english = languagesRepository.findByIsoCode("en");

        List<FreelancerModel> freelancers = List.of(
            createFreelancer("john", "john@example.com", "John", "Doe", "Senior Java Developer",
                85.0, getSkillsByNames(allSkills, "Java", "Spring Boot", "AWS", "PostgreSQL"), english, locations.get(0),
                "Experienced backend developer with 8+ years building enterprise applications."),
            
            createFreelancer("sarah", "sarah@example.com", "Sarah", "Smith", "Full Stack Developer",
                75.0, getSkillsByNames(allSkills, "JavaScript", "React", "Node.js", "TypeScript", "MongoDB"), english, locations.get(0),
                "Full stack developer passionate about creating beautiful and functional web apps."),
            
            createFreelancer("minh", "minh@example.com", "Minh", "Tran", "Python Data Scientist",
                65.0, getSkillsByNames(allSkills, "Python", "Machine Learning", "AWS", "PostgreSQL"), english, locations.get(1),
                "Data scientist specializing in machine learning and predictive analytics."),
            
            createFreelancer("emma", "emma@example.com", "Emma", "Wilson", "UI/UX Designer",
                60.0, getSkillsByNames(allSkills, "UI/UX Design", "Figma", "Adobe XD", "React"), english, locations.get(2),
                "Creative designer focused on user-centered design principles."),
            
            createFreelancer("alex", "alex@example.com", "Alex", "Johnson", "Mobile Developer",
                70.0, getSkillsByNames(allSkills, "Flutter", "React Native", "Java", "AWS"), english, locations.get(0),
                "Mobile developer building cross-platform apps with excellent user experience."),
            
            createFreelancer("lisa", "lisa@example.com", "Lisa", "Brown", "DevOps Engineer",
                80.0, getSkillsByNames(allSkills, "Docker", "Kubernetes", "AWS", "Python", "Java"), english, locations.get(2),
                "DevOps engineer with expertise in cloud infrastructure and CI/CD pipelines."),
            
            createFreelancer("david", "david@example.com", "David", "Lee", "React Developer",
                55.0, getSkillsByNames(allSkills, "React", "JavaScript", "TypeScript", "Node.js"), english, locations.get(1),
                "Frontend developer specializing in React and modern JavaScript frameworks."),
            
            createFreelancer("nina", "nina@example.com", "Nina", "Patel", "Content Writer",
                35.0, getSkillsByNames(allSkills, "Content Writing", "SEO"), english, locations.get(0),
                "Professional content writer creating engaging and SEO-optimized content.")
        );

        freelancersRepository.saveAll(freelancers);
        log.info("Created {} freelancers", freelancers.size());
    }

    private FreelancerModel createFreelancer(String username, String email, String firstName, 
            String lastName, String title, double hourlyRate, Set<SkillModel> skills, 
            LanguageModel language, LocationModel location, String bioSummary) {
        
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(firstName + " " + lastName);
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setRole(UserRoles.FREELANCER);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user = userRepository.save(user);

        FreelancerModel freelancer = new FreelancerModel();
        freelancer.setUser(user);
        freelancer.setFreelancerId(user.getUserId());
        freelancer.setFirstName(firstName);
        freelancer.setLastName(lastName);
        freelancer.setTitle(title);
        freelancer.setHourlyRate(hourlyRate);
        freelancer.setSkills(skills);
        freelancer.setOnboardingCompleted(true);
        freelancer.setWallet(0.0);
        freelancer.setConnections(0);
        freelancer.setHoursPerWeek(40);
        freelancer.setLocation(location);

        Set<LanguageModel> languages = new HashSet<>();
        if (language != null) languages.add(language);
        freelancer.setLanguages(languages);

        Bio bio = new Bio();
        bio.setSummary(bioSummary);
        freelancer.setBio(bio);

        return freelancer;
    }

    private void seedClients() {
        List<LocationModel> locations = locationsRepository.findAll();
        
        List<ClientModel> clients = List.of(
            createClient("techstartup", "contact@techstartup.com", "Tech", "Startup", locations.get(0),
                "We are a growing tech startup building innovative products."),
            createClient("ecommerceco", "hello@ecommerceco.com", "E-Commerce", "Co", locations.get(0),
                "Leading e-commerce platform serving millions of customers."),
            createClient("fintechpro", "info@fintechpro.com", "FinTech", "Pro", locations.get(1),
                "Financial technology company revolutionizing banking."),
            createClient("mediagroup", "team@mediagroup.com", "Media", "Group", locations.get(2),
                "Digital media company creating engaging content."),
            createClient("retailgiant", "biz@retailgiant.com", "Retail", "Giant", locations.get(1),
                "Global retail company with online presence.")
        );

        clientsRepository.saveAll(clients);
        log.info("Created {} clients", clients.size());
    }

    private ClientModel createClient(String username, String email, String firstName, 
            String lastName, LocationModel location, String bioSummary) {
        
        UserModel user = new UserModel();
        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(firstName + " " + lastName);
        user.setPasswordHash(passwordEncoder.encode("password123"));
        user.setRole(UserRoles.CLIENT);
        user.setAccountStatus(AccountStatus.ACTIVE);
        user = userRepository.save(user);

        ClientModel client = new ClientModel();
        client.setUser(user);
        client.setClientId(user.getUserId());
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(email);
        client.setUsername(username);
        client.setIsVerified(true);
        client.setIsBlocked(false);
        client.setLocation(location);

        Bio bio = new Bio();
        bio.setSummary(bioSummary);
        client.setBio(bio);

        return client;
    }

    private void seedProjects() {
        List<ClientModel> clients = clientsRepository.findAll();
        List<CategoryModel> categories = categoriesRepository.findAll();
        List<SkillModel> allSkills = skillsRepository.findAll();
        
        if (clients.isEmpty() || categories.isEmpty()) {
            log.warn("Cannot seed projects: no clients or categories found");
            return;
        }

        List<ProjectModel> projects = List.of(
            createProject(clients.get(0), categories.get(0), "E-commerce Platform Development",
                "Build a modern e-commerce platform with React frontend and Node.js backend. " +
                "Features include product catalog, shopping cart, payment integration, and admin dashboard.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("5000"), new BigDecimal("10000"),
                "3 to 6 months", "Intermediate", "Full-time",
                getSkillsByNames(allSkills, "React", "Node.js", "JavaScript", "PostgreSQL", "AWS")),
            
            createProject(clients.get(1), categories.get(1), "Mobile App for Food Delivery",
                "Develop a cross-platform mobile app for food delivery service. " +
                "Include restaurant listing, ordering, tracking, and payment features.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("8000"), new BigDecimal("15000"),
                "1 to 3 months", "Expert", "Full-time",
                getSkillsByNames(allSkills, "Flutter", "React Native", "AWS")),
            
            createProject(clients.get(2), categories.get(2), "Cloud Infrastructure Setup",
                "Set up and migrate our infrastructure to AWS. " +
                "Implement CI/CD pipelines, auto-scaling, and monitoring.",
                ProjectStatus.OPEN, BudgetTypes.HOURLY_RATE, new BigDecimal("50"), new BigDecimal("100"),
                "1 to 3 months", "Intermediate", "Part-time",
                getSkillsByNames(allSkills, "AWS", "Docker", "Kubernetes", "Python")),
            
            createProject(clients.get(0), categories.get(3), "Machine Learning Model for Recommendations",
                "Build a recommendation engine using machine learning. " +
                "Analyze user behavior and suggest relevant products.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("12000"), new BigDecimal("20000"),
                "3 to 6 months", "Expert", "Full-time",
                getSkillsByNames(allSkills, "Python", "Machine Learning", "AWS")),
            
            createProject(clients.get(3), categories.get(4), "Website Redesign",
                "Redesign our corporate website with modern UI/UX. " +
                "Focus on mobile responsiveness and accessibility.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("3000"), new BigDecimal("5000"),
                "1 to 3 months", "Intermediate", "Part-time",
                getSkillsByNames(allSkills, "UI/UX Design", "Figma", "React")),
            
            createProject(clients.get(4), categories.get(0), "API Development for Inventory System",
                "Develop RESTful APIs for inventory management system. " +
                "Support CRUD operations, reporting, and integrations.",
                ProjectStatus.OPEN, BudgetTypes.HOURLY_RATE, new BigDecimal("40"), new BigDecimal("80"),
                "1 to 3 months", "Intermediate", "Full-time",
                getSkillsByNames(allSkills, "Java", "Spring Boot", "PostgreSQL", "REST API")),
            
            createProject(clients.get(1), categories.get(5), "SEO Content for Blog",
                "Write SEO-optimized blog posts about e-commerce trends, " +
                "shopping tips, and product reviews. 20 articles per month.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("1000"), new BigDecimal("2000"),
                "1 to 3 months", "Entry level", "Part-time",
                getSkillsByNames(allSkills, "Content Writing", "SEO")),
            
            createProject(clients.get(2), categories.get(0), "Payment Gateway Integration",
                "Integrate multiple payment gateways (Stripe, PayPal) into existing platform. " +
                "Handle transactions, refunds, and webhooks.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("4000"), new BigDecimal("7000"),
                "1 to 3 months", "Intermediate", "Full-time",
                getSkillsByNames(allSkills, "JavaScript", "Node.js", "REST API")),
            
            createProject(clients.get(3), categories.get(2), "Docker Containerization",
                "Containerize existing applications using Docker. " +
                "Set up development and production environments.",
                ProjectStatus.OPEN, BudgetTypes.HOURLY_RATE, new BigDecimal("45"), new BigDecimal("90"),
                "Less than 1 month", "Intermediate", "Part-time",
                getSkillsByNames(allSkills, "Docker", "Kubernetes", "AWS")),
            
            createProject(clients.get(4), categories.get(4), "Dashboard UI Design",
                "Design a data visualization dashboard for analytics. " +
                "Include charts, graphs, and real-time data displays.",
                ProjectStatus.OPEN, BudgetTypes.FIXED_PRICE, new BigDecimal("2500"), new BigDecimal("4000"),
                "1 to 3 months", "Intermediate", "Part-time",
                getSkillsByNames(allSkills, "UI/UX Design", "Figma", "React"))
        );

        projectsRepository.saveAll(projects);
        
        for (ProjectModel project : projects) {
            BudgetModel budget = new BudgetModel();
            budget.setProject(project);
            budget.setType(project.getBudget().getType());
            budget.setMinValue(project.getBudget().getMinValue());
            budget.setMaxValue(project.getBudget().getMaxValue());
            budget.setCurrencyUnit("USD");
            budgetsRepository.save(budget);
        }
        
        log.info("Created {} projects", projects.size());
    }

    private ProjectModel createProject(ClientModel client, CategoryModel category, String title,
            String description, ProjectStatus status, BudgetTypes budgetType,
            BigDecimal minValue, BigDecimal maxValue, String duration, String level,
            String workload, Set<SkillModel> skills) {
        
        ProjectModel project = new ProjectModel();
        project.setTitle(title);
        project.setDescription(description);
        project.setStatus(status);
        project.setClient(client);
        project.setCategory(category);
        project.setSkills(skills);
        project.setConnections(0);
        project.setIsInternship(false);
        project.setCreatedAt(Timestamp.valueOf(LocalDateTime.now().minusDays((long)(Math.random() * 30))));
        project.setUpdatedAt(Timestamp.valueOf(LocalDateTime.now()));

        ProjectScope scope = new ProjectScope();
        scope.setDuration(duration);
        scope.setLevel(level);
        scope.setWorkload(workload);
        project.setScope(scope);

        BudgetModel budget = new BudgetModel();
        budget.setType(budgetType);
        budget.setMinValue(minValue);
        budget.setMaxValue(maxValue);
        budget.setCurrencyUnit("USD");
        project.setBudget(budget);

        return project;
    }
}
