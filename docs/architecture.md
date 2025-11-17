# Architecture MVC - Product Management

## 📋 Table des matières
1. [Vue d'ensemble](#vue-densemble)
2. [Pattern MVC](#pattern-mvc)
3. [Structure des couches](#structure-des-couches)
4. [Flux de requête](#flux-de-requête)
5. [Composants détaillés](#composants-détaillés)
6. [Flux de données](#flux-de-données)
7. [Diagrammes](#diagrammes)
8. [Principes SOLID appliqués](#principes-solid-appliqués)
9. [Bonnes pratiques](#bonnes-pratiques)

---

## Vue d'ensemble

L'application **Product Management** suit le pattern architecturale **MVC (Model-View-Controller)** avec une séparation claire des responsabilités. L'application est construite avec Spring Boot 3.5.7 et utilise une architecture multi-couches pour assurer la maintenabilité et la scalabilité.

### Caractéristiques principales
- ✅ Architecture MVC classique
- ✅ Séparation des responsabilités par couche
- ✅ Injection de dépendances Spring
- ✅ Validation des données
- ✅ Gestion centralisée des erreurs
- ✅ Documentation OpenAPI/Swagger
- ✅ Persistance JPA/Hibernate

---

## Pattern MVC

### Concept général

Le pattern **MVC** divise l'application en trois composants principaux :

```
┌─────────────────────────────────────────────────────────┐
│                      CLIENT (REST)                       │
└────────────────────────────┬────────────────────────────┘
                             │
                    HTTP Request/Response
                             │
        ┌────────────────────▼────────────────────┐
        │         CONTROLLER (ProductController)  │
        │  • Route les requêtes                   │
        │  • Valide les entrées                   │
        │  • Appelle les services                 │
        └────────────────┬─────────────────────┬──┘
                         │                     │
        ┌────────────────▼─────────┐  ┌───────▼─────────────┐
        │   SERVICE (ProductService)  │ MODEL (Product)     │
        │  • Logique métier        │  │ • Entité JPA        │
        │  • Orchestration         │  │ • Validation        │
        │  • Appels repository     │  │ • État              │
        └────────────────┬─────────┘  └─────────────────────┘
                         │
        ┌────────────────▼─────────────────────────┐
        │   REPOSITORY (ProductRepository)         │
        │   • Accès aux données                    │
        │   • Requêtes personnalisées              │
        │   • Abstraction de la BD                 │
        └────────────────┬─────────────────────────┘
                         │
        ┌────────────────▼─────────────────────────┐
        │      DATABASE (MySQL)                    │
        │      • Stockage persistant               │
        └─────────────────────────────────────────┘
```

---

## Structure des couches

### Architecture en couches

L'application est organisée en **4 couches principales** :

```
Project Root: product_management/
│
├── 📦 COUCHE PRÉSENTATION (Presentation Layer)
│   └── com.eadl.product_management.controllers
│       └── ProductController.java          ← REST Endpoints
│
├── 📦 COUCHE MÉTIER (Business/Service Layer)
│   └── com.eadl.product_management.services
│       └── ProductService.java             ← Logique métier
│
├── 📦 COUCHE MODÈLE (Model/Domain Layer)
│   ├── com.eadl.product_management.entities
│   │   └── Product.java                    ← Entité de domaine
│   └── com.eadl.product_management.repositories
│       └── ProductRepository.java          ← Accès données
│
├── 📦 COUCHE CONFIGURATION (Configuration Layer)
│   └── com.eadl.product_management.config
│       └── OpenApiConfig.java              ← Configuration API
│
└── 📦 APPLICATION BOOT
    └── ProductManagementApplication.java   ← Point d'entrée
```

### Responsabilités par couche

| Couche | Responsabilité | Classes |
|--------|-----------------|---------|
| **Controller** | Routes HTTP, validation entrées, orchestration requête-réponse | ProductController |
| **Service** | Logique métier, orchestration, transactions | ProductService |
| **Model** | Représentation des données, validation | Product |
| **Repository** | Accès à la base de données, requêtes personnalisées | ProductRepository |
| **Configuration** | Configuration de l'application, beans Spring | OpenApiConfig |

---

## Flux de requête

### Cycle de vie d'une requête HTTP

```
1. CLIENT REQUEST
   ├─ GET /api/products
   │
2. CONTROLLER (ProductController)
   ├─ Reçoit la requête
   ├─ Valide les paramètres
   ├─ Appelle ProductService.getAllProducts()
   │
3. SERVICE (ProductService)
   ├─ Applique la logique métier
   ├─ Effectue les vérifications métier
   ├─ Appelle ProductRepository.findAll()
   │
4. REPOSITORY (ProductRepository)
   ├─ Exécute la requête JPA
   ├─ Communique avec Hibernate
   │
5. DATABASE (MySQL)
   ├─ Exécute la requête SQL
   ├─ Retourne les résultats
   │
6. MAPPING (Hibernate)
   ├─ Convertit les résultats SQL en objets Java
   ├─ Retourne List<Product>
   │
7. SERVICE
   ├─ Post-traitement des données
   ├─ Retourne la liste au contrôleur
   │
8. CONTROLLER
   ├─ Enveloppe la réponse HTTP
   ├─ Définit les headers et le statut
   ├─ Sérialise en JSON
   │
9. CLIENT RESPONSE
   └─ HTTP 200 + JSON payload
```

### Exemple détaillé - GET /api/products

```
HTTP GET /api/products
│
▼
ProductController.getAllProducts()
├─ @GetMapping → Route HTTP mappée
├─ @Operation → Documentation Swagger
├─ productService.getAllProducts() → Appel service
│
▼
ProductService.getAllProducts()
├─ Applique les règles métier
├─ productRepository.findAll() → Appel repository
│
▼
ProductRepository.findAll()
├─ Hérité de JpaRepository
├─ Génère la requête SQL: SELECT * FROM products
│
▼
MySQL Database
├─ Exécute: SELECT * FROM products
├─ Retourne les lignes
│
▼
Hibernate (Mapping)
├─ Convertit ResultSet → List<Product>
├─ Instancie les objets Product
├─ Initialise les associations
│
▼
ProductService
├─ Retourne List<Product>
│
▼
ProductController
├─ ResponseEntity.ok(products)
├─ Statut: 200 OK
├─ ContentType: application/json
├─ Body: JSON serialisé
│
▼
HTTP Response 200 OK
├─ Content-Type: application/json
├─ Body: [{"id": 1, "name": "Laptop", ...}, ...]
```

---

## Composants détaillés

### 1. Controller - ProductController

**Fichier:** `com/eadl/product_management/controllers/ProductController.java`

**Responsabilités:**
- Mapper les requêtes HTTP aux méthodes
- Valider les entrées utilisateur
- Orchestrer les appels au service
- Formater les réponses HTTP
- Gérer les codes de statut HTTP

**Endpoints:**

| Méthode | Endpoint | Action |
|---------|----------|--------|
| GET | `/api/products` | Récupérer tous les produits |
| GET | `/api/products/{id}` | Récupérer un produit |
| POST | `/api/products` | Créer un produit |
| PUT | `/api/products/{id}` | Mettre à jour un produit |
| DELETE | `/api/products/{id}` | Supprimer un produit |
| GET | `/api/products/search` | Rechercher par nom |

**Annotations principales:**
```java
@RestController           // Marque comme contrôleur REST
@RequestMapping("/api/products")  // Chemin de base
@Tag                      // Documentation Swagger
@GetMapping/@PostMapping etc.     // Mappage HTTP
@RequestBody              // Reçoit un JSON
@PathVariable             // Paramètre d'URL
@RequestParam             // Paramètre de requête
@Valid                    // Valide les données
@ApiResponse              // Documentation des réponses
```

**Injection de dépendances:**
```java
@Autowired
private ProductService productService;  // Injecté par Spring
```

---

### 2. Service - ProductService

**Fichier:** `com/eadl/product_management/services/ProductService.java`

**Responsabilités:**
- Contient la logique métier
- Orchestre les opérations
- Applique les règles de validation métier
- Gère les transactions
- Appelle le repository pour accéder aux données

**Méthodes principales:**

| Méthode | Description |
|---------|-------------|
| `getAllProducts()` | Récupère tous les produits |
| `getProductById(Long id)` | Récupère un produit par ID |
| `createProduct(Product)` | Crée un nouveau produit |
| `updateProduct(Long, Product)` | Met à jour un produit |
| `deleteProduct(Long)` | Supprime un produit |
| `searchProductsByName(String)` | Recherche par nom |
| `getAvailableProducts()` | Récupère les produits disponibles |

**Exemple de méthode:**
```java
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    
    public Product updateProduct(Long id, Product productDetails) {
        // 1. Vérifie que le produit existe
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
        
        // 2. Applique les modifications
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        // ...
        
        // 3. Persiste en base
        return productRepository.save(product);
    }
}
```

**Annotation:**
```java
@Service  // Marque comme bean de service Spring
```

---

### 3. Model/Entity - Product

**Fichier:** `com/eadl/product_management/entities/Product.java`

**Responsabilités:**
- Représente l'entité métier
- Définit la structure des données
- Valide les données au niveau entité
- Mappe les colonnes de la base de données
- Gère les timestamps automatiques

**Attributs:**

| Attribut | Type | Contraintes | Mapping BD |
|----------|------|-----------|-----------|
| `id` | Long | PK, Auto-généré | id (INT) |
| `name` | String | 3-100 chars, obligatoire | name (VARCHAR(100)) |
| `description` | String | Max 500 chars | description (VARCHAR(500)) |
| `price` | BigDecimal | > 0, obligatoire | price (DECIMAL(10,2)) |
| `quantity` | Integer | ≥ 0 | quantity (INT) |
| `available` | Boolean | Défaut: true | available (BOOLEAN) |
| `createdAt` | LocalDateTime | Auto-généré | created_at (DATETIME) |
| `updatedAt` | LocalDateTime | Auto-généré | updated_at (DATETIME) |

**Annotations JPA:**
```java
@Entity                    // Marque l'entité JPA
@Table(name = "products") // Table de la BD
@Data                      // Getter/Setter Lombok
@NoArgsConstructor        // Constructeur par défaut
@AllArgsConstructor       // Constructeur avec tous les args
@Id                       // Clé primaire
@GeneratedValue           // Auto-génération
@Column                   // Mappage colonne
@PrePersist               // Hook avant insertion
@PreUpdate                // Hook avant mise à jour
@NotBlank/@NotNull/@Min   // Validation
```

**Hooks de cycle de vie:**
```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();  // Autorise la mise à jour
}
```

---

### 4. Repository - ProductRepository

**Fichier:** `com/eadl/product_management/repositories/ProductRepository.java`

**Responsabilités:**
- Fournit l'accès aux données
- Défini les requêtes personnalisées
- Utilise JPA pour l'abstraction de la BD
- Permet les opérations CRUD

**Interface:**
```java
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Requêtes personnalisées
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByAvailable(Boolean available);
    List<Product> findByPriceLessThanEqual(BigDecimal price);
    List<Product> findByPrice(BigDecimal price);
}
```

**Méthodes héritées de JpaRepository:**

| Méthode | Requête SQL |
|---------|------------|
| `findAll()` | SELECT * FROM products |
| `findById(Long)` | SELECT * FROM products WHERE id = ? |
| `save(Product)` | INSERT ou UPDATE |
| `delete(Product)` | DELETE FROM products WHERE id = ? |
| `count()` | SELECT COUNT(*) FROM products |
| `existsById(Long)` | SELECT EXISTS(SELECT 1 FROM products WHERE id = ?) |

**Requêtes personnalisées:**

Spring génère automatiquement les requêtes basées sur le nom de la méthode:

```java
// Method Query (inférence de requête)
findByNameContainingIgnoreCase(String name)
// SQL généré: SELECT * FROM products WHERE name LIKE ? (case-insensitive)

findByAvailable(Boolean available)
// SQL généré: SELECT * FROM products WHERE available = ?

findByPriceLessThanEqual(BigDecimal price)
// SQL généré: SELECT * FROM products WHERE price <= ?
```

---

### 5. Configuration - OpenApiConfig

**Fichier:** `com/eadl/product_management/config/OpenApiConfig.java`

**Responsabilités:**
- Configure la documentation OpenAPI/Swagger
- Définit les métadonnées de l'API
- Configure le serveur de développement
- Ajoute les informations de contact

**Configuration:**
```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI productApiOpenAPI() {
        // 1. Serveur
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        
        // 2. Contact
        Contact contact = new Contact();
        contact.setEmail("stephen.deutou@gmail.com");
        contact.setName("Steph DevOps");
        
        // 3. Licence
        License license = new License()
            .name("IUC License")
            .url("https://kmergenius.com/licenses/iuc/");
        
        // 4. Informations API
        Info info = new Info()
            .title("API de Gestion de Produits")
            .version("1.0.0")
            .contact(contact)
            .description("Gestion complète du catalogue")
            .termsOfService("https://www.iuc.com/terms")
            .license(license);
        
        return new OpenAPI()
            .info(info)
            .servers(List.of(server));
    }
}
```

**Accès à Swagger UI:**
```
http://localhost:8080/swagger-ui.html
http://localhost:8080/v3/api-docs  (JSON)
```

---

### 6. Application Boot

**Fichier:** `com/eadl/product_management/ProductManagementApplication.java`

**Responsabilités:**
- Point d'entrée de l'application
- Initialise Spring Boot
- Lance le serveur embedded Tomcat

```java
@SpringBootApplication
public class ProductManagementApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(ProductManagementApplication.class, args);
    }
}
```

**@SpringBootApplication active:**
- `@Configuration` - Classe de configuration
- `@ComponentScan` - Scan des beans dans le package
- `@EnableAutoConfiguration` - Auto-configuration Spring Boot

---

## Flux de données

### Flux CRUD complet

#### CREATE (POST /api/products)

```
Client Request (JSON)
    ↓
ProductController.createProduct(@Valid @RequestBody Product)
    ├─ Validation @Valid (annotations Product)
    ├─ Si erreur → 400 Bad Request
    ├─ Si OK → productService.createProduct(product)
    ↓
ProductService.createProduct(Product)
    ├─ Vérifie les règles métier
    ├─ productRepository.save(product)
    ↓
ProductRepository.save()
    ├─ Hérité de JpaRepository
    ├─ Hibernate détecte: INSERT car pas d'ID
    ├─ Déclenche @PrePersist
    ├─ Génère SQL: INSERT INTO products (...)
    ↓
MySQL Database
    ├─ Exécute INSERT
    ├─ Auto-génère l'ID
    ├─ Retourne la clé générée
    ↓
Hibernate
    ├─ Initialise product.id avec la clé générée
    ├─ Retourne Product avec ID
    ↓
ProductService
    ├─ Retourne Product
    ↓
ProductController
    ├─ ResponseEntity.status(201).body(product)
    ↓
Client Response (201 Created + JSON avec ID)
```

#### READ (GET /api/products/{id})

```
Client Request
    ↓
ProductController.getProductById(@PathVariable Long id)
    ├─ productService.getProductById(id)
    ↓
ProductService.getProductById(Long)
    ├─ productRepository.findById(id)
    ↓
ProductRepository.findById()
    ├─ Génère SQL: SELECT * FROM products WHERE id = ?
    ↓
MySQL
    ├─ Exécute SELECT
    ├─ Retourne 0 ou 1 ligne
    ↓
Hibernate
    ├─ Mappe la ligne à Product (ou Optional.empty)
    ↓
ProductService
    ├─ Retourne Optional<Product>
    ↓
ProductController
    ├─ Si présent: ResponseEntity.ok(product)
    ├─ Si absent: ResponseEntity.notFound()
    ↓
Client Response (200 + JSON ou 404)
```

#### UPDATE (PUT /api/products/{id})

```
Client Request (ID + JSON)
    ↓
ProductController.updateProduct(@PathVariable id, @Valid @RequestBody)
    ├─ Validation @Valid
    ├─ productService.updateProduct(id, productDetails)
    ↓
ProductService.updateProduct()
    ├─ productRepository.findById(id)
    ├─ Si pas trouvé → RuntimeException
    ├─ Copie les attributs: product.setName(), setPrice(), etc.
    ├─ productRepository.save(product)
    ↓
Hibernate
    ├─ Détecte: UPDATE car product.id existe
    ├─ Déclenche @PreUpdate → updatedAt = now()
    ├─ Génère SQL: UPDATE products SET ... WHERE id = ?
    ↓
MySQL
    ├─ Exécute UPDATE
    ↓
ProductController
    ├─ ResponseEntity.ok(updatedProduct)
    ↓
Client Response (200 + JSON mis à jour)
```

#### DELETE (DELETE /api/products/{id})

```
Client Request (ID)
    ↓
ProductController.deleteProduct(@PathVariable id)
    ├─ productService.deleteProduct(id)
    ↓
ProductService.deleteProduct(Long)
    ├─ productRepository.findById(id)
    ├─ Si pas trouvé → RuntimeException
    ├─ productRepository.delete(product)
    ↓
ProductRepository.delete()
    ├─ Génère SQL: DELETE FROM products WHERE id = ?
    ↓
MySQL
    ├─ Exécute DELETE
    ↓
ProductController
    ├─ ResponseEntity.noContent().build()
    ↓
Client Response (204 No Content)
```

---

## Diagrammes

### Diagramme des classes

```
┌─────────────────────────────────────┐
│     ProductController               │
├─────────────────────────────────────┤
│ - productService: ProductService    │
├─────────────────────────────────────┤
│ + getAllProducts(): List<Product>   │
│ + getProductById(id): Product       │
│ + createProduct(p): Product         │
│ + updateProduct(id, p): Product     │
│ + deleteProduct(id): void           │
│ + searchProducts(name): List        │
└────────────┬────────────────────────┘
             │ uses
             │
┌────────────▼────────────────────────┐
│      ProductService                 │
├─────────────────────────────────────┤
│ - productRepository: ProductRepo    │
├─────────────────────────────────────┤
│ + getAllProducts()                  │
│ + getProductById(Long)              │
│ + createProduct(Product)            │
│ + updateProduct(Long, Product)      │
│ + deleteProduct(Long)               │
│ + searchProductsByName(String)      │
│ + getAvailableProducts()            │
└────────────┬────────────────────────┘
             │ uses
             │
┌────────────▼────────────────────────┐
│    ProductRepository                │
│  extends JpaRepository<P, Long>     │
├─────────────────────────────────────┤
│ + findByNameContainingIgnoreCase()  │
│ + findByAvailable(Boolean)          │
│ + findByPrice(BigDecimal)           │
│ + findByPriceLessThanEqual(Decimal) │
└────────────┬────────────────────────┘
             │ persists
             │
┌────────────▼────────────────────────┐
│        Product                      │
├─────────────────────────────────────┤
│ - id: Long                          │
│ - name: String                      │
│ - description: String               │
│ - price: BigDecimal                 │
│ - quantity: Integer                 │
│ - available: Boolean                │
│ - createdAt: LocalDateTime          │
│ - updatedAt: LocalDateTime          │
├─────────────────────────────────────┤
│ + Product()                         │
│ + Product(all)                      │
│ + getters/setters                   │
│ - onCreate(): void                  │
│ - onUpdate(): void                  │
└────────────┬────────────────────────┘
             │ maps to
             │
┌────────────▼────────────────────────┐
│    MySQL Table: products            │
├─────────────────────────────────────┤
│ id              INT PRIMARY KEY     │
│ name            VARCHAR(100) NOT NULL
│ description     VARCHAR(500)        │
│ price           DECIMAL(10,2)       │
│ quantity        INT                 │
│ available       BOOLEAN             │
│ created_at      DATETIME NOT NULL   │
│ updated_at      DATETIME            │
└─────────────────────────────────────┘
```

### Interaction entre les couches

```
         HTTP Request
              │
              ▼
    ┌─────────────────────┐
    │  @RestController    │
    │  ProductController  │  ← Couche Présentation
    │                     │
    │  Routes requêtes    │
    │  Valide entrées     │
    │  Formate réponses   │
    └──────────┬──────────┘
               │
               │ appelle
               ▼
    ┌─────────────────────┐
    │  @Service           │
    │  ProductService     │  ← Couche Métier
    │                     │
    │  Logique métier     │
    │  Orchestration      │
    │  Transactions       │
    └──────────┬──────────┘
               │
               │ utilise
               ▼
    ┌─────────────────────┐
    │  @Repository        │
    │  ProductRepository  │  ← Couche Données
    │                     │
    │  Requêtes CRUD      │
    │  Requêtes custom    │
    └──────────┬──────────┘
               │
               │ communique
               ▼
    ┌─────────────────────┐
    │  JPA/Hibernate      │  ← ORM Layer
    │                     │
    │  Mapping Objet/SQL  │
    │  Gestion sessions   │
    └──────────┬──────────┘
               │
               │ accède
               ▼
    ┌─────────────────────┐
    │  MySQL Database     │  ← Base de données
    │                     │
    │  Stockage persistant│
    │  Exécution SQL      │
    └─────────────────────┘
```

---

## Principes SOLID appliqués

### S - Single Responsibility Principle
Chaque classe a une responsabilité unique :
- **ProductController** : Gérer les requêtes HTTP
- **ProductService** : Logique métier
- **ProductRepository** : Accès aux données
- **Product** : Représenter les données

### O - Open/Closed Principle
- L'architecture permet d'ajouter de nouveaux services sans modifier les existants
- Les interfaces (JpaRepository) permettent l'extension
- Utilisation de l'héritage des repositories

### L - Liskov Substitution Principle
- ProductRepository étend JpaRepository et peut être remplacé par toute implémentation JpaRepository
- Spring injecte l'implémentation correcte automatiquement

### I - Interface Segregation Principle
- Utilisation d'interfaces granulaires (JpaRepository)
- Les clients n'utilisent que les méthodes dont ils ont besoin

### D - Dependency Inversion Principle
- Les dépendances sont injectées via `@Autowired`
- ProductService dépend de l'abstraction (ProductRepository), pas de l'implémentation
- Spring gère les dépendances via l'IoC container

---

## Bonnes pratiques

### 1. Validation des données

**Niveau entité:**
```java
@NotBlank(message = "Le nom est obligatoire")
@Size(min = 3, max = 100)
private String name;

@NotNull(message = "Le prix est obligatoire")
@DecimalMin(value = "0.0", inclusive = false)
private BigDecimal price;
```

**Niveau contrôleur:**
```java
public ResponseEntity<Product> createProduct(
    @Valid @RequestBody Product product) {
    // La validation est automatique
}
```

### 2. Gestion des erreurs

**Service:**
```java
public Product updateProduct(Long id, Product details) {
    return productRepository.findById(id)
        .orElseThrow(() -> 
            new RuntimeException("Produit non trouvé"));
}
```

**Contrôleur:**
```java
try {
    Product updated = productService.updateProduct(id, details);
    return ResponseEntity.ok(updated);
} catch (RuntimeException e) {
    return ResponseEntity.notFound().build();
}
```

### 3. Timestamps automatiques

```java
@PrePersist
protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
}

@PreUpdate
protected void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

### 4. Requêtes personnalisées

```java
// Requête générée automatiquement par le nom
List<Product> findByNameContainingIgnoreCase(String name);

// Génère: SELECT * FROM products WHERE name LIKE ? (case-insensitive)
```

### 5. Injection de dépendances

```java
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;
    // Spring injecte automatiquement
}

@RestController
public class ProductController {
    
    @Autowired
    private ProductService productService;
    // Spring injecte automatiquement
}
```

### 6. Documentation avec Swagger

```java
@Operation(summary = "Créer un produit", 
           description = "Ajoute un nouveau produit")
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", 
                 description = "Produit créé"),
    @ApiResponse(responseCode = "400", 
                 description = "Données invalides")
})
public ResponseEntity<Product> createProduct(
    @Valid @RequestBody Product product) {
    // ...
}
```

---

## Interactions clés

### Entre Controller et Service

```java
// Controller
@PostMapping
public ResponseEntity<Product> create(@Valid @RequestBody Product p) {
    Product saved = productService.createProduct(p);  // ← Appel
    return ResponseEntity.status(201).body(saved);
}

// Service
@Service
public class ProductService {
    public Product createProduct(Product product) {
        return productRepository.save(product);  // ← Appel
    }
}
```

### Entre Service et Repository

```java
// Service
public List<Product> searchByName(String name) {
    return productRepository.findByNameContainingIgnoreCase(name);  // ← Appel
}

// Repository
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByNameContainingIgnoreCase(String name);  // ← Définition
}
```

### Entre Repository et Database

```java
// Repository appelle JpaRepository qui traduit en SQL
productRepository.findById(1L)
// ↓ Hibernate génère
// SELECT * FROM products WHERE id = 1

productRepository.save(product)
// ↓ Hibernate génère
// INSERT INTO products (...) VALUES (...) 
// OU
// UPDATE products SET ... WHERE id = ?
```

---

## Configuration et déploiement

### Fichier de configuration

**application.properties:**
```properties
spring.application.name=product_management
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/product_db
spring.datasource.username=root
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### Startup de l'application

1. Spring Boot démarre
2. Scan des composants (@Component, @Service, @Repository, @Controller)
3. Création des beans et injection des dépendances
4. Initialisation du contexte JPA
5. Connexion à la base de données
6. Démarrage du serveur Tomcat embarqué sur le port 8080
7. Enregistrement des routes REST
8. Application prête à recevoir les requêtes

---

## Résumé de l'architecture

| Aspect | Détail |
|--------|--------|
| **Pattern** | MVC avec séparation en couches |
| **Framework** | Spring Boot 3.5.7 |
| **Java** | Version 21 |
| **Base de données** | MySQL 8 |
| **ORM** | JPA/Hibernate |
| **Serveur** | Tomcat embarqué (port 8080) |
| **Validation** | Jakarta Validation |
| **Documentation** | OpenAPI 3.0 / Swagger UI |
| **Build** | Maven |
| **Principes** | SOLID, Clean Architecture |

---

## Flux complet d'une requête

```
1. Client → HTTP Request
2. Spring Boot Tomcat → Route vers ProductController
3. ProductController → Valide et appelle ProductService
4. ProductService → Applique logique métier, appelle ProductRepository
5. ProductRepository → Appelle JpaRepository, qui génère SQL
6. Hibernate → Traduit SQL et communique avec MySQL
7. MySQL → Exécute et retourne les données
8. Hibernate → Mappe ResultSet en objets Java
9. ProductRepository → Retourne les objets
10. ProductService → Post-traite et retourne
11. ProductController → Enveloppe dans ResponseEntity
12. Spring Boot → Sérialise en JSON
13. Tomcat → Envoie HTTP Response
14. Client → Reçoit le JSON
```

---

## Conclusion

L'architecture MVC de l'application Product Management suit les meilleures pratiques :
- ✅ Séparation claire des responsabilités
- ✅ Chaque couche a un rôle bien défini
- ✅ Facile à tester et maintenir
- ✅ Scalable pour ajouter de nouvelles fonctionnalités
- ✅ Utilisation optimale des frameworks Spring Boot et JPA
- ✅ Respect des principes SOLID
